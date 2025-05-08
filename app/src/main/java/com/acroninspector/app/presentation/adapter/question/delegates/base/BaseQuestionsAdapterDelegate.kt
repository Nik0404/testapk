package com.acroninspector.app.presentation.adapter.question.delegates.base

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.TooltipCompat
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.MENU_ITEM_CANCEL_ANSWER
import com.acroninspector.app.common.constants.Constants.MENU_ITEM_COMMENT
import com.acroninspector.app.common.constants.Constants.MENU_ITEM_DEFECT
import com.acroninspector.app.common.constants.Constants.MENU_ITEM_MEDIAFILE
import com.acroninspector.app.common.constants.Constants.NO_ANSWER
import com.acroninspector.app.presentation.adapter.question.delegates.value.inprogress.ValueViewHolder
import com.acroninspector.app.presentation.adapter.question.listener.OnClickQuestionListener
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

abstract class BaseQuestionsAdapterDelegate<T>(
        private val clickListener: OnClickQuestionListener
) : AdapterDelegate<T>() {

    private var isPopupMenuOpen = false

    internal fun prepareListPopupWindow(context: Context, holder: IBaseQuestionViewHolder) {
        val listPopupWindow = getListPopupWindow(context, holder.getButtonMenu())
        listPopupWindow.setAdapter(holder.getPopupMenuAdapter())
        listPopupWindow.setOnDismissListener { isPopupMenuOpen = false }

        setClickListenersToButton(holder.getButtonMenu(), {
            if (!isPopupMenuOpen) {
                listPopupWindow.show()
                isPopupMenuOpen = true
            }
            clickListener.onClickMenu()
        }, R.string.tooltip_menu)

        holder.getPopupMenuAdapter().setOnMenuItemClickListener {
            when (it) {
                MENU_ITEM_CANCEL_ANSWER -> {
                    if (holder !is ValueViewHolder) {
                        clickListener.updateAnswer(holder.getQuestionPosition(), NO_ANSWER)
                    } else {
                        holder.resetItemState()
                    }
                    listPopupWindow.dismiss()
                }
                MENU_ITEM_MEDIAFILE -> {
                    clickListener.onClickAttachments(holder.getQuestionPosition())
                    listPopupWindow.dismiss()
                }
                MENU_ITEM_COMMENT -> {
                    clickListener.onClickComment(holder.getQuestionPosition())
                    listPopupWindow.dismiss()
                }
                MENU_ITEM_DEFECT -> {
                    clickListener.onClickEditDefect(holder.getQuestionPosition())
                    listPopupWindow.dismiss()
                }
            }
        }
    }

    private fun getListPopupWindow(context: Context, anchorView: View): ListPopupWindow {
        val listPopupWindow = ListPopupWindow(context)
        listPopupWindow.setBackgroundDrawable(context.getDrawable(
                R.drawable.dialog_with_padding_background
        ))
        listPopupWindow.anchorView = anchorView
        listPopupWindow.isModal = true
        listPopupWindow.width = 750
        listPopupWindow.verticalOffset = 15
        listPopupWindow.horizontalOffset = -675

        return listPopupWindow
    }

    fun setHeaderButtonsListeners(holder: IBaseQuestionViewHolder) {
        setClickListenersToButton(holder.getButtonAttachments(), {
            clickListener.onClickAttachments(holder.getQuestionPosition())
        }, R.string.attachments)
        setClickListenersToButton(holder.getButtonComment(), {
            clickListener.onClickComment(holder.getQuestionPosition())
        }, R.string.comment)
        setClickListenersToButton(holder.getButtonDefectName(), {
            clickListener.onClickEditDefect(holder.getQuestionPosition())
        }, R.string.defect_name)
        setClickListenersToButton(holder.getButtonCriticality(), {
            clickListener.onClickEditDefect(holder.getQuestionPosition())
        }, R.string.defect_criticality)
    }

    private fun setClickListenersToButton(button: AppCompatImageButton, listener: ((View) -> Unit),
                                          @StringRes tooltipTextId: Int) {
        button.setOnClickListener(listener)
        button.setOnLongClickListener {
            TooltipCompat.setTooltipText(it, it.context.getString(tooltipTextId))
            false
        }
    }
}