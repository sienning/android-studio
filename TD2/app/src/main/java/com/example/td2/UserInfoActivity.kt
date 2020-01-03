package com.example.td2

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.td2.network.API.userService
import com.example.td2.network.UserInfo
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class UserInfoActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_PERMISSION_CODE = 42
        const val CAMERA_REQUEST_CODE = 2001
        const val GALLERY_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        take_picture_button.setOnClickListener(){
            askCameraPermissionAndOpenCamera()
        }

        upload_image_button.setOnClickListener(){
            openGallery(data = null)
        }

        //val glide = Glide.with(this)
        //lifecycleScope.launch {
            //val userInfo = userService.getInfo()
            //glide.load(userInfo.avatar).into(image_view)
        //}

    }

    override fun onResume() {
        super.onResume()

        //Glide.with(this).load("https://goo.gl/gEgYUd").into(image_view)


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
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        //handlePhotoTaken(cameraIntent)
    }

    private fun openGallery(data :Intent?) {
        //Pour ouvrir la gallerie:
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)

        //Pour récupérer le bitmap dans onActivityResult
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED){
            openCamera()
        } else {
            Toast.makeText(this, "Si vous refusez, on peut pas prendre de photo ! 😢", Toast.LENGTH_LONG).show()
        }
    }


    private fun handlePhotoTaken(data: Intent?) {
        val image = data?.extras?.get("data") as? Bitmap
        // Afficher l'image ici
        //Glide.with(this).load(image).into(image_view)
        //.load("http://3.bp.blogspot.com/-UIzEBbk95ek/VUvXlskYijI/AAAAAAAAz_Y/9SiDbp3aRZA/s1600/pinguin_PNG2.png")

        Glide
            .with(applicationContext)
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(image_view)


        val imageBody = imageToBody(image)
        // Plus tard, on l'enverra au serveur

        Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show()


        //userService.updateAvatar(avatar = imageBody)
    }

    private fun imageToBody(image: Bitmap?): MultipartBody.Part {
        val f = File(cacheDir, "tmpfile.jpg")
        f.createNewFile()
        try {
            val fos = FileOutputStream(f)
            image?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(MediaType.parse("image/png"), f)
        return MultipartBody.Part.createFormData("avatar", f.path, body)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handlePhotoTaken(data)

    }
}
