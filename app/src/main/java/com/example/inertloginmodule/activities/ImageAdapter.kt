package com.example.inertloginmodule.activities

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import com.example.inertloginmodule.R

class ImageAdapter internal constructor(private val mContext: Context) : BaseAdapter() {

    // References to our images
    private val mThumbIds = arrayOf(R.drawable.emergency, R.drawable.track, R.drawable.security, R.drawable.dearonce, R.drawable.weather, R.drawable.profile, R.drawable.battery, R.drawable.place)


   /* private val itemList: Array<String>
        get() = arrayOf("Emergency","Tracking","Security","DearOnce","Weather","Profile","Battery","Places")*/

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
