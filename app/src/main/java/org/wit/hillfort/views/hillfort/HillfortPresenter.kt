package org.wit.hillfort.views.hillfort

import android.content.Intent
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.jetbrains.anko.toast
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import org.wit.hillfort.views.editlocation.EditLocationView

class HillfortPresenter(view: BaseView) : BasePresenter(view) {

  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2

  var hillfort = HillfortModel()
  var defLocation = Location(52.245696, -7.139102, 15f)
  var edit = false;
  var user = UserModel()
  var visitedCount = 0;

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
    view?.finish()
  }

  fun doVisitedCount () {
    visitedCount = 0
    for (i in app.hillforts.findAll()) {
      if (i.visited == true) {
        visitedCount++
      }
    }
    user.visitedNo = visitedCount
  }

  fun doCancel() {
    view?.finish()
  }

  fun doDelete() {
    app.hillforts.delete(hillfort)
    view?.toast(R.string.hillfort_delete)
    view?.finish()
  }

  fun doSelectImage() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }

  fun doSetLocation() {
    if (edit == false) {
      view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", defLocation)
    } else {
      view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.lat, hillfort.lng, hillfort.zoom))
    }

    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
      when (requestCode) {
        IMAGE_REQUEST -> {
          hillfort.image = data.data.toString()
          view?.showHillfort(hillfort)
        }
        LOCATION_REQUEST -> {
          val location = data.extras.getParcelable<Location>("location")
          hillfort.lat = location.lat
          hillfort.lng = location.lng
          hillfort.zoom = location.zoom
        }
      }
    }
  }
}