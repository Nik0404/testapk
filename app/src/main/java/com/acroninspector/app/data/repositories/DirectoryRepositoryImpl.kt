package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.DirectoryDao
import com.acroninspector.app.domain.entity.local.display.DisplayDirectory
import com.acroninspector.app.domain.repositories.DirectoryRepository
import io.reactivex.Flowable

class DirectoryRepositoryImpl(
    private val directoryDao: DirectoryDao
) : DirectoryRepository {

    override fun getDirectories(parentId: Int): Flowable<List<DisplayDirectory>> {
        return directoryDao.getDirectories(parentId)
    }

    override fun getSearchedDirectories(query: String): Flowable<List<DisplayDirectory>> {
        return directoryDao.getSearchedDirectories(query)
    }
}
