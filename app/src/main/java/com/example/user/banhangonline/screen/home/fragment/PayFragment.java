package com.example.user.banhangonline.screen.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.model.Categories;

public class PayFragment extends Fragment {

    private static final String ARG_BOOK = "book";
    private Categories categories;

    public static PayFragment newInstance(Categories categories) {
        PayFragment fragment = new PayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_BOOK, categories);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categories = (Categories) getArguments().getSerializable(ARG_BOOK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycerview);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        return view;
    }
}
