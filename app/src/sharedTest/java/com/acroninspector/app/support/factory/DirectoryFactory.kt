package com.acroninspector.app.support.factory

import com.acroninspector.app.support.constant.DirectoryConstant
import com.acroninspector.app.domain.entity.local.database.Directory
import com.acroninspector.app.domain.entity.local.display.DisplayDirectory

object DirectoryFactory {

    fun getFirstLevelDisplayDirectory(): DisplayDirectory {
        return DisplayDirectory(
            DirectoryConstant.FIRST_LEVEL_DIRECTORY_ID_1,
            DirectoryConstant.FIRST_LEVEL_DIRECTORY_NAME_1,
            DirectoryConstant.FIRST_LEVEL_DIRECTORY_PARENT_ID_1,
            DirectoryConstant.FIRST_LEVEL_DIRECTORY_END_LVL_1
        )
    }

    fun getSecondLevelDisplayDirectories(): List<DisplayDirectory> {
        val d1 = DisplayDirectory(
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_ID_1,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_NAME_1,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_PARENT_ID_1,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_END_LVL_1
        )

        val d2 = DisplayDirectory(
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_ID_2,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_NAME_2,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_PARENT_ID_2,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_END_LVL_2
        )

        return listOf(d1, d2)
    }

    fun getThirdLevelDisplayDirectories(): List<DisplayDirectory> {
        val d1 = DisplayDirectory(
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_ID_1,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_NAME_1,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_PARENT_ID_1,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_END_LVL_1
        )

        val d2 = DisplayDirectory(
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_ID_2,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_NAME_2,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_PARENT_ID_2,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_END_LVL_2
        )

        return listOf(d1, d2)
    }

    fun getFirstLevelDirectory(): Directory {
        return Directory(
            DirectoryConstant.FIRST_LEVEL_DIRECTORY_ID_1,
            DirectoryConstant.FIRST_LEVEL_DIRECTORY_NAME_1, "",
            DirectoryConstant.FIRST_LEVEL_DIRECTORY_PARENT_ID_1,
            DirectoryConstant.FIRST_LEVEL_DIRECTORY_END_LVL_1
        )
    }

    fun getSecondLevelDirectories(): List<Directory> {
        val d1 = Directory(
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_ID_1,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_NAME_1, "",
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_PARENT_ID_1,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_END_LVL_1
        )

        val d2 = Directory(
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_ID_2,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_NAME_2, "",
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_PARENT_ID_2,
            DirectoryConstant.SECOND_LEVEL_DIRECTORY_END_LVL_2
        )

        return listOf(d1, d2)
    }

    fun getThirdLevelDirectories(): List<Directory> {
        val d1 = Directory(
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_ID_1,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_NAME_1, "",
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_PARENT_ID_1,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_END_LVL_1
        )

        val d2 = Directory(
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_ID_2,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_NAME_2, "",
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_PARENT_ID_2,
            DirectoryConstant.THIRD_LEVEL_DIRECTORY_END_LVL_2
        )

        return listOf(d1, d2)
    }
}
