package com.spirited.carpool.train;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baidu.mapapi.map.MapView;
import com.spirited.carpool.R;
import com.spirited.carpool.api.train.RouteEntity;
import com.spirited.carpool.api.waitinghall.TrainEntity;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.component.AFToast;
import com.spirited.support.component.TypefaceHelper;
import com.spirited.support.constants.RouteConstants;
import com.spirited.support.logger.Logger;

/**
 * 车次编辑页面
 */
public class TrainSettingActivity extends AutoBaseTitleActivity {
    private TrainEntity trainEntity;
    private String type;    //A: add,  U: update

    private TextView tvStartTime;
    private TimePicker timePicker;
    private EditText etDistance, etOccupiedHour, etOccupiedMinus;

    private EditText etStartPosition, etEndPosition;
    private MapView mapView;

    private EditText etPrice;
    private Switch switchAutoPublish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_train_setting);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        trainEntity = (TrainEntity) getIntent().getSerializableExtra("train");

        if (TextUtils.isEmpty(type) || (type.equals("U") && trainEntity == null)) {
            type = "A";
        }

        initView();

        if (type.equals("U")) {
            updateView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tvTitle);
        LinearLayout llyBack = findViewById(R.id.llyBack);
        TextView tvDelete = findViewById(R.id.tvDelete);

        tvStartTime = findViewById(R.id.tvStartTime);
        timePicker = findViewById(R.id.timePicker);
        etDistance = findViewById(R.id.etDistance);

        etOccupiedHour = findViewById(R.id.etOccupiedHour);
        etOccupiedMinus = findViewById(R.id.etOccupiedMinus);

        etStartPosition = findViewById(R.id.etStartPosition);
        LinearLayout llyStartPosition = findViewById(R.id.llyStartPosition);

        etEndPosition = findViewById(R.id.etEndPosition);
        LinearLayout llyEndPosition = findViewById(R.id.llyEndPosition);
        mapView = findViewById(R.id.mapView);
        View viewMap = findViewById(R.id.viewMap);

        etPrice = findViewById(R.id.etPrice);
        switchAutoPublish = findViewById(R.id.switchAutoPublish);

        TextView tvCancel = findViewById(R.id.tvCancel);
        TextView tvSubmit = findViewById(R.id.tvSubmit);

        etDistance.setTypeface(TypefaceHelper.get(this));
        etOccupiedHour.setTypeface(TypefaceHelper.get(this));
        etOccupiedMinus.setTypeface(TypefaceHelper.get(this));
        etStartPosition.setTypeface(TypefaceHelper.get(this));
        etEndPosition.setTypeface(TypefaceHelper.get(this));
        etPrice.setTypeface(TypefaceHelper.get(this));

        llyBack.setOnClickListener(clickListener);
        tvDelete.setOnClickListener(clickListener);
        tvStartTime.setOnClickListener(clickListener);
        llyStartPosition.setOnClickListener(clickListener);
        llyEndPosition.setOnClickListener(clickListener);
        viewMap.setOnClickListener(clickListener);
        tvCancel.setOnClickListener(clickListener);
        tvSubmit.setOnClickListener(clickListener);

        if (type.equals("A")) {
            tvTitle.setText("添加车次信息");
            tvDelete.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
        } else if (type.equals("U")) {
            tvTitle.setText("编辑车次信息");
            tvDelete.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);
        }

        timePicker.setVisibility(View.GONE);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(timeChangedListener);
    }

    private void updateView() {
        tvStartTime.setText(trainEntity.train.startTime);
        etDistance.setText(String.valueOf(trainEntity.train.distance));
        etOccupiedHour.setText(String.valueOf(trainEntity.train.occupiedTime / 60));
        etOccupiedMinus.setText(String.valueOf(trainEntity.train.occupiedTime % 60));

        for (int i = 0; i < trainEntity.routeEntities.size(); ++i) {
            RouteEntity routeEntity = trainEntity.routeEntities.get(i);
            if (routeEntity.route.type.equals(RouteConstants.route_type_start)) {
                etStartPosition.setText(routeEntity.route.description);
            } else if (routeEntity.route.type.equals(RouteConstants.route_type_end)) {
                etEndPosition.setText(routeEntity.route.description);
            }
        }

        etPrice.setText(String.valueOf(trainEntity.train.price));
        if (trainEntity.autoPublishType) {
            switchAutoPublish.setChecked(true);
        } else {
            switchAutoPublish.setChecked(false);
        }
    }

    private void updateTrain(String type) {
        Logger.getLogger().d("更新车次设置：" + type);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvStartTime:
                    if (timePicker.getVisibility() == View.VISIBLE) {
                        timePicker.setVisibility(View.GONE);
                    } else {
                        timePicker.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.llyStartPosition:
                    startActivity(new Intent(TrainSettingActivity.this, RouteSettingActivity.class));
                    break;

                case R.id.llyEndPosition:
                    startActivity(new Intent(TrainSettingActivity.this, RouteSettingActivity.class));
                    break;

                case R.id.viewMap:
                    startActivity(new Intent(TrainSettingActivity.this, RouteSettingActivity.class));
                    break;

                case R.id.tvCancel:
                    AFToast.showShort(TrainSettingActivity.this, "撤销本次车次发布");
                    break;

                case R.id.tvDelete:
                    updateTrain("D");
                    AFToast.showShort(TrainSettingActivity.this, "删除本车次信息");
                    break;

                case R.id.tvSubmit:
                    if (type.equals("A")) {
                        updateTrain("A");
                    } else if (type.equals("U")) {
                        updateTrain("U");
                    }
                    break;

            }
        }
    };

    /**
     * 时间改变 监听器
     */
    TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
            Logger.getLogger().d("时间：" + hour + "..." + minute);
            tvStartTime.setText(hour + ":" + minute);
        }
    };
}

