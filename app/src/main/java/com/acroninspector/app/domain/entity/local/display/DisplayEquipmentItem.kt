package com.acroninspector.app.domain.entity.local.display

import androidx.room.Ignore
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import java.lang.StringBuilder

data class DisplayEquipmentItem(
        val id: Int,
        val directoryId: Int,
        val name: String,
        val className: String?,
        val code: String,
        val subdivision: String?,
        val defectsCount: Int,
        val building: String,
        val additionalInfo: String?
) : DisplayEquipment {

    override fun getEquipmentType() = Constants.EQUIPMENT_ITEM

    @Ignore
    var path: String = ""

    @Ignore
    var nfcMarks: List<NfcEquipment> = ArrayList()

    val hasDefect: Boolean
        get() = defectsCount > 0

    val nfcMarkCodes: String
        get() {
            val stringBuilder = StringBuilder()
            for (nfcMark in nfcMarks) {
                stringBuilder.append(nfcMark.code).append('\n')
            }
            return stringBuilder.toString().trim()
        }

    val nfcMarkNames: String
        get() {
            val stringBuilder = StringBuilder()
            for (nfcMark in nfcMarks) {
                stringBuilder.append(nfcMark.name).append('\n')
            }
            return stringBuilder.toString()
        }
}
