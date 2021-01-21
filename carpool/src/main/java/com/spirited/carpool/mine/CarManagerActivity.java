package com.spirited.carpool.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.utils.logger.Logger;
import com.spirited.carpool.R;
import com.spirited.carpool.api.CustomObserver;
import com.spirited.carpool.api.PageBody;
import com.spirited.carpool.api.mine.CarListEntity;
import com.spirited.carpool.api.mine.MineManager;
import com.spirited.carpool.api.waitinghall.CarInfo;
import com.spirited.carpool.mine.adapter.CarAdapter;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.component.RecyclerViewItemDecoration;
import com.spirited.support.component.RecyclerViewScrollListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 车辆管理页面
 */
public class CarManagerActivity extends AutoBaseTitleActivity {

    private CarAdapter carAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<CarInfo> dataList = new ArrayList<>();
    private int page = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);

        initView();
        getData();
    }

    private void initView() {
        LinearLayout llyBack = findViewById(R.id.llyBack);
        TextView tvAdd = findViewById(R.id.tvAdd);

        llyBack.setOnClickListener(clickListener);
        tvAdd.setOnClickListener(clickListener);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewItemDecoration itemDecoration = new RecyclerViewItemDecoration(this, AutoUtils.getPercentWidthSize(10));
        recyclerView.addItemDecoration(itemDecoration);
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        carAdapter = new CarAdapter(this, dataList);
        recyclerView.setAdapter(carAdapter);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                dataList.clear();
                page = 0;
                getData();
            }
        });

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onScrollToBottom() {
                carAdapter.setLoadState(carAdapter.LOADING);
                ++page;
                getData();
            }
        });

        getData();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvAdd:
                    Intent intent = new Intent(CarManagerActivity.this, CarSettingActivity.class);
                    intent.putExtra("type", "A");
                    startActivity(intent);
                    break;
            }
        }
    };

    private void getData() {
        Logger.getLogger().d("获取车辆列表");
        PageBody body = new PageBody();
        body.page = page;
        body.number = 10;

        new MineManager().getCarList(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<CarListEntity>() {

                    @Override
                    public void onError(String message) {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        carAdapter.setLoadState(carAdapter.LOADING_COMPLETE);

                        CarListEntity carListEntity = new CarListEntity();
                        carListEntity.data = new ArrayList<>();

                        for (int i = 0; i < 10; ++i) {
                            CarInfo carInfo = new CarInfo();
                            carInfo.totalOrderedCount = i * 100;
                            carInfo.approvedLoadNumber = i * 30;
                            carInfo.cover = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2241951752,3741800679&fm=26&gp=0.jpg";
                            carInfo.contact = "米晓明";
                            carInfo.description = "平安回家";
                            carInfo.id = i * i + "";
                            carInfo.telephone = "13718863263";
                            carInfo.carNumber = "冀A X753F";
                            carInfo.pictures.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2241951752,3741800679&fm=26&gp=0.jpg");

                            carListEntity.data.add(carInfo);
                        }

                        dataList.addAll(carListEntity.data);
                        carAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onSuccess(CarListEntity result) {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        carAdapter.setLoadState(carAdapter.LOADING_COMPLETE);

                        List<CarInfo> trainList = result.data;
                        if (trainList == null || trainList.size() == 0) {
                            carAdapter.setLoadState(carAdapter.LOADING_END);
                            return;
                        }

                        dataList.addAll(trainList);
                        carAdapter.notifyDataSetChanged();
                    }
                });
    }
}

