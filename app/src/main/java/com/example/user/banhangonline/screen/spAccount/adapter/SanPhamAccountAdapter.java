package com.example.user.banhangonline.screen.spAccount.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.model.SanPham;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SanPhamAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SanPham> mList;
    private IOnClickSanPham mListener;

    public SanPhamAccountAdapter(Context context, List<SanPham> mList, IOnClickSanPham mListener) {
        this.context = context;
        this.mList = mList;
        this.mListener = mListener;

    }

    public class SanPhamAcViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_sanpham)
        ImageView imgSanPham;

        @BindView(R.id.tv_mota)
        TextView tvMota;

        @BindView(R.id.tv_header)
        TextView tvHeader;

        @BindView(R.id.tv_gia)
        TextView tvGia;

        public SanPhamAcViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickSanPham(mList.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sanpham_account_row, parent, false);
        return new SanPhamAcViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mList.get(position) instanceof SanPham) {
            SanPham sanPham = mList.get(position);
            SanPhamAcViewHolder viewHolder = (SanPhamAcViewHolder) holder;
            viewHolder.tvMota.setText(sanPham.getMota());
            viewHolder.tvHeader.setText(sanPham.getHeader());
            viewHolder.tvGia.setText(sanPham.getGia());
            Glide.with(context).load(sanPham.getListFiles().getUrl1()).into(viewHolder.imgSanPham);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface IOnClickSanPham {
        void onClickSanPham(SanPham sanPham);
    }
}
