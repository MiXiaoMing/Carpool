package com.spirited.carpool.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spirited.carpool.R;
import com.spirited.support.BaseFragment;
import com.spirited.support.constants.Constants;
import com.spirited.support.utils.AuthUtil;
import com.spirited.support.utils.UserInfoUtil;
import com.umeng.analytics.MobclickAgent;

public class MineFragment extends BaseFragment {
    private View view;
    private LinearLayout llyUser, llySetting;
    private TextView tvName, tvCellphone, tvNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, null);
            init(view);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        return view;
    }

    private void init(View view) {
        llyUser = view.findViewById(R.id.llyUser);

        // TODO: 2019/4/3 用户头像现在是摆设
        ImageView ivAvatar = view.findViewById(R.id.ivAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvCellphone = view.findViewById(R.id.tvCellphone);

        llySetting = view.findViewById(R.id.llySetting);
        RelativeLayout rlySetting = view.findViewById(R.id.rlySetting);
        RelativeLayout rlyLogout = view.findViewById(R.id.rlyLogout);

        LinearLayout llyCar = view.findViewById(R.id.llyCar);
        tvNumber = view.findViewById(R.id.tvNumber);
        LinearLayout llyFeedback = view.findViewById(R.id.llyFeedback);
        LinearLayout llyServer = view.findViewById(R.id.llyServer);

        llyUser.setOnClickListener(clickListener);
        rlySetting.setOnClickListener(clickListener);
        rlyLogout.setOnClickListener(clickListener);
        llyCar.setOnClickListener(clickListener);
        llyFeedback.setOnClickListener(clickListener);
        llyServer.setOnClickListener(clickListener);
    }

    private void initData() {
        if (UserInfoUtil.isLogin()) {
            llyUser.setEnabled(false);
            tvName.setText(UserInfoUtil.getUserName());
            tvCellphone.setVisibility(View.VISIBLE);
            tvCellphone.setText(UserInfoUtil.getCellphone());
            llySetting.setVisibility(View.VISIBLE);
            tvNumber.setText(UserInfoUtil.getCarTotalCount() + "");
        } else {
            llyUser.setEnabled(true);
            tvName.setText("点击登录");
            tvCellphone.setVisibility(View.GONE);
            llySetting.setVisibility(View.GONE);
            tvNumber.setText("--");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llyUser:
                    startActivity(new Intent(MineFragment.this.getActivity(), LoginActivity.class));
                    break;

                case R.id.rlySetting:
//                    startActivity(new Intent(MineFragment.this.getActivity(), UserInfoActivity.class));
                    break;

                case R.id.rlyLogout:
                    AuthUtil.saveAuth("");
                    UserInfoUtil.clear();
                    MobclickAgent.onProfileSignOff();
                    initData();
                    break;

                case R.id.llyCar:
//                    startActivity(new Intent(MineFragment.this.getActivity(), AddressListActivity.class));
                    break;

                case R.id.llyFeedback:
                    startActivity(new Intent(MineFragment.this.getActivity(), FeedbackActivity.class));
                    break;

                case R.id.llyServer:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + Constants.phone_server_number);
                    intent.setData(data);
                    startActivity(intent);
                    break;
            }
        }
    };
}
