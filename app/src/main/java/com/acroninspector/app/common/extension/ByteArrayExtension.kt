package com.acroninspector.app.common.extension

fun ByteArray.toHexString(): String {
    var responseIndex = this.size - 1
    var hexIndex: Int
    var `in`: Int
    val hex =
            arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
    val separator = ":"

    val out = arrayListOf<String>()
    while (responseIndex >= 0) {
        `in` = this[responseIndex].toInt() and 0xff
        hexIndex = `in` shr 4 and 0x0f
        out.add(hex[hexIndex])
        hexIndex = `in` and 0x0f
        out.add(hex[hexIndex])
        out.add(separator)
        --responseIndex
    }

    val reversedHexString = out.joinToString("")
    val reversedHexList = reversedHexString.split(":")

    val hexList = reversedHexList.reversed()
    val hexString = hexList.joinToString(":")

    return hexString.substring(1, hexString.length)
}

fun ByteArray.toDecString(): String {
    var result = 0L
    for (byte in this.reversedArray()) {
        result = (result shl 8) + (byte.toInt() and 0xFF) // Собираем байты в число
    }
    return result.toString()
}