package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.DeviceRegistrationColumns
import com.google.gson.annotations.SerializedName

class DeviceRegistrationValue (
    @SerializedName(DeviceRegistrationColumns.DEVICE_ID_COLUMN_NAME) val deviceId: String,
    @SerializedName(DeviceRegistrationColumns.REG_DATE_COLUMN_NAME) val registrationDate: String
) : AcronValue