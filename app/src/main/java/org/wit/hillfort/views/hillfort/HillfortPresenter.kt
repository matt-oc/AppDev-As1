package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.R.id.info
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.jetbrains.anko.toast
import org.wit.hillfort.helpers.checkLocationPermissions
import org.wit.hillfort.helpers.createDefaultLocationRequest
import org.wit.hillfort.helpers.isPermissionGranted
import org.wit.hillfort.models.UserModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.info
import org.wit.hillfort.views.*
import org.wit.hillfort.views.editlocation.EditLocationView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.wit.hillfort.views.hillfortlist.HillfortListPresenter


class HillfortPresenter(view: BaseView) : BasePresenter(view) {

  val locationRequest = createDefaultLocationRequest()

  var hillfort = HillfortModel()
  var defLocation = Location(52.245696, -7.139102, 15f)
  var edit = false;
  var user = UserModel()

  var map: GoogleMap? = null
  var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
  init {
    app = view.application as MainApp
    if (view.intent.hasExtra("hillfort_edit")) {
      edit = true
      hillfort = view.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
      view.showHillfort(hillfort)
    }
    else {
      if (checkLocationPermissions(view)) {
        doSetCurrentLocation()
      }
    }
  }

  fun doAddOrSave(title: String, description: String, notes: String, hillfortVisited: Boolean, dateVisited: String) {
    hillfort.title = title
    hillfort.description = description
    hillfort.notes = notes
    hillfort.visited = hillfortVisited
    hillfort.date = dateVisited
    async(UI) {
      if (edit) {
        app.hillforts.update(hillfort)
      } else {
        app.hillforts.create(hillfort)
      }
      view?.finish()
    }
  }

  //fun doVisitedCount () {
   // visitedCount = 0
    //for (i in app.hillforts.findAll()) {
     // if (i.visited == true) {
      //  visitedCount++
     // }
    //}
    //user.visitedNo = visitedCount
  //}

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
      when (requestCode) {
        IMAGE_REQUEST -> {
          hillfort.image = data.data.toString()
          view?.showHillfort(hillfort)
        }
        LOCATION_REQUEST -> {
          val location = data.extras.getParcelable<Location>("location")
          hillfort.location = location
          locationUpdate(location)
        }
      }
    }
  }
