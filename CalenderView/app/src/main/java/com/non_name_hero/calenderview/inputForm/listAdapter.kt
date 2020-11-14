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

    private var list: MutableList<ScheduleGroup>                                /*色グループのリスト*/

    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext) /**/

    private var repository: ScheduleRepository                                  /**/

    private val mActivity: Activity                                             /*InputActivityのActivity*/

    var deleteDialog: PigLeadDeleteDialog? = null                               /**/

    /*カスタムセルを拡張したらここでWigetを定義*/
    private class ViewHolder {
        lateinit var categoryButton: Button                                     /*ListViewのカテゴリーボタン*/
        lateinit var destroyButton: Button                                      /*ListViewの削除ボタン*/
    }

    fun setList(input: List<ScheduleGroup>) {
        list = input as MutableList<ScheduleGroup>
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
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        /*
        getViewでは引数convertViewに画面外に移動してリサイクルされたViewが入る(初めの数回は画面外に移動するViewなどはもちろんない)
        リサイクルされるViewがない場合 -> Viewを生成する。Viewのタグにviewオブジェクト(textやボタン)への参照を格納しておく
        リサイクルされるVIewがあるとき -> Viewを生成しない。
        その後holerに格納されている参照を利用してリストのitemを更新する
         */
        val view = convertView ?: mLayoutInflater.inflate(R.layout.list_cell, null).apply {
            //convertViewがnullだった場合、タグにviewHolderを設定するため、新たにviewholderを作成する
            this.tag = ViewHolder()
            (this.tag as ViewHolder).categoryButton = this.findViewById(R.id.categoryButton)
            (this.tag as ViewHolder).destroyButton = this.findViewById(R.id.colorDestroyButton)
        }

        /*リストのセルを取得(viewHolder)*/
        val holder: ViewHolder = view.tag as ViewHolder

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
                    repository.deleteScheduleGroup(groupId, object : DeleteCallback {
                        override fun onDeleted() {
                            list.removeAt(position)
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
        return view
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
        list = ArrayList()
        mActivity = activity
        repository = Injection.provideScheduleRepository(mContext)
    }
    /************************************************/

}