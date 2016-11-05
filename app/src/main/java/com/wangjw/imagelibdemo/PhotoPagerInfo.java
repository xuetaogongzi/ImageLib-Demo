package com.wangjw.imagelibdemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wangjw on 2016/8/30.
 */
public class PhotoPagerInfo implements Parcelable {

    private List<String> photoList;

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.photoList);
    }

    public PhotoPagerInfo() {
    }

    protected PhotoPagerInfo(Parcel in) {
        this.photoList = in.createStringArrayList();
    }

    public static final Creator<PhotoPagerInfo> CREATOR = new Creator<PhotoPagerInfo>() {
        @Override
        public PhotoPagerInfo createFromParcel(Parcel source) {
            return new PhotoPagerInfo(source);
        }

        @Override
        public PhotoPagerInfo[] newArray(int size) {
            return new PhotoPagerInfo[size];
        }
    };
}
