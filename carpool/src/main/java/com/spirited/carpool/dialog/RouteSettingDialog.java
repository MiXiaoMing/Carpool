package com.spirited.carpool.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.spirited.carpool.R;
import com.spirited.carpool.api.route.Route;
import com.spirited.support.common.Callback;
import com.spirited.support.component.BaseDialog;
import com.spirited.support.constants.RouteConstants;
import com.spirited.support.utils.ReportUtil;

/**
 * 选择物品类型弹框
 */

public class RouteSettingDialog extends BaseDialog {

    private Route route;
    private String type; //add：添加  modify：修改

    private EditText etAddress;
    private TextView tvTime;

    private Callback<Route> callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Window window = getDialog().getWindow();
        View view = inflater.inflate(R.layout.dialog_route_setting, (ViewGroup) window.findViewById(android.R.id.content), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);

        route = (Route) getArguments().getSerializable("route");
        type = getArguments().getString("type", RouteConstants.dialog_setting_type_add);
        if (!type.equals("add") && route == null) {
            ReportUtil.reportError("路径数据错误：route == null");
            dismiss();
        }

        initView(view);

        return view;
    }

    private void initView(View view) {
        etAddress = view.findViewById(R.id.etAddress);
        tvTime = view.findViewById(R.id.tvTime);

        TextView tvCancel = view.findViewById(R.id.tvDelete);
        if (type.equals(RouteConstants.dialog_setting_type_modify)) {
            tvCancel.setText("删除");
        }

        view.findViewById(R.id.tvSearch).setOnClickListener(clickListener);
        tvCancel.setOnClickListener(clickListener);
        view.findViewById(R.id.tvSure).setOnClickListener(clickListener);
    }

    public void setCallback(Callback<Route> callback) {
        this.callback = callback;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.tvSearch) {

            } else if (id == R.id.tvTime) {

            } else if (id == R.id.tvDelete) {
                if (callback != null) {
                    callback.fail("");
                }
                dismiss();
            } else if (id == R.id.tvSure) {
                if (callback != null) {
                    callback.success(route);
                }
                dismiss();
            }
        }
    };
}
