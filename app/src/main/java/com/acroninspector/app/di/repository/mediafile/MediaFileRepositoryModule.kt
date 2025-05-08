package com.acroninspector.app.di.repository.mediafile

import com.acroninspector.app.data.datasource.database.AppDatabase
import com.acroninspector.app.data.datasource.database.dao.MediaFileDao
import com.acroninspector.app.data.datasource.network.FunctionsApi
import com.acroninspector.app.data.repositories.MediaFileRepositoryImpl
import com.acroninspector.app.data.util.mappers.AttachmentMapper
import com.acroninspector.app.di.global.scope.PerScreen
import com.acroninspector.app.di.repository.api.FunctionsApiModule
import com.acroninspector.app.domain.repositories.MediaFileRepository
import dagger.Module
import dagger.Provides

@Module(includes = [FunctionsApiModule::class])
class MediaFileRepositoryModule {

    @PerScreen
    @Provides
    fun provideMediaFileRepository(
            functionsApi: FunctionsApi,
            mediaFilesDao: MediaFileDao,
            attachmentMapper: AttachmentMapper
    ): MediaFileRepository {
        return MediaFileRepositoryImpl(functionsApi, mediaFilesDao, attachmentMapper)
    }

    @PerScreen
    @Provides
    fun provideMediaFilesDao(appDatabase: AppDatabase): MediaFileDao {
        return appDatabase.mediaFileDao()
    }

    @PerScreen
    @Provides
    fun provideAttachmentMapper(): AttachmentMapper {
        return AttachmentMapper()
    }
}