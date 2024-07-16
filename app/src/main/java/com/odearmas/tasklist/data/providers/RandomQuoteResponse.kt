package com.odearmas.tasklist.data.providers

import com.google.gson.annotations.SerializedName

data class RandomQuoteResponse(

    @SerializedName("id") val id: Int,
    @SerializedName("quote") val quote: String,
    @SerializedName("author") val author: String
)


