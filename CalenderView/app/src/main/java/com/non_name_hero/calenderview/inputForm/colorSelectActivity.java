package com.non_name_hero.calenderview.inputForm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.data.ScheduleGroup;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;
import com.non_name_hero.calenderview.utils.Injection;
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDeleteDialog;
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class colorSelectActivity extends AppCompatActivity implements PigLeadDeleteDialog {

    private static final int REQUEST_CODE = 1;
    private static final String DELETE_DIALOG_TAG = "DELETE_DIALOG";

    private Context context;

    private listAdapter listAdapter;
    private ListView listView;

    private Button editButton;
    private Button colorCreateButton;

    private Intent intentOut;
    private ScheduleRepository repository;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private boolean createFlag = FALSE;

    //コンストラクタ
    public colorSelectActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        repository = Injection.provideScheduleRepository(context);

        setContentView(R.layout.color_select);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorSelectToolbar);
        editButton = findViewById(R.id.editButton);
        setSupportActionBar(myToolbar);

        //ListViewのIDを取得
        listView = findViewById(R.id.listView);

        //リストのアダプターを使用してViewを作成
        listAdapter = new listAdapter(context, this);
        //削除ダイアログを
        listAdapter.deleteDialog = this;
        listView.setAdapter(listAdapter);

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

        //editFlag判定用Flag
        createFlag = TRUE;

    }

    @Override
    public void onResume() {
        super.onResume();

        //createFlagがTRUEならば
        if (createFlag) {
            //アプリ再開時にeditFlagを0にする
            //TODO　リストビューから削除ボタンを非表示に
            jdgEditMode(FALSE, "編集");
        }
        //それ以外
        else {
            /* 何もしない */
        }

    }

    private void jdgEditMode(boolean value, String str) {
        prefs = getSharedPreferences("input_data", MODE_PRIVATE);
        editor = prefs.edit();
        //SharedPreferenceにeditFlagの値を保存
        editor.putBoolean("editFlag", value);
        //非同期処理ならapply()、同期処理ならcommit()
        editor.commit();
        listView.setAdapter(listAdapter);
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
                } else if (resultCode == RESULT_CANCELED) {
                    //キャンセルボタンを押して戻ってきたときの処理
                } else {
                    //その他
                }
                break;
            default:
                break;
        }
    }

    private void loadColorList() {
        repository.getListScheduleGroup(new ScheduleDataSource.GetScheduleGroupsCallback() {
            @Override
            public void onScheduleGroupsLoaded(List<ScheduleGroup> Groups) {
                //取得後の処理
                listAdapter.setList(Groups);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    /**
     * 削除用ダイアログを設定
     *
     * @param callback ダイアログのボタン押下時の処理
     * @return dialog DialogFragmentのオブジェクト
     */
    @Override
    public PigLeadDialogFragment getDeleteDialog(final ScheduleGroup scheduleGroup, @NonNull final DialogCallback callback) {
        // 表示させるメッセージの定義
        final ArrayList<String> dialogMessages = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.delete_schedule_group_dialog_massage)));
        // 表示メッセージに削除対象の名前を挿入
        dialogMessages.set(0, String.format(dialogMessages.get(0), scheduleGroup.getGroupName()));
        final String positiveBtnMessage = getString(R.string.delete_schedule_group_positive);
        final String negativeBtnMessage = getString(R.string.delete_schedule_group_negative);
        // AlertDialogを設定
        PigLeadDialogFragment dialog = new PigLeadDialogFragment(context);
        dialog.setDialogMessage(dialogMessages)
                .setPositiveBtnMessage(positiveBtnMessage)
                .setNegativeBtnMessage(negativeBtnMessage)
                .setPositiveClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       callback.onClickPositiveBtn();
                    }
                })
                .setNegativeClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onClickNegativeBtn();
                    }
                });
        return dialog;
    }

    @Override
    public void showPigLeadDiaLog(PigLeadDialogFragment dialog) {
        // AlertDialogを表示
        dialog.show(getSupportFragmentManager(), DELETE_DIALOG_TAG);
    }
}
