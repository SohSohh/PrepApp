package com.example.myapplication.dataAndNetwork

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

public interface questionApi {
    @GET("test/{bio}/{chem}/{comp}/{eng}/{intel}/{math}/{phy}/")
    fun getQuestions(@Path("bio")bio:Int = 0, @Path("chem")chem:Int = 0,@Path("comp")comp:Int = 0, @Path("eng")eng:Int = 0, @Path("intel")intel:Int = 0, @Path("math")math:Int = 0, @Path("phy")phy:Int = 0):Call<List<question>>

    @GET("test/limits/")
    fun getLimits():Call<List<Int>>
}

val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:8000/").addConverterFactory(GsonConverterFactory.create()).build()

val Api = retrofit.create(questionApi::class.java)
