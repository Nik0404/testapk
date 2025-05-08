package com.acroninspector.app.presentation.adapter.notifications

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.databinding.ItemNotificationBinding
import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import timber.log.Timber
import java.util.*

class NotificationsAdapter : RecyclerSwipeAdapter<NotificationsAdapter.ViewHolder>() {

    private lateinit var clickListener: OnClickNotificationListener

    interface OnClickNotificationListener {

        fun onClickNotification(position: Int)

        fun onClickDelete(position: Int)
    }

    fun setClickListener(listener: OnClickNotificationListener) {
        clickListener = listener
    }

    inline fun setOnClickNotificationListener(
            crossinline onClickNotification: (Int) -> Unit,
            crossinline onClickDelete: (Int) -> Unit) {
        setClickListener(object : OnClickNotificationListener {
            override fun onClickNotification(position: Int) = onClickNotification(position)

            override fun onClickDelete(position: Int) = onClickDelete(position)
        })
    }

    private var notificationList: List<DisplayNotification> = LinkedList()

    fun setData(data: List<DisplayNotification>) {
        val diffUtilCallback = NotificationsDiffUtil(notificationList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        notificationList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNotificationBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_notification, parent, false
        )
        val holder = ViewHolder(binding, clickListener)

        binding.deleteNotificationLayout.setOnClickListener { holder.onClickDelete() }
        binding.notificationLayout.setOnClickListener {
            clickListener.onClickNotification(holder.adapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    class ViewHolder(private val binding: ItemNotificationBinding,
                     private val clickListener: OnClickNotificationListener)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: DisplayNotification) {
            binding.notification = notification
            binding.executePendingBindings()

            binding.swipeLayout.isSwipeEnabled = !notification.isNew
        }

        fun onClickDelete() {
            vibrate()
            binding.swipeLayout.close()

            Handler().postDelayed({
                try {
                    clickListener.onClickDelete(adapterPosition)
                } catch (e: IndexOutOfBoundsException) {
                    Timber.e(e)
                }
            }, 400)
        }

        @Suppress("DEPRECATION")
        private fun vibrate() {
            val vibrator = binding.root.context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else vibrator.vibrate(50)
        }
    }
}