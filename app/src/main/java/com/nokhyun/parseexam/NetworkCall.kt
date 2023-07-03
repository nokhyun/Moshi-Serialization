package com.nokhyun.parseexam

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface Service {
    @GET("todos/1/")
    fun fetchTodos(): Call<Todo>

    @GET("todos/2/")
    fun fetchTodosSerialization(): Call<Todo>
}

object NetworkCall {

    private val moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    // moshi
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // kotlinx-serialization
    private val contentType = "application/json".toMediaType()
    private val serializationRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    fun<T> moshiService(api: Class<T>): T{
        Log.e("NetworkCall", "[moshiService]")
        return retrofit.create(api)
    }

    fun<T> serializationService(api: Class<T>): T {
        Log.e("NetworkCall", "[serializationService]")
        return serializationRetrofit.create(api)
    }
}