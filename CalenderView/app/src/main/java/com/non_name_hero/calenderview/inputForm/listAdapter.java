package com.non_name_hero.calenderview.inputForm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.data.ScheduleGroup;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;
import com.non_name_hero.calenderview.utils.Injection;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static java.lang.Boolean.FALSE;

public class listAdapter extends BaseAdapter {

    private static final int REQUEST_CODE = 1;

    private List<ScheduleGroup> list;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ScheduleRepository repository;
    private SharedPreferences prefs;

    private Intent intentOut;

    private Activity mActivity;

    //カスタムセルを拡張したらここでWigetを定義
    private static class ViewHolder {
        public Button categoryButton;
        public Button destroyButton;
    }

    //コンストラクタ
    public listAdapter(Context context, Activity activity) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        list = new ArrayList<>();
        mActivity = activity;
        repository = Injection.provideScheduleRepository(mContext);
    }

    public void setList(List<ScheduleGroup> input) {
        list = input;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //データベースのサイズを返す
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final listAdapter.ViewHolder holder;
        //初回時の処理 convertViewがnullの場合にはinflateしたViewを代入する。
        if (convertView == null) {
            //convertViewに
            convertView = mLayoutInflater.inflate(R.layout.list_cell, null);
            /*------リストセルの作成-----*/
            holder = new listAdapter.ViewHolder();

            //色ボタン
            holder.categoryButton = convertView.findViewById(R.id.categoryButton);
            //削除ボタン
            holder.destroyButton = convertView.findViewById(R.id.colorDestroyButton);

            convertView.setTag(holder);
        } else {
            //convertViewがnullでなければconvertViewを再利用する。
            holder = (listAdapter.ViewHolder) convertView.getTag();
        }

        //リストの色ボタンにテキストをセット
        holder.categoryButton.setText(list.get(position).getGroupName());
        //リストの色ボタンに色をセット
        holder.categoryButton.setBackgroundColor(list.get(position).getBackgroundColor());
        //リストの色ボタンに文字色をセット
        if (list.get(position).getCharacterColor().equals("黒")) {//黒ならば
            holder.categoryButton.setTextColor(Color.BLACK);
        }
        else {//白ならば
            holder.categoryButton.setTextColor(Color.WHITE);
        }

        //TODO　削除ボタン表示
        //SharedPreferenceからeditFlagの値を取得
        prefs = mContext.getSharedPreferences("input_data", MODE_PRIVATE);
        if (prefs.getBoolean("editFlag", FALSE)
        && (list.get(position).getColorNumber() != 43)) {
            holder.destroyButton.setVisibility(View.VISIBLE);
        }
        //TODO　削除ボタン非表示
        else {
            holder.destroyButton.setVisibility(View.GONE);
        }

        holder.categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean("editFlag", FALSE)){
                    if (list.get(position).getColorNumber() == 43){
                        /* 何もしない */
                    }
                    else {
                        goColorCreateActivity(position);
                    }
                }
                else {
                    returnInputActivity(position);
                }
            }
        });

        holder.destroyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleGroup group = list.get(position);
                repository.deleteScheduleGroup(group.getGroupId(), new ScheduleDataSource.DeleteCallback() {
                    @Override
                    public void onDeleted() {
                        list.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onDataNotDeleted() {
                        Log.d("ERROR", "削除に失敗しました。");
                    }
                });
                //TODO 成功したら削除
                //TODO 削除するか確認もしたい？
                //TODO 確認する→現在この色が使われているスケジュールはすべて未分類になる
            }
        });

        return convertView;
    }

    //色作成画面に遷移
    private void goColorCreateActivity(int position) {
        //色作成遷移用intent
        Intent intentOut = new Intent(mContext, colorCreateActivity.class);
        //ボタンの色番号を遷移先へgo
        intentOut.putExtra("ColorNumberPre", list.get(position).getColorNumber());
        //戻り値を設定して色画面に遷移
        mActivity.startActivityForResult(intentOut, REQUEST_CODE);
    }

    //input画面に遷移
    private void returnInputActivity(int position) {
        //色選択遷移用intent
        intentOut = new Intent();
        //ボタンの色番号を遷移先へreturn
        intentOut.putExtra("ColorNumber", list.get(position).getColorNumber());
        mActivity.setResult(RESULT_OK, intentOut);
        mActivity.finish();
    }


}
