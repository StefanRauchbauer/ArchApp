package com.example.stefanr.archapp.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.stefanr.archapp.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


val REQUEST_CAMERA_PERMISSIONS_REQUEST_CODE = 35
val REQUEST_STORAGE_PERMISSIONS_REQUEST_CODE = 36

var mCurrentPhotoPath: String = ""
private var photoFile: File? = null
private var newFile: String = ""

// Checks the Camera Permissions
fun checkCameraPermissions(activity: Activity): Boolean {
    if (ActivityCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    } else {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSIONS_REQUEST_CODE
        )
        return false
    }
}

// Checks the Storage Permissions
fun checkStoragePermissions(activity: Activity): Boolean {
    if (ActivityCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    } else {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_STORAGE_PERMISSIONS_REQUEST_CODE
        )
        return false
    }
}

// Checks if Permission is Granted
fun isCameraPermissionGranted(code: Int, grantResults: IntArray): Boolean {
    var permissionGranted = false;
    if (code == REQUEST_CAMERA_PERMISSIONS_REQUEST_CODE) {
        when {
            grantResults.isEmpty() -> Log.i("Camera", "User interaction was cancelled.")
            (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                permissionGranted = true
                Log.i("Camera", "Permission Granted.")
            }
            else -> Log.i("Camera", "Permission Denied.")
        }
    }
    return permissionGranted
}


// Shows the Image Picker as a new Activity
fun showImagePicker(parent: Activity, id: Int) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val chooser = Intent.createChooser(intent, R.string.site_btn_select_image.toString())
    parent.startActivityForResult(chooser, id)
}

// Camera or Gallery
fun getImageFromGallery(parent: Activity, id: Int) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)

    parent.startActivityForResult(intent, id)
}


fun dispatchTakePictureIntent(parent: Activity, imageNumber: Int) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    parent.startActivityForResult(intent, imageNumber)
}

fun getphotoFile(): String {
    return newFile
}

private fun displayMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

@Throws(IOException::class)
fun createImageFile(parent: Activity): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = parent.cacheDir
//    parent.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    )

    mCurrentPhotoPath = image.absolutePath
    return image
}

// Reads the Image and returns it
fun readImage(activity: Activity, resultCode: Int, data: Intent?): Bitmap? {
    var bitmap: Bitmap? = null
    if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, data.data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return bitmap
}

// Reads the Image when a Site should be changed
fun readImageFromPath(context: Context, path: String): Bitmap? {
    var bitmap: Bitmap? = null
    val uri = Uri.parse(path)
    if (uri != null) {
        try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor.fileDescriptor
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
        } catch (e: Exception) {
            val myBitmap = BitmapFactory.decodeFile(path)
            return myBitmap
        }
    }
    return bitmap
}