package com.non_name_hero.calenderview.inputForm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.Category
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection

class CategorySelectActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var context: Context                           /*CategorySelectActivityのcontext*/

    private lateinit var ListAdapter: CategoryListAdapter           /*カテゴリーグループリストアダプタ*/

    private lateinit var listView: ListView                         /*カテゴリーグループリストビュー*/

    private lateinit var repository: ScheduleRepository             /**/

    private var balanceCategoryId = 22                               /*サブカテゴリID*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*初期設定***************************************/
        context = this
        repository = Injection.provideScheduleRepository(context)
        setContentView(R.layout.category_select)
        val myToolbar = findViewById<View>(R.id.categorySelectToolbar) as Toolbar
        setSupportActionBar(myToolbar)

        /*ListViewのIDを取得*/
        listView = findViewById(R.id.listView)

        /*リストのアダプターを使用してViewを作成*/
        ListAdapter = CategoryListAdapter(context, this)
        listView.adapter = ListAdapter

        /*DBから情報を取得*/
        loadCategoryList()
    }

    /*List内容を更新する関数************************/
    private fun loadCategoryList() {
        repository.getCategory(object : ScheduleDataSource.GetCategoryCallback {
            override fun onCategoryLoaded(category: List<Category>) {
                //取得後の処理
                ListAdapter.setList(category)
            }

            override fun onDataNotAvailable() {}
        })
    }
    /************************************************/

    /*SubCategorySelectActivityからbalanceCategoryIdをもらって、InputBalanceActivityに渡す関数*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    /*バランスカテゴリID受け取り*/
                    balanceCategoryId = data!!.getIntExtra("BalanceCategoryId", 22)
                    /*InputBalanceActivity遷移用intent*/
                    val intentOut = Intent(this, InputBalanceActivity::class.java)
                    /*バランスカテゴリIDを遷移先へreturn*/
                    intentOut.putExtra("BalanceCategoryId", balanceCategoryId)
                    setResult(RESULT_OK, intentOut)
                    finish()
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

    /*定数定義****************************************/
    companion object {
        private const val REQUEST_CODE = 1
    }
    /************************************************/

}