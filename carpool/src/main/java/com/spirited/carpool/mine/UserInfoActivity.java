package com.spirited.carpool.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spirited.carpool.R;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.constants.Constants;
import com.spirited.support.logger.Logger;
import com.spirited.support.utils.UserInfoUtil;

/**
 * 用户信息
 */
public class UserInfoActivity extends AutoBaseTitleActivity {
    private TextView tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        initView();
    }

    private void initView() {
        LinearLayout llyBack = findViewById(R.id.llyBack);

        LinearLayout llyName = findViewById(R.id.llyName);
        tvName = findViewById(R.id.tvName);
        TextView tvCellphone = findViewById(R.id.tvCellphone);

        tvCellphone.setText(UserInfoUtil.getCellphone());

        llyBack.setOnClickListener(clickListener);
        llyName.setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvName.setText(UserInfoUtil.getUserName());
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.llyName:
                    Intent intent = new Intent(UserInfoActivity.this, SettingNameActivity.class);
                    String content = tvName.getText().toString().trim();
                    intent.putExtra("content", content);
                    startActivityForResult(intent, Constants.req_setting_name);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.req_setting_name:
                    if (null != data) {
                        String content = data.getStringExtra("content");
                        if (!TextUtils.isEmpty(content)) {
                            UserInfoUtil.setUserName(content);
                            tvName.setText(content);

                            updateUserName();
                        }
                    }
                    break;
            }
        }
    }

    private void updateUserName() {
        Logger.getLogger().d("更新用户名称");
        String userName = tvName.getText().toString().trim();

//        UserDataManager dataManager = new UserDataManager();
//        dataManager.updateUserName(userName)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<EmptyEntity>() {
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ReportUtil.reportError(e);
//                        Logger.getLogger().e("更新用户名称：" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(EmptyEntity result) {
//                        if (!result.success) {
//                            Logger.getLogger().e("更新用户名称，msgCode：" + result.errCode + "/n" + result.message);
//                        } else {
//                            if (result.data == null) {
//                                Logger.getLogger().e("更新用户名称, result为空");
//                            }
//                        }
//                    }
//                });
    }
}
