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

import com.appframe.library.component.image.ImageLoader;
import com.spirited.carpool.R;
import com.spirited.carpool.waitinghall.TrainDetailActivity;
import com.spirited.carpool.api.waitinghall.TrainEntity;
import com.spirited.support.component.LoadMoreAdapter;

import java.util.List;


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
            TrainEntity entity = dataList.get(position);
            ImageLoader.normal(context, entity.carInfo.avatar, R.drawable.default_image_white, holder.ivAvatar);
            holder.tvPrice.setText(String.valueOf(entity.train.price));
            holder.tvStart.setText(entity.train.startPoint);
            holder.tvEnd.setText(entity.train.endPoint);
            holder.tvStartTime.setText(entity.train.startTime);
            holder.tvEndTime.setText(entity.train.endTime);
            holder.tvNumber.setText(entity.train.orderedNumber + "/" + entity.carInfo.approvedLoadNumber);
            if (entity.train.orderedNumber < entity.carInfo.approvedLoadNumber) {
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
