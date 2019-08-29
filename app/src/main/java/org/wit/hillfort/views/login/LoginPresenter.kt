package org.wit.hillfort.views.login

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.common.api.ApiException
import org.wit.hillfort.views.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider

class LoginPresenter(view: BaseView) : BasePresenter(view) {

  var auth: FirebaseAuth = FirebaseAuth.getInstance()
  var fireStore: HillfortFireStore? = null
  // Build a GoogleSignInClient with the options specified by gso.

  private lateinit var googleSignInClient: GoogleSignInClient

  val RC_SIGN_IN = 1

  // Configure sign-in to request the user's ID, email address, and basic
  // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
  var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestEmail()
      .build()

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


  }

}
