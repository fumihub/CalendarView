package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.data.ScheduleGroup;
import com.non_name_hero.calenderview.data.source.ScheduleDataSource;
import com.non_name_hero.calenderview.data.source.ScheduleRepository;
import com.non_name_hero.calenderview.utils.Injection;

import static java.lang.Boolean.FALSE;

public class colorCreateActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private Intent intentIn;
    private Intent intentOut;

    private SharedPreferences prefs;
    private ScheduleRepository repository;

    private EditText colorCreateTitle;

    private Button color1;
    private Button color2;
    private Button cancelButton;
    private Button doneButton;

    private RadioGroup radioGroup;
    private RadioButton blackRadioButton;
    private RadioButton whiteRadioButton;

    private String textColor;

    private int groupId;
    private int colorNumberPre = 255;
    private int colorNumber = 255;
    private int color = 0;

    //コンストラクタ
    public colorCreateActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = Injection.provideScheduleRepository(getApplicationContext());
        setContentView(R.layout.color_create);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorCreateToolbar);
        setSupportActionBar(myToolbar);

        colorCreateTitle = findViewById(R.id.colorCreateTitle);
        color1 = findViewById(R.id.colorButton1);
        color2 = findViewById(R.id.colorButton2);
        cancelButton = findViewById(R.id.cancelButton);
        doneButton = findViewById(R.id.doneButton);
        radioGroup = findViewById(R.id.RadioGroup);
        blackRadioButton = findViewById(R.id.RadioButton1);
        whiteRadioButton = findViewById(R.id.RadioButton2);

        //TODO　色ボタン情報表示
        //SharedPreferenceからeditFlagの値を取得
        prefs = getSharedPreferences("input_data", MODE_PRIVATE);
        if (prefs.getBoolean("editFlag", FALSE)) {
            //表示する色番号を取得
            intentIn = getIntent();
            colorNumberPre = intentIn.getIntExtra("ColorNumberPre", 43);
            colorNumber = colorNumberPre;
            //DBからcolorNumberをキーにその要素を取得
            repository.getScheduleGroup(
                    colorNumberPre,
                    new ScheduleDataSource.GetScheduleGroupCallback() {

                        @Override
                        public void onScheduleGroupLoaded(ScheduleGroup group) {
                            //グループIDの取得
                            groupId = group.getGroupId();
                            //色タイトルに色名をセット
                            colorCreateTitle.setText(group.getGroupName());
                            //色ボタン2に色をセット
                            color = group.getBackgroundColor();
                            color2.setBackgroundColor(color);
                            //色ボタン2にに文字色をセット
                            if (group.getCharacterColor().equals("黒")) {//黒ならば
                                blackRadioButton.setChecked(true);
                            }
                            else {//白ならば
                                whiteRadioButton.setChecked(true);
                            }
                        }
                    }
            );
        }

        /*色ボタンが押されたとき**************************/
        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色画面に遷移
                goColorActivity();
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色画面に遷移
                goColorActivity();
            }
        });
        /************************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色選択画面に遷移
                finish();
            }
        });
        /************************************************/

        /*保存ボタンが押されたとき************************/
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色選択画面に遷移
                returnColorSelectActivity();
            }
        });
        /************************************************/

    }

    public void goColorActivity(){
        //色画面遷移用intent
        intentOut = new Intent(this, colorActivity.class);
        //色番号前回値を引数で色画面に渡す
        intentOut.putExtra("colorNumberPre", colorNumberPre);
        //戻り値を設定して色画面に遷移
        startActivityForResult(intentOut, REQUEST_CODE);
    }

    public void returnColorSelectActivity(){

        int checkedId = radioGroup.getCheckedRadioButtonId();
        if (checkedId != -1) {
            // 選択されているラジオボタンの取得
            // (Fragmentの場合は「getActivity().findViewById」にする)
            RadioButton radioButton = (RadioButton) findViewById(checkedId);

            // ラジオボタンのテキスト(色)を取得
            textColor = radioButton.getText().toString();
        }

        //エラー処理
        if (color == 0 || textColor.isEmpty() || colorCreateTitle.getText().toString().isEmpty()) {
            //トースト表示
            Toast errorToast = Toast.makeText(
                    getApplicationContext(),
                    "全ての項目を埋めてください！",
                    Toast.LENGTH_SHORT
            );
            errorToast.show();
        }
        else {
            //編集画面の場合
            if (prefs.getBoolean("editFlag", FALSE)) {
                repository.updateScheduleGroup(
                        new ScheduleGroup(
                                groupId,
                                colorNumber,
                                colorCreateTitle.getText().toString(),
                                textColor,
                                color
                        ),
                        new ScheduleDataSource.SaveScheduleGroupCallback() {
                            @Override
                            public void onScheduleGroupSaved() {
                                setResult(RESULT_OK, intentOut);
                                finish();
                            }

                            @Override
                            public void onDataNotSaved() {

                            }
                        });
            }
            //新規作成の場合
            else {
                repository.insertScheduleGroup(
                        new ScheduleGroup(
                                colorNumber,
                                colorCreateTitle.getText().toString(),
                                textColor,
                                color
                        ),
                        new ScheduleDataSource.SaveScheduleGroupCallback() {
                            @Override
                            public void onScheduleGroupSaved() {
                                setResult(RESULT_OK, intentOut);
                                finish();
                            }

                            @Override
                            public void onDataNotSaved() {

                            }
                        }
                );
            }

        }

    }

    //Activityから戻り値(色番号、文字色、背景色)を受け取る処理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //colorActivityから戻ってきた場合
            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    //色番号
                    colorNumber = data.getIntExtra("ColorNumber", 0);//defaultValue:ColorNumberキーに値が入っていなかった時に返す値
                    //色ボタンのテキスト色受け取り
                    color = data.getIntExtra("Color",0);

                    //ボタンの背景色を色ボタンの色に変更
                    color2.setBackgroundColor(color);

                    //colorNumberを前回値として保持
                    colorNumberPre = colorNumber;
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
}
