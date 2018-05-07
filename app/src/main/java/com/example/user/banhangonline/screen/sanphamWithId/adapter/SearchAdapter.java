package com.example.user.banhangonline.screen.sanphamWithId.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.model.SearchSP;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SearchSP> mList;
    private IOnClickSearch mListener;

    public SearchAdapter(Context context, List<SearchSP> mList, IOnClickSearch mListener) {
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
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_row, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mList.get(position) instanceof SearchSP) {
            SearchViewHolder viewHolder = (SearchViewHolder) holder;
            viewHolder.tvFilter.setText(mList.get(position).getHeaderSp());

            viewHolder.tvFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mList != null) {
                        if (mList.size() > 0) {
                            mListener.onClickSanPham(mList.get(position));
                        }
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface IOnClickSearch {
        void onClickSanPham(SearchSP searchSP);
    }
}
