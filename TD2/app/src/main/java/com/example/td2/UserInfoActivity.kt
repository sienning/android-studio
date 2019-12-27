package com.example.td2

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_info.*

class UserInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        take_picture_button.setOnClickListener(){
            askCameraPermissionAndOpenCamera()
        }
    }

    override fun onResume() {
        super.onResume()

        Glide.with(this).load("https://goo.gl/gEgYUd").into(image_view)
    }

    private fun askCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // l'OS dit d'expliquer pourquoi on a besoin de cette permission:
                showDialogBeforeRequest()
            } else {
                // l'OS ne demande pas d'explication, on demande directement:
                requestCameraPermission()
            }
        } else {
            openCamera()
        }
    }

    private fun showDialogBeforeRequest() {
        // Affiche une popup (Dialog) d'explications:
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton(android.R.string.ok) { _, _ -> requestCameraPermission() }
            setCancelable(true)
            show()
        }
    }

    private fun requestCameraPermission() {
        // CAMERA_PERMISSION_CODE est défini par nous et sera récupéré dans onRequestPermissionsResult
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE )
    }

    private fun openCamera() {
        // On va utiliser un Intent implicite
    }

    companion object {
        const val CAMERA_PERMISSION_CODE = 42
    }


    /*override fun onRequestPermissionsResult() {
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED)

    }*/
}
