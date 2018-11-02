package org.wit.placemark.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.wit.placemark.R

/**
 * A Login Form Example in Kotlin Android
 */
class LoginActivity : AppCompatActivity() {

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
      val user_name = et_user_name.text;
      val password = et_password.text;
      Toast.makeText(this, user_name, Toast.LENGTH_LONG).show()

      //todo

    }
  }
}