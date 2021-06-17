package com.example.fixx.techOrderDetailsScreen.views

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import com.example.fixx.R
import com.example.fixx.constants.Constants

class ImageFullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_screen)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val image = intent.getParcelableExtra(Constants.TRANS_IMAGES) as? Bitmap
        val imageView : ImageView = findViewById(R.id.full_image_activity_imageView)

        if(image != null){
            imageView.setImageBitmap(image)
        }else{
            finish()
        }
    }
}