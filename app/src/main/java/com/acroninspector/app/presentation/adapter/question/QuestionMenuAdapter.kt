package com.acroninspector.app.presentation.adapter.question

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.MENU_ITEM_CANCEL_ANSWER
import com.acroninspector.app.common.constants.Constants.MENU_ITEM_COMMENT
import com.acroninspector.app.common.constants.Constants.MENU_ITEM_DEFECT
import com.acroninspector.app.common.constants.Constants.MENU_ITEM_MEDIAFILE

class QuestionMenuAdapter(
        private val context: Context
) : BaseAdapter() {

    private lateinit var onClickItem: (Int) -> Unit

    fun setOnMenuItemClickListener(listener: (Int) -> Unit) {
        onClickItem = listener
    }

    data class SimpleMenuItem(val id: Int, val icon: Int, var title: Int)

    private val menuItemCancelAnswer = SimpleMenuItem(MENU_ITEM_CANCEL_ANSWER, R.drawable.ic_close_circle, R.string.menu_cancel_answer)
    private val menuItemMediaFile = SimpleMenuItem(MENU_ITEM_MEDIAFILE, R.drawable.ic_attachments, R.string.menu_add_file)
    private val menuItemComment = SimpleMenuItem(MENU_ITEM_COMMENT, R.drawable.ic_comment, R.string.menu_add_comment)
    private val menuItemEditDefect = SimpleMenuItem(MENU_ITEM_DEFECT, R.drawable.ic_defect_list_dark, R.string.edit_defect)

    private val menuItems = mutableListOf(
            menuItemMediaFile,
            menuItemComment
    )

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return menuItems.size
    }

    override fun getItem(position: Int): SimpleMenuItem? {
        return menuItems[position]
    }

    override fun getItemId(position: Int): Long {
        return menuItems[position].icon.toLong()
    }

    @SuppressLint("TaskViewHolder", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.item_popup_menu_simple, parent, false)
        val menuItem = getItem(position)

        val icon = view.findViewById(R.id.iv_icon) as AppCompatImageView
        val title = view.findViewById(R.id.tv_title) as AppCompatTextView

        icon.setImageDrawable(ContextCompat.getDrawable(context, menuItem?.icon!!))

        title.text = context.resources.getString(menuItem.title)

        view.setOnClickListener { onClickItem(menuItem.id) }
        return view
    }

    fun setCancelAnswerVisibility(isAnswered: Boolean) {
        if (isAnswered && !menuItems.contains(menuItemCancelAnswer)) {
            menuItems.add(0, menuItemCancelAnswer)
        } else if (!isAnswered) {
            menuItems.remove(menuItemCancelAnswer)
        }

        updateMenu()
    }

    fun setEditDefectVisibility(isVisible: Boolean) {
        if (isVisible && !menuItems.contains(menuItemEditDefect))
            menuItems.add(menuItemEditDefect)
        else if (!isVisible) {
            menuItems.remove(menuItemEditDefect)
        }

        updateMenu()
    }

    fun setMenuCommentTitle(hasComment: Boolean) {
        menuItemComment.title = if (hasComment) {
            R.string.menu_edit_comment
        } else R.string.menu_add_comment

        updateMenu()
    }

    private fun updateMenu() {
        Handler().postDelayed({
            notifyDataSetChanged()
        }, 250)
    }
}