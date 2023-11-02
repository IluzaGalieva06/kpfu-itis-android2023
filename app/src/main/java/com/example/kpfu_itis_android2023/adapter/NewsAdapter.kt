package com.example.kpfu_itis_android2023.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.model.NewsDataModel
import com.example.kpfu_itis_android2023.ui.holder.ButtonViewHolder
import com.example.kpfu_itis_android2023.ui.holder.DateViewHolder
import com.example.kpfu_itis_android2023.ui.holder.NewsViewHolder

class NewsAdapter(
    private val newsItemClickListener: NewsItemClickListener,
    private val fragmentManager: FragmentManager,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var newsList = mutableListOf<NewsDataModel>()
    var onFavoriteClick: ((Int) -> Unit)? = null

    fun getItems(): List<NewsDataModel> {
        return newsList
    }


    fun setItems(items: List<NewsDataModel>) {
        newsList.clear()
        newsList.addAll(items)
        notifyDataSetChanged()
    }

    interface NewsItemClickListener {
        fun onNewsItemClicked(news: NewsDataModel)
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> VIEW_TYPE_BUTTON
            position == 1 || position == 10 || position == 19 || position == 28 || position == 37 || position == 46 -> VIEW_TYPE_DATE
            else -> VIEW_TYPE_NEWS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return when (viewType) {
            VIEW_TYPE_BUTTON -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false)
                ButtonViewHolder(view, this, fragmentManager = fragmentManager)
            }

            VIEW_TYPE_DATE -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
                DateViewHolder(view)
            }

            VIEW_TYPE_NEWS -> {

                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
                NewsViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type: $viewType")

        }
    }

    override fun getItemCount(): Int {
        return 1 + newsList.size + ((newsList.size / 8) + 1)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ButtonViewHolder -> {

            }

            is DateViewHolder -> {
                holder.onBindDateItem()


            }

            is NewsViewHolder -> {
                val news = newsList[calculateNewsPosition(position)]
                holder.onBindNewsItem(news)
                holder.itemView.setOnClickListener {
                    newsItemClickListener?.onNewsItemClicked(news)
                }
                setupFavoriteButton(
                    holder.viewBinding.btnFavorite,
                    newsList[calculateNewsPosition(position)]
                )


            }
        }
    }

    companion object {
        private const val VIEW_TYPE_BUTTON = 0
        private const val VIEW_TYPE_DATE = 1
        private const val VIEW_TYPE_NEWS = 2

    }

    private fun calculateNewsPosition(position: Int): Int {
        if (position in listOf(2, 3, 4, 5, 6, 7, 8, 9)) {
            return position - 2
        } else if (position < 2) {
            return 0
        } else {
            val offset = if (position > 9) 2 else 1
            return (position - offset) - (position - offset) / 8
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val gridLayoutManager = recyclerView.layoutManager as? GridLayoutManager
        gridLayoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (getItemViewType(position)) {
                    VIEW_TYPE_BUTTON, VIEW_TYPE_DATE -> 2
                    else -> 1
                }
            }
        }
    }

    private fun setupFavoriteButton(likeButton: ImageButton, item: NewsDataModel) {
        likeButton.setOnClickListener {
            val isFavorite = !item.isFavorite
            item.isFavorite = isFavorite
            val imageResource = if (isFavorite) R.drawable.ic_like_red else R.drawable.ic_like_gray
            likeButton.setImageResource(imageResource)
            onFavoriteClick?.invoke(newsList.indexOf(item))
        }
    }

}