package com.example.optional.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.ThingsDataModel
import com.example.optional.holder.ThingsHolder

class ThingsAdapter(
    val newsList: List<ThingsDataModel>,
    var inputNumber: Int,
    private val context: Context
): RecyclerView.Adapter<ThingsHolder>() {
    companion object {
        private const val VIEW_TYPE_FULL_WIDTH = 0
        private const val VIEW_TYPE_DEFAULT = 1
    }
    private val displayMetrics: DisplayMetrics = context.resources.displayMetrics

    override fun getItemViewType(position: Int): Int {
        return if ((inputNumber % 6 == 1 && (position % 6 == 0 || position % 6 == 5)) ||
                (inputNumber % 6 == 2 && (position % 6 == 5 || position == getMaxPositionWithRemainderOne(inputNumber, 1) || position % 6 == 0)) ||
                (inputNumber % 6 == 3 && (position % 6 == 0 || position % 6 == 5)) ||
                (inputNumber % 6 == 4 && ((position == getMaxPositionWithRemainderOne(inputNumber, 3)) || position % 6 == 0 || position % 6 == 5)) ||
                (inputNumber % 6 == 5 && (position % 6 == 0 || position % 6 == 5)) ||
                (inputNumber % 6 == 0 && (position % 6 == 0 || position % 6 == 5))) {
            VIEW_TYPE_FULL_WIDTH
        } else {
            VIEW_TYPE_DEFAULT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ThingsHolder{

        var isFullSpan = false
        var layoutId = R.layout.item_things
        if (viewType == 1) {
            layoutId = R.layout.item_things
        } else if (viewType == 0) {
            layoutId = R.layout.item_things
            isFullSpan = true

        }

        val layoutView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        (layoutView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan= isFullSpan
        return ThingsHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return  newsList.size
    }

    override fun onBindViewHolder(holder: ThingsHolder, position: Int) {
        val news = newsList[position]
        holder.onBindNewsItem(news, getItemViewType(position) == VIEW_TYPE_FULL_WIDTH, inputNumber)



        val availableWidth = displayMetrics.widthPixels
        val numColumns = 2
        val viewWidth = availableWidth / numColumns
        val layoutParams = holder.itemView.layoutParams

        if (holder.viewBinding.ivCover.drawable.constantState?.toString() ==
            context.resources.getDrawable(R.drawable.item_3, null).constantState?.toString() ||holder.viewBinding.ivCover.drawable.constantState?.toString() ==
            context.resources.getDrawable(R.drawable.item_4, null).constantState?.toString()) {
            val viewHeight = viewWidth * 1.89
            layoutParams.width = viewWidth
            layoutParams.height = viewHeight.toInt()
        } else if(holder.viewBinding.ivCover.drawable.constantState?.toString() ==
            context.resources.getDrawable(R.drawable.item_2, null).constantState?.toString() ||holder.viewBinding.ivCover.drawable.constantState?.toString() ==
            context.resources.getDrawable(R.drawable.item_5, null).constantState?.toString()) {
            val viewHeight = viewWidth
            layoutParams.width = viewWidth
            layoutParams.height = viewHeight
        }

        holder.itemView.layoutParams = layoutParams

    }



    fun getMaxPositionWithRemainderOne(inputNumber: Int, value: Int): Int {
        var maxPosition = 0
        var maxRemainder = 0
        for (i in 0 until inputNumber) {
            val remainder = i % 6
            if (remainder == value && i > maxPosition) {
                maxPosition = i
                maxRemainder = remainder
            }
        }
        return maxPosition
    }
}