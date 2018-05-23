package com.example.user.banhangonline.screen.sanphamWithIdCate.adapter;

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
import com.example.user.banhangonline.model.search.SearchAccount;
import com.example.user.banhangonline.model.search.SearchSP;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SEARCH_PRODUCT = 1;
    private static final int VIEW_TYPE_SEARCH_ACCOUNT = 2;
    private Context context;
    private List<Object> mList;
    private IOnClickSearch mListener;

    public SearchAdapter(Context context, List<Object> mList, IOnClickSearch mListener) {
        this.context = context;
        this.mList = mList;
        this.mListener = mListener;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_filter)
        TextView tvFilter;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mList != null) {
                        if (mList.size() > 0) {
                            mListener.onClickSanPham(mList.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }
    }

    public class SearchAccountHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_account)
        ImageView imgAccount;

        @BindView(R.id.tv_name)
        TextView tvName;

        public SearchAccountHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mList != null) {
                        if (mList.size() > 0) {
                            mListener.onClickSanPham(mList.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof SearchSP) {
            return VIEW_TYPE_SEARCH_PRODUCT;
        }
        if (mList.get(position) instanceof SearchAccount) {
            return VIEW_TYPE_SEARCH_ACCOUNT;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SEARCH_PRODUCT) {
            View view = LayoutInflater.from(context).inflate(R.layout.search_row, parent, false);
            return new SearchViewHolder(view);
        } else if (viewType == VIEW_TYPE_SEARCH_ACCOUNT) {
            View view = LayoutInflater.from(context).inflate(R.layout.search_account_row, parent, false);
            return new SearchAccountHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mList.get(position) instanceof SearchSP) {
            final SearchSP searchSP = (SearchSP) mList.get(position);
            SearchViewHolder viewHolder = (SearchViewHolder) holder;
            viewHolder.tvFilter.setText(searchSP.getHeaderSp());

        } else if (mList.get(position) instanceof SearchAccount) {
            SearchAccount account = (SearchAccount) mList.get(position);
            SearchAccountHolder accountHolder = (SearchAccountHolder) holder;
            accountHolder.tvName.setText(account.getNameAc());
            Glide.with(context).load(account.getUrlAvt()).error(R.drawable.ic_account).into(accountHolder.imgAccount);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface IOnClickSearch {
        void onClickSanPham(Object searchSP);
    }
}
