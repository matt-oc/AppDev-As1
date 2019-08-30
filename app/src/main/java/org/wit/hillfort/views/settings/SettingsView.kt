package org.wit.hillfort.views.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.user_settings.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.login.LoginPresenter

/**
 * Matthew O'Connor
 * OCT 2018
 * Applied Computing
 *
 * User Settings Class
 */

class SettingsView : BaseView(), AnkoLogger {

  var hillfort = HillfortModel()
  lateinit var presenter: SettingsPresenter
  lateinit var authPresenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.user_settings)
    super.init(toolbarSettings, true)
    info("Hillfort Activity started..")

    presenter = initPresenter (SettingsPresenter(this)) as SettingsPresenter
    presenter.countHillforts()


    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
      user_email.setText("User: ${user.email}")
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_user_settings, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun displayCount(count: Int) {
    total_hillforts.setText("Total Hillforts is: " + count)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {

      R.id.user_logout -> {
        authPresenter.doLogout()
      }
    }
    return super.onOptionsItemSelected(item)
  }

}