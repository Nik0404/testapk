package com.acroninspector.app.data.datasource.database.dao

import androidx.room.*
import com.acroninspector.app.domain.entity.local.database.MediaFile
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import com.acroninspector.app.domain.entity.local.other.MediaFileWithHash
import io.reactivex.*

@Dao
interface MediaFileDao {

    @Query("UPDATE media_file SET is_uploaded = 1 WHERE media_file_id = :mediaFileId")
    fun mediaFileUploaded(mediaFileId: Int): Completable

    @Query("UPDATE media_file SET defect_log_id = :defectLogId WHERE media_file_id = :mediaFileId")
    fun updateMediaFileDefectLogId(mediaFileId: Int, defectLogId: Int): Completable

    @Query("""
        SELECT media_file_id AS id,
            media_file.media_type AS mediaType,
            media_file.file_path AS filePath,
            media_file.uri AS uri
        FROM media_file
            LEFT JOIN check_list, route
                ON media_file.check_list_id = check_list.id
        WHERE check_list.route_id = route.id
        AND route.task_id = :taskId
    """
    )
    fun getMediaFilesByTaskId(taskId: Int): Flowable<List<DisplayMediaFile>>

    @Query(
        """
        SELECT media_file_id AS id,
            media_file.media_type AS mediaType,
            media_file.file_path AS filePath,
            media_file.uri AS uri
        FROM media_file
            LEFT JOIN check_list, route
                ON media_file.check_list_id = check_list.id
        WHERE check_list.route_id = route.id
        AND route.task_id = :taskId
        AND media_file.is_uploaded = 1
    """
    )
    fun getMediaFilesByTaskToDelete(taskId: Int): Flowable<List<DisplayMediaFile>>

    @Query(
        """
        SELECT media_file_id AS id,
            media_file.media_type AS mediaType,
            media_file.file_path AS filePath,
            media_file.uri AS uri
        FROM media_file
            LEFT JOIN check_list
                ON media_file.check_list_id = check_list.id
        WHERE check_list.route_id = :routeId
    """
    )
    fun getMediaFilesByRouteId(routeId: Int): Flowable<List<DisplayMediaFile>>

    @Query("""
        SELECT media_file_id AS id,
            media_file.media_type AS mediaType,
            media_file.file_path AS filePath,
            media_file.uri AS uri
        FROM media_file
        WHERE media_file.check_list_id = :checkListId
    """)
    fun getMediaFilesByCheckListId(checkListId: Int): Flowable<List<DisplayMediaFile>>

    @Query("""
        SELECT media_file_id AS id,
            media_file.media_type AS mediaType,
            media_file.file_path AS filePath,
            media_file.uri AS uri
        FROM media_file
        WHERE media_file.defect_log_id = :defectLogId
    """)
    fun getMediaFilesByDefectId(defectLogId: Int): Flowable<List<DisplayMediaFile>>

    @Query("SELECT * FROM media_file WHERE media_file.defect_log_id = :defectLogId")
    fun getDatabaseMediaFilesByDefectLogId(defectLogId: Int): Single<List<MediaFile>>

    @Query("SELECT * FROM media_file WHERE media_file.check_list_id = :checkListId")
    fun getDatabaseMediaFilesByCheckListId(checkListId: Int): Single<List<MediaFile>>

    @Query("SELECT * FROM media_file WHERE media_file_id = :id")
    fun getMediaFileById(id: Int): Single<MediaFile>

    @Query("""
        SELECT 
            media_file_id AS id,
            media_file.media_type AS mediaType,
            media_file.file_path AS filePath,
            media_file.defect_log_id AS defectLogId,
            media_file.check_list_id AS checkListId
        FROM media_file
            LEFT JOIN check_list, route, task
                ON media_file.check_list_id = check_list.id
        WHERE check_list.route_id = route.id
            AND route.task_id = task.id
            AND task.task_status_code = 30
            AND media_file.is_uploaded = 0""")
    fun getMediaFilesInTasksForUploading(): Single<List<MediaFileWithHash>>

    @Query("""
        SELECT 
            media_file_id AS id,
            media_file.media_type AS mediaType,
            media_file.file_path AS filePath,
            media_file.defect_log_id AS defectLogId,
            media_file.check_list_id AS checkListId
        FROM media_file
        WHERE media_file.check_list_id = -999 
            AND media_file.is_uploaded = 0
    """)
    fun getMediaFilesInDefectsForUploading(): Single<List<MediaFileWithHash>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMediaFile(mediaFile: MediaFile): Completable

    @Query("DELETE FROM media_file WHERE media_file_id = :mediaFileId")
    fun deleteMediaFile(mediaFileId: Int): Completable

    @Query("DELETE FROM media_file WHERE media_file.defect_log_id = :localDefectId")
    fun deleteMediaFileByLocalDefectId(localDefectId: Int): Completable

    @Query("""
        DELETE FROM media_file 
        WHERE media_file.is_uploaded = 1 AND media_file.check_list_id
        IN
        (
        SELECT check_list.id 
            FROM check_list
                LEFT JOIN route 
                    ON route.id = check_list.route_id
            WHERE route.task_id = :taskId
        )
    """)
    fun deleteMediaFilesByTaskId(taskId: Int): Completable

    @Delete
    fun deleteMediaFiles(mediaFiles: List<MediaFile>): Completable
}