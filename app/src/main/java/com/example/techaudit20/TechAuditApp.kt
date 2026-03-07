package com.example.techaudit20

import android.app.Application
import com.example.techaudit20.data.AppDatabase
import com.example.techaudit20.data.TechAuditRepository

class TechAuditApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { TechAuditRepository(database.techAuditDao()) }
}