package com.example.user.banhangonline.screen.sell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Categories;
import com.example.user.banhangonline.model.ListFileImages;
import com.example.user.banhangonline.model.Part;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.home.HomeActivity;
import com.example.user.banhangonline.screen.library.LibraryActivity;
import com.example.user.banhangonline.screen.sell.adapter.ImageAdapter;
import com.example.user.banhangonline.screen.sell.adapter.SpinnerAdapter;
import com.example.user.banhangonline.untils.NetworkUtils;
import com.example.user.banhangonline.untils.TimeNowUtils;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyIDCategory;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyIDPart;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyTitleCategory;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyTitlePart;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateThoiTrang;
import static com.example.user.banhangonline.untils.KeyUntils.keyListImage;

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

    @BindView(R.id.recycerview_list_bitmap)
    RecyclerView recyclerViewBitmap;

    @BindView(R.id.edt_thongtin)
    EditText edtHeader;

    @BindView(R.id.edt_mota)
    EditText edtMota;

    private Unbinder unbinder;
    private SellPresenter mPresenter;
    private SpinnerAdapter adapterCate;
    private SpinnerAdapter adapterPart;
    private ImageAdapter adapterImage;
    LinearLayoutManager managerImage;

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
        initSpinnerAndBitmap();
        saveTextEdit();
    }

    private void initSpinnerAndBitmap() {
        //spiner category
        LinearLayoutManager managerCate = new LinearLayoutManager(this);
        managerCate.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCate.setHasFixedSize(true);
        recyclerViewCate.setLayoutManager(managerCate);

        adapterCate = new SpinnerAdapter(this, mPresenter.getListCategory(), new SpinnerAdapter.IClickAdapterSpiner() {
            @Override
            public void onClickItemSpinner(Object object) {
                recyclerViewCate.setVisibility(View.GONE);
                expandSpinner(recyclerViewCate, spnCategory);
                Categories categories = Categories.class.cast(object);
                spnCategory.setText(categories.getTitle());
                mPresenter.setIdCate(categories.getId());
                mPresenter.setTitleCate(categories.getTitle());
                if (categories.getId() == keyIdCateThoiTrang) {
                    lnPart.setVisibility(View.VISIBLE);
                } else {
                    lnPart.setVisibility(View.INVISIBLE);
                }
            }
        });
        recyclerViewCate.setAdapter(adapterCate);

        //spinner part
        LinearLayoutManager managerPart = new LinearLayoutManager(this);
        managerPart.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPart.setHasFixedSize(true);
        recyclerViewPart.setLayoutManager(managerPart);
        adapterPart = new SpinnerAdapter(this, mPresenter.getListPart(), new SpinnerAdapter.IClickAdapterSpiner() {
            @Override
            public void onClickItemSpinner(Object object) {
                Part part = (Part) object;
                spnPart.setText(part.getTitle());
                mPresenter.setIdPart(part.getIDPart());
                mPresenter.setTitlePart(part.getTitle());
                recyclerViewPart.setVisibility(View.GONE);
                expandSpinner(recyclerViewPart, spnPart);
            }
        });
        recyclerViewPart.setAdapter(adapterPart);

        //recyclerview image
        mPresenter.setListFiles((List<File>) getIntent().getSerializableExtra(keyListImage));
        managerImage = new GridLayoutManager(this, 2);
        managerImage.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewBitmap.setHasFixedSize(true);
        recyclerViewBitmap.setLayoutManager(managerImage);
        if (mPresenter.getListFiles() != null) {
            adapterImage = new ImageAdapter(this, mPresenter.getListFiles(), new ImageAdapter.OnClickItemImage() {
                @Override
                public void onClickImageDelete(int position) {
                    mPresenter.getListFiles().remove(position);
                    adapterImage.notifyDataSetChanged();
                }
            });
            if (mPresenter.getListFiles().size() == 1) {
                managerImage = new GridLayoutManager(this, 1);
            }
            recyclerViewBitmap.setAdapter(adapterImage);
        }
    }

    private void saveTextEdit() {
        String idCate, titleCate, idPart, titlePart;
        idCate = getIntent().getStringExtra(keyIDCategory);
        titleCate = getIntent().getStringExtra(keyTitleCategory);
        idPart = getIntent().getStringExtra(keyIDPart);
        titlePart = getIntent().getStringExtra(keyTitlePart);
        if (idCate != null && titleCate != null) {
            mPresenter.setIdCate(idCate);
            mPresenter.setTitleCate(titleCate);
            spnCategory.setText(titleCate);
        }

        if (idPart != null && titlePart != null) {
            mPresenter.setIdPart(idPart);
            mPresenter.setTitlePart(titlePart);
            spnPart.setText(titlePart);
            lnPart.setVisibility(View.VISIBLE);
        }
        edtHeader.setText(PreferManager.getTextHeader(this));
        edtHeader.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                PreferManager.setTextHeader(SellActivity.this, editable.toString());
            }
        });

        edtHeader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (edtHeader.getRight() - edtHeader.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        edtHeader.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
        edtMota.setText(PreferManager.getTextMota(this));
        edtMota.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                PreferManager.setTextMota(SellActivity.this, editable.toString());
            }
        });
        edtMota.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (edtMota.getRight() - edtMota.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        edtMota.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
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
        if (mPresenter.getListFiles() == null && mPresenter.getIdCate() == null) {
            startActivity(new Intent(SellActivity.this, HomeActivity.class));
            finish();
        } else {
            showConfirmDialog(getString(R.string.message_huy_bai_viet), new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                @Override
                public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                    startActivity(new Intent(SellActivity.this, HomeActivity.class));
                    finish();
                }

                @Override
                public void onClickAnswerNegative(DialogPositiveNegative dialog) {
                    dialog.dismiss();
                }
            });
        }

    }

    @OnClick(R.id.btn_dangbai)
    public void onClickDangBai() {
        if (NetworkUtils.isConnected(this)) {
            if (edtHeader.getText().length() > 255) {
                showSnackbar(getString(R.string.tieu_de_qua_lon));
                return;
            }
            if (edtHeader.getText().length() < 255 && mPresenter.getListFiles() != null && mPresenter.getIdCate() != null) {
                if (lnPart.getVisibility() != View.VISIBLE) {
                    uploadListImage();
                }
                if (lnPart.getVisibility() == View.VISIBLE) {
                    if (mPresenter.getIdPart() != null) {
                        uploadListImage();
                    } else {
                        showSnackbar(getString(R.string.chon_hang_muc));
                    }
                }
            } else {
                showSnackbar(getString(R.string.chua_nhap_du_lieu));
                dismissDialog();
            }
        } else {
            showNoInternet();
        }
    }

    private void uploadListImage() {
        showDialog();
        mPresenter.upLoadFileImageToStorage(mStorageReferrence, PreferManager.getEmailID(this));
    }

    private void uploadListFile(String idPart) {
        mPresenter.upLoadSanPhamToFirebase(mDataBase,
                 new SanPham(PreferManager.getEmailID(this),
                          PreferManager.getNameAccount(this),
                          PreferManager.getEmailID(this) + Calendar.getInstance().getTimeInMillis(),
                          mPresenter.getIdCate(),
                          idPart,
                          edtHeader.getText().toString().trim(),
                          edtMota.getText().toString().trim(),
                          TimeNowUtils.getTimeNow(),
                          new ListFileImages().getContructor(mPresenter.getListImages(), mPresenter.getListNameImages())));
        dismissDialog();
    }

    @OnClick(R.id.tv_add_image)
    public void addImageSell() {
        showDialog();
        Intent intent = new Intent(SellActivity.this, LibraryActivity.class);
        if (mPresenter.getIdCate() != null && mPresenter.getTitleCate() != null) {
            intent.putExtra(keyIDCategory, mPresenter.getIdCate());
            intent.putExtra(keyTitleCategory, mPresenter.getTitleCate());
        }

        if (mPresenter.getIdPart() != null && mPresenter.getTitlePart() != null) {
            intent.putExtra(keyIDPart, mPresenter.getIdPart());
            intent.putExtra(keyTitlePart, mPresenter.getTitlePart());
        }
        startActivity(intent);
        dismissDialog();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        unbinder = null;
        mPresenter.onDestroy();
        mPresenter.detach();
        super.onDestroy();
    }

    @Override
    public void upLoadToFirebaseSuccess() {
        PreferManager.setTextHeader(this, "");
        PreferManager.setTextMota(this, "");
        startActivity(new Intent(SellActivity.this, HomeActivity.class));
        finish();
        dismissDialog();
    }

    @Override
    public void upLoadToFirebaseError() {
        dismissDialog();
    }

    @Override
    public void upLoadImagesSuccess(List<String> listNames) {
        if (mPresenter.getListImages().size() == mPresenter.getListFiles().size()) {
            if (lnPart.getVisibility() != View.VISIBLE) {
                uploadListFile(null);
            }
            if (lnPart.getVisibility() == View.VISIBLE) {
                if (mPresenter.getIdPart() != null) {
                    uploadListFile(mPresenter.getIdPart());
                } else {
                    showSnackbar(getString(R.string.chon_hang_muc));
                }
            }
        }
    }

    @Override
    public void upLoadImagErrror() {
        showSnackbar(getString(R.string.error_retry));
        dismissDialog();
    }

    @Override
    public void displayPercent(String percent) {
        showDialogMessage(percent);
    }
}
