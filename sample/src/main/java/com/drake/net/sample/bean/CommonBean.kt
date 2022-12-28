package com.drake.net.sample.bean


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonBean(
    @SerialName("code")
    var code: Int = 0,
    @SerialName("count")
    var count: Int = 0,
    @SerialName("data")
    var data: String = "",
    @SerialName("msg")
    var msg: String = "",
    @SerialName("otherData")
    var otherData: String = ""
)