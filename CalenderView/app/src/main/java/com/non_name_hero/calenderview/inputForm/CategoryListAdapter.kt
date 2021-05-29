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
import android.widget.ImageButton
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.Category
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.DeleteCallback
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDeleteDialog
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase.DialogCallback
import java.util.*




class CategoryListAdapter(private val mContext: Context, activity: Activity) : BaseAdapter() {

    /*TODO カテゴリーリスト作成*/
    private var list: MutableList<Category>                                     /*大カテゴリ－のリスト*/

    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext) /**/

    private var repository: ScheduleRepository                                  /**/

    private val mActivity: Activity                                             /*InputActivityのActivity*/

    /*カスタムセルを拡張したらここでWigetを定義*/
    private class ViewHolder {
        lateinit var categoryIconButton: ImageButton                            /*ListViewのカテゴリーアイコンボタン*/
        lateinit var categoryButton: Button                                     /*ListViewのカテゴリーボタン*/
        lateinit var destroyButton: Button                                      /*ListViewの削除ボタン*/
    }

    fun setList(input: List<Category>) {
        list = input as MutableList<Category>
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
        val view = convertView ?: mLayoutInflater.inflate(R.layout.category_list_cell, null).apply {
            //convertViewがnullだった場合、タグにviewHolderを設定するため、新たにviewholderを作成する
            this.tag = ViewHolder()
            (this.tag as ViewHolder).categoryIconButton = this.findViewById(R.id.categoryIconButton)
            (this.tag as ViewHolder).categoryButton = this.findViewById(R.id.categoryButton)
            (this.tag as ViewHolder).destroyButton = this.findViewById(R.id.categoryDestroyButton)
        }

        /*リストのセルを取得(viewHolder)*/
        val holder: ViewHolder = view.tag as ViewHolder

        /*アイコンボタン設定*/
//        val id: Int = mContext.getResources().getIdentifier(list[position].imgURL, "drawable", mContext.getPackageName())
//        holder.categoryIconButton.setImageResource(id)
//        holder.categoryIconButton.setImageDrawable(mContext.getResources().getDrawable(id))
        holder.categoryIconButton.setBackgroundColor(list[position].categoryColor)
        /*カテゴリボタン設定*/
        holder.categoryButton.text = list[position].bigCategoryName
        /*削除ボタン非表示*/
        holder.destroyButton.visibility = View.GONE

        /*クリックリスナー設定*/
        holder.categoryButton.setOnClickListener {
            /*サブカテゴリー選択画面遷移*/
            goSubCategorySelectActivity(position)
        }

        return view
    }

    /************************************************/

    /*サブカテゴリー選択画面遷移*******************************/
    private fun goSubCategorySelectActivity(position: Int) {
        /*サブカテゴリー選択画面遷移用intent*/
        val intentOut = Intent(mContext, SubCategorySelectActivity::class.java)
        /*カテゴリーIDをサブカテゴリー選択画面へ渡す*/
        intentOut.putExtra("CategoryID", list[position].categoryId)
        /*戻り値を設定してサブカテゴリー選択画面遷移*/
        mActivity.startActivityForResult(intentOut, REQUEST_CODE)
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