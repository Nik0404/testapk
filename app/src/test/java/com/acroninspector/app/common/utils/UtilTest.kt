package com.acroninspector.app.common.utils

import org.jetbrains.anko.collections.forEachWithIndex
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilTest {

    @Test
    fun testStringToDouble() {
        val numberString = "5125,30"

        val numberDouble = try {
            val indexOfComma = numberString.indexOf(',')

            val wholePart = numberString.substring(0, indexOfComma)
            val fraction = numberString.substring(indexOfComma + 1, numberString.length)

            wholePart.plus(".").plus(fraction).toDouble()
        } catch (e: StringIndexOutOfBoundsException) {
            numberString.toDouble()
        }

        assertEquals("Double:", numberDouble, 5125.30, 0.1)
    }

    @Test
    fun testStringToByteArray() {
        val byteArrayString = "2,-3,-6,0,0,1,40"

        val parsedString = byteArrayString.split(",")
        val content = ByteArray(parsedString.size)
        parsedString.forEachWithIndex { index, byteString ->
            content[index] = byteString.toByte()
        }

        assertEquals(2.toByte(), content[0])
        assertEquals((-6).toByte(), content[2])
        assertEquals(0.toByte(), content[4])
        assertEquals(40.toByte(), content[6])
    }
}