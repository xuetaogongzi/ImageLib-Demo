package com.wangjw.imagelibdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wangjw on 2016/8/30.
 */

public class PhotoPagerActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_PAGER_INFO = "pager_info";
    public static final String EXTRA_KEY_INDEX = "index";

    private PhotoViewPager mViewPager;
    private TextView mTvIndicator;

    private PhotoPagerAdapter mAdapter;
    private PhotoPagerInfo mPagerInfo;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);

        mPagerInfo = getIntent().getParcelableExtra(EXTRA_KEY_PAGER_INFO);
        mIndex = getIntent().getIntExtra(EXTRA_KEY_INDEX, 0);
        if (savedInstanceState != null) {
            mPagerInfo = savedInstanceState.getParcelable(EXTRA_KEY_PAGER_INFO);
            mIndex = savedInstanceState.getInt(EXTRA_KEY_INDEX, 0);
        }

        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_KEY_PAGER_INFO, mPagerInfo);
        outState.putInt(EXTRA_KEY_INDEX, mIndex);
    }

    private void initViews() {
        mViewPager = (PhotoViewPager) findViewById(R.id.ViewPager);
        mTvIndicator = (TextView) findViewById(R.id.TextView_Indicator);

        mAdapter = new PhotoPagerAdapter(mPagerInfo.getPhotoList());
        mViewPager.setAdapter(mAdapter);

        updateIndicator(1, mViewPager.getAdapter().getCount());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateIndicator(position + 1, mViewPager.getAdapter().getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(mIndex);
    }

    private void updateIndicator(int index, int total) {
        SpannableString spanStr = new SpannableString(index + "/" + total);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#dedede"));
        spanStr.setSpan(colorSpan, 0, (index + "").length(), 0);
        mTvIndicator.setText(spanStr);
    }

    private class PhotoPagerAdapter extends PagerAdapter {

        private List<String> mDataList;

        public PhotoPagerAdapter(List<String> list) {
            mDataList = list;
        }

        @Override
        public int getCount() {
            return mDataList != null ? mDataList.size() : 0;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            MyPhotoView photoView = new MyPhotoView(container.getContext());
            photoView.setImageUri("file://" + mDataList.get(position), null);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
