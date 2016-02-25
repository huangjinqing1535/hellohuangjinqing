package com.huang.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huang.adapter.SessionListAdapter;
import com.huang.adapter.ViewPagerAdapter;
import com.huang.database.util.DBDao;
import com.huang.entity.Constact;
import com.huang.entity.MsgContent;
import com.huang.getui.R;
import com.huang.listener.OnFragmentInteractionListener;
import com.huang.server.MessageService;
import com.huang.utils.Constants;
import com.huang.utils.ExpressionUtil;
import com.huang.utils.Flog;
import com.huang.utils.HttpUtils;
import com.huang.utils.JsonUtils;
import com.huang.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;


public class SessionFragment extends Fragment implements View.OnClickListener, HttpUtils.HttpUtilsListener, MessageService.MessageListener,ViewPagerAdapter.FaceMessageListener{


    public static final String TAG = "SessionFragment";
    private OnFragmentInteractionListener mListener;
    private View mView;
    private String constactName, constactClientid;
    private ImageView mBack,imageFace,dots1,dots2,dots3;


    private TextView name, state, sendMsg, addFriend;
    private EditText msgText;
    private List<MsgContent> msgContentList;
    private ListView msgListView;
    private SessionListAdapter adapter;
    private MessageService messageService = null;
    private LinearLayout imageFaceLayout = null;
    private ViewPager mViewPager;
    private List<View> views = new ArrayList<>();
    private View view1,view2,view3;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout faceLayout;
    private List<ImageView> dots = new ArrayList<>();
    private View currentDots;
    private int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_session, container, false);
        Bundle data = getArguments();//获得从activity中传递过来的值
        if (data != null) {
            constactName = data.getString("name");
            constactClientid = data.getString("clientid");
            Flog.e(TAG, "bundle==constactName==" + constactName + "constactClientid==" + constactClientid);
        }
        initView();
        initData();
        initViewPager(inflater);
        return mView;
    }

    private void initView() {
        imageFace = (ImageView)mView.findViewById(R.id.image_face);
        imageFace.setOnClickListener(this);
        imageFaceLayout =(LinearLayout)mView.findViewById(R.id.chat_face_container);

        mBack = (ImageView) mView.findViewById(R.id.back_home);
        mBack.setOnClickListener(this);

        msgText = (EditText) mView.findViewById(R.id.session_sms);
        msgText.setCursorVisible(true);
        msgText.setOnClickListener(this);
        msgText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Flog.e(TAG,"v===="+v+"====hasFocus=="+hasFocus);
            }
        });


        sendMsg = (TextView) mView.findViewById(R.id.session_send_sms);
        sendMsg.setOnClickListener(this);

        msgListView = (ListView) mView.findViewById(R.id.session_chat_listview);

        addFriend = (TextView) mView.findViewById(R.id.session_add_friend);
        if (!DBDao.getInstance(getActivity()).queryContantByName(constactName)) {
            addFriend.setVisibility(View.VISIBLE);
        }
        addFriend.setOnClickListener(this);

        name = (TextView) mView.findViewById(R.id.session_name);
        name.setText("与" + constactName + "聊天中");
        state = (TextView) mView.findViewById(R.id.session_state);
        HttpUtils.getInstance().setHttpUtilsListener(this);
        HttpUtils.getInstance().queryOnline(constactClientid);

        messageService = MessageService.getInstance(getActivity());
        messageService.setMessageListener(this);

    }

    private void initData() {
        msgContentList = DBDao.getInstance(getActivity()).queryMessagesByConstact(constactName);
        adapter = new SessionListAdapter(getActivity(), msgContentList);
        msgListView.setAdapter(adapter);

    }

    private void initViewPager(LayoutInflater inflater){
        mViewPager = (ViewPager)mView.findViewById(R.id.face_viewpager);
        faceLayout = (LinearLayout)mView.findViewById(R.id.chat_face_container);
        dots1 = (ImageView)mView.findViewById(R.id.dots1);
        dots2 = (ImageView)mView.findViewById(R.id.dots2);
        dots3 = (ImageView)mView.findViewById(R.id.dots3);
        dots.add(dots1);
        dots.add(dots2);
        dots.add(dots3);
        view1 = inflater.inflate(R.layout.face_view1,null);
        view2 = inflater.inflate(R.layout.face_view2,null);
        view3 = inflater.inflate(R.layout.face_view3,null);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        viewPagerAdapter = new ViewPagerAdapter(getActivity(),views);
        viewPagerAdapter.setFaceMessageListener(SessionFragment.this);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Flog.e(TAG,"viewpager=====onPageScrolled===position=="+position);
            }

            @Override
            public void onPageSelected(int position) {
                Flog.e(TAG, "viewpager=====position==" + position);
                setButton(dots.get(position));
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Flog.e(TAG,"viewpager=====state=="+state);
            }
        });

    }

    private void setButton(View v){
        if(currentDots!=null&&currentDots.getId()!=v.getId()){
            currentDots.setEnabled(true);
        }
        v.setEnabled(false);
        currentDots=v;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_home:
                Flog.e(TAG, "添加监听器");
                mListener.onFragmentInteraction(Constants.CONSTACT_FRAGMENT);
                break;
            case R.id.session_send_sms:
                String content = msgText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getActivity(), "输入的内容为空", Toast.LENGTH_SHORT).show();
                } else {

                    MsgContent msgContent = new MsgContent();
                    String fromName = PreferencesUtils.getSharePreString(getActivity(), "username");
                    msgContent.setFromName(fromName);
                    msgContent.setFromClientid(PreferencesUtils.getSharePreString(getActivity(), "clientid"));
                    msgContent.setToName(constactName);
                    msgContent.setToClientid(constactClientid);
                    msgContent.setMsgName(constactName);
                    msgContent.setContent(content);
                    msgContent.setType(Constants.TEXT);
                    msgContent.setDate(JsonUtils.getCurrentDate());
                    msgContent.setIsRead("false");
                    Flog.e(TAG, "sendmsg===" + msgContent.toString());
                    DBDao.getInstance(getActivity()).insertMessage(msgContent);
                    msgContentList.add(msgContent);
                    adapter.notifyDataSetChanged();
                    HttpUtils.getInstance().postTest(msgContent);
                    msgText.setText("");
                }
                break;
            case R.id.session_add_friend:
                Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                Constact constact = new Constact(constactName,constactClientid,"好友","online");
                DBDao.getInstance(getActivity()).insertConstact(constact);
                addFriend.setVisibility(View.GONE);
                break;
            case R.id.image_face:
                if(faceLayout.getVisibility()==View.VISIBLE){
                    showSoftInputView(msgText);
                    faceLayout.setVisibility(View.GONE);
                }else{
                    hideSoftInputView();
                    faceLayout.setVisibility(View.VISIBLE);
                    setButton(dots.get(0));
                }
                break;
            case R.id.session_sms:
                Flog.e(TAG,"输入框显示");
