package com.spirited.carpool;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.appframe.utils.logger.Logger;
import com.spirited.carpool.adapter.TrainAdapter;
import com.spirited.carpool.api.CustomObserver;
import com.spirited.carpool.api.PageBody;
import com.spirited.carpool.api.waitinghall.CarInfo;
import com.spirited.carpool.api.waitinghall.Train;
import com.spirited.carpool.api.waitinghall.TrainListEntity;
import com.spirited.carpool.api.waitinghall.WaitingHallManager;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.component.RecyclerViewItemDecoration;
import com.spirited.support.component.RecyclerViewScrollListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 候车大厅
 */
public class WaitingHallActivity extends AutoBaseTitleActivity {

    private TrainAdapter trainAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Train> dataList = new ArrayList<>();
    private int page = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waiting_hall);

        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewItemDecoration itemDecoration = new RecyclerViewItemDecoration(this, AutoUtils.getPercentWidthSize(15));
        recyclerView.addItemDecoration(itemDecoration);
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        trainAdapter = new TrainAdapter(this, dataList);
        recyclerView.setAdapter(trainAdapter);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                dataList.clear();
                page = 0;
                getData();

                // 延时1s关闭下拉刷新
//                swipeRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                }, 1000);
            }
        });

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onScrollToBottom() {
                trainAdapter.setLoadState(trainAdapter.LOADING);
                ++page;
                getData();
            }
        });

        getData();
    }

    private void getData() {
        Logger.getLogger().d("获取候车大厅 -- 车次列表");
        PageBody body = new PageBody();
        body.page = page;
        body.number = 10;

        new WaitingHallManager().getTrainList(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<TrainListEntity>() {

                    @Override
                    public void onError(String message) {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        trainAdapter.setLoadState(trainAdapter.LOADING_COMPLETE);

                        ArrayList<Train> trainList = new ArrayList<>();

                        for (int i = 0; i < 10; ++i) {
                            Train train = new Train();
                            train.id = "123";
                            train.price = i * 20;
                            train.orderedNumber = i;
                            train.startPoint = "北京沙河市";
                            train.endPoint = "保定易县";
                            train.startTime = "20:00";
                            train.endTime = "22:00";

                            CarInfo carInfo = new CarInfo();
                            train.carInfo = carInfo;
                            carInfo.totalOrderedCount = i * 100;
                            carInfo.approvedLoadNumber = i * 30;
                            carInfo.avatar = "http://192.168.1.47/jbh/image/icon_clean_daily.jpg";
                            carInfo.contact = "米晓明";
                            carInfo.description = "平安回家";
                            carInfo.id = i * i + "";
                            carInfo.telephone = "13718863263";

                            trainList.add(train);
                        }

                        dataList.addAll(trainList);
                        trainAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onSuccess(TrainListEntity result) {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        trainAdapter.setLoadState(trainAdapter.LOADING_COMPLETE);

                        List<Train> trainList = result.data.trainList;
                        if (trainList == null || trainList.size() == 0) {
                            trainAdapter.setLoadState(trainAdapter.LOADING_END);
                            return;
                        }

                        dataList.addAll(trainList);
                        trainAdapter.notifyDataSetChanged();
                    }
                });
    }
}
