package org.wit.hillfort.models

interface HillfortStore {
  suspend fun findAll(): List<HillfortModel>
  suspend fun create(hillfortModel: HillfortModel)
  suspend fun update(hillfort: HillfortModel)
  suspend fun delete(hillfort: HillfortModel)
  suspend fun findById(id:Long) : HillfortModel?
}