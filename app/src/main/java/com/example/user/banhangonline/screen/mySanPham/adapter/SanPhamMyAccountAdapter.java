package com.example.user.banhangonline.screen.mySanPham.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.SanPham;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SanPhamMyAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SANPHAM = 1;
    private Context context;
    private List<SanPham> mListSp;
    private IOnClickSanphamAdapter mListener;

    public SanPhamMyAccountAdapter(Context context, List<SanPham> mList, IOnClickSanphamAdapter mListener) {
        this.context = context;
        this.mListSp = mList;
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mListSp.get(position) instanceof SanPham ? VIEW_TYPE_SANPHAM : 0;
    }

    public class SanPhamMyAccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_account)
        ImageView imgMyAccount;

        @BindView(R.id.tv_account_name)
        TextView tvAccountName;

        @BindView(R.id.tv_time)
        TextView tvTime;

        @BindView(R.id.img_menu_update)
        ImageView imgMenuUpdate;

        @BindView(R.id.lb_update)
        LinearLayout lnUpdate;

        @BindView(R.id.btn_change)
        TextView btnChange;

        @BindView(R.id.btn_delete)
        TextView btnDelete;

        @BindView(R.id.tv_header)
        TextView tvHeader;

        @BindView(R.id.tv_mota)
        TextView tvMota;

        @BindView(R.id.rv_sanpham_myaccount)
        RecyclerView recyclerView;

        public SanPhamMyAccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imgMenuUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lnUpdate.getVisibility() == View.GONE) {
                        lnUpdate.setVisibility(View.VISIBLE);
                    } else {
                        lnUpdate.setVisibility(View.GONE);
                    }
                }
            });

            recyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickSanPham(mListSp.get(getAdapterPosition()));
                }
            });

            btnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onChangeSanpham(mListSp.get(getAdapterPosition()));
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onDeleteSanPham(getAdapterPosition(), mListSp.get(getAdapterPosition()));
                }
            });
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_account_sanpham, parent, false);
        return new SanPhamMyAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SanPhamMyAccountViewHolder holder = (SanPhamMyAccountViewHolder) viewHolder;
        SanPham sanPham = mListSp.get(position);
        holder.tvAccountName.setText(PreferManager.getNameAccount(context));
        holder.tvTime.setText(sanPham.getTime());
        holder.tvHeader.setText(sanPham.getHeader());
        holder.tvMota.setText(sanPham.getMota());

        if (sanPham.getListFiles().getListUrl().size() == 1) {
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        } else {
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        }
        holder.recyclerView.setHasFixedSize(true);

        ListImagesCartAdapter adapter = new ListImagesCartAdapter(context, sanPham.getListFiles().getListUrl(), new ListImagesCartAdapter.IOnClickImageMyAccount() {
            @Override
            public void onClickImageMyAccount() {

            }
        });
        holder.recyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return mListSp != null ? mListSp.size() : 0;
    }

    public interface IOnClickSanphamAdapter {
        void onClickSanPham(SanPham sanPham);

        void onChangeSanpham(SanPham sanPham);

        void onDeleteSanPham(int position, SanPham sanPham);
    }
}
