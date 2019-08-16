package org.wit.hillfort.views.login

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.R


class LoginView : BaseView() {

  lateinit var presenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    //super.init(toolbar, false)

    presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

    btn_register.setOnClickListener {
      val email = et_email.text.toString()
      val password = et_password.text.toString()
      if (email == "" || password == "") {
        toast("Please provide email + password")
      }
      else {
        presenter.doSignUp(email,password)
      }
    }

    btn_submit.setOnClickListener {
      val email = et_email.text.toString()
      val password = et_password.text.toString()
      if (email == "" || password == "") {
        toast("Please provide email + password")
      }
      else {
        presenter.doLogin(email,password)
      }
    }
  }
}