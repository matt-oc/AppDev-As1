package org.wit.hillfort.models

interface UserStore {
  fun findAll(): List<UserModel>
  fun create(userModel: UserModel)
}