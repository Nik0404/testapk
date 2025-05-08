package com.acroninspector.app.domain.entity.local.display

import android.os.Parcelable
import androidx.room.Ignore
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_STATUS
import kotlinx.android.parcel.Parcelize

@Parcelize
class DisplayDefectLog(
    val id: Int,
    val equipmentName: String,
    val equipmentCode: String,
    val timestamp: String,
    val criticality: Int,
    val comment: String
) : Parcelable {

    var attachmentsCount: Int? = 0

    var defectCauseName: String? = ""
    var defectName: String? = ""

    var checkListId: Int = DEFAULT_INVALID_ID

    var taskName: String? = ""
    var taskNumber: String? = ""
    var taskStatus: Int? = DEFAULT_INVALID_STATUS
        set(value) {
            field = value ?: DEFAULT_INVALID_STATUS
        }

    @Ignore
    var isLocalDefect: Boolean = true

    @Ignore
    var isCouldEdit: Boolean = true

//    constructor(parcel: Parcel) : this(
//            parcel.readInt(),
//            parcel.readString()!!,
//            parcel.readString()!!,
//            parcel.readString()!!,
//            parcel.readInt(),
//            parcel.readString()!!) {
//        attachmentsCount = parcel.readValue(Int::class.java.classLoader) as? Int
//        defectCauseName = parcel.readString()
//        defectName = parcel.readString()
//        checkListId = parcel.readInt()
//        taskName = parcel.readString()
//        taskNumber = parcel.readString()
//        isLocalDefect = parcel.readByte() != 0.toByte()
//    }

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeInt(id)
//        parcel.writeString(equipmentName)
//        parcel.writeString(equipmentCode)
//        parcel.writeString(timestamp)
//        parcel.writeInt(criticality)
//        parcel.writeString(comment)
//        parcel.writeValue(attachmentsCount)
//        parcel.writeString(defectCauseName)
//        parcel.writeString(defectName)
//        parcel.writeInt(checkListId)
//        parcel.writeString(taskName)
//        parcel.writeString(taskNumber)
//        parcel.writeByte(if (isLocalDefect) 1 else 0)
//    }

//    override fun describeContents(): Int {
//        return 0
//    }

//    companion object CREATOR : Parcelable.Creator<DisplayDefectLog> {
//        override fun createFromParcel(parcel: Parcel): DisplayDefectLog {
//            return DisplayDefectLog(parcel)
//        }
//
//        override fun newArray(size: Int): Array<DisplayDefectLog?> {
//            return arrayOfNulls(size)
//        }
//    }
}