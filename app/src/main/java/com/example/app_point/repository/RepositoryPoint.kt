package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.constants.ConstantsPoint
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.entity.PointsEntity

class RepositoryPoint(context: Context?) {

    private val mDataBasePoint: DataBaseEmployee = DataBaseEmployee(context)

    fun getPoint(employee: String, date: String, hour: String): Boolean {

        val mSearchPoint = selectFullPoints(employee, date)

        return try {
            val db = mDataBasePoint.writableDatabase
            val projection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " + ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(employee, date)
            val insertValues = ContentValues()

            when {
                mSearchPoint?.employee == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.EMPLOYEE, employee)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1, hour)
                    db.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)
                }
                mSearchPoint.data != null && mSearchPoint.hora1 == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1, hour)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
                }
                mSearchPoint.data != null && mSearchPoint.hora1 != null && mSearchPoint.hora2 == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR2, hour)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
                }
                mSearchPoint.data != null && mSearchPoint.hora1 != null && mSearchPoint.hora2 != null
                        && mSearchPoint.hora3 == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR3, hour)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
                }
                mSearchPoint.data != null && mSearchPoint.hora1 != null && mSearchPoint.hora2 != null
                        && mSearchPoint.hora3 != null && mSearchPoint.hora4 == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR4, hour)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
                }
                mSearchPoint.hora4 != null -> {
                    return false
                }
            }
            true

        } catch (e: Exception) {
            false
        }
    }

    fun storePointEmployee(): List<String> {

        val list: ArrayList<String> = ArrayList()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.EMPLOYEE)
            val orderBy = ConstantsPoint.POINT.COLUMNS.ID

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, orderBy
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val employee =
                        cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    list.add(employee)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storePointHour(): List<String> {

        val list: ArrayList<String> = ArrayList()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.HOUR1)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val hours =
                        cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                    list.add(hours)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storePointDate(): List<String> {

        val list: ArrayList<String> = ArrayList()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.DATE)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val date =
                        cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))
                    list.add(date)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storeSelectName(nome: String): List<String> {

        val list: ArrayList<String> = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.EMPLOYEE)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(nome)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val employee =
                        cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.EMPLOYEE))

                    list.add(employee)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storeSelectDate(nome: String): List<String> {

        val list: ArrayList<String> = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.DATE)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(nome)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val date =
                        cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))

                    list.add(date)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storeSelectHours(nome: String): List<String> {

        var list: List<String> = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(nome)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val hour1 = cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR4))

                    list = listOf(hour1, hour2, hour3, hour4)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storeSelectNameDate(nome: String, date: String): List<String> {

        val list: ArrayList<String> = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.DATE
            )
            val selection = ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(date)

            cursor = db.query(ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val dateSelected = cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))
                    list.add(dateSelected)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun storeSelectNameHours(nome: String, date: String): List<String> {

        val list: ArrayList<String> = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.HOUR1, ConstantsPoint.POINT.COLUMNS.DATE)
            val selection = ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {

                    val hour = cursor.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                    list.add(hour)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun selectFullPoints(nome: String, date: String): PointsEntity? {

        var list: PointsEntity? = null
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4
            )
            val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                            ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(nome, date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.ID))
                    val name = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    val data = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))
                    val hour1 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR4))

                    list = PointsEntity(id, name, data, hour1, hour2, hour3, hour4)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun fullPoints(): ArrayList<PointsEntity>? {

        var list: ArrayList<PointsEntity>? = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4
            )

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.ID))
                    val name = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    val data = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))
                    val hour1 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR4))

                    list?.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun fullPointsToName(nome: String, date: String): ArrayList<PointsEntity> {

        val list: ArrayList<PointsEntity> = arrayListOf()

        if (date == "") {
            try {
                val cursor: Cursor
                val db = mDataBasePoint.readableDatabase
                val projection = arrayOf(
                    ConstantsPoint.POINT.COLUMNS.ID,
                    ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                    ConstantsPoint.POINT.COLUMNS.DATE,
                    ConstantsPoint.POINT.COLUMNS.HOUR1,
                    ConstantsPoint.POINT.COLUMNS.HOUR2,
                    ConstantsPoint.POINT.COLUMNS.HOUR3,
                    ConstantsPoint.POINT.COLUMNS.HOUR4
                )
                val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? "
                val args = arrayOf(nome)

                cursor = db.query(
                    ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                    null, null, null
                )

                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id = cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.ID))
                        val name = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                        val data = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))
                        val hour1 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                        val hour2 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR2))
                        val hour3 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR3))
                        val hour4 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR4))

                        list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4))
                    }
                }
                cursor?.close()
                return list

            } catch (e: Exception) {
                return list
            }
        } else {
            try {
                val cursor: Cursor
                val db = mDataBasePoint.readableDatabase
                val projection = arrayOf(
                    ConstantsPoint.POINT.COLUMNS.ID,
                    ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                    ConstantsPoint.POINT.COLUMNS.DATE,
                    ConstantsPoint.POINT.COLUMNS.HOUR1,
                    ConstantsPoint.POINT.COLUMNS.HOUR2,
                    ConstantsPoint.POINT.COLUMNS.HOUR3,
                    ConstantsPoint.POINT.COLUMNS.HOUR4
                )
                val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                                ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
                val args = arrayOf(nome, date)

                cursor = db.query(
                    ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                    null, null, null
                )

                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id = cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.ID))
                        val name = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                        val data = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))
                        val hour1 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                        val hour2 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR2))
                        val hour3 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR3))
                        val hour4 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR4))

                        list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4))
                    }
                }
                cursor?.close()
                return list

            } catch (e: Exception) {
                return list
            }
        }
    }

    fun selectEmployeePoints(nome: String, date: String): ArrayList<PointsEntity>? {

        val list: ArrayList<PointsEntity>? = arrayListOf()
        try {
            val cursor: Cursor
            val db = mDataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4
            )
            val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(nome, date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.ID))
                    val name = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    val data = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))
                    val hour1 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR4))

                    list?.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }
}