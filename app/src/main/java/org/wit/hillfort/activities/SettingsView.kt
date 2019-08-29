package org.wit.hillfort.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.user_settings.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.login.LoginView

/**
 * Matthew O'Connor
 * OCT 2018
 * Applied Computing
 *
 * User Settings Class
 */

class SettingsView : AppCompatActivity(), AnkoLogger {

  var user = UserModel()
  var hillfort = HillfortModel()
  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.user_settings)
    toolbarSettings.title = getResources().getString(R.string.user_settings_title)
    setSupportActionBar(toolbarSettings)

    app = application as MainApp
    if (intent.hasExtra("ID")) {
      user = intent.extras.getParcelable<UserModel>("ID")
    }
    async(UI) {
      user_email.setText(getResources().getString(R.string.email_string) + user.email)
      user_password.setText(getResources().getString(R.string.password_string) + user.password)
      visited_hillforts.setText(getResources().getString(R.string.no_visited_sites) + user.visitedNo)
      total_hillforts.setText(getResources().getString(R.string.no_listed_sites) + app.hillforts.findAll().size)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_user_settings, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {

      R.id.item_cancel -> {
        finish()
      }
      R.id.user_logout -> {
        startActivityForResult<LoginView>(0)
        toast(R.string.user_logged_out)
        finish()
      }
    }
    return super.onOptionsItemSelected(item)
  }

}