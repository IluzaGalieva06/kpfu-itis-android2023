package com.example.kpfu_itis_android2023.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.data.db.AppDatabase
import com.example.kpfu_itis_android2023.data.db.entity.FilmEntity
import com.example.kpfu_itis_android2023.databinding.FragmentAddFilmBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddMovieFragment : Fragment(R.layout.fragment_add_film) {
    private var _viewBinding: FragmentAddFilmBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentAddFilmBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmDao = AppDatabase.getInstance(requireContext()).filmDao()
        with(viewBinding) {
            btnSave.setOnClickListener {
                val title = etMovieTitle.text.toString()
                val year = etYear.text.toString().toIntOrNull() ?: 0
                val description = etDescription.text.toString()
                val photoUrl = etPhotoUrl.text.toString()

                if (title.isNotEmpty() && year > 0 && description.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val existingMovie = filmDao.getFilmByTitleAndYear(title, year)
                        if (existingMovie == null) {
                            val film = FilmEntity(
                                title = title,
                                year = year,
                                description = description,
                                photoUrl = photoUrl
                            )
                            filmDao.addFilm(film)
                            withContext(Dispatchers.Main) {
                                etMovieTitle.setText("")
                                etYear.setText("")
                                etDescription.setText("")
                                etPhotoUrl.setText("")
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    R.string.film_exist,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.empty,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}