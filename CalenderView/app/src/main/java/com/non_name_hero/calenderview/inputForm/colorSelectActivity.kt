package com.non_name_hero.calenderview.inputForm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.calendar.CalendarViewModel
import com.non_name_hero.calenderview.calendar.MainActivity
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.GetScheduleGroupsCallback
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.databinding.ActivityMainBinding
import com.non_name_hero.calenderview.databinding.ColorSelectBinding
import com.non_name_hero.calenderview.utils.Injection
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDeleteDialog
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase.DialogCallback
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogFragment
import com.non_name_hero.calenderview.utils.obtainViewModel

class ColorSelectActivity  /*コンストラクタ*/
    : AppCompatActivity(), PigLeadDeleteDialog {

    private lateinit var binding: ColorSelectBinding                /*ColorSelectActivityのbinding*/

    private lateinit var context: Context                           /*ColorSelectActivityのcontext*/

    private lateinit var ListAdapter: ListAdapter                   /*色グループリストアダプタ*/

    private lateinit var listView: ListView                         /*色グループリストビュー*/

    private lateinit var editButton: Button                         /*色グループ編集ボタン*/
    private lateinit var colorCreateButton: Button                  /*色グループ作成ボタン*/

    private lateinit var repository: ScheduleRepository             /**/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*初期設定***************************************/
        context = this
        repository = Injection.provideScheduleRepository(context)
        //DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.color_select)
        //LifecycleOwnerを指定
        binding.lifecycleOwner = this
        /*ビューモデル設定*/
        binding.viewModel = obtainViewModel()
        val myToolbar = findViewById<View>(R.id.colorSelectToolbar) as Toolbar
        editButton = findViewById(R.id.editButton)
        colorCreateButton = findViewById(R.id.colorCreateButton)
        setSupportActionBar(myToolbar)

        /*ListViewのIDを取得*/
        listView = findViewById(R.id.listView)

        /*リストのアダプターを使用してViewを作成*/
        ListAdapter = ListAdapter(context, this)
        //削除ダイアログを設定
        ListAdapter.deleteDialog = this
        listView.adapter = ListAdapter

        /*DBから情報を取得*/
        loadColorList()
        /************************************************/

        /*編集ボタンが押されたとき***********************/
        editButton.setOnClickListener {
            /*ボタンの文字が「編集」ならば*/
            if (editButton.text.toString() == "編集") {
                /*リストビューに削除ボタン表示*/
                jdgEditMode(true, "完了")
                /*色作成ボタン非表示*/
                colorCreateButton.visibility = View.GONE
            } else {
                /*リストビューから削除ボタンを非表示に*/
                jdgEditMode(false, "編集")
                /*色作成ボタン表示*/
                colorCreateButton.visibility = View.VISIBLE
            }
        }
        /************************************************/

        /*色作成ボタンが押されたとき********************/
        colorCreateButton.setOnClickListener {
            /*色作成画面に遷移*/
            goColorCreateActivity()
        }
        /************************************************/

    }

//    /*画面表示時処理関数*******************************/
//    public override fun onResume() {
//        super.onResume()
//
//        /*アプリ再開時にeditFlagを0にする*/
//        /*リストビューから削除ボタンを非表示に*/
//        jdgEditMode(false, "編集")
//        /*色作成ボタン表示*/
//        colorCreateButton.visibility = View.VISIBLE
//    }
//
//    /************************************************/

    /*編集モードかを判定する関数********************/
    private fun jdgEditMode(value: Boolean, str: String) {
//        /*SharedPreferenceでeditFlagの値を変更*/
//        val prefs = getSharedPreferences("input_data", MODE_PRIVATE)
//        val editor = prefs.edit()
//        /*SharedPreferenceにeditFlagの値を保存*/
//        editor.putBoolean("editFlag", value)
//        /*非同期処理ならapply()、同期処理ならcommit()*/
//        editor.apply()
        binding.viewModel?.setCurrentEditColorMode(value)
        listView.adapter = ListAdapter
        /*ボタン文字の切り替え(編集/完了)*/
        editButton.text = str
    }

    /************************************************/

    /*色作成画面遷移関数*****************************/
    private fun goColorCreateActivity() {
        /*色作成画面用intent*/
        val intentOut = Intent(this, ColorCreateActivity::class.java)
        /*戻り値を設定して色画面に遷移*/
        startActivityForResult(intentOut, REQUEST_CODE)
    }

    /************************************************/

    /*Activityから戻ってきてからList内容を更新する関数*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    /*DB問い合わせて更新*/
                    loadColorList()
                }
                RESULT_CANCELED -> {
                    /*キャンセルボタンを押して戻ってきたときの処理*/
                }
                else -> {
                    /*その他*/
                }
            }
            else -> {
            }
        }
    }

    /************************************************/

    /*List内容を更新する関数************************/
    private fun loadColorList() {
        repository.getListScheduleGroup(object : GetScheduleGroupsCallback {
            override fun onScheduleGroupsLoaded(Groups: List<ScheduleGroup>) {
                //取得後の処理
                ListAdapter.setList(Groups)
            }

            override fun onDataNotAvailable() {}
        })
    }
    /************************************************/

    /**
     * 削除用ダイアログを設定
     *
     * @param callback ダイアログのボタン押下時の処理
     * @return dialog DialogFragmentのオブジェクト
     */
    override fun getDeleteDialog(scheduleGroup: ScheduleGroup?, callback: DialogCallback): PigLeadDialogFragment? {
        /*表示させるメッセージの定義*/
        val dialogMessages = ArrayList(listOf(*resources.getStringArray(R.array.delete_schedule_group_dialog_massage)))
        /*表示メッセージに削除対象の名前を挿入*/
        dialogMessages[0] = String.format(dialogMessages[0], scheduleGroup?.groupName)
        val positiveBtnMessage = getString(R.string.delete_schedule_group_positive)
        val negativeBtnMessage = getString(R.string.delete_schedule_group_negative)
        /*AlertDialogを設定*/
        val dialog = PigLeadDialogFragment(context)
        dialog.setDialogMessage(dialogMessages)
                .setPositiveBtnMessage(positiveBtnMessage)
                .setNegativeBtnMessage(negativeBtnMessage)
                .setPositiveClickListener { dialog, which -> callback.onClickPositiveBtn() }
                .setNegativeClickListener { dialog, which -> callback.onClickNegativeBtn() }
        return dialog
    }

    /************************************************/

    /*削除警告ダイアログ表示関数*********************/
    override fun showPigLeadDiaLog(dialog: PigLeadDialogFragment?) {
        /*AlertDialogを表示*/
        dialog?.show(supportFragmentManager, DELETE_DIALOG_TAG)
    }

    /************************************************/

    /*定数定義***************************************/
    companion object {
        private const val REQUEST_CODE = 1
        private const val DELETE_DIALOG_TAG = "DELETE_DIALOG"
    }
    /************************************************/

    /**
     * ViewModelを取得する
     * (this.obtainViewModel(Class: ViewModel)は拡張関数)
     * @return viewModel {CalendarViewModel} カレンダー関連の情報を保持するViewModel
     */
    fun obtainViewModel(): ColorSelectViewModel = this.obtainViewModel(ColorSelectViewModel::class.java)

}