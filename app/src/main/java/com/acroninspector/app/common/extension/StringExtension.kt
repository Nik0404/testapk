package com.acroninspector.app.common.extension

import com.acroninspector.app.common.constants.Constants
import java.util.*

fun String.toId(): Int = try {
    Integer.parseInt(this)
} catch (e: NumberFormatException) {
    Constants.DEFAULT_INVALID_ID
}

fun String.filePathToFileName(): String {
    return try {
        val indexOfSlash = this.lastIndexOf('/')
        this.substring(indexOfSlash + 1, this.length)
    } catch (e: Exception) {
        Calendar.getInstance().timeInMillis.toString()
    }
}

fun String.toMinValue(): Double {
    return if (this.isEmpty()) {
        Constants.DEFAULT_MIN_VALUE
    } else {
        this.numberToDouble()
    }
}

fun String.toMaxValue(): Double {
    return if (this.isEmpty()) {
        Constants.DEFAULT_MAX_VALUE
    } else {
        this.numberToDouble()
    }
}

fun String.numberToDouble(): Double {
    val number = this.deleteSpaces()
    return try {
        val separator: Char = when {
            number.contains(',') -> ','
            number.contains('.') -> '.'
            else -> throw StringIndexOutOfBoundsException()
        }

        val sign = if (number.contains('-')) "-" else ""
        val wholePart = number.getNumberWholePart(separator)
        val fraction = number.getNumberFraction(separator)

        sign.plus(wholePart).plus(".").plus(fraction).toDouble()
    } catch (e: StringIndexOutOfBoundsException) {
        number.toDouble()
    }
}

fun String.isValueAnswerIsValid(): Boolean {
    return this.isNotEmpty() && this != "." && this != "-"
}

fun String.isValueAnswerIsDefect(minValue: Double, maxValue: Double): Boolean {
    val isAnswerValid = this.isValueAnswerIsValid()
    return if (isAnswerValid) {
        this.numberToDouble() < minValue || this.numberToDouble() > maxValue
    } else false
}

fun String.removeZeroFraction(): String {
    return if (this.contains('.')) {
        val zeroFraction = this.substring(this.length - 2, this.length)
        if (zeroFraction == ".0") {
            this.substring(0, this.length - 2)
        } else this
    } else this
}

fun String.wholePartLengthMoreThan(count: Int): Boolean {
    return try {
        return this.getWholePartLength() > count
    } catch (e: Exception) {
        false
    }
}

fun String.fractionLengthMoreThan(count: Int): Boolean {
    return try {
        return this.getNumberFraction('.').length > count
    } catch (e: Exception) {
        false
    }
}

fun String.shortenWholePart(index: Int, symbolsBeforeComma: Int): String {
    return if (this.getNumberWholePart('.').length > symbolsBeforeComma + 1) {
        if (this.contains('-')) {
            this.substring(0, symbolsBeforeComma + 1)
        } else {
            this.substring(0, symbolsBeforeComma)
        }
    } else this.removeAt(index)
}

fun String.shortenFraction(index: Int): String {
    return this.removeAt(index)
}

fun String.deleteSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}

fun String.getWholePartLength(): Int {
    return this.getNumberWholePart('.').length
}

private fun String.getNumberWholePart(separator: Char): String {
    var indexOfComma = this.indexOf(separator)
    if (indexOfComma == -1) {
        indexOfComma = this.length
    }

    return if (this.contains('-')) {
        this.substring(1, indexOfComma)
    } else this.substring(0, indexOfComma)
}

private fun String.getNumberFraction(separator: Char): String {
    val indexOfComma = this.indexOf(separator)
    if (indexOfComma == -1) {
        return ""
    }
    return this.substring(indexOfComma + 1, this.length)
}

private fun String.removeAt(index: Int): String {
    return this.substring(0, index) + this.substring(index + 1)
}
