package org.wit.hillfort.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.*
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.models.UserStore
import java.util.*

val USER_JSON_FILE = "users.json"
val userGsonBuilder = GsonBuilder().setPrettyPrinting().create()
val userListType = object : TypeToken<java.util.ArrayList<UserModel>>() {}.type

private fun generateRandomId(): Long {
  return Random().nextLong()
}

class UserJSONStore : UserStore, AnkoLogger {

  val context: Context
  var users = mutableListOf<UserModel>()

  constructor (context: Context) {
    this.context = context
    if (exists(context, USER_JSON_FILE)) {
      deserialize()
    }
  }

  override fun findAll(): MutableList<UserModel> {
    return users
  }

  override fun create(user: UserModel) {
    user.id = generateRandomId()
    users.add(user)
    serialize()
  }


  private fun serialize() {
    val jsonString = userGsonBuilder.toJson(users, userListType)
    write(context, USER_JSON_FILE, jsonString)
  }

  private fun deserialize() {
    val jsonString = read(context, USER_JSON_FILE)
    users = Gson().fromJson(jsonString, userListType)
  }
}
