package com.example.inertloginmodule.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.inertloginmodule.R
import com.example.inertloginmodule.models.Service
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.service_entry.view.*


class ProfileActivity : AppCompatActivity() {

    var adapter: ProfileActivity.ServicesAdapter? = null
    var serviceList = ArrayList<Service>()
    lateinit var toolbar: ActionBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

Log.e("log","entered")
        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // load foods
       serviceList.add(Service("Urgency", R.drawable.emergency))
       serviceList.add(Service("Tracking",  R.drawable.track))
       serviceList.add(Service("Security",  R.drawable.security))
       serviceList.add(Service("DearOnce", R.drawable.dearonce))
       serviceList.add(Service("Weather",  R.drawable.weather))
       serviceList.add(Service("Profile",  R.drawable.profile))
       serviceList.add(Service("Battery",  R.drawable.battery))
       serviceList.add(Service("Nearby",  R.drawable.place))
        adapter = ServicesAdapter(this, serviceList)

        gvFoods.adapter = adapter
    }

    class ServicesAdapter : BaseAdapter {
        var serviceList = ArrayList<Service>()
        var context: Context? = null

        constructor(context: Context, foodsList: ArrayList<Service>) : super() {
            this.context = context
            this.serviceList = foodsList
        }

        override fun getCount(): Int {
            return serviceList.size
        }

        override fun getItem(position: Int): Any {
            return serviceList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val food = this.serviceList[position]

            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var serviceView = inflator.inflate(R.layout.service_entry, null)
            serviceView.imgFood.setImageResource(food.image!!)
            serviceView.tvName.text = food.name!!

            return serviceView
        }

    }


    }
