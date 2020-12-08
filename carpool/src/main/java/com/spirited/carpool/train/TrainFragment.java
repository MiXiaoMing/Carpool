package com.spirited.carpool.train;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appframe.utils.logger.Logger;
import com.spirited.carpool.R;
import com.spirited.carpool.waitinghall.adapter.TrainAdapter;
import com.spirited.carpool.api.CustomObserver;
import com.spirited.carpool.api.PageBody;
import com.spirited.carpool.api.waitinghall.CarInfo;
import com.spirited.carpool.api.waitinghall.Train;
import com.spirited.carpool.api.waitinghall.TrainEntity;
import com.spirited.carpool.api.waitinghall.TrainListEntity;
import com.spirited.carpool.api.waitinghall.WaitingHallManager;
import com.spirited.support.BaseFragment;
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
public class TrainFragment extends BaseFragment {
    private View view;

    private TrainAdapter trainAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<TrainEntity> dataList = new ArrayList<>();
    private int page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_waiting_hall, null);

            initView();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    private void initView() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        RecyclerViewItemDecoration itemDecoration = new RecyclerViewItemDecoration(this.getContext(), AutoUtils.getPercentWidthSize(15));
        recyclerView.addItemDecoration(itemDecoration);
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        trainAdapter = new TrainAdapter(this.getContext(), dataList);
        recyclerView.setAdapter(trainAdapter);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
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

                        TrainListEntity trainList = new TrainListEntity();
                        trainList.data = new ArrayList<>();

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
                            carInfo.totalOrderedCount = i * 100;
                            carInfo.approvedLoadNumber = i * 30;
                            carInfo.avatar = "http://192.168.1.47/jbh/image/icon_clean_daily.jpg";
                            carInfo.contact = "米晓明";
                            carInfo.description = "平安回家";
                            carInfo.id = i * i + "";
                            carInfo.telephone = "13718863263";
                            carInfo.carNumber = "冀A X753F";

                            TrainEntity entity = new TrainEntity();
                            entity.carInfo = carInfo;
                            entity.train = train;

                            trainList.data.add(entity);
                        }

                        dataList.addAll(trainList.data);
                        trainAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onSuccess(TrainListEntity result) {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        trainAdapter.setLoadState(trainAdapter.LOADING_COMPLETE);

                        List<TrainEntity> trainList = result.data;
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
