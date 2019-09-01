package org.wit.hillfort.views

import android.content.Intent
import org.wit.hillfort.main.MainApp

/**
 * Matthew O'Connor
 * 2019
 * Applied Computing
 */

open class BasePresenter(var view: BaseView?) {

  var app: MainApp =  view?.application as MainApp

  open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
  }

  open fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
  }

  open fun onDestroy() {
    view = null
  }

  fun clear()
  {}
}
