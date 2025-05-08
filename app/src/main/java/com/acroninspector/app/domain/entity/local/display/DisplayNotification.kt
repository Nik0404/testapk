package com.acroninspector.app.domain.entity.local.display

import android.os.Parcel
import android.os.Parcelable

class DisplayNotification(
        val dateCreation: String,
        val dateOfReading: String,
        val executorName: String?,
        val executorId: Int?,
        val taskId: Int,
        val taskStartDatePlanned: String,
        val taskEndDatePlanned: String,
        val taskStatus: Int,
        val taskNumber: String,
        val taskName: String
) : Parcelable {

    val isNew: Boolean
        get() = dateOfReading.isEmpty()

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dateCreation)
        parcel.writeString(dateOfReading)
        parcel.writeString(executorName)
        parcel.writeValue(executorId)
        parcel.writeInt(taskId)
        parcel.writeString(taskStartDatePlanned)
        parcel.writeString(taskEndDatePlanned)
        parcel.writeInt(taskStatus)
        parcel.writeString(taskNumber)
        parcel.writeString(taskName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DisplayNotification> {
        override fun createFromParcel(parcel: Parcel): DisplayNotification {
            return DisplayNotification(parcel)
        }

        override fun newArray(size: Int): Array<DisplayNotification?> {
            return arrayOfNulls(size)
        }
    }

}