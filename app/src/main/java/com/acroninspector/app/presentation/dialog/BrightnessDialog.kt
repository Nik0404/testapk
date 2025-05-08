package com.acroninspector.app.presentation.dialog

import android.app.Dialog
import android.content.ContentResolver
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import com.acroninspector.app.common.utils.BrightnessRegulator
import com.acroninspector.app.databinding.DialogBrightnessBinding
import com.acroninspector.app.domain.entity.local.database.User
import org.jetbrains.anko.sdk27.coroutines.onClick

class BrightnessDialog() : DialogFragment() {

    private lateinit var binding: DialogBrightnessBinding
    private lateinit var brightnessRegulator: BrightnessRegulator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_brightness, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // Убираем заголовок
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brightnessRegulator = BrightnessRegulator(requireActivity())
        binding.btnCloseDialog.setOnClickListener { dismiss() }

        val currentBrightness = getCurrentBrightness()
        binding.brightnessSeekbar.progress = (currentBrightness * 100).toInt() // Установка значения SeekBar

        binding.brightnessSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val brightnessValue = progress / 100f
                brightnessRegulator.setBrightness(brightnessValue)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Действия при начале перемещения ползунка
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Действия при остановке перемещения ползунка
            }
        })
    }

    private fun getCurrentBrightness(): Float {
        return try {
            // Получаем текущее значение яркости из настроек
            val contentResolver: ContentResolver = requireActivity().contentResolver
            val brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
            brightness / 255f // Преобразуем в диапазон 0.0 - 1.0
        } catch (e: Settings.SettingNotFoundException) {
            0.5f // Если не удалось получить значение, возвращаем 50%
        }
    }


    companion object {
        const val TAG = "Acron.Tag.BrightnessDialog"
    }

}