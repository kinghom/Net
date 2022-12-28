package com.drake.net.sample.bean


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginBean(
    @SerialName("code")
    var code: Int = 0,
    @SerialName("count")
    var count: Int = 0,
    @SerialName("data")
    var data: Data = Data(),
    @SerialName("msg")
    var msg: String = "",
    @SerialName("otherData")
    var otherData: String = ""
) {
    @Serializable
    data class Data(
        @SerialName("loginUrl")
        var loginUrl: String = "",
        @SerialName("token")
        var token: Token = Token()
    ) {
        @Serializable
        data class Token(
            @SerialName("expires_in")
            var expiresIn: Int = 0,
            @SerialName("success")
            var success: Boolean = false,
            @SerialName("token")
            var token: String = "",
            @SerialName("token_type")
            var tokenType: String = ""
        )
    }
}