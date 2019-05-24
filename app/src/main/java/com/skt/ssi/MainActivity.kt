package com.skt.ssi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.system.ErrnoException
import android.system.Os
import android.util.Log
import org.hyperledger.indy.sdk.LibIndy
import org.hyperledger.indy.sdk.wallet.Wallet
import org.json.JSONObject
import java.util.concurrent.ExecutionException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataDir = applicationContext.dataDir
        System.out.println("datadir= ${dataDir.absolutePath}")
        val externalFilesDir = getExternalFilesDir(null)
        val path = externalFilesDir!!.absolutePath
        println("axel externalFilesDir=$path")

        try {
            Os.setenv("EXTERNAL_STORAGE", path, true)
        } catch (e: ErrnoException) {
            e.printStackTrace()
        }

        LibIndy.init()
        val WALLET = "Wallet1"
        val TYPE = "default"
        val WALLET_CREDENTIALS = JSONObject()
            .put("key", "key")
            .toString()
        val WALLET_CONFIG = JSONObject()
            .put("id", WALLET)
            .put("storage_type", TYPE)
            .toString()

        try {
            Wallet.createWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get()
        } catch (e: ExecutionException) {
            println(e.message)
            if (e.message!!.indexOf("WalletExistsException") >= 0) {
                // ignore
            } else {
                throw RuntimeException(e)
            }
        }

        val wallet = Wallet.openWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get()
        Log.d("TEST", "wallet: $wallet")
    }
}
