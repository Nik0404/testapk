package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.display.DisplayDirectory
import io.reactivex.Flowable

interface DirectoryRepository {

    fun getDirectories(parentId: Int): Flowable<List<DisplayDirectory>>

    fun getSearchedDirectories(query: String): Flowable<List<DisplayDirectory>>
}
