package com.skt.ssi

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.skt.ssi.indy.UserData
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
    }

    override fun onDestroy() {
        super.onDestroy()
        userData.destroy()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
