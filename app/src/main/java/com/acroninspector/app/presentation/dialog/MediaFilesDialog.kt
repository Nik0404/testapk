package com.acroninspector.app.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.acroninspector.app.R
import com.acroninspector.app.databinding.DialogMediafilesBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MediaFilesDialog : BottomSheetDialogFragment() {

    interface MediaFilesDialogCallback {

        fun onClickAddVideo()

        fun onClickAddPhoto()

        fun onClickAddAudio()
    }

    private lateinit var mediaFilesDialogCallback: MediaFilesDialogCallback

    private lateinit var binding: DialogMediafilesBinding

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_mediafiles, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCloseDialog.setOnClickListener { dismiss() }

        binding.layoutAddVideo.setOnClickListener {
            mediaFilesDialogCallback.onClickAddVideo()
            dismiss()
        }
        binding.layoutAddPhoto.setOnClickListener {
            mediaFilesDialogCallback.onClickAddPhoto()
            dismiss()
        }
        binding.layoutAddAudio.setOnClickListener {
            mediaFilesDialogCallback.onClickAddAudio()
            dismiss()
        }
    }

    fun showDialog(manager: FragmentManager?, callback: MediaFilesDialogCallback) {
        this.mediaFilesDialogCallback = callback
        show(manager!!, "")
    }
}