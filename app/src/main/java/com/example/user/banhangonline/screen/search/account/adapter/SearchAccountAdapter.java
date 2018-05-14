package com.example.user.banhangonline.screen.search.account.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.screen.home.fragment.adapter.SanPhamAdapter;
import com.example.user.banhangonline.untils.DialogUntils;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Account> mList;
    private IOnClickAccount mListener;
    boolean isLoading;

    private SanPhamAdapter.OnLoadMoreListener mOnLoadMore;

    public void setmOnLoadMore(SanPhamAdapter.OnLoadMoreListener mOnLoadMore) {
        this.mOnLoadMore = mOnLoadMore;
    }

    public SearchAccountAdapter(RecyclerView recyclerView, Context context, List<Account> mList, IOnClickAccount mListener) {
        this.context = context;
        this.mList = mList;
        this.mListener = mListener;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
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

    public class SearchAccountViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_lanscape)
        ImageView imgLanscape;

        @BindView(R.id.img_avt)
        ImageView imgAvt;

        @BindView(R.id.tv_account_name)
        TextView tvAccountName;

        @BindView(R.id.tv_account_phone)
        TextView tvAccountPhone;

        @BindView(R.id.tv_account_adress)
        TextView tvAccountAdress;

        public SearchAccountViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickAccount(mList.get(getAdapterPosition()));
                }
            });
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.account_row, parent, false);
        return new SearchAccountViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mList.get(position) instanceof Account) {
            Account account = mList.get(position);
            final SearchAccountViewholder viewholder = (SearchAccountViewholder) holder;
            Glide.with(context).load(account.getUrlLanscape()).error(R.drawable.bg_app).into(viewholder.imgLanscape);
            Glide.with(context).load(account.getUrlAvt()).error(R.drawable.ic_account).into(viewholder.imgAvt);
            viewholder.tvAccountName.setText(account.getName());
            viewholder.tvAccountPhone.setText(account.getPhoneNumber());
            viewholder.tvAccountAdress.setText(account.getAddress());

            viewholder.tvAccountPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogUntils.showConfirmDialogDefault(context,
                             context.getString(R.string.app_name),
                             context.getString(R.string.xac_nhan_call) + viewholder.tvAccountPhone.getText().toString(),
                             new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                                 @SuppressLint("MissingPermission")
                                 @Override
                                 public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                                     mListener.onClickPhoneNumber(viewholder.tvAccountPhone.getText().toString());
                                     dialog.dismiss();
                                 }

                                 @Override
                                 public void onClickAnswerNegative(DialogPositiveNegative dialog) {
                                     dialog.dismiss();
                                 }
                             });
                }
            });
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface IOnClickAccount {
        void onClickAccount(Account account);

        void onClickPhoneNumber(String phoneNumber);
    }
}
