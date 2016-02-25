package com.huang.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.huang.adapter.ConstactAdapter;
import com.huang.database.util.DBDao;
import com.huang.getui.R;
import com.huang.listener.OnFragmentInteractionListener;
import com.huang.utils.Constants;
import com.huang.utils.Flog;

import java.util.List;


public class SettingFragment extends Fragment implements View.OnClickListener{


    public static final String TAG = "SettingFragment";
    OnFragmentInteractionListener mListener;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting, container, false);

        return mView;
    }




    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



}
