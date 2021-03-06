package com.example.stefanr.archapp.models

interface SiteService {
    suspend fun findAll(): List<SiteModel>
    suspend fun findById(id: Long): SiteModel?
    suspend fun create(site: SiteModel)
    suspend fun update(site: SiteModel)
    suspend fun delete(site: SiteModel)
    fun clear()
}