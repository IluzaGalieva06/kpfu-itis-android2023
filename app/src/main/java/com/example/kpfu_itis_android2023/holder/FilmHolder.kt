package com.example.kpfu_itis_android2023.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.data.db.entity.FilmEntity
import com.example.kpfu_itis_android2023.databinding.ItemFilmBinding
import com.example.kpfu_itis_android2023.util.FilmListType

class FilmHolder(
    private val binding: ItemFilmBinding,
    private val glide: RequestManager,
    private val onItemClick: (FilmEntity) -> Unit,
    private val onDeleteClick: (Int) -> Unit,
    private val onFavoriteClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val options: RequestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    fun onBind(
        filmEntity: FilmEntity,
        filmListType: FilmListType,
        favoriteFilmIds: MutableList<Int>
    ) {
        with(binding) {
            tvTitle.text = filmEntity.title
            root.setOnClickListener {
                onItemClick(filmEntity)
            }
            if (filmListType == FilmListType.ALL_MOVIES) {
                imageDeleteButton.visibility = View.VISIBLE
                imageDeleteButton.setOnClickListener {
                    onDeleteClick(adapterPosition)
                }
            } else {

                imageDeleteButton.visibility = View.GONE
            }
            val isFavorite = favoriteFilmIds.contains(filmEntity.filmId)

            updateFavoriteButtonImage(isFavorite)

            binding.imageFavoriteButton.setOnClickListener {
                onFavoriteClick(adapterPosition)

                val newFavoriteState = !isFavorite

                updateFavoriteButtonImage(newFavoriteState)

                if (newFavoriteState) {
                    favoriteFilmIds.add(filmEntity.filmId)
                } else {
                    favoriteFilmIds.remove(filmEntity.filmId)
                }
            }

            glide.load(filmEntity.photoUrl)
                .apply(options)
                .placeholder(R.drawable.download)
                .error(R.drawable.baseline_error_24)
                .into(movieImageView)
        }
    }

    private fun updateFavoriteButtonImage(isFavorite: Boolean) {
        val imageResource = if (isFavorite) R.drawable.favorite_selected else R.drawable.favorite
        binding.imageFavoriteButton.setImageResource(imageResource)
    }

}