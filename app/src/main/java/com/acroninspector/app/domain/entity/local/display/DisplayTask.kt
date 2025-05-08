package com.acroninspector.app.domain.entity.local.display

import android.os.Parcel
import android.os.Parcelable

data class DisplayTask(
        val id: Int,
        val executorGroupId: Int,
        val number: String,
        val name: String,
        var executorId: Int,
        var executorName: String?,
        val startDatePlanned: String,
        val endDatePlanned: String,
        var checkLists: Int,
        var defectsCount: Int,
        val attachmentsCount: Int,
        var status: Int,
        val notificationDate: String,
        val deviceIdOnStart: String,
        val deviceIdOnFinish: String
) : Parcelable {

    var startDateActual: String = ""
    var endDateActual: String = ""

    var comment: String = ""

    var unansweredCheckLists: Int = 0
        get() = checkLists - field

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
    ) {
        startDateActual = parcel.readString()!!
        endDateActual = parcel.readString()!!
        comment = parcel.readString()!!
        unansweredCheckLists = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(executorGroupId)
        parcel.writeString(number)
        parcel.writeString(name)
        parcel.writeInt(executorId)
        parcel.writeString(executorName)
        parcel.writeString(startDatePlanned)
        parcel.writeString(endDatePlanned)
        parcel.writeInt(checkLists)
        parcel.writeInt(defectsCount)
        parcel.writeInt(attachmentsCount)
        parcel.writeInt(status)
        parcel.writeString(notificationDate)
        parcel.writeString(startDateActual)
        parcel.writeString(endDateActual)
        parcel.writeString(comment)
        parcel.writeInt(unansweredCheckLists)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DisplayTask> {
        override fun createFromParcel(parcel: Parcel): DisplayTask {
            return DisplayTask(parcel)
        }

        override fun newArray(size: Int): Array<DisplayTask?> {
            return arrayOfNulls(size)
        }
    }

}