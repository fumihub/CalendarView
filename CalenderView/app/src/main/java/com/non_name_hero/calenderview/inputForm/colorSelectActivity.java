package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;

import java.util.ArrayList;
import java.util.List;

public class colorSelectActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private LinearLayout linearLayout;

    private List<Button> categoryList = new ArrayList<Button>();
    private Button colorCreateButton;

    private int colorNumber;

    private Intent intentOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.color_select);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorSelectToolbar);
        setSupportActionBar(myToolbar);

        //色作成画面用intent
        intentOut = new Intent(this, colorCreateActivity.class);

        linearLayout = findViewById(R.id.linearLayout);

        categoryList.add((Button) (findViewById(R.id.noCategoryButton)));
        colorCreateButton = findViewById(R.id.colorCreateButton);

        /*categoryListのボタンが押されたとき******************/
        for (Button b :categoryList) {
            b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //input画面に遷移
                        returnInputActivity(v);
                    }
                });
        }
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

    public void goColorCreateActivity() {
        startActivity(intentOut);
        //戻り値を設定して色画面に遷移
        startActivityForResult(intentOut, REQUEST_CODE);
    }

    public void returnInputActivity(View v) {

        ColorDrawable backgroundColor = (ColorDrawable) ((Button) v).getBackground();
        int colorId = backgroundColor.getColor();
        //色選択遷移用intent
        intentOut = getIntent();
        //ボタンの色IDを遷移先へreturn
        intentOut.putExtra("ColorId", colorId);
        //色タイトルを遷移先へreturn
        intentOut.putExtra("ColorTitle", ((Button) v).getText().toString());
        //文字色を遷移先へreturn
        intentOut.putExtra("textColor", ((Button) v).getCurrentTextColor());
        //ボタンの色番号を遷移先へreturn
        intentOut.putExtra("ColorNumber", colorNumber);
        setResult(RESULT_OK, intentOut);
        finish();
    }

    //Activityから戻り値(色、色タイトル、文字色、色番号)を受け取る処理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //colorCreateActivityから戻ってきた場合
            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    /*ここから*/
                    //色タイトルの受け取り
                    String colorTitle = data.getStringExtra("ColorTitle");
                    //ボタンの色の受け取り
                    int color = data.getIntExtra("Color",0);
                    //文字色の受け取り
                    String textColor = data.getStringExtra("textColor");
                    //色番号
                    colorNumber = data.getIntExtra("ColorNumber", 0);//defaultValue:ColorNumberキーに値が入っていなかった時に返す値

                    /*ここまでの値をデータベースに保存*/
                    /*ColorNumberも保存しているため、SharedPreferencesを使用する必要はない*/

                    //色ボタン作成
                    Button colorButton = new Button(this);
                    colorButton.setText(colorTitle);
                    colorButton.setBackgroundColor(color);
                    if (textColor.equals("黒")) {
                        colorButton.setTextColor(Color.BLACK);
                    }
                    else {
                        colorButton.setTextColor(Color.WHITE);
                    }

                    //リニアレイアウトに色ボタン追加
                    linearLayout.addView(colorButton);
                    //色ボタンをカテゴリーリストに追加
                    categoryList.add(colorButton);
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
