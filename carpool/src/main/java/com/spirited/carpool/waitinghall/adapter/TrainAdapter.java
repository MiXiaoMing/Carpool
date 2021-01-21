package com.spirited.carpool.waitinghall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.spirited.carpool.R;
import com.spirited.carpool.api.train.RouteEntity;
import com.spirited.carpool.waitinghall.TrainDetailActivity;
import com.spirited.carpool.api.waitinghall.TrainEntity;
import com.spirited.support.component.LoadMoreAdapter;
import com.spirited.support.constants.RouteConstants;
import com.spirited.support.utils.ImageUtil;
import com.spirited.support.utils.TimeUtils;

import java.util.List;

/**
 * 车次 item
 */
public class TrainAdapter extends LoadMoreAdapter<TrainEntity> {
    private Context context;

    public TrainAdapter(Context context, List<TrainEntity> dataList) {
        super(dataList);
        this.context = context;
    }

    @Override
    public ViewHolder handleCreateViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_train, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void handleBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            TrainEntity trainEntity = dataList.get(position);
            ImageUtil.loadImageCenterInside(context, trainEntity.carInfo.cover, R.drawable.default_image_white, holder.ivAvatar);
            holder.tvPrice.setText(String.valueOf(trainEntity.train.price));

            for (int i = 0; i < trainEntity.routeEntities.size(); ++i) {
                RouteEntity routeEntity = trainEntity.routeEntities.get(i);
                if (routeEntity.route.type.equals(RouteConstants.route_type_start)) {
                    holder.tvStart.setText(routeEntity.route.description);
                } else if (routeEntity.route.type.equals(RouteConstants.route_type_end)) {
                    holder.tvEnd.setText(routeEntity.route.description);
                }
            }

            holder.tvStartTime.setText(trainEntity.train.startTime);
            holder.tvEndTime.setText(TimeUtils.addTime(trainEntity.train.startTime, trainEntity.train.occupiedTime));
            holder.tvNumber.setText(trainEntity.train.orderedNumber + "/" + trainEntity.carInfo.approvedLoadNumber);
            if (trainEntity.train.orderedNumber < trainEntity.carInfo.approvedLoadNumber) {
                holder.tvOrder.setText("预约");
                holder.tvOrder.setEnabled(true);
            } else {
                holder.tvOrder.setText("已满员");
                holder.tvOrder.setEnabled(false);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvPrice, tvStart, tvEnd, tvStartTime, tvEndTime;
        TextView tvNumber, tvOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStart = itemView.findViewById(R.id.tvStart);
            tvEnd = itemView.findViewById(R.id.tvEnd);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvOrder = itemView.findViewById(R.id.tvOrder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = ViewHolder.this.getAdapterPosition();
                    if (position >= 0) {
                        Intent intent = new Intent(context, TrainDetailActivity.class);
                        intent.putExtra("carID", dataList.get(position).carInfo.id);
                        intent.putExtra("trainID", dataList.get(position).train.id);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
