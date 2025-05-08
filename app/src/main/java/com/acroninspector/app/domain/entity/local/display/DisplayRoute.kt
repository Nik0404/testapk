package com.acroninspector.app.domain.entity.local.display

import android.os.Parcel
import android.os.Parcelable

data class DisplayRoute(
        val id: Int,
        val taskId: Int,
        val equipmentId: Int,
        var number: Int,
        val equipmentName: String,
        val equipmentCode: String,
        val equipmentClass: String?,
        val questions: Int,
        var answeredQuestions: Int,
        val nfcMarks: Int,
        val answeredNfcMarks: Int,
        val attachmentsCount: Int,
        val defectsCount: Int
) : Parcelable {

    var startDateActual: String = ""
    var endDateActual: String = ""

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
        startDateActual = parcel.readString()!!
        endDateActual = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(taskId)
        parcel.writeInt(equipmentId)
        parcel.writeInt(number)
        parcel.writeString(equipmentName)
        parcel.writeString(equipmentCode)
        parcel.writeString(equipmentClass)
        parcel.writeInt(questions)
        parcel.writeInt(answeredQuestions)
        parcel.writeInt(nfcMarks)
        parcel.writeInt(answeredNfcMarks)
        parcel.writeInt(attachmentsCount)
        parcel.writeInt(defectsCount)
        parcel.writeString(startDateActual)
        parcel.writeString(endDateActual)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DisplayRoute> {
        override fun createFromParcel(parcel: Parcel): DisplayRoute {
            return DisplayRoute(parcel)
        }

        override fun newArray(size: Int): Array<DisplayRoute?> {
            return arrayOfNulls(size)
        }
    }
}