package com.example.inertloginmodule.activities

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.inertloginmodule.R

class ImageAdapter internal constructor(private val mContext: Context) : BaseAdapter() {

    // References to our images
    private val mThumbIds = arrayOf(R.drawable.sample_0, R.drawable.sample_1, R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5, R.drawable.sample_6, R.drawable.sample_7)

    override fun getCount(): Int {
        return mThumbIds.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    // Create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
            imageView.layoutParams = ViewGroup.LayoutParams(300, 300)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            imageView = (convertView as ImageView?)!!
        }

        imageView.setImageResource(mThumbIds[position])
        return imageView
    }
}
