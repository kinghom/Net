package com.drake.net.sample.bean


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginBean1(
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