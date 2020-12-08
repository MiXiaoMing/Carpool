package com.spirited.carpool.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.spirited.carpool.R;
import com.spirited.support.AutoBaseTitleActivity;

public class LoginActivity extends AutoBaseTitleActivity {
//    private EditText etAccount, etSmsCode;
//    private TextView tvSmsCode;
//    private UserDataManager dataManager = new UserDataManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

//        LinearLayout llyBack = findViewById(R.id.llyBack);
//
//        etAccount = findViewById(R.id.etAccount);
//        etSmsCode = findViewById(R.id.etSmsCode);
//
//        etAccount.setTypeface(TypefaceHelper.get(this));
//        etSmsCode.setTypeface(TypefaceHelper.get(this));
//
//        tvSmsCode = findViewById(R.id.tvSmsCode);
//        TextView tvLogin = findViewById(R.id.tvLogin);
//
//        llyBack.setOnClickListener(clickListener);
//        tvSmsCode.setOnClickListener(clickListener);
//        tvLogin.setOnClickListener(clickListener);
    }

//    private void sendSms() {
//        Logger.getLogger().d("发送验证码");
//
//        tvSmsCode.setEnabled(false);
//
//        String cellphone = etAccount.getText().toString().trim();
//
//        if (TextUtils.isEmpty(cellphone)) {
//            ToastUtil.show(LoginActivity.this, "请输入手机号");
//            return;
//        } else if (!PhoneFormatCheckUtils.isPhoneLegal(cellphone)) {
//            ToastUtil.show(LoginActivity.this, "请输入正确的手机号");
//            return;
//        }
//
//        countDownTimer.start();
//
//        dataManager.sendSms(RequestBody.create(MediaType.parse("text/plain"), cellphone))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<StringResult>() {
//                    @Override
//                    public void onError(Throwable e) {
//                        ReportUtil.reportError(e);
//                        tvSmsCode.setEnabled(true);
//                        Logger.getLogger().e("发送验证码：" + e.getMessage());
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
//                    public void onNext(StringResult result) {
//                        if (!result.success) {
//                            Logger.getLogger().e("发送验证码，msgCode：" + result.errCode + "/n" + result.message);
//                            tvSmsCode.setEnabled(true);
//                        } else {
//                            if (result.data == null) {
//                                Logger.getLogger().e("发送验证码, result为空");
//                                tvSmsCode.setEnabled(true);
//                                return;
//                            }
//
//                            etSmsCode.setText(result.data);
//                        }
//                    }
//                });
//    }
//
//    private void login() {
//        Logger.getLogger().d("用户登录");
//        String cellphone = etAccount.getText().toString().trim();
//        String smsCode = etSmsCode.getText().toString().trim();
//
//        if (TextUtils.isEmpty(cellphone)) {
//            ToastUtil.show(LoginActivity.this, "请输入手机号");
//            return;
//        } else if (!PhoneFormatCheckUtils.isPhoneLegal(cellphone)) {
//            ToastUtil.show(LoginActivity.this, "请输入正确的手机号");
//            return;
//        } else if (TextUtils.isEmpty(smsCode)) {
//            ToastUtil.show(LoginActivity.this, "请输入验证码");
//            return;
//        }
//
//
//        // TODO: 2020/6/23 这里获取 CID
////        PushUtil.getCID()
//
//        LoginBody body = new LoginBody();
//        body.phoneNumber = cellphone;
//        body.smsCode = smsCode;
//
//        dataManager.login(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CustomObserver<StringResult>() {
//
//                    @Override
//                    public void onError(String message) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(StringResult result) {
//                        AuthUtil.saveAuth(result.data);
//                        MobclickAgent.onProfileSignIn("Login_PhoneNum", UserInfoUtil.getCellphone());
//
//                        getUser();
//                    }
//                });
//    }
//
//    private void getUser() {
//        dataManager.getUser()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CustomObserver<UserInfo>() {
//
//                    @Override
//                    public void onError(String message) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(UserInfo result) {
//                        UserInfoUtil.setUserID(result.data.id);
//                        UserInfoUtil.setUserName(result.data.name);
//                        UserInfoUtil.setUserType(result.data.type);
//                        UserInfoUtil.setCellphone(result.data.phoneNumber);
//                        finish();
//                    }
//                });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (TextUtils.isEmpty(UserInfoUtil.getUserID())) {
//            AuthUtil.saveAuth("");
//        }
//    }
//
//    private View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.llyBack:
//                    finish();
//                    break;
//
//                case R.id.tvSmsCode:
//                    sendSms();
//                    break;
//
//                case R.id.tvLogin:
//                    login();
//                    break;
//            }
//        }
//    };
//
//    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//            tvSmsCode.setText(millisUntilFinished / 1000 + "s");
//        }
//
//        @Override
//        public void onFinish() {
//            tvSmsCode.setEnabled(true);
//            tvSmsCode.setText("获取验证码");
//        }
//    };
}
