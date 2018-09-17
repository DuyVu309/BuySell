package com.example.user.banhangonline.screen.library.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView;
import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoAdapter extends DragSelectRecyclerViewAdapter<DragSelectRecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_IMAGE = 1;
    private Context context;
    private SortedList<File> mList;
    private OnLickListenerPhoto mView;
    SortedListAdapterCallback<File> mCallback;

    public PhotoAdapter(Context context, OnLickListenerPhoto view) {
        this.context = context;
        this.mView = view;
        mCallback = new SortedListAdapterCallback<File>(PhotoAdapter.this) {
            @Override
            public int compare(File file1, File file2) {
                if (((File) file1).lastModified() > ((File) file2).lastModified()) {
                    return -1;
                } else if (((File) file1).lastModified() < ((File) file2).lastModified()) {
                    return 1;
                } else {
                }
                return 0;

            }

            @Override
            public boolean areContentsTheSame(File oldItem, File newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(File item1, File item2) {
                return false;
            }
        };
        mList = new SortedList<File>(File.class, mCallback);
    }

    public void addAllFile(List<File> fileList) {
        mList.addAll(fileList);
    }

    public void addFile(File file) {
        mList.add(file);
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) instanceof File ? VIEW_TYPE_IMAGE : 0;
    }

    public void setSelectedPhotos(List<File> list) {
        if (list == null || list.isEmpty()) return;
        for (int i = 0; i < mList.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equals(mList.get(i))) {
                    toggleSelected(i);
                }
            }
        }
    }

    public List<File> getSelectedPhotos() {
        Integer[] indicator = getSelectedIndices();
        List<File> selected = new ArrayList<>();
        for (Integer index : indicator) {
            if (index < 0) {
                continue;
            }
            selected.add(mList.get(index));
        }
        return selected;
    }

    @NonNull
    @Override
    public DragSelectRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_IMAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_row, parent, false);
            return new PhotoHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(DragSelectRecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mList.get(position) instanceof File) {
            PhotoHolder photoHolder = (PhotoHolder) holder;
            Glide.with(context).load(mList.get(position).toString()).centerCrop().into(photoHolder.image);
            if (isIndexSelected(position)) {
                photoHolder.check.setVisibility(View.VISIBLE);
                photoHolder.circle.setVisibility(View.VISIBLE);
                photoHolder.circle.setActivated(true);
                photoHolder.image.setActivated(true);
            } else {
                photoHolder.check.setVisibility(View.GONE);
                photoHolder.circle.setVisibility(View.GONE);
                photoHolder.circle.setActivated(false);
                photoHolder.image.setActivated(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }


    public class PhotoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_library)
        ImageView image;

        @BindView(R.id.check)
        ImageView check;

        @BindView(R.id.circle)
        View circle;

        public PhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.onClickSelected(mList.get(getAdapterPosition()), getAdapterPosition());
                    toggleSelected(getAdapterPosition());
                }
            });
            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    toggleSelected(getAdapterPosition());
                    mView.onLongClickSelected(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public interface OnLickListenerPhoto {
        void onClickSelected(File file, int position);

        void onLongClickSelected(int position);
    }
}
