package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.R
import org.wit.hillfort.activities.*
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.hillfort.HillfortView
import org.wit.hillfort.views.map.HillfortMapView

class HillfortListView : AppCompatActivity(), HillfortListener {

  lateinit var presenter: HillfortListPresenter
  lateinit var app: MainApp
  var user = UserModel()
  var visitedCount = 0;

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    toolbarMain.title = title
    setSupportActionBar(toolbarMain)

    presenter = HillfortListPresenter(this)
    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = HillfortAdapter(presenter.getHillforts(), this)
    recyclerView.adapter?.notifyDataSetChanged()
    hillfortsVisited()

    if (intent.hasExtra("ID")) {
      user = intent.extras.getParcelable<UserModel>("ID")
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_add -> {
        startActivityForResult<HillfortView>(0)
      }
      R.id.user_logout -> {
        startActivityForResult<LoginActivity>(0)
        finish()
      }

      R.id.user_settings -> {
        startActivityForResult(intentFor<SettingsView>().putExtra("ID", user), 0)
      }

      R.id.item_map -> {
        startActivityForResult<HillfortMapView>(0)
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    presenter.doEditHillfort(hillfort)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    recyclerView.adapter?.notifyDataSetChanged()
    super.onActivityResult(requestCode, resultCode, data)
  }

  private fun hillfortsVisited() {
    visitedCount = 0
    for (i in app.hillforts.findAll()) {
      if (i.visited == true) {
        visitedCount++
      }

    }
    user.visitedNo = visitedCount
  }
}