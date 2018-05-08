package com.example.user.banhangonline.screen.home.fragment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.user.banhangonline.model.SanPham;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SanPhamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SANPHAM = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private Context context;
    private List<SanPham> mList;
    private ISelectPayAdapter mListener;

    boolean isLoading;

    private OnLoadMoreListener mOnLoadMore;

    public void setmOnLoadMore(OnLoadMoreListener mOnLoadMore) {
        this.mOnLoadMore = mOnLoadMore;
    }

    public SanPhamAdapter(RecyclerView recyclerView, Context context, List<SanPham> mList, ISelectPayAdapter mListener) {
        this.context = context;
        this.mList = mList;
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
    }

    public class SanPhamViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_sanpham)
        ImageView imgSanPham;

        @BindView(R.id.tv_header_sanpham)
        TextView tvHeaderSanPham;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mList.get(position) instanceof SanPham) {
            SanPhamViewHolder viewHolder = (SanPhamViewHolder) holder;
            String url = mList.get(position).getListFiles().getUrl1();
            Glide.with(context).load(url).centerCrop().into(viewHolder.imgSanPham);
            viewHolder.tvHeaderSanPham.setText(mList.get(position).getHeader());
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

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
}
