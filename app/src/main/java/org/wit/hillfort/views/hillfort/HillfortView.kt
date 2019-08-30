package org.wit.hillfort.views.hillfort

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.helpers.GlideApp
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

class HillfortView : BaseView(), AnkoLogger {

  lateinit var presenter: HillfortPresenter
  lateinit var map: GoogleMap
  var hillfort = HillfortModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)
    super.init(toolbar, true)
    info("Hillfort Activity started..")

    presenter = initPresenter (HillfortPresenter(this)) as HillfortPresenter

    chooseImage.setOnClickListener { presenter.doSelectImage() }

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      map = it
      presenter.doConfigureMap(map)
      it.setOnMapClickListener { presenter.doSetLocation() }
    }
  }

  override fun showHillfort(hillfort: HillfortModel) {
    hillfortTitle.setText(hillfort.title)
    description.setText(hillfort.description)
    notes.setText(hillfort.notes)
    hillfortVisited.setChecked(hillfort.visited)
    dateVisited.setText(hillfort.date)
    GlideApp.with(this).load(hillfort.image).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(hillfortImage);
    if (hillfort.image != null) {
      chooseImage.setText(R.string.change_hillfort_image)
    }
    lat.setText("%.6f".format(hillfort.location.lat))
    lng.setText("%.6f".format(hillfort.location.lng))
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort_edit, menu)
    if (presenter.edit) menu.getItem(0).setVisible(true)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_delete -> {
        presenter.doDelete()
      }
      R.id.item_cancel -> {
        presenter.doCancel()
      }

      R.id.item_save -> {
        hillfort.title = hillfortTitle.text.toString()
        hillfort.description = description.text.toString()
        hillfort.notes = notes.text.toString()
        hillfort.visited = hillfortVisited.isChecked()
        hillfort.date = dateVisited.text.toString()
        if (hillfort.title.isEmpty() || (hillfort.visited && hillfort.date.isEmpty())) {
          if (hillfort.title.isEmpty()) {
            toast(R.string.enter_hillfort_title)
          }
          if ((hillfort.visited && hillfort.date.isEmpty())) {
            toast(R.string.enter_date_visited)
          }
        } else {
          if (!hillfort.visited) {
            hillfort.date = ("")
          }
          presenter.doAddOrSave(hillfort.title, hillfort.description, hillfort.notes, hillfort.visited, hillfort.date)
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
    presenter.doResartLocationUpdates()
  }

  override fun onBackPressed() {
    presenter.doCancel()
  }
}