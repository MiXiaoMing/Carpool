package com.spirited.carpool.waitinghall;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.appframe.utils.logger.Logger;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.spirited.carpool.R;
import com.spirited.carpool.api.CustomObserver;
import com.spirited.carpool.api.train.Route;
import com.spirited.carpool.api.waitinghall.CarInfo;
import com.spirited.carpool.api.waitinghall.CarStatisticsDataEntity;
import com.spirited.carpool.api.waitinghall.Train;
import com.spirited.carpool.api.waitinghall.TrainEntity;
import com.spirited.carpool.api.waitinghall.TrainInfoBody;
import com.spirited.carpool.api.waitinghall.TrainStatistics;
import com.spirited.carpool.api.waitinghall.WaitingHallManager;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.constants.RouteConstants;
import com.spirited.support.utils.TimeUtils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TrainDetailActivity extends AutoBaseTitleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_train_detail);

        initView();
        getData();
        getStatisticsData();
    }

    private void initView() {
        LinearLayout llyBack = findViewById(R.id.llyBack);

        llyBack.setOnClickListener(clickListener);
    }

    private void updateView(TrainEntity entity) {
        TextView tvContact = findViewById(R.id.tvContact);
        TextView tvCarNumber = findViewById(R.id.tvCarNumber);
        TextView tvApprovedLoadNumber = findViewById(R.id.tvApprovedLoadNumber);
        ImageView ivAvatar = findViewById(R.id.ivAvatar);
        TextView tvTotalOrderedCount = findViewById(R.id.tvTotalOrderedCount);
        TextView tvOrderedNumber = findViewById(R.id.tvOrderedNumber);
        TextView tvStart = findViewById(R.id.tvStart);
        TextView tvEnd = findViewById(R.id.tvEnd);
        TextView tvStartTime = findViewById(R.id.tvStartTime);
        TextView tvEndTime = findViewById(R.id.tvEndTime);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvOrder = findViewById(R.id.tvOrder);

        tvContact.setText(entity.carInfo.contact);
        tvCarNumber.setText(entity.carInfo.carNumber);
        tvApprovedLoadNumber.setText("核载：" + entity.carInfo.approvedLoadNumber + "人");
        ImageLoader.normal(this, entity.carInfo.cover, R.drawable.default_image_white, ivAvatar);
        tvTotalOrderedCount.setText("已安全营运 " + entity.carInfo.totalOrderedCount + " 车次");
        tvOrderedNumber.setText(entity.train.orderedNumber + "/" + entity.carInfo.approvedLoadNumber);
        for (int i = 0; i < entity.routeEntities.size(); ++i) {
            Route route = entity.routeEntities.get(i).route;
            if (route.type.equals(RouteConstants.route_type_start)) {
                tvStart.setText(route.description);
            } else if (route.type.equals(RouteConstants.route_type_end)) {
                tvEnd.setText(route.description);
            }
        }
        tvStartTime.setText(entity.train.startTime);
        tvEndTime.setText(TimeUtils.addTime(entity.train.startTime, entity.train.occupiedTime));
        tvPrice.setText("票价：" + entity.train.price + "元");
        tvOrder.setVisibility(View.VISIBLE);
        tvOrder.setOnClickListener(clickListener);
    }

    private void updateChart(final ArrayList<TrainStatistics> dataList) {
        LineChart lineChart = findViewById(R.id.lineChart);

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); ++i) {
            Entry entry = new Entry(i, dataList.get(i).count);
            entries.add(entry);
        }

        LineDataSet one = new LineDataSet(entries, "车次统计");       //将数据赋值到你的线条上
        one.setCircleColor(Color.parseColor("#67BCFF"));    //设置点的颜色
        one.setColor(Color.parseColor("#67BCFF"));          //设置线的颜色
        one.setDrawCircleHole(false);                                   //设置绘制点是空心还是实心,默认是空心（true），实心为false

        LineData lineData = new LineData();     //线的总管理
        lineData.addDataSet(one);               //每加一条就add一次
        lineChart.setData(lineData);            //把线条设置给你的lineChart上
        lineChart.invalidate();                 //刷新

        XAxis xAxis = lineChart.getXAxis();                 //得到x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);      //设置x轴的位置，在整个图形的底部
        xAxis.setLabelCount(dataList.size(), true);  //设置X轴刻度 第一个参数是想要x轴有多少条刻度,第二个参数true是将刻度数设置为你的第一个参数的数量 ，false是将刻度数设置为你的第一个参数的数量+1（0.0点也要算哦）
        xAxis.setGranularity(1f);                           //设置x轴坐标间的最小间距
        xAxis.setAxisMaximum(dataList.size() - 1);          //设置x轴的最大范围
        xAxis.setAxisMinimum(0f);                           //设置x轴的最小范围
        xAxis.setGridColor(Color.TRANSPARENT);              //设置x轴刻度透明
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dataList.get((int) value).date;
            }
        });

        //Y轴不是有左右两边嘛，这就是获取左右两边的y轴
