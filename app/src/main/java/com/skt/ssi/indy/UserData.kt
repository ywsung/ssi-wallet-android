package com.skt.ssi.indy

import android.content.Context
import android.system.ErrnoException
import android.system.Os
import android.util.Log
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.hyperledger.indy.sdk.wallet.Wallet
import org.json.JSONObject


class UserData {

    var init = false
        private set

    lateinit var wallet: Wallet
        private set

    private val compositeDisposable = CompositeDisposable()

    fun init(applicationContext: Context) {
        initInternal(applicationContext)
        init = true
    }

    fun destroy() {
        compositeDisposable.clear()
    }

    private fun initInternal(applicationContext: Context) {
        initStorage(applicationContext)

        Single.fromFuture(Wallet.createWallet(WALLET_CONFIG, WALLET_CREDENTIALS))
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "create wallet completed")
            }, {
                Log.e(TAG, "createWallet: ${it.message}")
            })
            .let {
                compositeDisposable.add(it)
            }
    }

    private fun initStorage(applicationContext: Context) {
        val dataDir = applicationContext.dataDir
        Log.d(TAG, "dataDir: ${dataDir.absolutePath}")
        val externalFilesDir = applicationContext.getExternalFilesDir(null)
        val path = externalFilesDir?.absolutePath
        Log.d(TAG, "externalFilesDir: $path")

        try {
            Os.setenv("EXTERNAL_STORAGE", path, true)
        } catch (e: ErrnoException) {
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = UserData::class.java.simpleName

        // constants
        private val WALLET_CREDENTIALS=
            JSONObject()
                .put("key", "key")
                .toString()
        private val WALLET_CONFIG =
            JSONObject()
                .put("id", "MyWallet")
                .put("storage_type", "default")
                .toString()
    }
}