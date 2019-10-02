/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.util.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import ml.dvnlabs.unsikadu.util.database.model.ProfileList


class CreateProfileDBHelper(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_VERSION: Int = 1
        private const val DATABASE_NAME : String = "sikadu_db"
        const val TABLE_NAME = "student"
        const val COLUMN_ID = "id"
        const val COLUMN_IDSTUDENT = "studentid"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_SELECTED = "selected"
        const val COLUMN_NAME = "name"
        const val COLUMN_IMG = "img"
    }


    var profileListModel : ArrayList<ProfileList>? = null


    override fun onCreate(db: SQLiteDatabase?) {
        val createStudentTable = ("CREATE TABLE "+ TABLE_NAME+"("+ COLUMN_ID+" INTEGER PRIMARY KEY,"+ COLUMN_IDSTUDENT+
                " TEXT,"+ COLUMN_NAME+
                " TEXT,"+ COLUMN_PASSWORD+" TEXT,"+ COLUMN_SELECTED+" INTEGER,"+ COLUMN_IMG+" TEXT)")
        db!!.execSQL(createStudentTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Not Yet Implemented
    }

    fun createNewProfile(studentId : String , password : String){
        val db: SQLiteDatabase = this.writableDatabase
        try {
            disableAllSelected()
            val values = ContentValues()
            val id = checkLastId()
            values.put(COLUMN_ID,id+1)
            values.put(COLUMN_PASSWORD,password)
            values.put(COLUMN_IDSTUDENT,studentId)
            values.put(COLUMN_NAME,"n/a")
            values.put(COLUMN_IMG,"")
            values.put(COLUMN_SELECTED,1)
            db.insert(TABLE_NAME,null,values)
            db.close()

        }catch (e : SQLiteException){
            e.printStackTrace()
        }
    }

    fun updateProfileInfo(studentId: String,name : String,profileImg : String){
        try {
            val db : SQLiteDatabase = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_NAME,name)
            values.put(COLUMN_IMG,profileImg)
            db.update(TABLE_NAME,values," $COLUMN_IDSTUDENT = $studentId",null)
            db.close()
        }catch (e : SQLiteException){
            e.printStackTrace()
        }
    }


    private fun checkLastId(): Int {
        try {
            val lastquery =
                "SELECT  * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC LIMIT 0,1"
            val db: SQLiteDatabase = this.readableDatabase
            val cursor: Cursor = db.rawQuery(lastquery, null)
            var index = 0
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    index = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    cursor.moveToNext()
                }
                cursor.close()
            }
            return index
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        return 0
    }

    fun changeSelectedToProfile(id : Int){
        val db : SQLiteDatabase = this.writableDatabase
        try {
            //First change to not selected for all item
            disableAllSelected()
            val values = ContentValues()
            values.put(COLUMN_SELECTED,1)
            db.update(TABLE_NAME,values," $COLUMN_ID = $id",null)
        }catch (e : SQLiteException){
            e.printStackTrace()
        }
    }
    private fun disableAllSelected(){
        val db : SQLiteDatabase = this.writableDatabase
        try {
            //First change to not selected for all item
            val values = ContentValues()
            values.put(COLUMN_SELECTED,0)
            db.update(TABLE_NAME,values,"",null)
        }catch (e : SQLiteException){
            e.printStackTrace()
        }
    }

    fun HasProfile() :Boolean{
        try {
            val countQuery = "SELECT  * FROM $TABLE_NAME"
            val db: SQLiteDatabase = this.readableDatabase
            val cursor: Cursor = db.rawQuery(countQuery, null)
            val count: Int = cursor.count
            cursor.close()
            if (count > 0) {
                return true
            }
            db.close()
            return false
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        return false
    }

    fun getProfileAll() : ArrayList<ProfileList>?{
        try {
            profileListModel = ArrayList()
            val getProfile = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_SELECTED DESC"
            val db : SQLiteDatabase = this.readableDatabase
            val cursor : Cursor = db.rawQuery(getProfile,null)

            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast){
                    val id: Int = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    val name: String = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val idStudent: String = cursor.getString(cursor.getColumnIndex(COLUMN_IDSTUDENT))
                    val profileImg: String = cursor.getString(cursor.getColumnIndex(COLUMN_IMG))
                    val selected : Int = cursor.getInt(cursor.getColumnIndex(COLUMN_SELECTED))
                    val password : String = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
                    profileListModel!!.add(ProfileList(id,name,idStudent,profileImg,selected,password))
                    cursor.moveToNext()
                }
                cursor.close()
            }
            db.close()
            return profileListModel!!
        }catch (e : SQLiteException){
            e.printStackTrace()
        }
        return null
    }
    fun getSelectedProfile():ProfileList?{
        try {
            var profil : ProfileList? = null
            val selectedQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_SELECTED = 1 LIMIT 0,1"
            val db : SQLiteDatabase = this.readableDatabase
            val cursor : Cursor = db.rawQuery(selectedQuery,null)
            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast){
                    val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val idStudent = cursor.getString(cursor.getColumnIndex(COLUMN_IDSTUDENT))
                    val profileImg = cursor.getString(cursor.getColumnIndex(COLUMN_IMG))
                    val selected = cursor.getInt(cursor.getColumnIndex(COLUMN_SELECTED))
                    val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
                    profil = ProfileList(id,name,idStudent,profileImg,selected,password)
                    cursor.moveToNext()
                }
                cursor.close()
            }
            db.close()
            return profil!!

        }catch (e : SQLiteException){
            e.printStackTrace()
        }
        return null
    }
    fun deleteProfile(id : Int){
        try {
            val db : SQLiteDatabase = this.readableDatabase
            db.delete(TABLE_NAME,"$COLUMN_ID = $id",null)
            db.close()

        }catch (e : SQLiteException){
            e.printStackTrace()
        }
    }
}

