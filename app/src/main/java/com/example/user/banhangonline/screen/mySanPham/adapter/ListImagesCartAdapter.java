package com.example.user.banhangonline.screen.mySanPham.adapter;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListImagesCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_IMAGE = 1;
    private Context context;
    private List<String> mList;
    private IOnClickImageMyAccount mListener;

    public ListImagesCartAdapter(Context context, List<String> mList, IOnClickImageMyAccount mListener) {
        this.context = context;
        this.mList = mList;
        this.mListener = mListener;
    }

    public class ImagesViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_library)
        SquareImageView imgPhoto;

        @BindView(R.id.circle)
        CircleView circleView;

        @BindView(R.id.check)
        ImageView imgCheck;

        public ImagesViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            circleView.setVisibility(View.GONE);
            imgCheck.setVisibility(View.GONE);
            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickImageMyAccount();
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_sell_row, parent, false);
        return new ImagesViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImagesViewholder viewholder = (ImagesViewholder) holder;
        Glide.with(context).load(mList.get(position))
                 .centerCrop().into(viewholder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface IOnClickImageMyAccount {
        void onClickImageMyAccount();
    }
}
