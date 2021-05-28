package com.non_name_hero.calenderview.inputForm

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection

class CategorySelectActivity  /*コンストラクタ*/
    : AppCompatActivity() {

    private lateinit var context: Context                           /*CategorySelectActivityのcontext*/

    private lateinit var ListAdapter: ListAdapter                   /*カテゴリーグループリストアダプタ*/

    private lateinit var listView: ListView                         /*カテゴリーグループリストビュー*/

    private lateinit var repository: ScheduleRepository             /**/

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
        ListAdapter = ListAdapter(context, this)
        listView.adapter = ListAdapter

        /*DBから情報を取得*/
        loadColorList()
    }

    /*List内容を更新する関数************************/
    private fun loadColorList() {
        repository.getListScheduleGroup(object : ScheduleDataSource.GetScheduleGroupsCallback {
            override fun onScheduleGroupsLoaded(Groups: List<ScheduleGroup>) {
                //取得後の処理
                ListAdapter.setList(Groups)
            }

            override fun onDataNotAvailable() {}
        })
    }
    /************************************************/

}