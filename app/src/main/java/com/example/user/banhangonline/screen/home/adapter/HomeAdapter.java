package com.example.user.banhangonline.screen.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.model.Part;
import com.example.user.banhangonline.model.Pay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_PAY = 1;
    private Context mContext;
    private List<Part> mlist;
    private IAdapterListener mListener;

    public HomeAdapter(Context context, List<Part> mlist, IAdapterListener mListener) {
        this.mContext = context;
        this.mlist = mlist;
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mlist.get(position) instanceof Part) {
            return VIEW_TYPE_PAY;
        }
        return 0;
    }

    public class HomeViewholder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_pay)
        ImageView imgPay;

        @BindView(R.id.tv_title_pay)
        TextView tvTitlePay;

        public HomeViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imgPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickItemPay(mlist.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PAY) {
            return new HomeViewholder(LayoutInflater.from(mContext).inflate(R.layout.pay_row, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomeViewholder viewholder = (HomeViewholder) holder;
        if (mlist.get(position).getTitle() != null) {
            viewholder.tvTitlePay.setText(mlist.get(position).getTitle());
        }
        viewholder.imgPay.setImageDrawable(mContext.getResources().getDrawable(mlist.get(position).getUrl()));
    }

    @Override
    public int getItemCount() {
        return mlist != null ? mlist.size() : 0;
    }

    public interface IAdapterListener {
        void onClickItemPay(Part part);
    }
}
