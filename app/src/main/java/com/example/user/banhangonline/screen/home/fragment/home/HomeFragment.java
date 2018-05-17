package com.example.user.banhangonline.screen.home.fragment.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Part;
import com.example.user.banhangonline.screen.home.adapter.HomeAdapter;
import com.example.user.banhangonline.screen.login.LoginActivity;
import com.example.user.banhangonline.screen.register.RegisterActivity;
import com.example.user.banhangonline.screen.sanphamWithIdCate.SanPhamWithIDActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyIdSanPham;
import static com.example.user.banhangonline.utils.KeyUntils.keyDoNguNoiY;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateCongNghe;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateDoAn;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateDoChoi;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateGiaDung;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateHocTap;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateKhac;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateMyPham;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCatePhuKien;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateThoiTrang;
import static com.example.user.banhangonline.utils.KeyUntils.keybeNam;
import static com.example.user.banhangonline.utils.KeyUntils.keybeNu;
import static com.example.user.banhangonline.utils.KeyUntils.keygiayNam;
import static com.example.user.banhangonline.utils.KeyUntils.keygiayNu;
import static com.example.user.banhangonline.utils.KeyUntils.keyphuKienNam;
import static com.example.user.banhangonline.utils.KeyUntils.keyphuKienNu;
import static com.example.user.banhangonline.utils.KeyUntils.keythoiTrangNam;
import static com.example.user.banhangonline.utils.KeyUntils.keythoiTrangNu;
import static com.example.user.banhangonline.utils.KeyUntils.keytuiSachNam;
import static com.example.user.banhangonline.utils.KeyUntils.keytuiSachNu;
import static com.example.user.banhangonline.utils.TextUntils.doAnVat;
import static com.example.user.banhangonline.utils.TextUntils.doChoiChoBe;
import static com.example.user.banhangonline.utils.TextUntils.doDienTu;
import static com.example.user.banhangonline.utils.TextUntils.doGiaDung;
import static com.example.user.banhangonline.utils.TextUntils.doNguNoiY;
import static com.example.user.banhangonline.utils.TextUntils.giayNam;
import static com.example.user.banhangonline.utils.TextUntils.giayNu;
import static com.example.user.banhangonline.utils.TextUntils.khac;
import static com.example.user.banhangonline.utils.TextUntils.myPham;
import static com.example.user.banhangonline.utils.TextUntils.phuKien;
import static com.example.user.banhangonline.utils.TextUntils.phuKienNam;
import static com.example.user.banhangonline.utils.TextUntils.phuKienNu;
import static com.example.user.banhangonline.utils.TextUntils.sachVo;
import static com.example.user.banhangonline.utils.TextUntils.thoiTrangBeGai;
import static com.example.user.banhangonline.utils.TextUntils.thoiTrangBeNam;
import static com.example.user.banhangonline.utils.TextUntils.thoiTrangNam;
import static com.example.user.banhangonline.utils.TextUntils.thoiTrangNu;
import static com.example.user.banhangonline.utils.TextUntils.tuiSachNam;
import static com.example.user.banhangonline.utils.TextUntils.tuiSachNu;

public class HomeFragment extends Fragment implements HomeAdapter.IAdapterListener,
         View.OnClickListener {

    private List<Part> mList;
    HomeAdapter adapter;
    RecyclerView recyclerView;
    LinearLayout lnHasAccount;
    Button btnDangKy, btnDangNhap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lnHasAccount = (LinearLayout) view.findViewById(R.id.ln_has_account);
        btnDangKy = (Button) view.findViewById(R.id.btn_dangki);
        btnDangNhap = (Button) view.findViewById(R.id.btn_dangnhap);
        if (PreferManager.getIsLogin(getActivity())) {
            lnHasAccount.setVisibility(View.GONE);
        } else {
            lnHasAccount.setVisibility(View.VISIBLE);
        }

        btnDangKy.setOnClickListener(this);
        btnDangNhap.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycerview);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        initAdapter();
        return view;
    }

    private void initAdapter() {
        mList = new ArrayList<>();
        initList();
        adapter = new HomeAdapter(getActivity(), mList, this);
        recyclerView.setAdapter(adapter);
    }

    private void initList() {
        mList.add(new Part(keyIdCateDoAn, null, R.drawable.bg_doanvat, doAnVat));
        mList.add(new Part(keyIdCateMyPham, null, R.drawable.bg_mypham, myPham));
        mList.add(new Part(keyIdCateThoiTrang, keythoiTrangNam, R.drawable.bg_thoitrangnam, thoiTrangNam));
        mList.add(new Part(keyIdCateThoiTrang, keygiayNam, R.drawable.bg_giaynam, giayNam));
        mList.add(new Part(keyIdCateThoiTrang, keytuiSachNam, R.drawable.bg_tuisachnam, tuiSachNam));
        mList.add(new Part(keyIdCateThoiTrang, keyphuKienNam, R.drawable.bg_phu_kien_nam, phuKienNam));
        mList.add(new Part(keyIdCateThoiTrang, keybeNam, R.drawable.bg_be_nam, thoiTrangBeNam));
        mList.add(new Part(keyIdCateThoiTrang, keythoiTrangNu, R.drawable.bg_quanao_nu, thoiTrangNu));
        mList.add(new Part(keyIdCateThoiTrang, keyDoNguNoiY, R.drawable.bg_dongu_noiy, doNguNoiY));
        mList.add(new Part(keyIdCateThoiTrang, keygiayNu, R.drawable.bg_giay_nu, giayNu));
        mList.add(new Part(keyIdCateThoiTrang, keytuiSachNu, R.drawable.bg_tuisachnu, tuiSachNu));
        mList.add(new Part(keyIdCateThoiTrang, keyphuKienNu, R.drawable.bg_phu_kien_nu, phuKienNu));
        mList.add(new Part(keyIdCateThoiTrang, keybeNu, R.drawable.bg_be_gai, thoiTrangBeGai));
        mList.add(new Part(keyIdCateCongNghe, null, R.drawable.bg_dientu, doDienTu));
        mList.add(new Part(keyIdCatePhuKien, null, R.drawable.bg_phukien, phuKien));
        mList.add(new Part(keyIdCateGiaDung, null, R.drawable.bg_dogiadung, doGiaDung));
        mList.add(new Part(keyIdCateHocTap, null, R.drawable.bg_sachvo, sachVo));
        mList.add(new Part(keyIdCateDoChoi, null, R.drawable.bg_dochoibe, doChoiChoBe));
        mList.add(new Part(keyIdCateKhac, null, R.drawable.bg_khac, khac));

    }

    @Override
    public void onClickItemPay(Part part) {
        if (part != null) {
            Intent intent = new Intent(getActivity(), SanPhamWithIDActivity.class);
            intent.putExtra(keyIdSanPham, part);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dangki:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                getActivity().finish();
                break;
            case R.id.btn_dangnhap:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }
}
