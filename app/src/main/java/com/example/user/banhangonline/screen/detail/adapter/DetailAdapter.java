package com.example.user.banhangonline.screen.detail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.user.banhangonline.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

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

        @BindView(R.id.pg_load)
        ProgressBar progressBar;

        @BindView(R.id.tv_total_anh)
        TextView tvAnh;

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
            final DetailViewholder holder = (DetailViewholder) viewHolder;
            Glide.with(context).load(mList.get(position)).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(holder.imgDetail);
            holder.tvAnh.setText("Ảnh " + (position + 1));
            PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(holder.imgDetail);
            photoViewAttacher.update();
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
}
