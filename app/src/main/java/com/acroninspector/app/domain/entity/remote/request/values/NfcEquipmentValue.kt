package com.acroninspector.app.domain.entity.remote.request.values

import com.acroninspector.app.data.util.constants.NfcEquipmentColumns
import com.google.gson.annotations.SerializedName

class NfcEquipmentValue(
        @SerializedName(NfcEquipmentColumns.EQUIPMENT_ID_COLUMN_NAME) val equipmentId: String,
        @SerializedName(NfcEquipmentColumns.NAME_COLUMN_NAME) val nfcName: String,
        @SerializedName(NfcEquipmentColumns.CODE_COLUMN_NAME) val nfcCode: String
) : AcronValue