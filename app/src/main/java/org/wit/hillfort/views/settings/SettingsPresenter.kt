package org.wit.hillfort.views.settings


import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView

/**
 * Matthew O'Connor
 * 2019
 * Applied Computing
 */

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

  var hillfort = HillfortModel()

  fun doCancel() {
    view?.finish()
  }

  fun countHillforts() {
    async(UI) {
      view?.displayCount(app.hillforts.findAll().size)
    }

  }
}