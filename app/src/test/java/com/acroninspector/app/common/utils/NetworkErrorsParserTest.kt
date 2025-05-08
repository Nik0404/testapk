package com.acroninspector.app.common.utils

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NetworkErrorsParserTest {

    private val networkErrorsParser = NetworkErrorsParser()

    private fun getFakeErrorMessage(): String {
        return "Error message"
    }

    private fun getFakeCorrectData(): String {
        return """  {
                      "errorCode": 0,
                      "errorUserMessage": "${getFakeErrorMessage()}",
                      "errorLog": "Error log"
                    }
               """
    }

    private fun getFakeIncorrectData(): String {
        return """  {
                      "errorCode": 0,
                    }
               """
    }

    @Test
    fun parseCorrectData() {
        val errorMessage = networkErrorsParser.parseErrorMessage(getFakeCorrectData())
        Assert.assertEquals(errorMessage, getFakeErrorMessage())
    }

    @Test
    fun parseIncorrectData() {
        val errorMessage = networkErrorsParser.parseErrorMessage(getFakeIncorrectData())
        Assert.assertEquals(errorMessage, "")
    }
}