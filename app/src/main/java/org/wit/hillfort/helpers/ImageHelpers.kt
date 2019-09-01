package org.wit.hillfort.helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import org.wit.hillfort.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Matthew O'Connor
 * 2019
 * Applied Computing
 */

val GALLERY = 1
val CAMERA = 3
var mCurrentPhotoPath: String = ""
var photoUri: Uri = Uri.parse("")

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
  if (checkImagePermissions(parent)) {
    val photoFile: File? = try {
      createImageFile(parent)
    } catch (ex: IOException) {
      // Error occurred while creating the File
      null
    }
    // Continue only if the File was successfully created
    photoFile?.also {
      photoUri = FileProvider.getUriForFile(
          parent,
          "org.wit.hillfort.fileprovider", it
      )
      val intent = Intent()
      intent.action = MediaStore.ACTION_IMAGE_CAPTURE
      intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
      intent.putExtra("path", mCurrentPhotoPath)
      parent.startActivityForResult(intent, id)
    }
  }
}

@Throws(IOException::class)
fun createImageFile(parent: Activity): File {
  // Create an image file name
  val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
  val storageDir: File = parent.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
  return File.createTempFile(
      "JPEG_${timeStamp}_", /* prefix */
      ".jpg", /* suffix */
      storageDir /* directory */
  ).apply {
    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = absolutePath
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