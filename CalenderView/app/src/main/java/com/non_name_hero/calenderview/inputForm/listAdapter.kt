package com.non_name_hero.calenderview.inputForm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.DeleteCallback
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDeleteDialog
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase.DialogCallback
import java.lang.Boolean
import java.util.*

class listAdapter(private val mContext: Context, activity: Activity) : BaseAdapter() {

    private var list: List<ScheduleGroup>

    private val mLayoutInflater: LayoutInflater

    private var repository: ScheduleRepository? = null

    private val mActivity: Activity

    var deleteDialog: PigLeadDeleteDialog? = null

    /*カスタムセルを拡張したらここでWigetを定義*/
    private class ViewHolder {
        lateinit var categoryButton: Button
        lateinit var destroyButton: Button
    }

    fun setList(input: List<ScheduleGroup>) {
        list = input
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        /*データベースのサイズを返す*/
        return list.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /*リストビュー定義*******************************/
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        /*初回時の処理 convertViewがnullの場合にはinflateしたViewを代入する。*/
        if (convertView == null) {
            /*convertViewに*/
            convertView = mLayoutInflater.inflate(R.layout.list_cell, null)
            /*リストセルの作成*/
            holder = ViewHolder()

            /*色ボタン*/
            holder.categoryButton = convertView.findViewById(R.id.categoryButton)
            /*削除ボタン*/
            holder.destroyButton = convertView.findViewById(R.id.colorDestroyButton)
            convertView.tag = holder
        } else {
            /*convertViewがnullでなければconvertViewを再利用する。*/
            holder = convertView.tag as ViewHolder
        }

        /*リストの色ボタンにテキストをセット*/
        holder.categoryButton.text = list[position].groupName
        /*リストの色ボタンに色をセット*/
        holder.categoryButton.setBackgroundColor(list[position].backgroundColor)
        /*リストの色ボタンに文字色をセット*/
        if (list[position].characterColor == "黒") { /*黒ならば*/
            holder.categoryButton.setTextColor(Color.BLACK)
        } else { /*白ならば*/
            holder.categoryButton.setTextColor(Color.WHITE)
        }

        //TODO　削除ボタン表示
        /*SharedPreferenceからeditFlagの値を取得*/
        val prefs = mContext.getSharedPreferences("input_data", Context.MODE_PRIVATE)
        if (prefs.getBoolean("editFlag", Boolean.FALSE)
                && list[position].colorNumber != 43) {
            holder.destroyButton.visibility = View.VISIBLE
        } else {
            holder.destroyButton.visibility = View.GONE
        }
        holder.categoryButton.setOnClickListener {
            if (prefs.getBoolean("editFlag", Boolean.FALSE)) {
                if (list[position].colorNumber == 43) {
                    /* 何もしない */
                } else {
                    goColorCreateActivity(position)
                }
            } else {
                returnInputActivity(position)
            }
        }
        holder.destroyButton.setOnClickListener {
            val scheduleGroup = list[position]
            /*指定したpositionを削除するダイアログを作成*/
            val dialog = deleteDialog!!.getDeleteDialog(scheduleGroup, object : DialogCallback {
                override fun onClickPositiveBtn() {
                    val groupId = scheduleGroup.groupId
                    /*DBから削除*/
                    repository!!.deleteScheduleGroup(groupId, object : DeleteCallback {
                        override fun onDeleted() {
                            list.remove(position)
                            notifyDataSetChanged()
                        }

                        override fun onDataNotDeleted() {
                            Log.d("ERROR", "削除に失敗しました。")
                        }
                    })
                }

                override fun onClickNegativeBtn() {
                    /*何もしない*/
                }
            })
            deleteDialog!!.showPigLeadDiaLog(dialog)
        }
        return convertView
    }
    /************************************************/

    /*色作成画面に遷移*******************************/
    private fun goColorCreateActivity(position: Int) {
        /*色作成遷移用intent*/
        val intentOut = Intent(mContext, colorCreateActivity::class.java)
        /*ボタンの色番号を遷移先へgo*/
        intentOut.putExtra("ColorNumberPre", list[position].colorNumber)
        /*戻り値を設定して色画面に遷移*/
        mActivity.startActivityForResult(intentOut, REQUEST_CODE)
    }
    /************************************************/

    /*input画面に遷移********************************/
    private fun returnInputActivity(position: Int) {
        /*新規作成画面遷移用intent*/
        val intentOut = Intent()
        /*ボタンの色番号を遷移先へreturn*/
        intentOut.putExtra("ColorNumber", list[position].colorNumber)
        mActivity.setResult(Activity.RESULT_OK, intentOut)
        mActivity.finish()
    }
    /************************************************/

    /*定数定義****************************************/
    companion object {
        private const val REQUEST_CODE = 1
    }
    /************************************************/

    /*コンストラクタ*********************************/
    init {
        mLayoutInflater = LayoutInflater.from(mContext)
        list = ArrayList()
        mActivity = activity
        repository = Injection.provideScheduleRepository(mContext)
    }
    /************************************************/

}