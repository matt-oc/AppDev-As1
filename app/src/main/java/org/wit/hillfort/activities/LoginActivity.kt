package org.wit.hillfort.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.hillfortlist.HillfortListView

/**
 * Matthew O'Connor
 * OCT 2018
 * Applied Computing
 *
 * Login Class
 */

class LoginActivity : AppCompatActivity(), AnkoLogger {

  var user = UserModel()
  var userList = listOf<UserModel>()
  var hillfort = HillfortModel()
  var count = 0

  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    app = application as MainApp

    // get reference to all views
    var et_user_name = findViewById(R.id.et_email) as EditText
    var et_password = findViewById(R.id.et_password) as EditText
    var btn_register = findViewById(R.id.btn_register) as Button
    var btn_submit = findViewById(R.id.btn_submit) as Button


    btn_register.setOnClickListener {
      user.email = et_user_name.text.toString()
      user.password = et_password.text.toString()
      if (user.email.isEmpty() || user.password.isEmpty()) {
        toast(R.string.blank_input_warning)
      }
      else {
        app.users.create(user.copy())
        info(user)
        info("User Create: $et_user_name")
        toast(R.string.registration_success)
        toast(R.string.login_prompt)
        info("---------" + app.users.findAll())
      }
    }

    // set on-click listener
    btn_submit.setOnClickListener {
      val user_name = et_user_name.text.toString()
      val password = et_password.text.toString()
      if (user_name.isEmpty() || password.isEmpty()) {
        toast(R.string.blank_input_warning)
      }
      else if(app.users.findAll().any{ user -> user.email == user_name && user.password == password }) {
        userList = app.users.findAll().filter{ user -> user.email == user_name }
        user = userList[0]
        getVisited()
        toast(R.string.login_success)
        startActivityForResult(intentFor<HillfortListView>().putExtra("ID", user), 0)
        finish()
      } else {
        toast(R.string.login_fail)
      }

    }
  }

  private fun getVisited() {
    count = app.hillforts.findAll().count{ hillfort -> hillfort.visited == true}
    user.visitedNo = count
    info(user.visitedNo)
  }
}