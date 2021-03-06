package com.example.user.banhangonline.screen.myGioHang.adapter;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.DonHang;
import com.example.user.banhangonline.model.maps.Directions;
import com.example.user.banhangonline.model.maps.Route;
import com.example.user.banhangonline.utils.GoogleMapUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyGioHangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MY_CART = 1;
    private static final int VIEW_TYPE_MY_CART_LOADING = 2;
    private Context mContext;
    private Location mLocation;
    private List<DonHang> mList;
    private IOnClickMyCart mlisenter;

    public MyGioHangAdapter(Context mContext, Location mLocation, List<DonHang> mList, IOnClickMyCart mlisenter) {
        this.mContext = mContext;
        this.mLocation = mLocation;
        this.mList = mList;
        this.mlisenter = mlisenter;
    }

    public class MyGioHangViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_position)
        TextView tvPosition;

        @BindView(R.id.img_my_cart)
        ImageView imgMyCart;

        @BindView(R.id.tv_header_mycart_sanpham)
        TextView tvHeaderMyCart;

        @BindView(R.id.tv_address_mycart)
        TextView tvAddressMyCart;

        @BindView(R.id.tv_time_mycart)
        TextView tvTimeMyCart;

        @BindView(R.id.tv_my_cart_ten_nguoi_mua)
        TextView tvTenNguoiMuaMyCart;

        @BindView(R.id.tv_my_cart_phoone)
        TextView tvPhoneMyCart;

        @BindView(R.id.tv_my_cart_distance)
        TextView tvDistance;

        @BindView(R.id.tv_my_cart_gia)
        TextView tvMyCartGia;

        @BindView(R.id.tv_my_cart_so_luong)
        TextView tvMyCartSoLuong;

        public MyGioHangViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mlisenter.onClickMyCart(mList.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mlisenter.onOptionSelectedMyCart(getAdapterPosition(), mList.get(getAdapterPosition()));
                    return false;
                }
            });

            tvTenNguoiMuaMyCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mlisenter.onClickInfoAccount(mList.get(getAdapterPosition()));
                }
            });
        }
    }

    public class MyGioHangLoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pb_loading)
        ProgressBar pbLoading;

        public MyGioHangLoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof DonHang) {
            return VIEW_TYPE_MY_CART;
        }

        if (mList.get(position) == null) {
            return VIEW_TYPE_MY_CART_LOADING;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MY_CART) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.my_cart_row, parent, false);
            return new MyGioHangViewHolder(view);
        } else if (viewType == VIEW_TYPE_MY_CART_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_row, parent, false);
            return new MyGioHangLoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mList.get(position) instanceof DonHang) {
            final MyGioHangViewHolder viewHolder = (MyGioHangViewHolder) holder;
            DonHang donHang = mList.get(position);
            viewHolder.tvPosition.setText("" + (position + 1));
            Glide.with(mContext).load(donHang.getUrlImg()).into(viewHolder.imgMyCart);
            viewHolder.tvHeaderMyCart.setText(donHang.getHeader());
            viewHolder.tvAddressMyCart.setText(donHang.getDiaChi());
            viewHolder.tvTimeMyCart.setText(donHang.getThoiGian());
            viewHolder.tvTenNguoiMuaMyCart.setText(donHang.getNameNguoiMua());
            if (donHang.getSoDienThoai() == null) {
                viewHolder.tvPhoneMyCart.setVisibility(View.GONE);
            }
            viewHolder.tvPhoneMyCart.setText(donHang.getSoDienThoai());
            viewHolder.tvMyCartGia.setText("Giá: " + donHang.getGia());
            viewHolder.tvMyCartSoLuong.setText("Số lượng: " + donHang.getSoLuong());

            if (PreferManager.getIsShowDistanceCart(mContext)) {
                if (donHang.getDiaChi() != null) {
                    GoogleMapUtils.getLatLongFromGivenAddress(mContext, donHang.getDiaChi());
                    LatLng latLng = GoogleMapUtils.getLatLong();
                    if (mLocation != null && latLng != null) {
                        try {
                            new Directions(mLocation.getLatitude(), mLocation.getLongitude(), latLng.latitude, latLng.longitude, new Directions.DirectionsListener() {
                                @Override
                                public void onDirectionSuccess(List<Route> routes) {
                                    for (Route route : routes) {
                                        viewHolder.tvDistance.setText("Cách " + route.distance);
                                    }
                                }
                            }).execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            viewHolder.tvDistance.setVisibility(View.GONE);
                        }
                    } else {
                        if (mLocation != null && donHang.getLatitude() != 0 && donHang.getLongitude() != 0) {
                            if (mLocation != null) {
                                try {
                                    new Directions(mLocation.getLatitude(), mLocation.getLongitude(), donHang.getLatitude(), donHang.getLongitude(), new Directions.DirectionsListener() {
                                        @Override
                                        public void onDirectionSuccess(List<Route> routes) {
                                            for (Route route : routes) {
                                                viewHolder.tvDistance.setText("Cách " + route.distance);
                                            }
                                        }
                                    }).execute();
                                } catch (UnsupportedEncodingException e1) {
                                    e1.printStackTrace();
                                    viewHolder.tvDistance.setVisibility(View.GONE);
                                }
                            } else {
                                viewHolder.tvDistance.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }


        } else if (mList.get(position) == null) {
            MyGioHangLoadingViewHolder viewHolder = (MyGioHangLoadingViewHolder) holder;
            viewHolder.pbLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface IOnClickMyCart {

        void onClickMyCart(DonHang donHang);

        void onOptionSelectedMyCart(int postion, DonHang donHang);

        void onClickInfoAccount(DonHang donHang);
    }
}
