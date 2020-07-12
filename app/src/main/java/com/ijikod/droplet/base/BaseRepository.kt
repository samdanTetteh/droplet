package com.ijikod.droplet.base

import com.ijikod.droplet.database.AppDatabase

abstract class BaseRepository {
    protected val appDatabase = AppDatabase.getInstance()
}