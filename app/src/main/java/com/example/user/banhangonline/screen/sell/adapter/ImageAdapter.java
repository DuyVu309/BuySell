package com.example.user.banhangonline.screen.sell.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.views.CircleView;
import com.example.user.banhangonline.views.SquareImageView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_IMAGE_SELl = 1;
    private Context context;
    private List<File> mList;
    private OnClickItemImage mListener;

    public ImageAdapter(Context context, List<File> mList, OnClickItemImage mListener) {
        this.context = context;
        this.mList = mList;
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof File) {
            return VIEW_TYPE_IMAGE_SELl;
        }
        return 0;
    }

    public class ImageSellHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_library)
        SquareImageView imgLibrary;

        @BindView(R.id.circle)
        CircleView circleView;

        @BindView(R.id.check)
        ImageView imgRemove;

        public ImageSellHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickImageDelete(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_IMAGE_SELl) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_sell_row, parent, false);
            return new ImageSellHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (mList.get(position) instanceof File) {
            ImageSellHolder holder = (ImageSellHolder) viewHolder;
            Glide.with(context).load(mList.get(position).toString()).into(holder.imgLibrary);
            holder.circleView.setColorCircle(context.getResources().getColor(R.color.transparent));
            holder.imgRemove.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clear));
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : null;
    }

    public interface OnClickItemImage {
        void onClickImageDelete(int position);
    }
}
