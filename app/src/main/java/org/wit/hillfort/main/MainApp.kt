package org.wit.hillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.HillfortStore
import org.wit.hillfort.models.firebase.HillfortFireStore

class MainApp : Application(), AnkoLogger {
  lateinit var hillforts: HillfortStore

  override fun onCreate() {
    super.onCreate()
    //hillfortsLocal = HillfortStoreRoom(applicationContext)
    hillforts = HillfortFireStore(applicationContext)
    info("Hillforts app started")
  }
}