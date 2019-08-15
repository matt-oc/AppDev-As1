package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel

/**
 * Matthew O'Connor
 * OCT 2018
 * Applied Computing
 *
 * Hillfort List Class
 */

class HillfortListActivity : AppCompatActivity(), HillfortListener, AnkoLogger {

  lateinit var app: MainApp
  var user = UserModel()
  var visitedCount = 0;

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    app = application as MainApp
    toolbarMain.title = title
    setSupportActionBar(toolbarMain)

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = HillfortAdapter(app.hillforts.findAll(), this)
    loadHillforts()

    if (intent.hasExtra("ID")) {
      user = intent.extras.getParcelable<UserModel>("ID")
    }
  }

  private fun loadHillforts() {
    visitedCount = 0
    showHillforts( app.hillforts.findAll())
    for (i in app.hillforts.findAll()) {
      if (i.visited == true) {
        visitedCount++
      }

    }
    user.visitedNo = visitedCount
  }

  fun showHillforts (hillforts: List<HillfortModel>) {
    recyclerView.adapter = HillfortAdapter(hillforts, this)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_add -> {
        startActivityForResult<HillfortActivity>(0)
      }
      R.id.user_logout -> {
        startActivityForResult<LoginActivity>(0)
        finish()
      }

      R.id.user_settings -> {
        startActivityForResult(intentFor<SettingsActivity>().putExtra("ID", user), 0)
      }

      R.id.item_map -> {
        startActivityForResult<HillfortMapsActivity>(0)
      }

    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort), 0)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    loadHillforts()
    super.onActivityResult(requestCode, resultCode, data)
  }
}