package com.spirited.carpool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.appframe.library.component.notify.AFToast;
import com.appframe.utils.logger.Logger;
import com.spirited.support.AutoBaseTitleActivity;

public class TrainDetailActivity extends AutoBaseTitleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_train_detail);

        getData();
    }


    /**
     * 获取 车次详情
     */
    private void getData() {
        String carID = getIntent().getStringExtra("carID");
        String trainID = getIntent().getStringExtra("trainID");
        if (TextUtils.isEmpty(carID) || TextUtils.isEmpty(trainID)) {
            Logger.getLogger().e("数据错误：" + carID + "..." + trainID);
            finish();
            return;
        }


    }
}
