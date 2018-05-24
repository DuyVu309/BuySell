package com.example.user.banhangonline.screen.search.account;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.maps.Directions;
import com.example.user.banhangonline.model.maps.Route;
import com.example.user.banhangonline.utils.GoogleMapUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyUntils.keyAccount;

public class SearchAccountPresenter implements SearchAccountContact.Presenter {
    SearchAccountContact.View mView;
    private Context context;
    private List<Account> mList;
    private List<String> keyList;
    boolean isLoadedWithLittle = false;
    private int total = 0;

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Account> getmList() {
        return mList;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public void attachView(SearchAccountContact.View View) {
        mList = new ArrayList<>();
        keyList = new ArrayList<>();
        mView = View;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void getKeyAccount(final DatabaseReference databaseReference, final String filter) {
        if (!isViewAttached()) return;
        keyList.clear();
        databaseReference.child(keyAccount).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Account account = snapshot.getValue(Account.class);
                    if (account.getName().toLowerCase().contains(filter.toLowerCase())) {
                        keyList.add(dataSnapshot.getKey().toString());
                    } else if (account.getPhoneNumber().equals(filter)) {
                        keyList.add(dataSnapshot.getKey().toString());
                    }
                }

                if (keyList != null) {
                    if (mView != null) {
                        mView.getKeySuccess();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mView != null) {
                    mView.getKeyError();
                }
            }
        });
    }

    @Override
    public void getListAccuntWithFilter(DatabaseReference databaseReference, final String filter) {
        if (!isViewAttached()) return;
        if (filter == null) return;
        if (keyList.size() >= total + 10) {
            databaseReference.child(keyAccount).orderByKey().startAt(keyList.get(total)).endAt(keyList.get(total + 9)).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Account account = dataSnapshot.getValue(Account.class);
                    if (account.getName().toLowerCase().contains(filter.toLowerCase())) {
                        mList.add(account);
                    } else if (account.getPhoneNumber().equals(filter)) {
                        mList.add(account);
                    }

                    if (mList != null) {
                        if (mView != null) {
                            mView.getListAccountSuccess();
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (mView != null) {
                        mView.getListAccountError();
                    }
                }
            });

        } else if (keyList.size() < total + 10 + 1) {
            if (!isLoadedWithLittle) {
                try {
                    databaseReference.child(keyAccount).orderByKey().startAt(keyList.get(total)).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Account account = dataSnapshot.getValue(Account.class);
                            if (account.getName().toLowerCase().contains(filter.toLowerCase())) {
                                mList.add(account);
                            } else if (account.getPhoneNumber().equals(filter)) {
                                mList.add(account);
                            }

                            if (mList != null) {
                                if (mView != null) {
                                    mView.getListAccountSuccess();
                                }
                            }
                            isLoadedWithLittle = true;
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (mView != null) {
                                mView.getListAccountError();
                            }
                        }
                    });
                } catch (IndexOutOfBoundsException e) {

                }
            }
        }
    }

    @Override
    public void recentScanAccount(DatabaseReference databaseReference, final Location mLocation) {
        if (!isViewAttached()) return;
        mList.clear();
        databaseReference.child(keyAccount).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Account account = dataSnapshot.getValue(Account.class);
//                GoogleMapUtils.getLatLongFromGivenAddress(context, account.getAddress());
//                LatLng endLatLng = GoogleMapUtils.getLatLong();
//                if (mLocation != null && endLatLng != null) {
//                    try {
//                        new Directions(mLocation.getLatitude(), mLocation.getLongitude(), endLatLng.latitude, endLatLng.longitude, new Directions.DirectionsListener() {
//                            @Override
//                            public void onDirectionSuccess(List<Route> routes) {
//                                for (Route route : routes) {
//                                    Log.d("TAG route", route.distance);
//                                }
//                            }
//                        }).execute();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//
//                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
