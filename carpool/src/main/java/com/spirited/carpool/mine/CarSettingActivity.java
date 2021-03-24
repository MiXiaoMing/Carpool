package com.spirited.carpool.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spirited.carpool.R;
import com.spirited.carpool.api.waitinghall.CarInfo;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.component.TypefaceHelper;
import com.spirited.support.logger.Logger;
import com.spirited.support.utils.ImageUtil;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 车辆 设置
 */
public class CarSettingActivity extends AutoBaseTitleActivity {
    private CarInfo carInfo;
    private String type;    //A: add,  U: update,  D: delete

    private TextView tvTitle;

    private ImageView ivCover;
    private EditText etCarNumber, etApprovedLoadNumber;

    private EditText etContact, etCellphone, etDesc;
    private TextView tvDescCount;

    private LinearLayout llyPictures;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_car_setting);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        carInfo = (CarInfo) getIntent().getSerializableExtra("car");

        if (TextUtils.isEmpty(type) || (type.equals("U") && carInfo == null)) {
            type = "A";
        }

        initView();
        if (type.equals("U")) {
            updateView();
        }
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        LinearLayout llyBack = findViewById(R.id.llyBack);
        TextView tvDelete = findViewById(R.id.tvDelete);

        ivCover = findViewById(R.id.ivCover);

        etCarNumber = findViewById(R.id.etCarNumber);
        etApprovedLoadNumber = findViewById(R.id.etApprovedLoadNumber);

        etContact = findViewById(R.id.etContact);
        etCellphone = findViewById(R.id.etCellphone);
        tvDescCount = findViewById(R.id.tvDescCount);
        etDesc = findViewById(R.id.etDesc);

        llyPictures = findViewById(R.id.llyPictures);

        etCarNumber.setTypeface(TypefaceHelper.get(this));
        etApprovedLoadNumber.setTypeface(TypefaceHelper.get(this));
        etContact.setTypeface(TypefaceHelper.get(this));
        etCellphone.setTypeface(TypefaceHelper.get(this));
        etDesc.setTypeface(TypefaceHelper.get(this));

        TextView tvSubmit = findViewById(R.id.tvSubmit);

        llyBack.setOnClickListener(clickListener);
        tvDelete.setOnClickListener(clickListener);
        tvSubmit.setOnClickListener(clickListener);

        if (type.equals("A")) {
            tvTitle.setText("添加车辆信息");
            tvDelete.setVisibility(View.GONE);
        } else if (type.equals("U")) {
            tvTitle.setText("编辑车辆信息");
            tvDelete.setVisibility(View.VISIBLE);
        }
    }

    private void updateView() {
        ImageUtil.loadImageCenterInside(this, carInfo.cover, R.drawable.default_image_white, ivCover);
        etCarNumber.setText(carInfo.carNumber);
        etApprovedLoadNumber.setText(String.valueOf(carInfo.approvedLoadNumber));
        etContact.setText(carInfo.contact);
        etCellphone.setText(carInfo.telephone);
        etDesc.setText(carInfo.description);

        LinearLayout root = null;
        for (int i = 0; i < carInfo.pictures.size(); ++i) {
            if (i % 4 == 0) {
                root = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                root.setLayoutParams(layoutParams);
                root.setOrientation(LinearLayout.HORIZONTAL);
                llyPictures.addView(root);
            }

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(65), AutoUtils.getPercentWidthSize(65));
            imageView.setLayoutParams(layoutParams);
            ImageUtil.normal(this, carInfo.pictures.get(i), R.drawable.default_image_white, imageView);
            root.addView(imageView);

            if (i % 4 != 3) {
                View view = new View(this);
                view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                root.addView(view);
            }
        }
    }

    private void updateCarInfo(String type) {
        Logger.getLogger().d("车辆信息：" + type);

        String contact = etContact.getText().toString().trim();
        String cellphone = etCellphone.getText().toString().trim();
//        String region = etRegion.getText().toString().trim();
//        String detail = etDetail.getText().toString().trim();
//
//        if (TextUtils.isEmpty(cellphone)) {
//            ToastUtil.show(AddressSettingActivity.this, "请输入手机号");
//            return;
//        } else if (!PhoneFormatCheckUtils.isPhoneLegal(cellphone)) {
//            ToastUtil.show(AddressSettingActivity.this, "请输入正确的手机号");
//            return;
//        } else if (TextUtils.isEmpty(contact)) {
//            ToastUtil.show(AddressSettingActivity.this, "请输入联系人");
//            return;
//        } else if (TextUtils.isEmpty(region)) {
//            ToastUtil.show(AddressSettingActivity.this, "请输入服务地址");
//            return;
//        }
//
//        AddressEntity address = new AddressEntity();
//        if (!type.equals("A")) {
//            address.id = this.carInfo.id;
//        }
//        address.contact = contact;
//        address.phoneNumber = cellphone;
//        address.region = region;
//        address.detail = detail;
//
//        dataManager.address(address, type)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CustomObserver<EmptyEntity>() {
//
//                    @Override
//                    public void onError(String message) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(EmptyEntity result) {
//                        finish();
//                    }
//                });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvDelete:
                    updateCarInfo("D");
                    break;

                case R.id.tvSubmit:
                    if (type.equals("A")) {
                        updateCarInfo("A");
                    } else if (type.equals("U")) {
                        updateCarInfo("U");
                    }
                    break;
            }
        }
    };
}

