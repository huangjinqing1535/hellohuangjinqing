package com.huang.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huang.database.util.DBDao;
import com.huang.entity.Constact;
import com.huang.entity.MsgContent;
import com.huang.entity.User;
import com.huang.getui.R;
import com.huang.listener.OnFragmentInteractionListener;
import com.huang.utils.Constants;
import com.huang.utils.Flog;
import com.huang.utils.GeTuiUtils;
import com.huang.utils.JsonUtils;
import com.huang.utils.PreferencesUtils;
import com.huang.utils.Worker;

import org.w3c.dom.Text;


public class AddFriendFragment extends Fragment implements View.OnClickListener {


    public static final String TAG = "AddFriendFragment";
    private OnFragmentInteractionListener mListener;
    private View mView;
    private ImageView mBack, addImage;
    private TextView searchName;
    private TextView mSearch;
    private EditText etName;
    private Constact mConstact;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_friend, container, false);

        initView();

        return mView;
    }


    private void initView() {
        mBack = (ImageView) mView.findViewById(R.id.back_home);
        mBack.setOnClickListener(this);

        mSearch = (TextView) mView.findViewById(R.id.add_search);
        mSearch.setOnClickListener(this);

        etName = (EditText) mView.findViewById(R.id.add_name);

        addImage = (ImageView) mView.findViewById(R.id.add_image);
        addImage.setOnClickListener(this);

        searchName = (TextView)mView.findViewById(R.id.search_result);
    }

    private Worker mWorker = new Worker(TAG);
    private Handler mHandler = new Handler(mWorker.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String mName = (String) msg.obj;
                    String clientid = GeTuiUtils.searchUser(mName);
                    if(clientid!=null){
                        mConstact = new Constact();
                        mConstact.setName(mName);
                        mConstact.setClientid(clientid);

                        Message message = new Message();
                        message.what =1;
                        message.obj = mName;
                        viewHandler.sendMessage(message);
                        if (GeTuiUtils.getIsUserOnline(clientid)){
                            mConstact.setState("online");
                            Toast.makeText(getActivity(), "此用户在线", Toast.LENGTH_SHORT).show();
                        }else {
                            mConstact.setState("offline");
                            Toast.makeText(getActivity(), "此用户不在线", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "此用户不存在", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if (DBDao.getInstance(getActivity()).queryContantName(mConstact)){
                        Toast.makeText(getActivity(),"他已是你的好友",Toast.LENGTH_SHORT).show();
                    }else{
                        MsgContent msgContent = new MsgContent();
                        msgContent.setFromName(PreferencesUtils.getSharePreString(getActivity(), "username"));
                        msgContent.setFromClientid(PreferencesUtils.getSharePreString(getActivity(), "clientid"));
                        msgContent.setToName(mConstact.getName());
                        msgContent.setContent("00");//00为加好友的请求，01为同意加为好友，02为拒绝加为好友
                        msgContent.setType(Constants.ADD_FRIEND);
                        msgContent.setToClientid(mConstact.getClientid());
                        msgContent.setIsRead("false");
                        msgContent.setDate(JsonUtils.getCurrentDate());
                        msgContent.setMsgName(mConstact.getName());
                        Flog.e(TAG, msgContent.toString());
                        GeTuiUtils.pushMessage(mConstact.getClientid(), JsonUtils.buildMsgContent(msgContent));
                        Toast.makeText(getActivity(),"请求已发送",Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };


    private Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String username = (String)msg.obj;
                    searchName.setText(mConstact.getName());
                    addImage.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_home:
                Flog.e(TAG, "添加监听器");
                mListener.onFragmentInteraction(Constants.CONSTACT_FRAGMENT);
                break;
            case R.id.add_search:
                String searchName = etName.getText().toString().trim();
                String myName = PreferencesUtils.getSharePreString(getActivity(), "username");
                if (TextUtils.isEmpty(searchName)) {
                    Toast.makeText(getActivity(), "搜索的名字不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (myName.equals(searchName)) {
                        Toast.makeText(getActivity(), "不能搜索自己的用户名", Toast.LENGTH_SHORT).show();
                    } else {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = searchName;
                        mHandler.sendMessage(message);
                    }
                }
                break;
            case R.id.add_image:
                if (mConstact!=null){
                    Message message = new Message();
                    message.what =2;
                    mHandler.sendMessage(message);
                }
                break;
        }
    }


}
