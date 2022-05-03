@file:Suppress("unused")

package com.max.mvvmsample.utils

import android.content.res.Resources
import java.math.RoundingMode
import java.text.DecimalFormat

fun String.toSafeInt(default: Int): Int {

    if (this.isBlank()) return default

    var result = default

    runCatching {
        this.toInt()
    }.onSuccess {
        result = it
    }.onFailure {
        it.printStackTrace()
    }

    return result
}

fun String.toSafeLong(default: Long): Long {

    if (this.isBlank()) return default

    var result = default

    runCatching {
        this.toLong()
    }.onSuccess {
        result = it
    }.onFailure {
        it.printStackTrace()
    }

    return result
}

fun String.toSafeFloat(default: Float): Float {

    if (this.isBlank()) return default

    var result = default

    runCatching {
        this.toFloat()
    }.onSuccess {
        result = it
    }.onFailure {
        it.printStackTrace()
    }

    return result
}

fun String.toList(
    withSpace: Boolean = false,
    size: Int = 0
): List<String> {

    if (this.isBlank()) return List(size) { "" }

    runCatching {
        this.split(when {
            withSpace -> ", "
            else -> ","
        })
    }.onSuccess {
        return it
    }.onFailure {
        it.printStackTrace()
    }

    return List(size) { "" }
}

fun Int.toByteLH(): ByteArray = byteArrayOf(
    this.toByte(),
    (this shr 8).toByte(),
    (this shr 16).toByte(),
    (this shr 24).toByte()
)

fun Boolean.toInt(): Int = when {
    this -> 1
    else -> 0
}

fun Char.toBoolean(): Boolean = when (this) {
    '1' -> true
    else -> false
}

fun Byte.toBoolean(): Boolean = when (this) {
    (0x01).toByte() -> true
    else -> false
}

fun Byte.to8bitsCharArray(): CharArray = this.toInt().and(0xFF).toString(2).padStart(8, '0').reversed().toCharArray()

fun ByteArray.toInt(): Int = get(2).toInt().and(0xFF) + (get(3).toInt().and(0xFF) shl 8).and(0xFFFF)

fun ByteArray.toHexStringSpace(): String = when {

    isEmpty() -> ""

    else -> StringBuilder().apply {

        this@toHexStringSpace.forEach {

            Integer.toHexString(it.toInt().and(0xFF)).let { hex ->

                append(when {
                    hex.length < 2 -> "0"
                    else -> ""
                })

                append(hex).append(" ")
            }
        }

    }.toString()
}

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Float.toTemperatureString(): String = DecimalFormat("##.#").apply {
    roundingMode = RoundingMode.HALF_UP
    applyPattern("00.00")
}.format(this)

fun String.toFillDataList(
    size: Int
): List<String> = this.toList(
    withSpace = true,
    size = size
).map {

    when {
        it.isBlank() || it == "-1" -> ""
        else -> it
    }
}