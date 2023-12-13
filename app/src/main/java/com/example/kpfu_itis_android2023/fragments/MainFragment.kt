package com.example.kpfu_itis_android2023.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.adapter.FilmAdapter
import com.example.kpfu_itis_android2023.data.db.AppDatabase
import com.example.kpfu_itis_android2023.data.db.dao.FilmDao
import com.example.kpfu_itis_android2023.data.db.dao.FilmRatingDao
import com.example.kpfu_itis_android2023.data.db.dao.UserFilmFavoriteDao
import com.example.kpfu_itis_android2023.data.db.dao.UsersDao
import com.example.kpfu_itis_android2023.data.db.entity.FilmEntity
import com.example.kpfu_itis_android2023.data.db.entity.UserFilmFavoriteCrossRef
import com.example.kpfu_itis_android2023.databinding.FragmentMainBinding
import com.example.kpfu_itis_android2023.util.FilmListType
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class MainFragment : Fragment(R.layout.fragment_main) {
    private var _viewBinding: FragmentMainBinding? = null
    private val viewBinding: FragmentMainBinding
        get() = _viewBinding!!

    private lateinit var filmAdapter: FilmAdapter
    private lateinit var filmDao: FilmDao
    private lateinit var filmFavoriteDao: UserFilmFavoriteDao
    private lateinit var filmFavoriteAdapter: FilmAdapter
    private lateinit var userDao: UsersDao
    private var userId by Delegates.notNull<Int>()
    private lateinit var filmRatingDao: FilmRatingDao
    private var currentSortOption: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentMainBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation)
        bottomNavigationView?.visibility = View.VISIBLE

        val sharedPreferences =
            requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)

        userId = sharedPreferences.getInt("user_id", -1)

        val db = AppDatabase.getInstance(requireContext())
        userDao = db.userDao()
        filmRatingDao = db.filmRatingDao()
        filmFavoriteDao = db.userFilmDao()

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerViewAllMovies.layoutManager = layoutManager

        filmAdapter =
            FilmAdapter(
                loadAllFilms(FilmListType.ALL_MOVIES),
                Glide.with(requireContext()),
                { filmEntity ->
                    val args = Bundle()
                    args.putString("title", filmEntity.title)
                    args.putInt("year", filmEntity.year)

                    val infoFragment = InfoFragment()
                    infoFragment.arguments = args

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.main_activity_container, infoFragment)
                        ?.addToBackStack(null)
                        ?.commit()
                },
                { position -> removeFilm(position, FilmListType.ALL_MOVIES) },
                FilmListType.ALL_MOVIES,
                { position -> toggleFavorite(position) },
                loadFilmsId()
            )

        viewBinding.recyclerViewAllMovies.adapter = filmAdapter
        viewBinding.recyclerViewAllMovies.visibility = View.VISIBLE


        if (loadFavoriteFilms() != null) {
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            viewBinding.recyclerViewFavorites.layoutManager = layoutManager
            filmFavoriteAdapter =
                FilmAdapter(
                    loadFavoriteFilms(),
                    Glide.with(requireContext()),
                    { filmEntity ->
                        val args = Bundle()
                        args.putString("title", filmEntity.title)
                        args.putInt("year", filmEntity.year)

                        val infoFragment = InfoFragment()
                        infoFragment.arguments = args

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.main_activity_container, infoFragment)
                            ?.addToBackStack(null)
                            ?.commit()
                    },
                    { },
                    FilmListType.FAVORITE_MOVIES,
                    { position -> toggleFavorite(position, FilmListType.FAVORITE_MOVIES) },
                    loadFilmsId()
                )
            viewBinding.recyclerViewFavorites.adapter = filmFavoriteAdapter
            viewBinding.recyclerViewFavorites.visibility = View.VISIBLE

        }
        val sortSpinner: Spinner = viewBinding.sortSpinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortSpinner.adapter = adapter
        val sharedPrefs =
            requireContext().getSharedPreferences("sort_prefs", Context.MODE_PRIVATE)

        val savedSortOption = sharedPrefs.getInt("sort_option", 0)
        sortSpinner.setSelection(savedSortOption)

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedPrefs.edit().putInt("sort_option", position).apply()
                currentSortOption = position

                when (position) {
                    0 -> sortFilmsByYearDescending()
                    1 -> sortFilmsByYearAscending()
                    2 -> sortFilmsByUserRatingDescending()
                    3 -> sortFilmsByUserRatingAscending()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun sortFilmsByYearDescending() {
        val allFilms = loadAllFilms(FilmListType.ALL_MOVIES)
        val sortedList = allFilms.sortedByDescending { it.year }
        filmAdapter.updateList(sortedList, FilmListType.ALL_MOVIES)
    }

    private fun sortFilmsByYearAscending() {
        val allFilms = loadAllFilms(FilmListType.ALL_MOVIES)
        val sortedList = allFilms.sortedBy { it.year }
        filmAdapter.updateList(sortedList, FilmListType.ALL_MOVIES)
    }

    private fun sortFilmsByUserRatingDescending() {
        lifecycleScope.launch {
            val allFilms = loadAllFilms(FilmListType.ALL_MOVIES)
            val ratings = allFilms.map { film ->
                async(Dispatchers.IO) {
                    getFilmUserRating(film.filmId, userId)
                }
            }.awaitAll()
            val sortedList = allFilms.zip(ratings).sortedByDescending { (_, rating) ->
                rating
            }.map { (film, _) ->
                film
            }

            filmAdapter.updateList(sortedList, FilmListType.ALL_MOVIES)
        }
    }

    private fun sortFilmsByUserRatingAscending() {
        lifecycleScope.launch {
            val allFilms = loadAllFilms(FilmListType.ALL_MOVIES)
            val ratings = allFilms.map { film ->
                async(Dispatchers.IO) {
                    getFilmUserRating(film.filmId, userId)
                }
            }.awaitAll()

            val sortedList = allFilms.zip(ratings).sortedBy { (_, rating) ->
                rating
            }.map { (film, _) ->
                film
            }

            filmAdapter.updateList(sortedList, FilmListType.ALL_MOVIES)
        }
    }

    private suspend fun getFilmUserRating(filmId: Int, userId: Int): Float {
        return withContext(Dispatchers.IO) {
            filmRatingDao.getRatingForFilm(userId, filmId) ?: 0f
        }
    }

    private fun toggleFavorite(position: Int, filmListType: FilmListType) {
        val filmToUpdate = when (filmListType) {
            FilmListType.FAVORITE_MOVIES -> loadFavoriteFilms()[position]
            else -> loadAllFilms(FilmListType.ALL_MOVIES)[position]
        }

        CoroutineScope(Dispatchers.IO).launch {
            val filmFavoriteDao = AppDatabase.getInstance(requireContext()).userFilmDao()

            if (filmListType == FilmListType.FAVORITE_MOVIES) {
                filmFavoriteDao.removeFromFavorites(
                    UserFilmFavoriteCrossRef(
                        userId = userId,
                        filmId = filmToUpdate.filmId
                    )
                )
                withContext(Dispatchers.Main) {
                    val favoriteFilms = loadAllFilms(FilmListType.FAVORITE_MOVIES)
                    filmFavoriteAdapter.updateList(favoriteFilms, FilmListType.FAVORITE_MOVIES)
                    filmFavoriteAdapter.updateFavoriteFilms(favoriteFilms)
                }
            } else {
                filmFavoriteDao.addToFavorites(
                    UserFilmFavoriteCrossRef(
                        userId = userId,
                        filmId = filmToUpdate.filmId
                    )
                )
                withContext(Dispatchers.Main) {
                    val favoriteFilms = loadAllFilms(FilmListType.FAVORITE_MOVIES)
                    filmFavoriteAdapter.updateList(favoriteFilms, FilmListType.FAVORITE_MOVIES)
                    filmFavoriteAdapter.updateFavoriteFilms(favoriteFilms)
                }
            }

            withContext(Dispatchers.Main) {
                val favoriteFilms = loadFavoriteFilms()
                filmFavoriteAdapter.updateList(favoriteFilms, FilmListType.FAVORITE_MOVIES)
                filmFavoriteAdapter.updateFavoriteFilms(favoriteFilms)

                val allFilms = loadAllFilms(FilmListType.ALL_MOVIES)
                filmAdapter.updateList(allFilms, FilmListType.ALL_MOVIES)
                filmAdapter.updateFavoriteFilms(favoriteFilms)
            }
        }
    }

    private fun toggleFavorite(position: Int) {
        val filmToUpdate = loadAllFilms(FilmListType.ALL_MOVIES)[position]

        CoroutineScope(Dispatchers.IO).launch {
            val filmFavoriteDao = AppDatabase.getInstance(requireContext()).userFilmDao()
            val isFavorite = filmFavoriteDao.isFilmFavoriteForUser(userId, filmToUpdate.filmId) != null

            if (isFavorite) {
                filmFavoriteDao.removeFromFavorites(
                    UserFilmFavoriteCrossRef(
                        userId = userId,
                        filmId = filmToUpdate.filmId
                    )
                )
                withContext(Dispatchers.Main) {
                    val favoriteFilms = loadAllFilms(FilmListType.FAVORITE_MOVIES)
                    filmFavoriteAdapter.updateList(favoriteFilms, FilmListType.FAVORITE_MOVIES)
                    filmFavoriteAdapter.updateFavoriteFilms(favoriteFilms)
                }
            } else {
                filmFavoriteDao.addToFavorites(
                    UserFilmFavoriteCrossRef(
                        userId = userId,
                        filmId = filmToUpdate.filmId
                    )
                )
                withContext(Dispatchers.Main) {
                    val favoriteFilms = loadAllFilms(FilmListType.FAVORITE_MOVIES)
                    filmFavoriteAdapter.updateList(favoriteFilms, FilmListType.FAVORITE_MOVIES)
                    filmFavoriteAdapter.updateFavoriteFilms(favoriteFilms)
                }
            }

            withContext(Dispatchers.Main) {

                val favoriteFilms = loadAllFilms(FilmListType.FAVORITE_MOVIES)
                filmFavoriteAdapter.updateList(favoriteFilms, FilmListType.FAVORITE_MOVIES)
                filmFavoriteAdapter.updateFavoriteFilms(favoriteFilms)
                val allFilms = loadAllFilms(FilmListType.ALL_MOVIES)
                filmAdapter.updateList(allFilms, FilmListType.ALL_MOVIES)
                filmAdapter.updateFavoriteFilms(favoriteFilms)
            }
        }
    }

    private fun loadFavoriteFilms(): List<FilmEntity> {
        filmDao = AppDatabase.getInstance(requireContext()).filmDao()
        filmFavoriteDao = AppDatabase.getInstance(requireContext()).userFilmDao()

        return runBlocking {
            val userWithFavoriteFilms = withContext(Dispatchers.IO) {
                filmFavoriteDao.getUserWithFavoriteFilms(userId)
            }
            userWithFavoriteFilms?.favoriteFilms ?: emptyList()
        }
    }


    private fun removeFilm(position: Int, filmListType: FilmListType) {
        val filmToDelete = loadAllFilms(filmListType).get(position)

        CoroutineScope(Dispatchers.IO).launch {
            val filmDao = AppDatabase.getInstance(requireContext()).filmDao()
            filmDao.deleteFilm(filmToDelete)

            withContext(Dispatchers.Main) {
                if (filmListType == FilmListType.ALL_MOVIES) {
                    filmAdapter.updateList(loadAllFilms(filmListType), filmListType)
                    val favoriteFilms = loadAllFilms(FilmListType.FAVORITE_MOVIES)
                    filmFavoriteAdapter.updateList(favoriteFilms, FilmListType.FAVORITE_MOVIES)
                } else {
                    filmFavoriteAdapter.updateList(
                        loadFavoriteFilms(),
                        FilmListType.FAVORITE_MOVIES
                    )
                }
            }
        }
    }

    private fun loadFilmsId(): MutableList<Int> {

        return runBlocking {
            val filmsId = withContext(Dispatchers.IO) {
                filmFavoriteDao.getFavoriteFilmIdsForUser(userId)
            }
            filmsId?.toMutableList() ?: mutableListOf()
        }
    }

    private fun loadAllFilms(filmListType: FilmListType): List<FilmEntity> {
        return when (filmListType) {
            FilmListType.ALL_MOVIES -> {
                val filmDao = AppDatabase.getInstance(requireContext()).filmDao()
                val allFilms = runBlocking {
                    withContext(Dispatchers.IO) {
                        filmDao.getAllFilms()
                    }
                }
                val sortedFilms = when (currentSortOption) {
                    0 -> allFilms?.sortedByDescending { it.year }
                    1 -> allFilms?.sortedBy { it.year }
                    2 -> sortFilmsByUserRatingDescending(allFilms)
                    3 -> sortFilmsByUserRatingAscending(allFilms)
                    else -> allFilms?.sortedByDescending { it.year }
                }
                sortedFilms ?: emptyList()
            }

            FilmListType.FAVORITE_MOVIES -> {
                loadFavoriteFilms()
            }
        }
    }

    private fun sortFilmsByUserRatingDescending(allFilms: List<FilmEntity>?): List<FilmEntity> {
        return runBlocking {
            val ratings = allFilms?.map { film ->
                async(Dispatchers.IO) {
                    getFilmUserRating(film.filmId, userId)
                }
            }?.awaitAll()

            val sortedList =
                allFilms?.zip(ratings ?: emptyList())?.sortedByDescending { (_, rating) ->
                    rating
                }?.map { (film, _) ->
                    film
                }

            sortedList ?: emptyList()
        }
    }

    private fun sortFilmsByUserRatingAscending(allFilms: List<FilmEntity>?): List<FilmEntity> {
        return runBlocking {
            val ratings = allFilms?.map { film ->
                async(Dispatchers.IO) {
                    getFilmUserRating(film.filmId, userId)
                }
            }?.awaitAll()

            val sortedList = allFilms?.zip(ratings ?: emptyList())?.sortedBy { (_, rating) ->
                rating
            }?.map { (film, _) ->
                film
            }

            sortedList ?: emptyList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
