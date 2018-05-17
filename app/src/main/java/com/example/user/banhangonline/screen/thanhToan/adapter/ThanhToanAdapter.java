package com.example.user.banhangonline.screen.thanhToan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.DonHang;
import com.example.user.banhangonline.model.SanPham;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThanhToanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private SanPham sanPham;
    private List<DonHang> mList;
    private IOnClickChangeInfo mListenter;

    public ThanhToanAdapter(Context mContext, SanPham sanPham, List<DonHang> mList, IOnClickChangeInfo mListenter) {
        this.mContext = mContext;
        this.sanPham = sanPham;
        this.mList = mList;
        this.mListenter = mListenter;
    }

    public class GioHangViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_address)
        TextView tvAddress;

        @BindView(R.id.btn_change_address)
        Button btnChangeAdress;

        @BindView(R.id.tv_sdt)
        TextView tvSdt;

        @BindView(R.id.btn_change_sdt)
        Button btnChangeSdt;

        @BindView(R.id.tv_xuli_by)
        TextView tvXuLyBy;

        @BindView(R.id.tv_name_nguoi_ban)
        TextView tvNameNguoiBan;

        @BindView(R.id.tv_gia)
        TextView tvGia;

        @BindView(R.id.img_sanpham)
        ImageView imgSanPham;

        @BindView(R.id.tv_mota_sanpham)
        TextView tvMotaSp;

        @BindView(R.id.tv_header_sanpham)
        TextView tvHeaderSp;

        public GioHangViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnChangeAdress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListenter.onChangeAdress(tvAddress.getText().toString().trim());
                }
            });

            btnChangeSdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListenter.onChangePhoneNumber(tvSdt.getText().toString().trim());
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.donhang_row, parent, false);
        return new GioHangViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mList.get(position) instanceof DonHang) {
            if (sanPham != null) {
                GioHangViewHolder viewHolder = (GioHangViewHolder) holder;
                viewHolder.tvAddress.setText(PreferManager.getMyAddress(mContext));
                viewHolder.tvSdt.setText(PreferManager.getPhoneNumber(mContext));

                viewHolder.tvXuLyBy.setText(sanPham.getNameNguoiBan());

                viewHolder.tvNameNguoiBan.setText(sanPham.getNameNguoiBan());
                Glide.with(mContext).load(sanPham.getListFiles().getUrl1()).into(viewHolder.imgSanPham);
                viewHolder.tvMotaSp.setText(sanPham.getMota());
                viewHolder.tvHeaderSp.setText(sanPham.getHeader());
                viewHolder.tvGia.setText(sanPham.getGia());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface IOnClickChangeInfo {
        void onChangeAdress(String adress);

        void onChangePhoneNumber(String phone);
    }
}
