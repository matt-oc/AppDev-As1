package org.wit.hillfort.helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import org.wit.hillfort.R
import java.io.IOException

/**
 * Matthew O'Connor
 * 2019
 * Applied Computing
 */

val GALLERY = 1
val CAMERA = 3

private val REQUEST_PERMISSIONS_REQUEST_CODE = 35

fun checkImagePermissions(activity: Activity) : Boolean {
  if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
    return true
  }
  else {
    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSIONS_REQUEST_CODE)
    return false
  }
}


fun showImagePicker(parent: Activity, id: Int) {
  val pictureDialog = AlertDialog.Builder(parent)
  pictureDialog.setTitle("Select Action")
  val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
  pictureDialog.setItems(pictureDialogItems
  ) { dialog, which ->
    when (which) {
      0 -> choosePhotoFromGallery(parent, GALLERY)
      1 -> takePhotoFromCamera(parent, CAMERA)
    }
  }
  pictureDialog.show()
}

private fun choosePhotoFromGallery(parent: Activity, id: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val chooser = Intent.createChooser(intent, R.string.select_hillfort_image.toString())
        parent.startActivityForResult(chooser, id)
      }


private fun takePhotoFromCamera(parent: Activity, id: Int) {
  if(checkImagePermissions(parent)) {
    val intent = Intent()
    intent.action = MediaStore.ACTION_IMAGE_CAPTURE
    parent.startActivityForResult(intent, id)
  }
}

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

fun readImageFromPath(context: Context, path: String): Bitmap? {
  var bitmap: Bitmap? = null
  val uri = Uri.parse(path)
  if (uri != null) {
    try {
      val parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r")
      val fileDescriptor = parcelFileDescriptor.getFileDescriptor()
      bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
      parcelFileDescriptor.close()
    } catch (e: Exception) {
    }
  }
  return bitmap
}