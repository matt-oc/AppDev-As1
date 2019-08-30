package org.wit.hillfort.views.login


import org.wit.hillfort.views.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Matthew O'Connor
 * 2019
 * Applied Computing
 */

class LoginPresenter(view: BaseView) : BasePresenter(view) {

  var auth: FirebaseAuth = FirebaseAuth.getInstance()
  var fireStore: HillfortFireStore? = null

  init {
    if (app.hillforts is HillfortFireStore) {
      fireStore = app.hillforts as HillfortFireStore
    }
  }

  fun doLogin(email: String, password: String) {
    view?.showProgress()
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
        if (fireStore != null) {
          fireStore!!.fetchHillforts {
            view?.hideProgress()
            view?.navigateTo(VIEW.LIST)
            view?.finish()
          }
        } else {
          view?.hideProgress()
          view?.navigateTo(VIEW.LIST)
          view?.finish()
        }
      } else {
        view?.hideProgress()
        view?.toast("Sign in Failed: ${task.exception?.message}")
      }
    }
  }


  fun doGoogleLogin(acct: GoogleSignInAccount) {
    view?.showProgress()
    val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
        if (fireStore != null) {
          fireStore!!.fetchHillforts {
            view?.hideProgress()
            view?.navigateTo(VIEW.LIST)
            view?.finish()
          }
        } else {
          view?.hideProgress()
          view?.navigateTo(VIEW.LIST)
          view?.finish()
        }
      } else {
        view?.hideProgress()
        view?.toast("Sign in Failed: ${task.exception?.message}")
      }
    }
  }


  fun doSignUp(email: String, password: String) {
    view?.showProgress()
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
        view?.hideProgress()
        view?.navigateTo(VIEW.LIST)
      } else {
        view?.hideProgress()
        view?.toast("Sign Up Failed: ${task.exception?.message}")
      }
    }
  }

  fun doLogout() {
    FirebaseAuth.getInstance().signOut()
    app.hillforts.clear()
    view?.navigateTo(VIEW.LOGIN)
    view?.finish()
  }

}
