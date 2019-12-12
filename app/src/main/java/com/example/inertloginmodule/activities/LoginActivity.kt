package com.example.inertloginmodule.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inertloginmodule.R
import com.example.inertloginmodule.api.RetrofitClient
import com.example.inertloginmodule.models.LoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fblogin.setOnClickListener{
        intent = Intent(applicationContext, FbLoginActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {

            val mobile = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if(mobile.isEmpty()){
                editTextEmail.error = "Mobile required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            if(password.isEmpty()){
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.userLogin(mobile, password,"guhgjhj",false)
                    .enqueue(object: Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                            Log.e("registrationAct", t.toString()); }

                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            Log.d("registrationAct-- ", response.body().toString());
                            Toast.makeText(applicationContext, response.body()?.msg, Toast.LENGTH_LONG).show()
                        }              })

        }
    }

    override fun onStart() {
        super.onStart()

        /*if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }*/
    }
}
