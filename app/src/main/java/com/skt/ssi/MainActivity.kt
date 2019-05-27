package com.skt.ssi

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.integration.android.IntentIntegrator
import com.skt.ssi.indy.UserData
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.hyperledger.indy.sdk.LibIndy


class MainActivity : AppCompatActivity() {

    private val userData = UserData()
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "init lib-indy")
        LibIndy.init()
        userData.init(applicationContext)

        initUi()
    }

    private fun initUi() {
        qrScan.setOnClickListener{ readQrCode() }
    }

    @SuppressLint("CheckResult")
    private fun readQrCode() {
        TedRxOnActivityResult.with(this)
            .startActivityForResult(IntentIntegrator(this).createScanIntent())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.resultCode == RESULT_OK) {
                    val result = IntentIntegrator.parseActivityResult(it.resultCode, it.data)
                    val text = result.contents
                    qrText.text = text
                    Log.d(TAG, "QR code: $text")
                }
            }, {
                Log.e(TAG, "error on code scanning")
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        userData.destroy()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
