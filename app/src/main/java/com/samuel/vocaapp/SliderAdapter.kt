package com.samuel.vocaapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val context: Context, private val items: List<SliderItem>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slideritem, parent, false)
        return SliderAdapterVH(view)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val item = items[position]
        viewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(context, item.imageResId))
    }

    override fun getCount(): Int {
        return items.size
    }

    inner class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageslider_1)
    }
}