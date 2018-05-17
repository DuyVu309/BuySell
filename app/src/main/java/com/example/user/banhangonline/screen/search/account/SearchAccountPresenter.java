package com.example.user.banhangonline.screen.search.account;

import com.example.user.banhangonline.model.Account;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyUntils.keyAccount;

public class SearchAccountPresenter implements SearchAccountContact.Presenter {
    SearchAccountContact.View mView;
    private List<Account> mList;
    private List<String> keyList;
    boolean isLoadedWithLittle = false;
    private int total = 0;

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

        } else if (keyList.size() < total + 10) {
            if (!isLoadedWithLittle) {
                databaseReference.child(keyAccount).orderByKey().startAt(keyList.get(total)).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Account account = dataSnapshot.getValue(Account.class);
                        if (account.getName().toLowerCase().contains(filter.toLowerCase())) {
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
            }
        }
    }
}
