package org.wit.hillfort.views.hillfortlist

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.hillfort.HillfortView

class HillfortListPresenter(val view: HillfortListView) {

  var app: MainApp

  init {
    app = view.application as MainApp
  }

  fun getHillforts() = app.hillforts.findAll()

  fun doAddHillfort() {
    view.startActivityForResult<HillfortView>(0)
  }

  fun doEditHillfort(placemark: HillfortModel) {
    view.startActivityForResult(view.intentFor<HillfortView>().putExtra("placemark_edit", placemark), 0)
  }

  fun doShowHillfortsMap() {
    view.startActivity<HillfortMapView>()
  }
}