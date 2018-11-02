/**
 * Matthew O'Connor
 * OCT 2018
 * Applied Computing
 *
 * Splash Screen Class
 */

package org.wit.placemark.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import org.wit.placemark.R


class SplashActivity : AppCompatActivity() {
  private var DelayHandler: Handler? = null
  private val SPLASH_DELAY: Long = 5000 //5 seconds

  internal val Runnable: Runnable = Runnable {
    if (!isFinishing) {
      val intent = Intent(applicationContext, PlacemarkListActivity::class.java)
      startActivity(intent)
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