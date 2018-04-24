package com.example.user.banhangonline.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class ListFileImages implements Serializable {
    private String url1;
    private String url2;
    private String url3;
    private String url4;
    private String name1;
    private String name2;
    private String name3;
    private String name4;
    private List<String> stringList = new ArrayList<>();

    public List<String> getListUrl() {
        stringList.clear();
        if (url1 != null)
            stringList.add(url1);
        if (url2 != null)
            stringList.add(url2);
        if (url3 != null)
            stringList.add(url3);
        if (url4 != null)
            stringList.add(url4);
        return stringList;
    }

    public List<String> getListName() {
        stringList.clear();
        if (name1 != null)
            stringList.add(name1);
        if (name2 != null)
            stringList.add(name2);
        if (name3 != null)
            stringList.add(name3);
        if (name4 != null)
            stringList.add(name4);
        return stringList;
    }


    public ListFileImages() {
    }

    public ListFileImages(String url1, String name1) {
        this.url1 = url1;
        this.name1 = name1;
    }

    public ListFileImages(String url1, String url2, String name1, String name2) {
        this.url1 = url1;
        this.url2 = url2;
        this.name1 = name1;
        this.name2 = name2;
    }

    public ListFileImages(String url1, String url2, String url3, String name1, String name2, String name3) {
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
    }

    public ListFileImages(String url1, String url2, String url3, String url4, String name1, String name2, String name3, String name4) {
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
        this.url4 = url4;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
    }

    public ListFileImages getContructor(List<String> mlist, List<String> mlistName) {
        if (mlist.size() == 1) {
            return new ListFileImages(mlist.get(0), mlistName.get(0));
        }
        if (mlist.size() == 2) {
            return new ListFileImages(mlist.get(0), mlist.get(1), mlistName.get(0), mlistName.get(1));

        }
        if (mlist.size() == 3) {
            return new ListFileImages(mlist.get(0), mlist.get(1), mlist.get(2), mlistName.get(0), mlistName.get(1), mlistName.get(2));

        }
        if (mlist.size() == 4) {
            return new ListFileImages(mlist.get(0), mlist.get(1), mlist.get(2), mlist.get(3), mlistName.get(0), mlistName.get(1), mlistName.get(2), mlistName.get(3));
        }
        return null;
    }


    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getUrl4() {
        return url4;
    }

    public void setUrl4(String url4) {
        this.url4 = url4;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getName4() {
        return name4;
    }

    public void setName4(String name4) {
        this.name4 = name4;
    }
}
