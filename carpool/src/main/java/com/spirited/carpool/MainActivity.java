package com.spirited.carpool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spirited.carpool.mine.MineFragment;
import com.spirited.carpool.train.TrainFragment;
import com.spirited.carpool.waitinghall.WaitingHallFragment;
import com.spirited.support.AutoBaseTitleActivity;
import com.spirited.support.component.AFToast;


public class MainActivity extends AutoBaseTitleActivity {

    private TextView tvWaitingHall, tvTrain, tvMine;
    private ImageView ivWaitingHall, ivTrain, ivMine;
    private FragmentManager fragmentManager;
    private Fragment waitingHallFragment, trainFragment, mineFragment, currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        LinearLayout llyStation = findViewById(R.id.llyWaitingHall);
        LinearLayout llyTrain = findViewById(R.id.llyTrain);
        LinearLayout llyMine = findViewById(R.id.llyMine);

        tvWaitingHall = findViewById(R.id.tvWaitingHall);
        tvTrain = findViewById(R.id.tvTrain);
        tvMine = findViewById(R.id.tvMine);

        ivWaitingHall = findViewById(R.id.ivWaitingHall);
        ivTrain = findViewById(R.id.ivTrain);
        ivMine = findViewById(R.id.ivMine);

        llyStation.setOnClickListener(clickListener);
        llyTrain.setOnClickListener(clickListener);
        llyMine.setOnClickListener(clickListener);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        waitingHallFragment = new WaitingHallFragment();
        fragmentTransaction.add(R.id.flyContainer, waitingHallFragment).commit();
        currentFragment = waitingHallFragment;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyWaitingHall:
                    if (currentFragment != waitingHallFragment) {
                        ivWaitingHall.setImageResource(R.drawable.icon_waiting_hall_click);
                        tvWaitingHall.setTextColor(getResources().getColor(R.color.blue_text));
                        ivTrain.setImageResource(R.drawable.icon_train_time_table);
                        tvTrain.setTextColor(getResources().getColor(R.color.gray_text));
                        ivMine.setImageResource(R.drawable.icon_mine);
                        tvMine.setTextColor(getResources().getColor(R.color.gray_text));

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.hide(currentFragment).show(waitingHallFragment).commit();
                        currentFragment = waitingHallFragment;
                    }
                    break;
                case R.id.llyTrain:
                    if (currentFragment != trainFragment) {
                        ivWaitingHall.setImageResource(R.drawable.icon_waiting_hall);
                        tvWaitingHall.setTextColor(getResources().getColor(R.color.gray_text));
                        ivTrain.setImageResource(R.drawable.icon_train_time_table_click);
                        tvTrain.setTextColor(getResources().getColor(R.color.blue_text));
                        ivMine.setImageResource(R.drawable.icon_mine);
                        tvMine.setTextColor(getResources().getColor(R.color.gray_text));

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (trainFragment == null) {
                            trainFragment = new TrainFragment();
                            fragmentTransaction.hide(currentFragment).add(R.id.flyContainer, trainFragment).commit();
                        } else {
                            fragmentTransaction.hide(currentFragment).show(trainFragment).commit();
                        }
                        currentFragment = trainFragment;
                    }
                    break;
                case R.id.llyMine:
                    if (currentFragment != mineFragment) {
                        ivWaitingHall.setImageResource(R.drawable.icon_waiting_hall);
                        tvWaitingHall.setTextColor(getResources().getColor(R.color.gray_text));
                        ivTrain.setImageResource(R.drawable.icon_train_time_table);
                        tvTrain.setTextColor(getResources().getColor(R.color.gray_text));
                        ivMine.setImageResource(R.drawable.icon_mine_click);
                        tvMine.setTextColor(getResources().getColor(R.color.blue_text));

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (mineFragment == null) {
                            mineFragment = new MineFragment();
                            fragmentTransaction.hide(currentFragment).add(R.id.flyContainer, mineFragment).commit();
                        } else {
                            fragmentTransaction.hide(currentFragment).show(mineFragment).commit();
                        }
                        currentFragment = mineFragment;
                    }
                    break;
            }
        }
    };


    // 退出登录
    private long clickTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - clickTime < 2000) {
                finish();
            } else {
                AFToast.showShort(MainActivity.this, "再点一次将退出程序");
                clickTime = secondTime;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
