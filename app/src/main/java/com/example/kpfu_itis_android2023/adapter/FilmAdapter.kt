package com.example.kpfu_itis_android2023.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.kpfu_itis_android2023.data.db.dao.FilmRatingDao
import com.example.kpfu_itis_android2023.data.db.entity.FilmEntity
import com.example.kpfu_itis_android2023.databinding.ItemFilmBinding
import com.example.kpfu_itis_android2023.holder.FilmHolder
import com.example.kpfu_itis_android2023.util.FilmListType

class FilmAdapter(
    private var list: List<FilmEntity>,
    private val glide: RequestManager,
    private val onItemClick: (FilmEntity) -> Unit,
    private val onDeleteClick: (Int) -> Unit,
    private var filmListType: FilmListType,
    private val onFavoriteClick: (Int) -> Unit,
    private var favoriteFilmIds: MutableList<Int>,
    private val filmRatingDao: FilmRatingDao,
    private val userId: Int
) : RecyclerView.Adapter<FilmHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilmHolder = FilmHolder(

        binding = ItemFilmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        glide = glide,
        onItemClick = onItemClick,
        onDeleteClick = onDeleteClick,
        onFavoriteClick = onFavoriteClick
    )

    override fun onBindViewHolder(
        holder: FilmHolder,
        position: Int
    ) {
        holder.onBind(list[position], filmListType, favoriteFilmIds, filmRatingDao, userId)


    }

    override fun getItemCount(): Int = list.size
    fun updateList(newList: List<FilmEntity>, newFilmListType: FilmListType) {
        list = newList
        filmListType = newFilmListType
        notifyDataSetChanged()
    }
    fun updateFavoriteFilms(favoriteFilms: List<FilmEntity>) {
        this.favoriteFilmIds.clear()
        this.favoriteFilmIds.addAll(favoriteFilms.map { it.filmId })
        notifyDataSetChanged()
    }
}