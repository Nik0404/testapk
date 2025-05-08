package com.acroninspector.app.domain.interactors.controlprocedure

import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import io.reactivex.Completable
import io.reactivex.Single

interface ControlProcedureInteractor {

    fun getControlProceduresSortedByNumber(taskId: Int): Single<List<DisplayControlProcedure>>

    fun replaceControlProcedures(displayControlProcedures: List<DisplayControlProcedure>): Completable
}