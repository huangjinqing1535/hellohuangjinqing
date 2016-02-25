package com.huang.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.huang.adapter.ConstactAdapter;
import com.huang.database.util.DBDao;
import com.huang.getui.R;
import com.huang.listener.IntoSessionListener;
import com.huang.listener.OnFragmentInteractionListener;
import com.huang.utils.Constants;
import com.huang.utils.Flog;

import java.util.List;


public class ConstactFragment extends Fragment implements View.OnClickListener{


    public static final String TAG = "ConstactFragment";
    OnFragmentInteractionListener mListener;
    private TextView constactAdd;
    private View mView;
    private ExpandableListView mListView;
    private List<String> groupNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_constact, container, false);

        initView();

        return mView;
    }


    private void initView(){
        constactAdd = (TextView)mView.findViewById(R.id.constact_add);
        constactAdd.setOnClickListener(this);

        mListView = (ExpandableListView)mView.findViewById(R.id.constact_group);
        groupNames = DBDao.getInstance(getActivity()).queryAllConstactGroupName();
        if (groupNames.size()>=1) {
            ConstactAdapter adapter = new ConstactAdapter(getActivity(),groupNames);
            mListView.setAdapter(adapter);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.constact_add:
                Flog.e(TAG, "添加监听器");
                mListener.onFragmentInteraction(Constants.ADD_CONSTACT_FRAGMENT);
                break;
        }
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
