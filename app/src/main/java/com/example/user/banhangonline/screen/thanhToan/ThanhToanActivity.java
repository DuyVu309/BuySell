package com.example.user.banhangonline.screen.thanhToan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.DonHang;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.utils.DialogUntils;
import com.example.user.banhangonline.utils.NetworkUtils;
import com.example.user.banhangonline.utils.TimeNowUtils;
import com.example.user.banhangonline.widget.dialog.DialogChangeAddress;
import com.example.user.banhangonline.widget.dialog.DialogChangePhoneNmber;
import com.example.user.banhangonline.widget.dialog.DialogMethodConnect;
import com.example.user.banhangonline.widget.dialog.DialogPositiveNegative;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartDetail;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartPhone;

public class ThanhToanActivity extends BaseActivity implements ThanhToanContact.View {


    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_sdt)
    TextView tvSdt;

    @BindView(R.id.tv_xuli_by)
    TextView tvXulyBy;

    @BindView(R.id.tv_name_nguoi_ban)
    TextView tvNameNguoiBan;

    @BindView(R.id.tv_gia)
    TextView tvGia;

    @BindView(R.id.img_sanpham)
    ImageView imgSp;

    @BindView(R.id.tv_mota_sanpham)
    TextView tvMota;

    @BindView(R.id.tv_header_sanpham)
    TextView tvHeader;

    @BindView(R.id.edt_sell_so_luong)
    EditText edtSoLuong;

    @BindView(R.id.btn_thanh_toan)
    Button btnThanhToan;

    ThanhToanPresenter mPresnter;
    private String phone;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        ButterKnife.bind(this);
        mPresnter = new ThanhToanPresenter();
        mPresnter.attachView(this);
        phone = getIntent().getStringExtra(keyStartPhone);
        mPresnter.setSanPham((SanPham) getIntent().getSerializableExtra(keyStartDetail));
        if (mPresnter.getSanPham() != null) {
            initInfo(mPresnter.getSanPham());
        }
    }

    private void initInfo(SanPham sanPham) {
        tvAddress.setText(PreferManager.getMyAddress(this));
        tvSdt.setText(PreferManager.getPhoneNumber(this));
        tvXulyBy.setText(sanPham.getNameNguoiBan());
        tvNameNguoiBan.setText(sanPham.getNameNguoiBan());
        tvGia.setText(sanPham.getGia());
        Glide.with(this).load(sanPham.getListFiles().getUrl1()).into(imgSp);
        tvMota.setText(sanPham.getMota());
        tvHeader.setText(sanPham.getHeader());
    }

    @OnClick(R.id.btn_change_address)
    public void changeMyAddress() {
        DialogUntils.showChangeBuyAddress(this, tvAddress.getText().toString().trim(), new DialogChangeAddress.IOnDoneChangeAddress() {
            @Override
            public void doneChangeAddress(String address) {
                PreferManager.setMyAddress(ThanhToanActivity.this, !address.equals("") ? address : null);
                tvAddress.setText(address);
            }
        });
    }

    @OnClick(R.id.btn_change_sdt)
    public void changePhoneNumber() {
        DialogUntils.showChangeBuyPhoneNumber(this, tvSdt.getText() != null ? tvSdt.getText().toString().trim() : "", new DialogChangePhoneNmber.IOnDoneChangeBuyPhoneNumber() {
            @Override
            public void doneChangeBuyPhoneNumber(String phoneNumber) {
                tvSdt.setText(phoneNumber);
            }
        });
    }

    @OnClick(R.id.btn_thanh_toan)
    public void pushDonHang() {
        if (NetworkUtils.isConnected(this)) {
            if (mPresnter.getSanPham() != null) {
                if (PreferManager.getUserID(this) == null) {
                    showSnackbar(getString(R.string.chua_dang_nhap));
                    showConfirmDialog(getString(R.string.ban_co_muon_dn_dk_khong), new DialogPositiveNegative.IPositiveNegativeDialogListener() {
                        @Override
                        public void onClickAnswerPositive(DialogPositiveNegative dialog) {
                            PreferManager.setIsLogin(ThanhToanActivity.this, false);
                            PreferManager.setIDBuySell(ThanhToanActivity.this, null);
                            PreferManager.setEmail(ThanhToanActivity.this, null);
                            PreferManager.setUserID(ThanhToanActivity.this, null);
                            PreferManager.setNameAccount(ThanhToanActivity.this, null);
                            PreferManager.setPhoneNumber(ThanhToanActivity.this, null);
                            logoutUser();
                        }

                        @Override
                        public void onClickAnswerNegative(DialogPositiveNegative dialog) {
                            dialog.dismiss();
                        }
                    });
                } else if (PreferManager.getUserID(this).equals(mPresnter.getSanPham().getIdNguoiban())) {
                    showSnackbar(getString(R.string.khong_the_mua_sp));
                } else if (PreferManager.getUserID(this) != null
                         && mPresnter.getSanPham().getIdNguoiban() != null
                         && tvAddress.getText().toString().trim() != null
                         && !tvAddress.getText().toString().trim().equals(getString(R.string.dont_address))
                         && !tvSdt.getText().toString().trim().equals(getString(R.string.dont_phone_number))
                         && !edtSoLuong.getText().toString().trim().equals("")) {
                    showDialog();
                    if (getMyLocation() != null) {
                        DonHang donHangCart = new DonHang(
                                 tvAddress.getText().toString().trim(),
                                 mPresnter.getSanPham().getHeader(),
                                 String.valueOf(Calendar.getInstance().getTimeInMillis()),
                                 mPresnter.getSanPham().getIdNguoiban(),
                                 PreferManager.getUserID(this),
                                 PreferManager.getNameAccount(this),
                                 tvSdt.getText().toString().trim(),
                                 TimeNowUtils.getTimeNow(),
                                 mPresnter.getSanPham().getListFiles().getUrl1(),
                                 mPresnter.getSanPham().getGia(),
                                 edtSoLuong.getText().toString().trim(),
                                 getMyLocation().getLatitude(),
                                 getMyLocation().getLongitude());
                        mPresnter.pushDonHangToFirebase(mDataBase, donHangCart);
                    } else {
                        DonHang donHangCart = new DonHang(tvAddress.getText().toString().trim(),
                                 mPresnter.getSanPham().getHeader(),
                                 String.valueOf(Calendar.getInstance().getTimeInMillis()),
                                 mPresnter.getSanPham().getIdNguoiban(),
                                 PreferManager.getUserID(this),
                                 PreferManager.getNameAccount(this),
                                 tvSdt.getText().toString().trim(),
                                 TimeNowUtils.getTimeNow(),
                                 mPresnter.getSanPham().getListFiles().getUrl1(),
                                 mPresnter.getSanPham().getGia(),
                                 edtSoLuong.getText().toString().trim(),
                                 0,
                                 0);
                        mPresnter.pushDonHangToFirebase(mDataBase, donHangCart);
                    }


                } else {
                    showSnackbar(getString(R.string.error_retry));
                }
            }

        } else {
            showNoInternet();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void pushDonHangSuccess() {
        btnThanhToan.setEnabled(false);
        Toast.makeText(this, getString(R.string.da_gui), Toast.LENGTH_SHORT).show();
        dismissDialog();
        DialogUntils.showMethodConnect(this, new DialogMethodConnect.IOnClickChooseMethodPay() {
            @Override
            public void onMethodCall() {
                if (phone != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
            }

            @Override
            public void onMethodSMS() {
                if (phone != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone));
                    intent.putExtra("sms_body", mPresnter.getSanPham().getHeader() +
                             "\n" + mPresnter.getSanPham().getAddress() +
                             "\n" + getString(R.string.so_luong) + ": " + edtSoLuong.getText().toString().trim() +
                             "\n" + TimeNowUtils.getTimeNow());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void pushDonHangError() {
        Toast.makeText(this, getString(R.string.error_retry), Toast.LENGTH_SHORT).show();
        dismissDialog();
    }

    @Override
    protected void onDestroy() {
        mPresnter.detach();
        super.onDestroy();
    }
}
