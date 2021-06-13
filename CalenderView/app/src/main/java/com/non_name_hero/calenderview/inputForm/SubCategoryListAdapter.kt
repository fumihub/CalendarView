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
import com.non_name_hero.calenderview.data.BalanceCategory
import com.non_name_hero.calenderview.data.CategoryData
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.DeleteCallback
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadBalanceCategoryDeleteDialog
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase.DialogCallback
import java.util.*

class SubCategoryListAdapter(private val mContext: Context, activity: Activity) : BaseAdapter() {

    private var list: MutableList<CategoryData>                              /*サブカテゴリーグループのリスト*/

    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext) /**/

    private var repository: ScheduleRepository                                  /**/

    private val mActivity: Activity                                             /*SubCategorySelectActivityのActivity*/

    var deleteDialog: PigLeadBalanceCategoryDeleteDialog? = null                /**/

    var editMode: Boolean = false

    /*カスタムセルを拡張したらここでWigetを定義*/
    private class ViewHolder {
        lateinit var categoryIconButton: ImageButton                            /*ListViewのカテゴリーアイコンボタン*/
        lateinit var categoryButton: Button                                     /*ListViewのカテゴリーボタン*/
        lateinit var categoryButton2: Button                                    /*ListViewの矢印ボタン*/
        lateinit var destroyButton: Button                                      /*ListViewの削除ボタン*/
    }

    fun setList(input: List<CategoryData>) {
        list = input as MutableList<CategoryData>
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
            (this.tag as ViewHolder).categoryButton2 = this.findViewById(R.id.categoryButton2)
            (this.tag as ViewHolder).destroyButton = this.findViewById(R.id.categoryDestroyButton)

        }

        /*リストのセルを取得(viewHolder)*/
        val holder: ViewHolder = view.tag as ViewHolder
        /*アイコンボタン設定*/
        val id: Int = mContext.getResources().getIdentifier(list[position].imgURL, "drawable", mContext.getPackageName())
        holder.categoryIconButton.setImageResource(id)
        holder.categoryIconButton.setBackgroundColor(list[position].categoryColor)
        /*リストのカテゴリーボタンにテキストをセット*/
        holder.categoryButton.text = list[position].categoryName
        /*矢印ボタン非表示*/
        holder.categoryButton2.visibility = View.GONE

        /*TODO　ViewModeに切り替え*/
        //TODO　削除ボタン表示
//        /*SharedPreferenceからeditFlagの値を取得*/
//        val prefs = mContext.getSharedPreferences("input_data", Context.MODE_PRIVATE)

        if (editMode
                && list[position].editableFlg) {
            holder.destroyButton.visibility = View.VISIBLE
        } else {
            holder.destroyButton.visibility = View.GONE
        }

        /*クリックリスナー設定*/
        holder.categoryButton.setOnClickListener {
            if (editMode) {
                /* 何もしない */
//                if (list[position].editableFlg) {
//                    /* 何もしない */
//                } else {
//                    goCategoryCreateActivity(position)
//                }
            } else {
                returnCategorySelectActivity(position)
            }
        }
        holder.categoryIconButton.setOnClickListener {
            if (editMode) {
                /* 何もしない */
//                if (list[position].editableFlg) {
//                    /* 何もしない */
//                } else {
//                    goCategoryCreateActivity(position)
//                }
            } else {
                returnCategorySelectActivity(position)
            }
        }
        /************************/

        holder.destroyButton.setOnClickListener {
            val categoryData = list[position]
            /*指定したpositionを削除するダイアログを作成*/
            val dialog = deleteDialog!!.getBalanceCategoryDeleteDialog(categoryData, object : DialogCallback {
                override fun onClickPositiveBtn() {
                    val categoryId = categoryData.categoryId
                    val balanceCategoryId = categoryData.balanceCategoryId
                    /*DBから削除*/
                    repository.deleteBalanceCategory(categoryId, balanceCategoryId, object : DeleteCallback {
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

//    /*カテゴリ作成画面に遷移*******************************/
//    private fun goCategoryCreateActivity(position: Int) {
//        /*カテゴリ作成画面遷移用intent*/
//        val intentOut = Intent(mContext, SubCategoryCreateActivity::class.java)
//        /*カテゴリIDを遷移先へgo*/
//        intentOut.putExtra("CategoryId", list[position].categoryId)
//        /*戻り値を設定してカテゴリ作成画面に遷移*/
//        mActivity.startActivityForResult(intentOut, REQUEST_CODE)
//    }
//
//    /************************************************/

    /*カテゴリ選択画面に遷移********************************/
    private fun returnCategorySelectActivity(position: Int) {
        /*カテゴリ選択画面遷移用intent*/
        val intentOut = Intent(mContext, CategorySelectActivity::class.java)
        /*バランスカテゴリIDを遷移先へreturn*/
        intentOut.putExtra("BalanceCategoryId", list[position].balanceCategoryId)
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