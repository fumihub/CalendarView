package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.data.ScheduleGroup;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;
import com.non_name_hero.calenderview.utils.Injection;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class colorSelectActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private listAdapter mListAdapter;
    private ListView listView;

    private Button editButton;
    private Button colorCreateButton;

    private Intent intentOut;
    private ScheduleRepository repository;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    //コンストラクタ
    public colorSelectActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = Injection.provideScheduleRepository(getApplicationContext());

        setContentView(R.layout.color_select);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorSelectToolbar);
        editButton = findViewById(R.id.editButton);
        setSupportActionBar(myToolbar);

        //ListViewのIDを取得
        listView = findViewById(R.id.listView);

        //リストのアダプターを使用してViewを作成
        mListAdapter = new listAdapter(getApplicationContext(), this);
        listView.setAdapter(mListAdapter);

        //DBから情報を取得
        loadColorList();

        //色作成画面用intent
        intentOut = new Intent(this, colorCreateActivity.class);

        colorCreateButton = findViewById(R.id.colorCreateButton);

        /*編集ボタンが押されたとき*************************/
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ボタンの文字が「編集」ならば
                if (editButton.getText().toString().equals("編集")) {
                    //TODO　リストビューに削除ボタン表示
                    jdgEditMode(TRUE, "完了");
                    //色作成ボタン非表示
                    colorCreateButton.setVisibility(View.GONE);
                }
                //ボタンの文字が「完了ならば」
                else {
                    //TODO　リストビューから削除ボタンを非表示に
                    jdgEditMode(FALSE, "編集");
                    //色作成ボタン表示
                    colorCreateButton.setVisibility(View.VISIBLE);
                }
            }
        });
        /************************************************/

        /*色作成ボタンが押されたとき************************/
        colorCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色作成画面に遷移
                goColorCreateActivity();
            }
        });
        /************************************************/

    }

    @Override
    public void onResume(){
        super.onResume();

        //アプリ再開時にeditFlagを0にする
        //TODO　リストビューから削除ボタンを非表示に
        jdgEditMode(FALSE, "編集");
    }

    private void jdgEditMode(boolean value, String str){
        prefs = getSharedPreferences("input_data", MODE_PRIVATE);
        editor = prefs.edit();
        //SharedPreferenceにeditFlagの値を保存
        editor.putBoolean("editFlag",value);
        //非同期処理ならapply()、同期処理ならcommit()
        editor.commit();
        listView.setAdapter(mListAdapter);
        //ボタン文字の切り替え(編集/完了)
        editButton.setText(str);
    }

    public void goColorCreateActivity() {
        //戻り値を設定して色画面に遷移
        startActivityForResult(intentOut, REQUEST_CODE);
    }

    //Activityから戻ってきたときの処理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //colorCreateActivityから戻ってきた場合
            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    //DB問い合わせて更新
                    loadColorList();
                }
                else if (resultCode == RESULT_CANCELED) {
                    //キャンセルボタンを押して戻ってきたときの処理
                }
                else {
                    //その他
                }
                break;
            default:
                break;
        }
    }
    
    private void loadColorList(){
        repository.getListScheduleGroup(new ScheduleDataSource.GetScheduleGroupsCallback() {
            @Override
            public void onScheduleGroupsLoaded(List<ScheduleGroup> Groups) {
                //取得後の処理
                mListAdapter.setList(Groups);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
