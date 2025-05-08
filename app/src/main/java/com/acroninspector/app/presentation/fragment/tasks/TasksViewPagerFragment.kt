package com.acroninspector.app.presentation.fragment.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.acroninspector.app.R
import com.acroninspector.app.databinding.FragmentTaskViewPagerBinding
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.custom.listener.NetworkStatusHandler
import com.acroninspector.app.presentation.custom.listener.NfcStatusHandler
import com.acroninspector.app.presentation.custom.listener.NotificationsCounterHandler
import com.acroninspector.app.presentation.fragment.tasks.completed.CompletedTasksFragment
import com.acroninspector.app.presentation.fragment.tasks.inprogress.InProgressTasksFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import org.jetbrains.anko.sdk27.coroutines.onClick

class TasksViewPagerFragment : Fragment(), NetworkStatusHandler, NfcStatusHandler, NotificationsCounterHandler {

    private lateinit var binding: FragmentTaskViewPagerBinding

    override fun onResume() {
        super.onResume()
        (activity as? MainView)?.unlockDrawer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_view_pager,
                container, false)

        (activity as? MainActivity)?.setNfcStatusHandler(this)
        (activity as? MainActivity)?.setNetworkStatusHandler(this)
        (activity as? MainActivity)?.setNotificationsCounterHandler(this)

        binding.btnMenu.onClick { (activity as? MainView)?.openDrawer() }
        binding.btnAccount.onClick { (activity as? MainView)?.showUserCardDialog() }
        binding.btnNfc.onClick { (activity as? MainView)?.openNfcSettingsActivity() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FragmentPagerItemAdapter(
                childFragmentManager, FragmentPagerItems.with(context)
                .add(getString(R.string.in_progress), InProgressTasksFragment::class.java)
                .add(getString(R.string.completed), CompletedTasksFragment::class.java)
                .create()
        )

        binding.viewpagerTab.setCustomTabView { container, position, _ ->
            val tabItem = LayoutInflater.from(context)
                    .inflate(R.layout.item_view_pager_tab, container, false)
            val tvTabName: TextView = tabItem.findViewById(R.id.tv_tab_name)

            when (position) {
                0 -> tvTabName.text = getString(R.string.in_process)
                1 -> tvTabName.text = getString(R.string.completed)
            }
            tabItem
        }

        binding.viewpager.adapter = adapter
        binding.viewpagerTab.setViewPager(binding.viewpager)
    }

    override fun handleNetworkStatus(isOnline: Boolean) {
        binding.isNetworkEnabled = isOnline
    }

    override fun handleNfcStatus(isEnabled: Boolean) {
        binding.isNfcEnabled = isEnabled
    }

    override fun handleNotificationsCount(count: Int) {
        if (count > 0) {
            binding.counterNotifications.tvCount.visibility = View.VISIBLE
            binding.counterNotifications.tvCount.text = count.toString()
        } else binding.counterNotifications.tvCount.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.releaseNfcStatusHandler(this)
        (activity as? MainActivity)?.releaseNetworkStatusHandler(this)
        (activity as? MainActivity)?.releaseNotificationsCounterHandler(this)
    }
}
