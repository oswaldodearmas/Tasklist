package com.odearmas.tasklist.data.providers

import retrofit2.http.GET
import retrofit2.http.Path

interface QuoteAPIService {
    @GET("{query}")
    suspend fun searchRandomQuote(@Path("query") query:String): RandomQuoteResponse


}
