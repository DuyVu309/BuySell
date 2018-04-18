package com.example.user.banhangonline.screen.sell.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.model.Categories;
import com.example.user.banhangonline.model.Part;

import java.util.List;

public class SpinnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SPINNER = 1;

    private Context context;
    private List<Object> mList;
    private IClickAdapterSpiner mListener;

    public SpinnerAdapter(Context context, List<Object> mList, IClickAdapterSpiner mListener) {
        this.context = context;
        this.mList = mList;
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof Categories) {
            return VIEW_TYPE_SPINNER;
        }
        if (mList.get(position) instanceof Part) {
            return VIEW_TYPE_SPINNER;
        }
        return 0;
    }

    public class CategoryrHolder extends RecyclerView.ViewHolder {
        TextView tvCategorySpinner;

        public CategoryrHolder(View itemView) {
            super(itemView);
            tvCategorySpinner = (TextView) itemView.findViewById(R.id.tv_cate_spinner);
            tvCategorySpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickItemSpinner(mList.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SPINNER) {
            View view = LayoutInflater.from(context).inflate(R.layout.spinner_row, parent, false);
            return new CategoryrHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mList.get(position) instanceof Categories) {
            Categories categories = (Categories) mList.get(position);
            CategoryrHolder spinnerHolder = (CategoryrHolder) holder;
            spinnerHolder.tvCategorySpinner.setText(categories.getTitle());
        }

        if (mList.get(position) instanceof Part) {
            Part part = (Part) mList.get(position);
            CategoryrHolder spinnerHolder2 = (CategoryrHolder) holder;
            spinnerHolder2.tvCategorySpinner.setText(part.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface IClickAdapterSpiner {
        void onClickItemSpinner(Object categories);
    }
}
