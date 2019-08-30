package org.wit.hillfort.views.settings


import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView

/**
 * Matthew O'Connor
 * OCT 2018
 * Applied Computing
 *
 * User Settings Class
 */

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

  var user = UserModel()
  var hillfort = HillfortModel()

  fun doCancel() {
    view?.finish()
  }


}