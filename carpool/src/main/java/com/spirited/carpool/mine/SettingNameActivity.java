package com.spirited.carpool.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spirited.carpool.R;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.component.TypefaceHelper;


/**
 * 设置用户名
 */
public class SettingNameActivity extends AutoBaseTitleActivity {

    private EditText etContent;
    private boolean exitType = false; //false:不更新  true:更新

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_name);

        LinearLayout llyBack = findViewById(R.id.llyBack);
        etContent = findViewById(R.id.etContent);
        etContent.setTypeface(TypefaceHelper.get(this));
        TextView tvOk = findViewById(R.id.tvOk);

        String content = getIntent().getStringExtra("content");
        if (!TextUtils.isEmpty(content)) {
            etContent.setText(content);
            etContent.setSelection(content.length());
        }

        llyBack.setOnClickListener(clickListener);
        tvOk.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvOk:
                    exitType = true;
                    finish();
                    break;
            }
        }
    };

    @Override
    public void finish() {
        if (exitType) {
            Intent intent = new Intent();
            intent.putExtra("content", etContent.getText().toString().trim());
            setResult(Activity.RESULT_OK, intent);
        }

        super.finish();
    }
}
