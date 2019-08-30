package org.wit.hillfort.views.settings


import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

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

  fun doLogout() {
    FirebaseAuth.getInstance().signOut()
    app.hillforts.clear()
    view?.navigateTo(VIEW.LOGIN)
    view?.finish()
  }

  fun countHillforts() {
    async(UI) {
      view?.displayCount(app.hillforts.findAll().size)
    }

  }
}