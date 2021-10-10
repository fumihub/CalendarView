package com.non_name_hero.calenderview.data.source.remote

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.non_name_hero.calenderview.data.*
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.*
import com.non_name_hero.calenderview.utils.AppExecutors
import java.util.*

class ScheduleDataRemoteSource() : ScheduleDataSource {
    private val PIGLEAD_SCHEDULES = "PigLeadSchedules"
    private val HOLIDAY_DOCUMENT = "holiday"
    private val PIGLEAD_USERS = "PigLeadUsers"

    /*リモートデータベース*/
    private val db: FirebaseFirestore

    /*UserInfo*/
    /*DBにメールアドレスの登録がない場合、ドキュメントID自動作成でメールアドレスとパスワードを登録*/
    override fun setUserInfo(mailAddress: String, password: String, callback: SaveUserInfoCallback) {

        /*メールアドレスが一致するドキュメントを取得*/
        db.collection(PIGLEAD_USERS)
                .whereEqualTo("mailAddress", mailAddress)
                .get()
                .addOnSuccessListener { documents ->
                    /*メールアドレスが一致するドキュメントがなければ*/
                    if (documents.size() == 0) {
                        val userInfo = hashMapOf(
                                "mailAddress" to mailAddress,
                                "password" to password
                        )
                        /*以下は別スレッドにて実行*/
                        db.collection(PIGLEAD_USERS)
                                .add(userInfo)
                                .addOnSuccessListener { documentReference ->
                                    /*callbackに引数を渡す(existFlag)*/
                                    callback.onUserInfoSaved(false)
                                }
                                .addOnFailureListener { e ->

                                }
                    }
                    /*メールアドレスが一致するドキュメントがあれば*/
                    else {
                        Log.w(ContentValues.TAG, "Error already exist mailAddress")
                        /*callbackに引数を渡す(existFlag)*/
                        callback.onUserInfoSaved(true)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }
    }

    /*メールアドレスを指定して、パスワードを取得する*/
    override fun getUserInfo(mailAddress: String, callback: GetUserInfoCallback) {
        /*以下は別スレッドにて実行*/
        /*メールアドレスが一致するドキュメントを取得*/
        db.collection(PIGLEAD_USERS)
                .whereEqualTo("mailAddress", mailAddress)
                .get()
                .addOnSuccessListener { documents ->
                    /*メールアドレスが一致するドキュメントがあれば*/
                    if (documents.size() != 0) {
                        /*callbackに引数を渡す(パスワード)*/
                        callback.onUserInfoLoaded(documents.documents[0].get("password") as String)
                        Log.d(ContentValues.TAG, "${documents.documents[0].id} => ${documents.documents[0].data}")
                    }
                    /*メールアドレスが一致するドキュメントがなければ*/
                    else {
                        /*callbackに引数を渡す(空文字)*/
                        callback.onUserInfoLoaded("")
                        Log.w(ContentValues.TAG, "Error no mailAddress")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }
    }
  
    /*メールアドレスを指定してパスワードを変更*/
    override fun changeUserInfo(mailAddress: String, newPassword: String, callback: ChangeUserInfoCallback) {

        /*メールアドレスが一致するドキュメントを取得*/
        db.collection(PIGLEAD_USERS)
                .whereEqualTo("mailAddress", mailAddress)
                .get()
                .addOnSuccessListener { documents ->
                    /*メールアドレスが一致するドキュメントがあれば*/
                    if (documents.size() != 0) {
                        val userInfo = hashMapOf(
                                "mailAddress" to mailAddress,
                                "password" to newPassword
                        )
                        /*以下は別スレッドにて実行*/
                        db.collection(PIGLEAD_USERS)
                                .document(documents.documents[0].id)
                                .set(userInfo)
                                .addOnSuccessListener { documentReference ->
                                    /*callbackに呼び出し*/
                                    callback.onUserInfoSaved()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(ContentValues.TAG, "Error password changing is failure")
                                }
                    }
                    /*メールアドレスが一致するドキュメントがなければ*/
                    else {
                        Log.w(ContentValues.TAG, "Error mailAddress don't exist")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                }
    }


    /*CalendarData*/
    override fun getCalendarDataList(callback: LoadCalendarDataCallback) {}

    override fun getHoliday(callback: LoadHolidayCalendarDataCallback) {
        /*以下は別スレッドにて実行*/
        db.collection(PIGLEAD_SCHEDULES)
                .document(HOLIDAY_DOCUMENT)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        /*documentにドキュメントデータがあればtrue*/
                        if (document!!.exists()) {
                            /*documentからholidayNameのバリューを取得*/
                            val holiday = document.data
                            /*受け取ったデータを整形*/
                            val holidaySchedules: MutableList<CalendarData> = ArrayList()
                            for (obj in holiday!!.values) {
                                val holidayData = autoCast<Map<String, Any>>(obj)!!
                                val date = (holidayData["date"] as Timestamp?)!!.seconds * 1000
                                val data = CalendarData()
                                data.scheduleTitle = holidayData["nameInJapan"] as String?
                                data.scheduleStartAtDatetime = Date(date)
                                data.isHoliday = true
                                holidaySchedules.add(data)
                            }
                            /*callbackに引数を渡す(データ配列)*/
                            callback.onHolidayCalendarDataLoaded(holidaySchedules)
                            Log.d(ContentValues.TAG, holiday.toString())
                        } else {
                            Log.d(ContentValues.TAG, "No such document")
                        }
                    } else {
                        Log.d(ContentValues.TAG, "failure task firebase")
                    }
                    Log.d(ContentValues.TAG, "get failed with ", task.exception)
                }
    }


    /*Schedule*/
    override fun getSchedule(ScheduleIds: LongArray, callback: GetScheduleCallback) {}
    override fun setSchedule(schedule: Schedule, callback: SaveScheduleCallback) {}
    override fun getAllSchedules(callback: GetScheduleCallback) {}
    override fun removeScheduleByScheduleId(scheduleId: Long) {}
    override fun pickUpSchedules(targetStartDate: Date, targetEndDate: Date, callback: PickUpScheduleCallback) {}


    /*ScheduleGroup*/
    override fun insertScheduleGroup(colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int, callback: SaveScheduleGroupCallback) {}
    override fun deleteScheduleGroup(groupId: Int, callback: DeleteCallback) {}
    override fun getScheduleGroup(colorNumber: Int, callback: GetScheduleGroupCallback) {}
    override fun getListScheduleGroup(callback: GetScheduleGroupsCallback) {}
    override fun updateScheduleGroup(groupId: Int, colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int, callback: UpdateScheduleGroupCallback) {}


    /*Balance*/
    override fun getAllBalances(callback: GetBalanceCallback) {}
    override fun insertBalance(balance: Balance, callback: SaveBalanceCallback) {}
    override fun removeBalanceByBalanceId(balanceId: Long) {}


    /*CategoryData*/
    override fun getCategoriesData(categoryId: Int, callback: GetCategoriesDataCallback) {}
    override fun getCategoryData(categoryId: Int, callback: GetCategoryDataCallback) {}


    /*Category*/
    override fun getCategory(callback: GetCategoryCallback) {}


    /*BalanceCategory*/
    override fun insertBalanceCategory(editFlag: Boolean, balanceCategoryName: String, categoryId: Int, callback: SaveBalanceCategoryCallback) {}
    override fun deleteBalanceCategory(categoryId: Int, balanceCategoryId: Int, callback: DeleteCallback) {}



    fun <T> autoCast(obj: Any?): T? {
        return obj as T?
    }


    /*singleTon*/
    companion object {
        @Volatile
        private var INSTANCE: ScheduleDataRemoteSource? = null
        @JvmStatic
        fun getInstance(appExecutors: AppExecutors): ScheduleDataRemoteSource? {
            if (INSTANCE == null) {
                synchronized(ScheduleDataRemoteSource::class.java) { INSTANCE = ScheduleDataRemoteSource() }
            }
            return INSTANCE
        }
    }


    init {
        db = FirebaseFirestore.getInstance()
    }
}