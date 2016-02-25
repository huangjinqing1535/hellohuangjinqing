package com.huang.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huang.getui.R;
import com.huang.utils.Flog;

import java.util.List;

/**
 * Created by huang on 1/14/16.
 */
public class ViewPagerAdapter extends PagerAdapter implements View.OnClickListener {
    private static final String TAG = "ViewPagerAdapter";

    private List<View> mListViews;
    private Context mContext;


    private FaceMessageListener mMessageListener;
    public interface FaceMessageListener {
        public void faceSelect(String tag);
    }

    public void setFaceMessageListener(FaceMessageListener messageListener) {
        this.mMessageListener = messageListener;
    }



    public ViewPagerAdapter(Context context, List<View> mListViews) {
        this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
        this.mContext = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));//删除页卡
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        View item = mListViews.get(position);
        container.addView(item, 0);//添加页卡
        ViewHolder holder = new ViewHolder();
        initFaceImageView(holder, item);
        return item;
    }

    /**
     * 加载头像
     *
     * @param holder
     * @param item
     */
    private void initFaceImageView(ViewHolder holder, View item) {
        holder.face1 = (ImageView) item.findViewById(R.id.face1);
        holder.face1.setOnClickListener(this);

        holder.face2 = (ImageView) item.findViewById(R.id.face2);
        holder.face2.setOnClickListener(this);

        holder.face3 = (ImageView) item.findViewById(R.id.face3);
        holder.face3.setOnClickListener(this);

        holder.face4 = (ImageView) item.findViewById(R.id.face4);
        holder.face4.setOnClickListener(this);

        holder.face5 = (ImageView) item.findViewById(R.id.face5);
        holder.face5.setOnClickListener(this);

        holder.face6 = (ImageView) item.findViewById(R.id.face6);
        holder.face6.setOnClickListener(this);

        holder.face7 = (ImageView) item.findViewById(R.id.face7);
        holder.face7.setOnClickListener(this);

        holder.face8 = (ImageView) item.findViewById(R.id.face8);
        holder.face8.setOnClickListener(this);

        holder.face9 = (ImageView) item.findViewById(R.id.face9);
        holder.face9.setOnClickListener(this);

        holder.face10 = (ImageView) item.findViewById(R.id.face10);
        holder.face10.setOnClickListener(this);

        holder.face11 = (ImageView) item.findViewById(R.id.face11);
        holder.face11.setOnClickListener(this);

        holder.face12 = (ImageView) item.findViewById(R.id.face12);
        holder.face12.setOnClickListener(this);

        holder.face13 = (ImageView) item.findViewById(R.id.face13);
        holder.face13.setOnClickListener(this);

        holder.face14 = (ImageView) item.findViewById(R.id.face14);
        holder.face14.setOnClickListener(this);

        holder.face15 = (ImageView) item.findViewById(R.id.face15);
        holder.face15.setOnClickListener(this);

        holder.face16 = (ImageView) item.findViewById(R.id.face16);
        holder.face16.setOnClickListener(this);

        holder.face17 = (ImageView) item.findViewById(R.id.face17);
        holder.face17.setOnClickListener(this);

        holder.face18 = (ImageView) item.findViewById(R.id.face18);
        holder.face18.setOnClickListener(this);

        holder.face19 = (ImageView) item.findViewById(R.id.face19);
        holder.face19.setOnClickListener(this);

        holder.face20 = (ImageView) item.findViewById(R.id.face20);
        holder.face20.setOnClickListener(this);

        holder.face21 = (ImageView) item.findViewById(R.id.face21);
        holder.face21.setOnClickListener(this);

        holder.face22 = (ImageView) item.findViewById(R.id.face22);
        holder.face22.setOnClickListener(this);

        holder.face23 = (ImageView) item.findViewById(R.id.face23);
        holder.face23.setOnClickListener(this);

        holder.faceDelete = (ImageView) item.findViewById(R.id.face_delete);
        holder.faceDelete.setOnClickListener(this);
    }

    @Override
    public int getCount() {
        return mListViews.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;//官方提示这样写
    }


    public class ViewHolder {
        ImageView face1, face2, face3, face4, face5, face6, face7, face8, face9, face10, face11, face12, face13, face14, face15,face16,face17,face18,face19,face20,face21,face22,face23,faceDelete;
    }


    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();
        switch (v.getId()) {
            case R.id.face1:
                Flog.e(TAG,"tag==="+tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face2:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face3:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face4:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face5:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face6:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face7:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face8:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face9:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face10:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face11:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face12:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face13:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face14:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face15:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face16:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face17:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face18:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face19:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face20:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face21:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face22:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face23:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
            case R.id.face_delete:
                Flog.e(TAG, "tag===" + tag);
                mMessageListener.faceSelect(tag);
                break;
        }
    }

}
