package com.example.user.banhangonline.screen.home.fragment.adapter;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.model.maps.Directions;
import com.example.user.banhangonline.model.maps.Route;
import com.example.user.banhangonline.utils.GoogleMapUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SanPhamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SANPHAM = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private Context context;
    private Location mLocation;
    private SortedList<SanPham> mList;
    private ISelectPayAdapter mListener;

    boolean isLoading;

    private OnLoadMoreListener mOnLoadMore;

    public void setmOnLoadMore(OnLoadMoreListener mOnLoadMore) {
        this.mOnLoadMore = mOnLoadMore;
    }

    public SanPhamAdapter(RecyclerView recyclerView, Context context, final Location mLocation, ISelectPayAdapter mListener) {
        this.context = context;
        this.mLocation = mLocation;
        this.mListener = mListener;

        final GridLayoutManager linearLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && linearLayoutManager.getItemCount() <= linearLayoutManager.findLastVisibleItemPosition() + 5) {
                    if (mOnLoadMore != null)
                        mOnLoadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });

        this.mList = new SortedList<SanPham>(SanPham.class, new SortedListAdapterCallback<SanPham>(SanPhamAdapter.this) {
            @Override
            public int compare(SanPham place1, SanPham place2) {
                double lat1 = place1.getLatitude();
                double lon1 = place1.getLongitude();
                double lat2 = place2.getLatitude();
                double lon2 = place2.getLongitude();

                double distanceToPlace1 = distance(mLocation.getLatitude(), mLocation.getLongitude(), lat1, lon1);
                double distanceToPlace2 = distance(mLocation.getLatitude(), mLocation.getLongitude(), lat2, lon2);
                return (int) (distanceToPlace1 - distanceToPlace2);
            }

            @Override
            public boolean areContentsTheSame(SanPham oldItem, SanPham newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(SanPham item1, SanPham item2) {
                return false;
            }
        });
    }

    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin(Math.sqrt(
                 Math.pow(Math.sin(deltaLat / 2), 2) +
                          Math.cos(fromLat) * Math.cos(toLat) *
                                   Math.pow(Math.sin(deltaLon / 2), 2)));
        return radius * angle;
    }

    public void addSanPham(SanPham sanPham) {
            mList.add(sanPham);
    }
    public class SanPhamViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_sanpham)
        ImageView imgSanPham;

        @BindView(R.id.tv_header_sanpham)
        TextView tvHeaderSanPham;

        @BindView(R.id.tv_gia_sanpham)
        TextView tvGiaSp;

        @BindView(R.id.tv_distance)
        TextView tvDistance;

        public SanPhamViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imgSanPham.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onSelectedSanPham(mList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof SanPham) {
            return VIEW_TYPE_SANPHAM;
        }
        if (mList.get(position) == null) {
            return VIEW_TYPE_LOADING;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SANPHAM) {
            View view = LayoutInflater.from(context).inflate(R.layout.sanpham_row, parent, false);
            return new SanPhamViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.loading_row, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mList.get(position) instanceof SanPham) {
            final SanPham sanPham = mList.get(position);
            final SanPhamViewHolder viewHolder = (SanPhamViewHolder) holder;
            String url = mList.get(position).getListFiles().getUrl1();
            Glide.with(context).load(url).centerCrop().into(viewHolder.imgSanPham);
            viewHolder.tvHeaderSanPham.setText(sanPham.getHeader());
            viewHolder.tvGiaSp.setText(sanPham.getGia());
            if (PreferManager.getIsShowDistanceSell(context)) {
                if (sanPham.getAddress() != null) {
                    GoogleMapUtils.getLatLongFromGivenAddress(context, sanPham.getAddress());
                    LatLng endLatLng = GoogleMapUtils.getLatLong();
                    if (mLocation != null && endLatLng != null) {
                        try {
                            new Directions(mLocation.getLatitude(), mLocation.getLongitude(), endLatLng.latitude, endLatLng.longitude, new Directions.DirectionsListener() {
                                @Override
                                public void onDirectionSuccess(List<Route> routes) {
                                    for (Route route : routes) {
                                        viewHolder.tvDistance.setText("Cách " + route.distance);
                                    }
                                }
                            }).execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (mLocation != null && sanPham.getLatitude() != 0 && sanPham.getLongitude() != 0) {
                            LatLng latLng = new LatLng(sanPham.getLatitude(), sanPham.getLongitude());
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
                                }
                            } else {
                                viewHolder.tvDistance.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface ISelectPayAdapter {
        void onSelectedSanPham(SanPham sanPham);

    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
