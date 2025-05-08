package com.acroninspector.app.presentation.fragment.mediafiles

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_TYPE
import com.acroninspector.app.common.constants.Constants.ENTITY_CHECK_LIST
import com.acroninspector.app.common.constants.Constants.ENTITY_DEFECT_LOG
import com.acroninspector.app.common.constants.Constants.KEY_CREATING_DEFECT
import com.acroninspector.app.common.constants.Constants.KEY_ENABLED_EDITING
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_ID
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_TYPE
import com.acroninspector.app.common.constants.Constants.KEY_FILE_MEDIA_TYPE
import com.acroninspector.app.common.constants.Constants.KEY_FILE_PATH
import com.acroninspector.app.common.constants.Constants.KEY_FILE_URI
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_AUDIO
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_PHOTO
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_VIDEO
import com.acroninspector.app.databinding.FragmentAttachmentsBinding
import com.acroninspector.app.di.fragment.mediafiles.MediaFilesModule
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import com.acroninspector.app.domain.entity.local.other.AcronFile
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.mediafiles.MediaFilesAdapter
import com.acroninspector.app.presentation.dialog.MediaFilesDialog
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Lazy
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.sdk27.coroutines.onClick
import javax.inject.Inject


class MediaFilesFragment : BaseFragment(), MediaFilesDialog.MediaFilesDialogCallback,
        MediaFilesView {

    private lateinit var binding: FragmentAttachmentsBinding

    private lateinit var navController: NavController

    private lateinit var rxPermissions: RxPermissions

    private var permissionsDisposable: Disposable? = null

    @Inject
    lateinit var mediaFilesDialog: Lazy<MediaFilesDialog>

    @Inject
    lateinit var mediaFilesAdapter: MediaFilesAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: MediaFilesPresenter

    @ProvidePresenter
    fun providePresenter(): MediaFilesPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass, MediaFilesModule(activity?.applicationContext!!))
                .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
        rxPermissions = RxPermissions(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getDataBindingView(R.layout.fragment_attachments, inflater, container)
        binding.handler = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        binding.btnBack.onClick { navController.popBackStack() }

        val isEnabledEditing = arguments?.getBoolean(KEY_ENABLED_EDITING, false)!!
        val entityType = arguments?.getInt(KEY_ENTITY_TYPE, DEFAULT_INVALID_TYPE)!!
        prepareFab(isEnabledEditing, entityType)

        presenter.entityType = entityType
        presenter.entityId = arguments?.getInt(KEY_ENTITY_ID, DEFAULT_INVALID_ID)!!

        presenter.isCreatingDefect = arguments?.getBoolean(KEY_CREATING_DEFECT, false)!!

        mediaFilesAdapter.isSwipeEnabled = isEnabledEditing
    }

    private fun prepareFab(isEnabledEditing: Boolean, entityType: Int) {
        if (isEnabledEditing) {
            when (entityType) {
                ENTITY_DEFECT_LOG -> binding.fabAddAttachment.show()
                ENTITY_CHECK_LIST -> binding.fabAddAttachment.show()
                else -> binding.fabAddAttachment.hide()
            }
        } else binding.fabAddAttachment.hide()
    }

    private fun initViews() {
        binding.recyclerAttachments.layoutManager = LinearLayoutManager(context!!)
        binding.recyclerAttachments.adapter = mediaFilesAdapter

        mediaFilesAdapter.setOnItemClickListener(
                onClickMediaFile = { presenter.onClickMediaFile(it) },
                onClickDeleteFile = { presenter.onClickDeleteFile(it) }
        )
    }

    override fun openDefaultVideoRecorder(file: AcronFile) {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, file.uri)
            .putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(intent, MEDIA_TYPE_VIDEO)
        } else {
            showSnackbar(R.string.you_has_no_video_recorder)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MEDIA_TYPE_PHOTO || requestCode == MEDIA_TYPE_VIDEO) {
                presenter.insertMediaFile(requestCode)
            } else if (requestCode == MEDIA_TYPE_AUDIO) {
                presenter.insertAudioFile(data?.data!!)
            }
        }
    }

    override fun updateMediaFiles(mediaFiles: List<DisplayMediaFile>) {
        mediaFilesAdapter.setData(mediaFiles)
    }

    override fun onClickAddPhoto() = presenter.onClickAddPhoto()

    override fun onClickAddVideo() = presenter.onClickAddVideo()

    override fun onClickAddAudio() = presenter.onClickAddAudio()

    override fun openDefaultMediaRecorder(intent: Intent, requestCode: Int) {
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(intent, requestCode)
        } else {
            showSnackbar(R.string.you_has_no_audio_recorder)
        }
    }

    override fun openImageViewer(filePath: String) {
        val args = Bundle()
        args.putString(KEY_FILE_PATH, filePath)

        navController.navigate(R.id.action_mediaFilesFragment_to_viewImageActivity, args)
    }

    override fun openPlayer(uri: String, mediaType: Int) {
        val args = Bundle()
        args.putParcelable(KEY_FILE_URI, Uri.parse(uri))
        args.putInt(KEY_FILE_MEDIA_TYPE, mediaType)

        navController.navigate(R.id.action_mediaFilesFragment_to_mediaPlayerActivity, args)
    }

    override fun showMediaFilesDialog() {
        permissionsDisposable = rxPermissions.requestEachCombined(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe { permissions ->
            if (permissions.granted) {
                mediaFilesDialog.get().showDialog(childFragmentManager, this)
            } else showSnackbar(R.string.error_permissions)
        }
    }

    override fun showSnackbar(resourceId: Int) {
        (activity as? MainView)?.showSnackbar(resourceId)
    }

    override fun showSnackbar(message: String) {
        (activity as? MainView)?.showSnackbar(message)
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun showEmptyState() {
        binding.layoutEmptyState.visibility = View.VISIBLE
        binding.tvEmptyStateTitle.visibility = View.VISIBLE

        val isEnabledEditing = arguments?.getBoolean(KEY_ENABLED_EDITING, false)!!
        if (isEnabledEditing) {
            binding.tvEmptyStateDetails.visibility = View.VISIBLE
        } else {
            binding.tvEmptyStateDetails.visibility = View.INVISIBLE
        }
    }

    override fun hideEmptyState() {
        binding.layoutEmptyState.visibility = View.INVISIBLE
    }

    override fun onDetach() {
        super.onDetach()
        permissionsDisposable?.dispose()

        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}
