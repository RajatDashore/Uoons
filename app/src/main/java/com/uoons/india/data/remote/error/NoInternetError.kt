package com.uoons.india.data.remote.error

import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class NoInternetError(override val data: Any? = null, override val statusCode: Int, override val message: String) : Failure(data, statusCode, message)