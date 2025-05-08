package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID

@Entity(tableName = "media_file")
data class MediaFile(
        @ColumnInfo(name = "media_type") var mediaType: Int,
        @ColumnInfo(name = "file_path") var filePath: String,
        @ColumnInfo(name = "uri") var uri: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "media_file_id")
    var id: Int = 0

    @ColumnInfo(name = "check_list_id")
    var checkListId: Int = DEFAULT_INVALID_ID

    @ColumnInfo(name = "defect_log_id")
    var defectLogId: Int = DEFAULT_INVALID_ID

    @ColumnInfo(name = "is_uploaded")
    var isUploaded: Boolean = false
}