/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.database.SleepNightDao
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepNightDao
    private lateinit var db: SleepDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        sleepDao = db.sleepNightDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insert() {
        val night = SleepNight()
        sleepDao.insert(night)
        val persistedNight = sleepDao.getById(1)

        assertEquals(1L, persistedNight?.nightId)
        assertEquals(night.startTimeMilli, persistedNight?.startTimeMilli)
        assertEquals(night.endTimeMilli, persistedNight?.endTimeMilli)
        assertEquals(night.sleepQuality, persistedNight?.sleepQuality)
    }

    @Test
    @Throws(Exception::class)
    fun update() {
        sleepDao.insert(SleepNight())

        val night = sleepDao.getById(1)
        night?.sleepQuality = 2

        if (night != null) {
            sleepDao.update(night)
        }

        assertEquals(2, sleepDao.getById(1)?.sleepQuality)
    }

    @Test
    @Throws(Exception::class)
    fun getById() {
        sleepDao.insert(SleepNight())
        val night = sleepDao.getById(1)
        assertEquals(1L, night?.nightId)
    }

    @Test
    @Throws(Exception::class)
    fun getMostRecentNight() {
        sleepDao.insert(SleepNight())
        val mostRecentNight = sleepDao.getMostRecentNight()
        assertEquals(-1, mostRecentNight?.sleepQuality)
    }
}

