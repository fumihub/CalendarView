package com.non_name_hero.calenderview.inputForm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.data.ScheduleGroup;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;
import com.non_name_hero.calenderview.utils.Injection;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class listAdapter extends BaseAdapter {

    private List<ScheduleGroup> list;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ScheduleRepository repository;

    private Intent intentOut;

    private Activity mActivity;
    private colorSelectActivity mColorSelectActivity;

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
        mColorSelectActivity = new colorSelectActivity();
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
        if (mColorSelectActivity.editFlag) {//ほかのActivityからとってきてる変数は常に初期化されている
            holder.destroyButton.setVisibility(View.VISIBLE);
        }
        //TODO　削除ボタン非表示
        else {
            holder.destroyButton.setVisibility(View.GONE);
        }

        holder.categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input画面に遷移
                //色選択遷移用intent
                intentOut = new Intent();
                //ボタンの色番号を遷移先へreturn
                intentOut.putExtra("ColorNumber", list.get(position).getColorNumber());
                mActivity.setResult(RESULT_OK, intentOut);
                mActivity.finish();
            }
        });

        holder.destroyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleGroup group = list.get(position);
                int colorNumber = group.getColorNumber();
                repository.deleteScheduleGroup(colorNumber);
                //TODO 成功したら削除
                //TODO 削除するか確認もしたい？
                //TODO 確認する→現在この色が使われているスケジュールはすべて未分類になる
                list.remove(position);
                notifyDataSetChanged();
            }
        });


        return convertView;
    }


}
