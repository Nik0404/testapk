package com.acroninspector.app.presentation.fragment.equipments

import com.acroninspector.app.domain.entity.local.display.DisplayEquipment
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface EquipmentBaseView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openSearchFragment()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefineNfcFragment(equipmentId: Int, equipmentScreen: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDefectListFragment(equipmentId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openRegisterDefectFragment(equipmentId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEquipmentByPassDialog(equipmentId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEquipmentNfcDialog(equipmentId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNfcErrorDialog(nfcCode: String)

    fun updateEquipments(equipments: List<DisplayEquipment>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmptyState()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideEmptyState()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showEquipmentItemsByDirectory(directoryId: Int, directoryName: String)
}
