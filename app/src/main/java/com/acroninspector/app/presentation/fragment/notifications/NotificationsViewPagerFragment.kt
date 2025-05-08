package com.acroninspector.app.presentation.fragment.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.acroninspector.app.R
import com.acroninspector.app.databinding.FragmentNotificationsViewPagerBinding
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.custom.listener.NetworkStatusHandler
import com.acroninspector.app.presentation.custom.listener.NfcStatusHandler
import com.acroninspector.app.presentation.fragment.notifications.readed.ReadedNotificationsFragment
import com.acroninspector.app.presentation.fragment.notifications.unreaded.NewNotificationsFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class NotificationsViewPagerFragment : Fragment(), NetworkStatusHandler, NfcStatusHandler {

    private lateinit var binding: FragmentNotificationsViewPagerBinding

    override fun onResume() {
        super.onResume()
        (activity as? MainView)?.unlockDrawer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications_view_pager,
                container, false)

        (activity as? MainActivity)?.setNfcStatusHandler(this)
        (activity as? MainActivity)?.setNetworkStatusHandler(this)

        binding.btnMenu.setOnClickListener { (activity as? MainView)?.openDrawer() }
        binding.btnAccount.setOnClickListener { (activity as? MainView)?.showUserCardDialog() }
        binding.btnNfc.setOnClickListener { (activity as? MainView)?.openNfcSettingsActivity() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FragmentPagerItemAdapter(
                childFragmentManager, FragmentPagerItems.with(context)
                .add(getString(R.string.new_notifications), NewNotificationsFragment::class.java)
                .add(getString(R.string.old_notifications), ReadedNotificationsFragment::class.java)
                .create()
        )

        binding.viewpagerTab.setCustomTabView { container, position, _ ->
            val tabItem = LayoutInflater.from(context)
                    .inflate(R.layout.item_view_pager_tab, container, false)
            val tvTabName: TextView = tabItem.findViewById(R.id.tv_tab_name)

            when (position) {
                0 -> tvTabName.text = getString(R.string.new_notifications)
                1 -> tvTabName.text = getString(R.string.old_notifications)
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

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.releaseNfcStatusHandler(this)
        (activity as? MainActivity)?.releaseNetworkStatusHandler(this)
    }
}