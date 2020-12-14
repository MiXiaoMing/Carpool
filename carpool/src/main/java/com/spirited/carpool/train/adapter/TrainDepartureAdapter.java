package com.spirited.carpool.train.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spirited.carpool.R;
import com.spirited.carpool.api.train.RouteEntity;
import com.spirited.carpool.api.waitinghall.TrainEntity;
import com.spirited.carpool.train.TrainSettingActivity;
import com.spirited.carpool.waitinghall.TrainDetailActivity;
import com.spirited.support.component.LoadMoreAdapter;
import com.spirited.support.constants.RouteConstants;

import java.util.List;

/**
 * 发车 item
 */
public class TrainDepartureAdapter extends LoadMoreAdapter<TrainEntity> {
    private Context context;

    public TrainDepartureAdapter(Context context, List<TrainEntity> dataList) {
        super(dataList);
        this.context = context;
    }

    @Override
    public ViewHolder handleCreateViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_train_departure, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void handleBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            TrainEntity trainEntity = dataList.get(position);

            for (int i = 0; i < trainEntity.routeEntities.size(); ++i) {
                RouteEntity routeEntity = trainEntity.routeEntities.get(i);
                if (routeEntity.route.type.equals(RouteConstants.route_type_start)) {
                    holder.tvStart.setText(routeEntity.route.description);
                } else if (routeEntity.route.type.equals(RouteConstants.route_type_end)) {
                    holder.tvEnd.setText(routeEntity.route.description);
                }
            }
            holder.tvPrice.setText(String.valueOf(trainEntity.train.price));
            holder.tvRemainingTime.setText("12:12");
            holder.tvNumber.setText(trainEntity.train.orderedNumber + "/" + trainEntity.carInfo.approvedLoadNumber);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStart, tvEnd;
        TextView tvPrice, tvRemainingTime, tvNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStart = itemView.findViewById(R.id.tvStart);
            tvEnd = itemView.findViewById(R.id.tvEnd);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRemainingTime = itemView.findViewById(R.id.tvRemainingTime);
            tvNumber = itemView.findViewById(R.id.tvNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = ViewHolder.this.getAdapterPosition();
                    if (position >= 0) {
                        Intent intent = new Intent(context, TrainSettingActivity.class);
                        intent.putExtra("type", "U");
                        intent.putExtra("train", dataList.get(position));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
