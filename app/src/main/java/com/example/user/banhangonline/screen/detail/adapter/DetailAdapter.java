package com.example.user.banhangonline.screen.detail.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> mList;

    public DetailAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
    }

    public class DetailViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_detail)
        ImageView imgDetail;

        public DetailViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_detail_row, parent, false);
        return new DetailViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (mList.get(position) instanceof String) {
            DetailViewholder holder = (DetailViewholder) viewHolder;
            Log.d("TAG UR", mList.get(position));
            Glide.with(context).load(mList.get(position)).into(holder.imgDetail);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
}
