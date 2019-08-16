package org.wit.hillfort.views.hillfort

import android.content.Intent
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.jetbrains.anko.toast
import org.wit.hillfort.views.editlocation.EditLocationView

class HillfortPresenter(val view: HillfortView) {

  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2

  var hillfort = HillfortModel()
  var location = Location(52.245696, -7.139102, 15f)
  var app: MainApp
  var edit = false;

  init {
    app = view.application as MainApp
    if (view.intent.hasExtra("hillfort_edit")) {
      edit = true
      hillfort = view.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
      view.showHillfort(hillfort)
    }
  }

  fun doAddOrSave(title: String, description: String, notes: String, hillfortVisited: Boolean, dateVisited: String) {
    hillfort.title = title
    hillfort.description = description
    hillfort.notes = notes
    hillfort.visited = hillfortVisited
    hillfort.date = dateVisited
    if (edit) {
      app.hillforts.update(hillfort)
    } else {
      app.hillforts.create(hillfort)
    }
    view.finish()
  }

  fun doCancel() {
    view.finish()
  }

  fun doDelete() {
    app.hillforts.delete(hillfort)
    view.toast(R.string.hillfort_delete)
    view.finish()
  }

  fun doSelectImage() {
    showImagePicker(view, IMAGE_REQUEST)
  }

  fun doSetLocation() {
    if (hillfort.zoom != 0f) {
      location.lat = hillfort.lat
      location.lng = hillfort.lng
      location.zoom = hillfort.zoom
    }
    view.startActivityForResult(view.intentFor<EditLocationView>().putExtra("location", location), LOCATION_REQUEST)
  }

  fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      IMAGE_REQUEST -> {
        hillfort.image = data.data.toString()
        view.showHillfort(hillfort)
      }
      LOCATION_REQUEST -> {
        location = data.extras.getParcelable<Location>("location")
        hillfort.lat = location.lat
        hillfort.lng = location.lng
        hillfort.zoom = location.zoom
      }
    }
  }
}