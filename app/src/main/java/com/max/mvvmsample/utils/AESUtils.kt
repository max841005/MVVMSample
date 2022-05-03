@file:Suppress("unused")

package com.max.mvvmsample.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8

object AESUtils {

    @Suppress("SpellCheckingInspection")
    private val SECRET_KEY = "bcsbc8rb1sbcU7sEb3AwUqc8rbsa7832".toByteArray(UTF_8)
    @Suppress("SpellCheckingInspection")
    private val IV_PARAMETER_KEY = "sbc8rbbqhu83gfci".toByteArray(UTF_8)

    fun encrypt(
        value: String
    ): String = Base64.encodeToString(cipher(ENCRYPT_MODE).doFinal(value.toByteArray(UTF_8)), 0)

    fun decrypt(
        value: String
    ): String = String(cipher(DECRYPT_MODE).doFinal(Base64.decode(value.toByteArray(UTF_8), 0)))

    private fun cipher(
        mode: Int
    ): Cipher {

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

        cipher.init(
            mode,
            SecretKeySpec(SECRET_KEY, "AES"),
            IvParameterSpec(IV_PARAMETER_KEY)
        )

        return cipher
    }
}