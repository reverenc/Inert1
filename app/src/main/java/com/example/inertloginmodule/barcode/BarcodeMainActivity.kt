package com.example.inertloginmodule

import android.content.Intent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.inertloginmodule.R
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.barcode_activity_main.*

class BarcodeMainActivity : AppCompatActivity() {
    var scannedResult: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.barcode_activity_main)

        btnScan.setOnClickListener {
            run {
                IntentIntegrator(this@BarcodeMainActivity).initiateScan();
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                scannedResult = result.contents
                txtValue.text = scannedResult
            } else {
                txtValue.text = "scan failed"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState?.putString("scannedResult", scannedResult)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.let {
            scannedResult = it.getString("scannedResult").toString()
            txtValue.text = scannedResult
        }
    }

}
