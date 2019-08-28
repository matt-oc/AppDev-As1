package org.wit.hillfort.views.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.R
import org.wit.hillfort.views.login.LoginView


/**
 * Matthew O'Connor
 * 2019
 * Applied Computing
 *
 * Splash Screen Class
 */

class SplashView : AppCompatActivity(), AnkoLogger {
  private var DelayHandler: Handler? = null
  private val SPLASH_DELAY: Long = 5000 //5 seconds

  internal val Runnable: Runnable = Runnable {
    if (!isFinishing) {
      startActivityForResult<LoginView>(0)
      finish()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    //Initialize the Handler
    DelayHandler = Handler()

    //Navigate with delay
    DelayHandler!!.postDelayed(Runnable, SPLASH_DELAY)
  }

  public override fun onDestroy() {

    if (DelayHandler != null) {
      DelayHandler!!.removeCallbacks(Runnable)
    }

    super.onDestroy()
  }

}