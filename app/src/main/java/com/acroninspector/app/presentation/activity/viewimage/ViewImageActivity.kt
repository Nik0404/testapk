package com.acroninspector.app.presentation.activity.viewimage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.KEY_FILE_PATH
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_view_image.*
import java.io.File


class ViewImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        Glide.with(this)
            .load(File(intent.getStringExtra(KEY_FILE_PATH)!!))
            .into(image_photo_view)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}
