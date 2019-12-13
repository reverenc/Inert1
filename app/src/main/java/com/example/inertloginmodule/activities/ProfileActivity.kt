package com.example.inertloginmodule.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.inertloginmodule.InlineScanActivity
import com.example.inertloginmodule.R
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    private val itemList: Array<String>
        get() = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


/*
       btnClick.setOnClickListener(View.OnClickListener {
           intent = Intent(applicationContext, LoginActivity::class.java)
           startActivity(intent)
       })*/

        val gridview = findViewById<GridView>(R.id.gridview)
//   gridview.adapter = ImageAdapter(this)

        val adapter = ImageListAdapter(this, R.layout.list_item, itemList)
        gridview.adapter = adapter


        gridview.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
           /* Toast.makeText(this@ProfileActivity, " Clicked Position: " + (position + 1),
                Toast.LENGTH_SHORT).show()*/

            if(position==0) {
                intent = Intent(applicationContext, UpdationActivity::class.java)
                startActivity(intent)
            }else if(position==1)
            {
                intent = Intent(applicationContext,UpdationActivity::class.java)
                startActivity(intent)
            }
            else if(position==2)
            {
                intent = Intent(applicationContext,InlineScanActivity::class.java)
                startActivity(intent)
            }
            else if(position==3)
            {
                intent = Intent(applicationContext,UpdationActivity::class.java)
                startActivity(intent)
            }
            else if(position==4)
            {
                intent = Intent(applicationContext,UpdationActivity::class.java)
                startActivity(intent)
            }
            else if(position==5)
            {
                intent = Intent(applicationContext,UpdationActivity::class.java)
                startActivity(intent)
            }
            else if(position==6)
            {
                intent = Intent(applicationContext,UpdationActivity::class.java)
                startActivity(intent)
            }
            else if(position==7)
            {
                intent = Intent(applicationContext,UpdationActivity::class.java)
                startActivity(intent)
            }
            else{
                intent = Intent(applicationContext, UpdationActivity::class.java)
                startActivity(intent)
            }
        }
    }
}