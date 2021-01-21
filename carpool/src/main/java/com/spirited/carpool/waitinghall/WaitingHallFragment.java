package com.spirited.carpool.waitinghall;

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
import com.spirited.carpool.api.CustomObserver;
import com.spirited.carpool.api.PageBody;
import com.spirited.carpool.api.ServerConfig;
import com.spirited.carpool.api.waitinghall.CarInfo;
import com.spirited.carpool.api.waitinghall.Carousel;
import com.spirited.carpool.api.waitinghall.CarouselListEntity;
import com.spirited.carpool.api.waitinghall.Train;
import com.spirited.carpool.api.waitinghall.TrainEntity;
import com.spirited.carpool.api.waitinghall.TrainListEntity;
import com.spirited.carpool.api.waitinghall.WaitingHallManager;
import com.spirited.carpool.waitinghall.adapter.TrainAdapter;
import com.spirited.support.BaseFragment;
import com.spirited.support.component.RecyclerViewItemDecoration;
import com.spirited.support.component.RecyclerViewScrollListener;
import com.spirited.support.constants.Constants;
import com.spirited.support.utils.GlideImageLoader;
import com.spirited.support.utils.ReportUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 候车大厅
 */
public class WaitingHallFragment extends BaseFragment {
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
            initCarousel();
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
                getTrainList();
            }
        });

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onScrollToBottom() {
                trainAdapter.setLoadState(trainAdapter.LOADING);
                ++page;
                getTrainList();
            }
        });

        getTrainList();
    }

    //轮播图
    private void initCarouselView(ArrayList<Carousel> carousels) {
        if (carousels == null || carousels.size() == 0) {
            Logger.getLogger().e("没有获取到轮播图数据");
            carousels = new ArrayList<>();
            carousels.add(new Carousel("image/carousel_default.jpg", Constants.carousel_url, "http://www.baidu.com"));
        }

        final List<String> images = new ArrayList<>();
        for (int i = 0; i < carousels.size(); i++) {
            images.add(ServerConfig.file_host + carousels.get(i).path);
        }

        Banner banner = view.findViewById(R.id.banner);
        final ArrayList<Carousel> finalCarousels = carousels;
        banner.setDelayTime(5000).setImages(images).setImageLoader(new GlideImageLoader()).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Carousel carousel = finalCarousels.get(position);
                if (carousel.type.equals(Constants.carousel_url)) {
                    // TODO: 2020/12/8 跳转网页地址
                } else {
                    // TODO: 2020/12/8 跳转原生页面
                }
            }
        }).start();
    }


    private void initCarousel() {
        new WaitingHallManager().getCarouselList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<CarouselListEntity>() {

                    @Override
                    public void onError(String message) {
                        ReportUtil.reportError(message);
                        Logger.getLogger().e("获取轮播图错误：" + message);

                        // TODO: 2020/12/8
                        initCarouselView(null);
                    }

                    @Override
                    public void onSuccess(CarouselListEntity result) {
                        if (!result.success) {
                            Logger.getLogger().e("获取轮播图错误，msgCode：" + result.errCode + "/n" + result.message);
                        } else {
                            if (result.data == null) {
                                Logger.getLogger().e("获取轮播图错误, result为空");
                                return;
                            }

                            initCarouselView(result.data);
                        }
                    }
                });
    }

    private void getTrainList() {
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
                            train.startTime = "20:00";
                            train.occupiedTime = i * 10;

                            CarInfo carInfo = new CarInfo();
                            carInfo.totalOrderedCount = i * 100;
                            carInfo.approvedLoadNumber = i * 30;
                            carInfo.cover = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2241951752,3741800679&fm=26&gp=0.jpg";
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
