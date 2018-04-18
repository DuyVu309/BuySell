package com.example.user.banhangonline.screen.sell;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.Categories;
import com.example.user.banhangonline.model.Part;
import com.example.user.banhangonline.screen.sell.adapter.SpinnerAdapter;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateThoiTrang;

public class SellActivity extends BaseActivity implements SellContact.View {

    @BindView(R.id.spiner_category)
    TextView spnCategory;

    @BindView(R.id.recycerview_cate)
    RecyclerView recyclerViewCate;

    @BindView(R.id.ln_part)
    LinearLayout lnPart;

    @BindView(R.id.spiner_part)
    TextView spnPart;

    @BindView(R.id.recycerview_part)
    RecyclerView recyclerViewPart;

    private Unbinder unbinder;
    private SellPresenter mPresenter;
    private SpinnerAdapter adapterCate;
    private SpinnerAdapter adapterPart;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        unbinder = ButterKnife.bind(this);
        mPresenter = new SellPresenter();
        mPresenter.onCreate();
        mPresenter.attachView(this);
        initSpinner();
    }

    private void initSpinner() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCate.setHasFixedSize(true);
        recyclerViewCate.setLayoutManager(manager);

        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPart.setHasFixedSize(true);
        recyclerViewPart.setLayoutManager(manager2);

        adapterCate = new SpinnerAdapter(this, mPresenter.getListCategory(), new SpinnerAdapter.IClickAdapterSpiner() {
            @Override
            public void onClickItemSpinner(Object object) {
                recyclerViewCate.setVisibility(View.GONE);
                expandSpinner(recyclerViewCate, spnCategory);
                Categories categories = Categories.class.cast(object);
                spnCategory.setText(categories.getTitle());
                if (categories.getId() == keyIdCateThoiTrang) {
                    lnPart.setVisibility(View.VISIBLE);
                } else {
                    lnPart.setVisibility(View.GONE);
                }
            }
        });

        adapterPart = new SpinnerAdapter(this, mPresenter.getListPart(), new SpinnerAdapter.IClickAdapterSpiner() {
            @Override
            public void onClickItemSpinner(Object object) {
                Part part = (Part) object;
                spnPart.setText(part.getTitle());
                recyclerViewPart.setVisibility(View.GONE);
                expandSpinner(recyclerViewPart, spnPart);
            }
        });
        recyclerViewCate.setAdapter(adapterCate);
        recyclerViewPart.setAdapter(adapterPart);
    }

    @OnClick(R.id.spiner_category)
    public void onClickSpinnerCate() {
        if (recyclerViewCate.getVisibility() == View.VISIBLE) {
            recyclerViewCate.setVisibility(View.GONE);
            expandSpinner(recyclerViewCate, spnCategory);
        } else {
            recyclerViewCate.setVisibility(View.VISIBLE);
            expandSpinner(recyclerViewCate, spnCategory);
        }

    }

    private void expandSpinner(RecyclerView recyclerView, TextView view) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more, 0);
        } else {
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less, 0);
        }
    }

    @OnClick(R.id.spiner_part)
    public void onClickSpinerPart() {
        if (recyclerViewPart.getVisibility() == View.VISIBLE) {
            recyclerViewPart.setVisibility(View.GONE);
            expandSpinner(recyclerViewPart, spnPart);
        } else {
            recyclerViewPart.setVisibility(View.VISIBLE);
            expandSpinner(recyclerViewPart, spnPart);
        }
    }

    @OnClick(R.id.img_arrow_back)
    public void backHomeActivity() {
        showConfirmDialog(getString(R.string.message_huy_bai_viet), new DialogPositiveNegative.IPositiveNegativeDialogListener() {
            @Override
            public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                finish();
            }

            @Override
            public void onClickAnswerNegative(DialogPositiveNegative dialog) {
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.tv_add_image)
    public void addImageSell(){

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        unbinder = null;
        mPresenter.onDestroy();
        mPresenter.detach();
        super.onDestroy();
    }
}
