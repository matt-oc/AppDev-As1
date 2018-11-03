package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.hillfort.R

/**
 * Matthew O'Connor
 * OCT 2018
 * Applied Computing
 *
 * Login Class
 */

class LoginActivity : AppCompatActivity(), AnkoLogger {



  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)



    // get reference to all views
    var et_user_name = findViewById(R.id.et_email) as EditText
    var et_password = findViewById(R.id.et_password) as EditText
    var btn_reset = findViewById(R.id.btn_register) as Button
    var btn_submit = findViewById(R.id.btn_submit) as Button

    btn_reset.setOnClickListener {
      // clearing email and password edit text views on reset button click
      et_user_name.setText("")
      et_password.setText("")
    }

    // set on-click listener
    btn_submit.setOnClickListener {
      val user_name = et_user_name.text.toString()
      val password = et_password.text.toString()
if(user_name.equals("matt") && password.equals("root")) {
  Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
  val intent = Intent(this, HillfortListActivity::class.java)
  startActivity(intent)
} else {
  toast("Login Unsuccessful")
}
      //todo

    }
  }
}