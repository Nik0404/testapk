package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.RouteColumns
import com.google.gson.annotations.SerializedName

data class RouteValue(
        @SerializedName(RouteColumns.ID_COLUMN_NAME) val routeId: String,
        @SerializedName(RouteColumns.ORDER_NUMBER_COLUMN_NAME) val orderNumber: String,
        @SerializedName(RouteColumns.COUNT_ANSWER_COLUMN_NAME) val countAnswers: String,
        @SerializedName(RouteColumns.COUNT_NFC_ANS_COLUMN_NAME) val countAnsweredNfcMarks: String,
        @SerializedName(RouteColumns.STATUS_CODE_COLUMN_NAME) val statusCode: String,
        @SerializedName(RouteColumns.FACT_START_DATE_COLUMN_NAME) val actualStartDate: String,
        @SerializedName(RouteColumns.FACT_END_DATE_COLUMN_NAME) val actualEndDate: String
) : AcronValue