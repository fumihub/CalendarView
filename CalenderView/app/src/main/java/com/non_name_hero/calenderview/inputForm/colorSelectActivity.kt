package com.non_name_hero.calenderview.inputForm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.GetScheduleGroupsCallback
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDeleteDialog
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase.DialogCallback
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogFragment
import java.util.*

class colorSelectActivity  //コンストラクタ
    : AppCompatActivity(), PigLeadDeleteDialog {
    private var context: Context? = null
    private var listAdapter: listAdapter? = null
    private var listView: ListView? = null
    private var editButton: Button? = null
    private var colorCreateButton: Button? = null
    private var intentOut: Intent? = null
    private var repository: ScheduleRepository? = null
    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var createFlag = java.lang.Boolean.FALSE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        repository = Injection.provideScheduleRepository(context)
        setContentView(R.layout.color_select)
        val myToolbar = findViewById<View>(R.id.colorSelectToolbar) as Toolbar
        editButton = findViewById(R.id.editButton)
        setSupportActionBar(myToolbar)

        //ListViewのIDを取得
        listView = findViewById(R.id.listView)

        //リストのアダプターを使用してViewを作成
        listAdapter = listAdapter(context, this)
        //削除ダイアログを設定
        listAdapter!!.deleteDialog = this
        listView.setAdapter(listAdapter)

        //DBから情報を取得
        loadColorList()

        //色作成画面用intent
        intentOut = Intent(this, colorCreateActivity::class.java)
        colorCreateButton = findViewById(R.id.colorCreateButton)

        /*編集ボタンが押されたとき*************************/editButton.setOnClickListener(View.OnClickListener {
            //ボタンの文字が「編集」ならば
            if (editButton.getText().toString() == "編集") {
                //TODO　リストビューに削除ボタン表示
                jdgEditMode(java.lang.Boolean.TRUE, "完了")
                //色作成ボタン非表示
                colorCreateButton.setVisibility(View.GONE)
            } else {
                //TODO　リストビューから削除ボタンを非表示に
                jdgEditMode(java.lang.Boolean.FALSE, "編集")
                //色作成ボタン表示
                colorCreateButton.setVisibility(View.VISIBLE)
            }
        })
        /** */

        /*色作成ボタンが押されたとき************************/colorCreateButton.setOnClickListener(View.OnClickListener { //色作成画面に遷移
            goColorCreateActivity()
        })
        /** */

        //editFlag判定用Flag
        createFlag = java.lang.Boolean.TRUE
    }

    public override fun onResume() {
        super.onResume()

        //createFlagがTRUEならば
        if (createFlag) {
            //アプリ再開時にeditFlagを0にする
            //TODO　リストビューから削除ボタンを非表示に
            jdgEditMode(java.lang.Boolean.FALSE, "編集")
        } else {
            /* 何もしない */
        }
    }

    private fun jdgEditMode(value: Boolean, str: String) {
        prefs = getSharedPreferences("input_data", MODE_PRIVATE)
        editor = prefs.edit()
        //SharedPreferenceにeditFlagの値を保存
        editor.putBoolean("editFlag", value)
        //非同期処理ならapply()、同期処理ならcommit()
        editor.commit()
        listView!!.adapter = listAdapter
        //ボタン文字の切り替え(編集/完了)
        editButton!!.text = str
    }

    fun goColorCreateActivity() {
        //戻り値を設定して色画面に遷移
        startActivityForResult(intentOut, REQUEST_CODE)
    }

    //Activityから戻ってきたときの処理
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> if (resultCode == RESULT_OK) {
                //DB問い合わせて更新
                loadColorList()
            } else if (resultCode == RESULT_CANCELED) {
                //キャンセルボタンを押して戻ってきたときの処理
            } else {
                //その他
            }
            else -> {
            }
        }
    }

    private fun loadColorList() {
        repository!!.getListScheduleGroup(object : GetScheduleGroupsCallback {
            override fun onScheduleGroupsLoaded(Groups: List<ScheduleGroup?>?) {
                //取得後の処理
                listAdapter!!.setList(Groups)
            }

            override fun onDataNotAvailable() {}
        })
    }

    /**
     * 削除用ダイアログを設定
     *
     * @param callback ダイアログのボタン押下時の処理
     * @return dialog DialogFragmentのオブジェクト
     */
    override fun getDeleteDialog(scheduleGroup: ScheduleGroup, callback: DialogCallback): PigLeadDialogFragment {
        // 表示させるメッセージの定義
        val dialogMessages = ArrayList(Arrays.asList(*resources.getStringArray(R.array.delete_schedule_group_dialog_massage)))
        // 表示メッセージに削除対象の名前を挿入
        dialogMessages[0] = String.format(dialogMessages[0], scheduleGroup.groupName)
        val positiveBtnMessage = getString(R.string.delete_schedule_group_positive)
        val negativeBtnMessage = getString(R.string.delete_schedule_group_negative)
        // AlertDialogを設定
        val dialog = PigLeadDialogFragment(context)
        dialog.setDialogMessage(dialogMessages)
                .setPositiveBtnMessage(positiveBtnMessage)
                .setNegativeBtnMessage(negativeBtnMessage)
                .setPositiveClickListener { dialog, which -> callback.onClickPositiveBtn() }
                .setNegativeClickListener { dialog, which -> callback.onClickNegativeBtn() }
        return dialog
    }

    override fun showPigLeadDiaLog(dialog: PigLeadDialogFragment) {
        // AlertDialogを表示
        dialog.show(supportFragmentManager, DELETE_DIALOG_TAG)
    }

    companion object {
        private const val REQUEST_CODE = 1
        private const val DELETE_DIALOG_TAG = "DELETE_DIALOG"
    }
}