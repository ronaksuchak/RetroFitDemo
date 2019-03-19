package com.example.retrofitdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var mCompositeDisposable: CompositeDisposable? = null
    private var mAndroidArrayList: ArrayList<Android>? = null
    private val BASE_URL = "https://learn2crack-json.herokuapp.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCompositeDisposable = CompositeDisposable()
        loadJSON()
    }

    private fun loadJSON() {

        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RequestInterface::class.java)

        mCompositeDisposable?.add(
            requestInterface.getData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )

    }

    private fun handleResponse(androidList: List<Android>) {

        mAndroidArrayList = ArrayList(androidList)

        for (i in 0 until (androidList.size)){
            val android:Android  = androidList.get(i)
            Log.e("TAG","name: ${android.name} ,ApiLevel: ${android.apiLevel} , version ${android.version}")
        }

    }

    private fun handleError(error: Throwable) {

        Log.d("TAG", error.localizedMessage)

        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
    }

}
