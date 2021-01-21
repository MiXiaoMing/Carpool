package com.spirited.carpool.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.spirited.carpool.R;
import com.spirited.carpool.api.waitinghall.CarInfo;
import com.spirited.carpool.mine.CarSettingActivity;
import com.spirited.support.component.LoadMoreAdapter;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 车辆 item
 */
public class CarAdapter extends LoadMoreAdapter<CarInfo> {
    private Context context;

    public CarAdapter(Context context, List<CarInfo> dataList) {
        super(dataList);
        this.context = context;
    }

    @Override
    public ViewHolder handleCreateViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void handleBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            CarInfo carInfo = dataList.get(position);

            ImageLoader.normal(context, carInfo.pictures.get(0), R.drawable.default_image_white, holder.ivCar);
            holder.tvCarNumber.setText(carInfo.carNumber);
            holder.tvTelephone.setText(carInfo.telephone);
            holder.tvNumber.setText("已安全运营" + carInfo.totalOrderedCount + "车次");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCar;
        TextView tvCarNumber, tvTelephone, tvNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ivCar = itemView.findViewById(R.id.ivCar);
            tvCarNumber = itemView.findViewById(R.id.tvCarNumber);
            tvTelephone = itemView.findViewById(R.id.tvTelephone);
            tvNumber = itemView.findViewById(R.id.tvNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = ViewHolder.this.getAdapterPosition();
                    if (position >= 0) {
                        Intent intent = new Intent(context, CarSettingActivity.class);
                        intent.putExtra("type", "U");
                        intent.putExtra("car", dataList.get(position));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
