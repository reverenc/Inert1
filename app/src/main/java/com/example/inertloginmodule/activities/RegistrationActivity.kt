package com.example.inertloginmodule.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inertloginmodule.R
import com.example.inertloginmodule.api.RetrofitClient
import com.example.inertloginmodule.models.DefaultResponse
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.editTextEmail
import kotlinx.android.synthetic.main.activity_registration.editTextPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        buttonSignUp.setOnClickListener {

            val username = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val mobile = editTextName.text.toString().trim()
            val emailid = editTextSchool.text.toString().trim()


            if (username.isEmpty()) {
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            if (password.isEmpty()) {
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            if (mobile.isEmpty()) {
                editTextName.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            if (emailid.isEmpty()) {
                editTextSchool.error = "School required"
                editTextSchool.requestFocus()
                return@setOnClickListener
            }


            RetrofitClient.instance.apicall(username, password, mobile, emailid)
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                            Log.e("registrationAct", t.toString()); }

                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            Log.d("registrationAct-- ", response.body().toString());
                            Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                        }

                    })

            buttonSignUp.setOnClickListener{
                intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onStart() {
        super.onStart()

    }
}