//        YAxis axisRight = lineChart.getAxisRight();
//        YAxis axisLeft = lineChart.getAxisLeft();
//        axisRight.setEnabled(false);//将右边的y轴隐藏
//        //y轴最大值最小值范围
//        axisLeft.setAxisMaximum(550f);
//        axisLeft.setAxisMinimum(0f);
//        //文字颜色
//        axisLeft.setTextColor(Color.parseColor("#F44336"));//设置左y轴字的颜色
//        axisLeft.setAxisLineColor(Color.YELLOW);//y轴颜色
//        axisLeft.setGridColor(Color.parseColor("#9C27B0"));//y轴线颜色
//
//        axisLeft.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                if (value == 0) {
//                    return 0 + "元";
//                }
//                return super.getFormattedValue(value);
//            }
//        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.llyBack:
                    finish();
                    break;

                case R.id.tvOrder:

                    break;
            }
        }
    };


    /**
     * 获取 车次详情
     */
    private void getData() {
        final String carID = getIntent().getStringExtra("carID");
        String trainID = getIntent().getStringExtra("trainID");
        if (TextUtils.isEmpty(carID) || TextUtils.isEmpty(trainID)) {
            Logger.getLogger().e("数据错误：" + carID + "..." + trainID);
            finish();
            return;
        }

        TrainInfoBody body = new TrainInfoBody();
        body.carID = carID;
        body.trainID = trainID;

        new WaitingHallManager().getTrainInfo(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<TrainEntity>() {

                    @Override
                    public void onError(String message) {
                        Train train = new Train();
                        train.id = "123";
                        train.price = 20;
                        train.orderedNumber = 3;
                        train.startTime = "20:00";
                        train.occupiedTime = 100;

                        CarInfo carInfo = new CarInfo();
                        carInfo.totalOrderedCount = 100;
                        carInfo.approvedLoadNumber = 30;
                        carInfo.cover = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2241951752,3741800679&fm=26&gp=0.jpg";
                        carInfo.contact = "米晓明";
                        carInfo.description = "平安回家";
                        carInfo.id = "123";
                        carInfo.telephone = "13718863263";
                        carInfo.carNumber = "冀A X753F";

                        TrainEntity trainInfoEntity = new TrainEntity();
                        trainInfoEntity.train = train;
                        trainInfoEntity.carInfo = carInfo;

                        updateView(trainInfoEntity);
                    }

                    @Override
                    public void onSuccess(TrainEntity result) {
                        updateView(result);
                    }
                });
    }

    /**
     * 获取 车次统计信息
     */
    private void getStatisticsData() {
        final String carID = getIntent().getStringExtra("carID");
        if (TextUtils.isEmpty(carID)) {
            Logger.getLogger().e("数据错误：" + carID);
            finish();
            return;
        }

        new WaitingHallManager().getCarStatistics(RequestBody.create(MediaType.parse("text/plain"), carID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<CarStatisticsDataEntity>() {

                    @Override
                    public void onError(String message) {
                        CarStatisticsDataEntity statisticsData = new CarStatisticsDataEntity();
                        statisticsData.data = new ArrayList<>();

                        for (int i = 0; i < 30; ++i) {
                            TrainStatistics trainStatistics = new TrainStatistics();
                            trainStatistics.count = i * 3;
                            if (i < 10) {
                                trainStatistics.date = "" + i;
                            } else {
                                trainStatistics.date = "" + i;
                            }
                            statisticsData.data.add(trainStatistics);
                        }

                        updateChart(statisticsData.data);
                    }

                    @Override
                    public void onSuccess(CarStatisticsDataEntity result) {
//                        updateChart(result.data);
                    }
                });
    }
}
