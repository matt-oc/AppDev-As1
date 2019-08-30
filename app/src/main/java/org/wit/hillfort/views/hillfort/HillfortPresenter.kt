package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.jetbrains.anko.toast
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.helpers.*
import org.wit.hillfort.views.*

/**
 * Matthew O'Connor
 * 2019
 * Applied Computing
 */


class HillfortPresenter(view: BaseView) : BasePresenter(view) {

  val locationRequest = createDefaultLocationRequest()

  var hillfort = HillfortModel()
  var defLocation = Location(52.245696, -7.139102, 15f)
  var edit = false;

  var map: GoogleMap? = null
  var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
  init {
    app = view.application as MainApp
    if (view.intent.hasExtra("hillfort_edit")) {
      edit = true
      hillfort = view.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
      view.showHillfort(hillfort)
      checkImagePermissions(view)
    }
    else {
      if (checkLocationPermissions(view)) {
        doSetCurrentLocation()
      }
    }
  }

  fun doAddOrSave(title: String, description: String, notes: String, hillfortVisited: Boolean, dateVisited: String, rating: Float, favourite: Boolean) {
    hillfort.title = title
    hillfort.description = description
    hillfort.notes = notes
    hillfort.visited = hillfortVisited
    hillfort.date = dateVisited
    hillfort.rating = rating
    hillfort.favourite = favourite
    async(UI) {
      if (edit) {
        app.hillforts.update(hillfort)
      } else {
        app.hillforts.create(hillfort)
      }
      view?.finish()
    }
  }

  fun doCancel() {
    view?.finish()
  }

  fun doDelete() {
    async(UI) {
      app.hillforts.delete(hillfort)
      view?.toast(R.string.hillfort_delete)
      view?.finish()
    }
  }


  fun doSelectImage() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }

  fun doSetLocation() {
      view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.location.lat, hillfort.location.lng, hillfort.location.zoom))

  }

  override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (isPermissionGranted(requestCode, grantResults)) {
        doSetCurrentLocation()
    } else {
      // permissions denied, so use the default location
      locationUpdate(defLocation)
    }
  }

  @SuppressLint("MissingPermission")
  fun doSetCurrentLocation() {
    var locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult != null && locationResult.locations != null) {
          val l = locationResult.locations.last()
          locationUpdate(Location(l.latitude, l.longitude))
        }
      }
    }
    if (!edit) {
      locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }
  }

    fun doConfigureMap(m: GoogleMap) {
      map = m
      locationUpdate(hillfort.location)
    }

    fun locationUpdate(location: Location) {
      hillfort.location = location
      hillfort.location.zoom = 15f
      map?.clear()
      map?.uiSettings?.setZoomControlsEnabled(true)
      val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.location.lat, hillfort.location.lng))
      map?.addMarker(options)
      map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.location.lat, hillfort.location.lng), hillfort.location.zoom))
      view?.showHillfort(hillfort)
    }

  @SuppressLint("MissingPermission")
  fun doResartLocationUpdates() {
    var locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult != null && locationResult.locations != null) {
          val l = locationResult.locations.last()
          locationUpdate(Location(l.latitude, l.longitude))
        }
      }
    }
    if (!edit) {
      locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }
  }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
      Log.i("REQUEST CODE: ", requestCode.toString())
      when (requestCode) {
        GALLERY -> {
          hillfort.image = data.data.toString()
          view?.showHillfort(hillfort)
        }
        LOCATION_REQUEST -> {
          val location = data.extras.getParcelable<Location>("location")
            hillfort.location = location
            locationUpdate(location)
        }
        CAMERA -> {
          hillfort.image = "" // not Correct
          view?.showHillfort(hillfort)
        }
      }
    }
  }
