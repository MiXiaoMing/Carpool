package com.spirited.carpool.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appframe.library.component.image.ImageLoader;
import com.spirited.carpool.NavigationActivity;
import com.spirited.carpool.R;
import com.spirited.carpool.api.route.UserInfo;

import java.util.ArrayList;


public class RouteUserAdapter extends BaseAdapter {
    private NavigationActivity activity;
    private ArrayList<UserInfo> entities = new ArrayList<>();

    public RouteUserAdapter(NavigationActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_route_user, null);
            viewHolder.ivIcon = convertView.findViewById(R.id.ivIcon);
            viewHolder.tvNick = convertView.findViewById(R.id.tvNick);
            viewHolder.tvEnd = convertView.findViewById(R.id.tvEnd);
            viewHolder.llyTelephone = convertView.findViewById(R.id.llyTelephone);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final UserInfo userInfo = entities.get(position);

        ImageLoader.normal(activity, userInfo.avatar, R.drawable.default_image_white, viewHolder.ivIcon);
        viewHolder.tvNick.setText(userInfo.name);
        viewHolder.tvEnd.setText("下车点：" + userInfo.endDesc);
        viewHolder.llyTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + userInfo.telephone);
                intent.setData(data);
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addAll(ArrayList<UserInfo> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvNick, tvEnd;
        LinearLayout llyTelephone;
    }
}