//                showSoftInputView(msgText);
                if(faceLayout.getVisibility()==View.VISIBLE){
                    faceLayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    boolean isOnline = (boolean) msg.obj;
                    if (isOnline) {
                        state.setText("在线");
                    } else {
                        state.setText("离线");
                    }
                    break;
            }
        }
    };


    @Override
    public void isOnline(boolean isOnline) {
        Flog.e(TAG, "isonline==" + isOnline);
        Message message = new Message();
        message.what = 1;
        message.obj = isOnline;
        mHandler.sendMessage(message);
    }

    @Override
    public void getMsgData(MsgContent msgContent) {
        Flog.e(TAG, "getMsgData==" + msgContent.toString());
        if (msgContent.getMsgName() != null) {
            if (msgContent.getMsgName().equals(constactName)) {
                msgContentList.add(msgContent);
                adapter.notifyDataSetChanged();
            }
        }
    }



    @Override
    public void faceSelect(String tag) {
        Flog.e(TAG, "faceSelect=======position===" + currentPage + "===tag====" + tag);
        if (!tag.contains("_del")) {// 如果不是删除图标
            ExpressionUtil.insert(msgText, ExpressionUtil.getFace(getActivity(), tag));
        } else {
            ExpressionUtil.delete(msgText);
        }
    }

    /**
     * 弹出输入法窗口
     */
    private void showSoftInputView(final View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }









}
