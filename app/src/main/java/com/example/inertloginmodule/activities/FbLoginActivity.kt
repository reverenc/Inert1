package com.example.inertloginmodule.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inertloginmodule.R
import com.example.inertloginmodule.models.LoginResponse
import com.example.kotlinfacebookloginwithtoken.api.service.MyClient

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult

import org.json.JSONException
import org.json.JSONObject

import java.util.Arrays

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FbLoginActivity:AppCompatActivity() {
    private var loginButton:LoginButton? = null
    private var displayImage:ImageView? = null
    private var displayName:TextView? = null
    private var emailId:TextView? = null

    private var callbackManager:CallbackManager? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_login)

        loginButton = findViewById(R.id.login_button1)
        displayName = findViewById(R.id.profile_name)
        emailId = findViewById(R.id.profile_email)
        displayImage = findViewById(R.id.profile_pic)

        loginButton!!.setReadPermissions(Arrays.asList("email", "public_profile"))
        callbackManager = CallbackManager.Factory.create()

        loginButton!!.registerCallback(callbackManager, object:FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult:LoginResult) {
                val accessToken = loginResult.accessToken
                userLoginInformation(accessToken)
                getToken()
                insertData()
                intent = Intent(applicationContext, ProfileActivity::class.java)
                startActivity(intent)
            }

            override fun onCancel() {

            }

            override fun onError(error:FacebookException) {

            }
        })
    }
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun userLoginInformation(newAccessToken:AccessToken) {
        val request = GraphRequest.newMeRequest(newAccessToken
        ) { `object`, response ->
            try {
                name = `object`.getString("name")
                email = `object`.getString("email")
                val image = `object`.getJSONObject("picture").getJSONObject("data").getString("url")//"https://graph.facebook.com/"+id+ "/picture?type=normal";
                insertData()
                emailId!!.text = email
                displayName!!.text = name

                insertData()
            } catch (e:JSONException) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,picture.width(200)")
        request.parameters = parameters
        request.executeAsync()

        /*intent = Intent(applicationContext, ProfileActivity::class.java)
        startActivity(intent)*/
    }

    private fun getToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this@FbLoginActivity, object:OnSuccessListener<InstanceIdResult> {
            override fun onSuccess(instanceIdResult:InstanceIdResult) {
                val newToken = instanceIdResult.token
                Log.e("newToken", newToken)
                token = newToken
            }
        })
    }

    private fun insertData() {
        /*    val call: Call<ResponseBody?>? = MyClient.instance?.insertdata(email,token, alreadyUser)
            call?.enqueue(object:Callback<ResponseBody> {
                override fun onResponse(call:Call<ResponseBody>, response:Response<ResponseBody>) {
                    Log.d("tocheck1", response.body().toString());
                    Toast.makeText(this@FbLoginActivity, "Data inserted succesfully..", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call:Call<ResponseBody>, t:Throwable) {
                    Log.e("tocheck2", t.toString());
                    Toast.makeText(this@FbLoginActivity, "Data Failed", Toast.LENGTH_LONG).show()
                }
            })*/
        var msg="this is insertion message"
        Log.d("registrationAct",msg.toString());
        MyClient.instance.insertdata(email,token,true)
            .enqueue(object: Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    Log.d("registrationAct", t.toString()); }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    Log.d("registrationAct-- ", response.body().toString());
                    Toast.makeText(applicationContext, response.body()?.msg, Toast.LENGTH_LONG).show()
                }              })

    }


    companion object {
         var email: String? = null
         var name:String? = null
         var token:String? = null
        var alreadyUser = true
    }
}

private fun <T> Call<T>?.enqueue(callback: Callback<ResponseBody>) {

}
