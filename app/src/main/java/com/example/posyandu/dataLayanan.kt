package com.example.posyandu


data class dataLayanan(
    val id: Int,
    val nama_poli_tsd: String,
    val kode_poli_tsd: String,
    val deskripsi: String,
    val slug: String,
    val syarat: String,
    val kuota: Int,
    val user_id: Int,
    val created_at: String,
    val updated_at: String
)