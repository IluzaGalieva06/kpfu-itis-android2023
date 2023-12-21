package com.example.kpfu_itis_android2023.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.data.db.AppDatabase
import com.example.kpfu_itis_android2023.data.db.dao.FilmDao
import com.example.kpfu_itis_android2023.data.db.dao.FilmRatingDao
import com.example.kpfu_itis_android2023.data.db.dao.UsersDao
import com.example.kpfu_itis_android2023.data.db.entity.FilmRatingEntity
import com.example.kpfu_itis_android2023.databinding.FragmentInfoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InfoFragment : Fragment(R.layout.fragment_info) {

    private var _viewBinding: FragmentInfoBinding? = null
    private val viewBinding: FragmentInfoBinding
        get() = _viewBinding!!

    private lateinit var filmDao: FilmDao
    private lateinit var filmRatingDao: FilmRatingDao
    private lateinit var userDao: UsersDao


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentInfoBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val userId = sharedPreferences.getInt("user_id", -1)


        var currentFilmId: Int? = null
        val title = arguments?.getString("title") ?: ""
        val year = arguments?.getInt("year") ?: 0

        val db = AppDatabase.getInstance(requireContext())
        filmDao = db.filmDao()
        filmRatingDao = db.filmRatingDao()
        userDao = db.userDao()

        with(viewBinding) {
            lifecycleScope.launch {
                val currentFilm = withContext(Dispatchers.IO) {
                    filmDao.getFilmByTitleAndYear(title, year)
                }
                currentFilm?.let {
                    currentFilmId = currentFilm.filmId
                    withContext(Dispatchers.Main) {
                        textViewTitle.text = currentFilm.title
                        textViewYear.text = currentFilm.year.toString()
                        textViewDescription.text = currentFilm.description

                        Glide.with(requireContext())
                            .load(currentFilm.photoUrl)
                            .into(imageViewMoviePoster)

                        val userRating = withContext(Dispatchers.IO) {
                            filmRatingDao.getRatingForFilm(userId, currentFilmId ?: -1)
                        }
                        userRating?.let { rating ->
                            ratingBar.rating = rating
                        }

                        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                            if (fromUser) {
                                currentFilmId?.let { filmId ->
                                    val filmRating = FilmRatingEntity(userId, filmId, rating)
                                    lifecycleScope.launch {
                                        withContext(Dispatchers.IO) {
                                            filmRatingDao.rateFilm(filmRating)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}