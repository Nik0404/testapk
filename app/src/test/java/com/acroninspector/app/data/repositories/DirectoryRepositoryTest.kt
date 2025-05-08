package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.DirectoryDao
import com.acroninspector.app.support.constant.DirectoryConstant
import com.acroninspector.app.support.factory.DirectoryFactory
import com.acroninspector.app.domain.repositories.DirectoryRepository
import io.reactivex.Flowable
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DirectoryRepositoryTest {

    @Mock
    private lateinit var directoryDaoMock: DirectoryDao

    private lateinit var systemUnderTest: DirectoryRepository

    @Before
    fun setUp() {
        systemUnderTest = DirectoryRepositoryImpl(directoryDaoMock)
    }

    @Test
    fun getDirectories_parentIdShouldSuccessfullyBePassedToDao() {
        oneDirectoryResult()
        val parentIdCaptor = ArgumentCaptor.forClass(Int::class.java)

        systemUnderTest.getDirectories(DirectoryConstant.FIRST_LEVEL_DIRECTORY_ID_1).test()
            .dispose()

        verify(directoryDaoMock).getDirectories(parentIdCaptor.capture())
        assertThat(parentIdCaptor.value, `is`(DirectoryConstant.FIRST_LEVEL_DIRECTORY_ID_1))
    }

    @Test
    fun getDirectories_setOfDirectoriesByParentId_setOfDirectoriesShouldBeReturned() {
        setOfDirectoriesResult()

        systemUnderTest.getDirectories(DirectoryConstant.FIRST_LEVEL_DIRECTORY_ID_1).test()
            .assertValue { directories -> directories == DirectoryFactory.getSecondLevelDisplayDirectories() }
            .dispose()
    }

    @Test
    fun getDirectories_noDirectoriesByParentId_nothingShouldBeReturned() {
        noDirectoriesResult()

        systemUnderTest.getDirectories(DirectoryConstant.FIRST_LEVEL_DIRECTORY_ID_1).test()
            .assertNoValues()
            .dispose()
    }

    private fun oneDirectoryResult() {
        `when`(directoryDaoMock.getDirectories(anyInt()))
            .thenReturn(Flowable.just(listOf(DirectoryFactory.getFirstLevelDisplayDirectory())))
    }

    private fun setOfDirectoriesResult() {
        `when`(directoryDaoMock.getDirectories(anyInt()))
            .thenReturn(Flowable.just(DirectoryFactory.getSecondLevelDisplayDirectories()))
    }

    private fun noDirectoriesResult() {
        `when`(directoryDaoMock.getDirectories(anyInt()))
            .thenReturn(Flowable.empty())
    }
}
