package com.example.optional.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.kpfu_itis_android2023.R
import com.example.kpfu_itis_android2023.ThingsDataModel
import com.example.kpfu_itis_android2023.databinding.ItemThingsBinding


class ThingsHolder(item: View) : RecyclerView.ViewHolder(item)  {

    private var item: ThingsDataModel? = null
    val viewBinding: ItemThingsBinding = ItemThingsBinding.bind(item)

    fun onBindNewsItem(item: ThingsDataModel, isFullSpan: Boolean, inputNumber: Int) = with(viewBinding) {
        if (isFullSpan) {
            ivCover.setImageResource(if (adapterPosition % 2 == 0) R.drawable.item_1 else R.drawable.item_6)
        } else if(inputNumber % 6 == 3 && ((position == getMaxPositionWithRemainderOne(inputNumber, 1))) || (inputNumber % 6 == 4 && ((position == getMaxPositionWithRemainderOne(inputNumber, 1))))){
            ivCover.setImageResource(if (adapterPosition % 2 == 0) R.drawable.item_3 else R.drawable.item_4)

        }
        else {
            item.image?.let { res ->
                ivCover.setImageResource(res)
            }
        }
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