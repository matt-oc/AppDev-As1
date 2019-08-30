package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.card_hillfort.rating
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.hillfort.HillfortPresenter


class HillfortListView : BaseView(), HillfortListener {

  lateinit var presenter: HillfortListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    super.init(toolbarMain, false)

    presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    presenter.loadHillforts()


  }

   override fun showHillforts(hillforts: List<HillfortModel>) {
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
        presenter.doAddHillfort()
      }
      R.id.user_logout -> {
        presenter.doLogout()
        finish()
      }

      R.id.user_settings -> {
        presenter.doSettings()
      }

      R.id.item_map -> {
        presenter.doShowHillfortsMap()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    presenter.doEditHillfort(hillfort)
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.loadHillforts()
    super.onActivityResult(requestCode, resultCode, data)
  }


}