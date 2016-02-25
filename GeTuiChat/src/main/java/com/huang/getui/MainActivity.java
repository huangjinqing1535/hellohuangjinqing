package com.huang.getui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.huang.database.util.DBDao;
import com.huang.entity.Constact;
import com.huang.entity.MsgContent;
import com.huang.fragment.AddFriendFragment;
import com.huang.fragment.ConstactFragment;
import com.huang.fragment.MessageFragment;
import com.huang.fragment.SessionFragment;
import com.huang.listener.IntoSessionListener;
import com.huang.listener.OnFragmentInteractionListener;
import com.huang.server.MessageService;
import com.huang.utils.Constants;
import com.huang.utils.Flog;
import com.igexin.sdk.PushManager;

/**
 * Created by huang on 12/29/15.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnFragmentInteractionListener, MessageService.MessageListener, IntoSessionListener {

    public static final String TAG = "MainActivity";
    private ImageButton mContact, mNews, mSetting;
    private FragmentManager fragmentManager = null;
    private Fragment mFragment = null;
    private MessageService messageService = null;
    private IntoSessionListener intoSessionListener = null;
    private View currentButton;
    private String currentFragment="ConstactFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 重新初始化sdk
        PushManager manager = PushManager.getInstance();
        manager.initialize(this.getApplicationContext());
        initView();
    }


    private void initView() {
        mContact = (ImageButton) findViewById(R.id.buttom_constact);
        mNews = (ImageButton) findViewById(R.id.buttom_news);
        mSetting = (ImageButton) findViewById(R.id.buttom_setting);

        mContact.setOnClickListener(this);
        mNews.setOnClickListener(this);
        mSetting.setOnClickListener(this);

        fragmentManager = getFragmentManager();
        mFragment = new ConstactFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_layout, mFragment)
                .commit();
        setButton(mContact);

        messageService = MessageService.getInstance(this);
        messageService.setMessageListener(this);

        DBDao.getInstance(this).createMsgList();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (this.getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        hideSoftInputView();
        switch (v.getId()) {
            case R.id.buttom_constact:
                setButton(v);
                currentFragment = "ConstactFragment";
                mFragment = new ConstactFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_layout, mFragment)
                        .commit();
                break;
            case R.id.buttom_news:
                setButton(v);
                currentFragment = "MessageFragment";
                mFragment = new MessageFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_layout, mFragment)
                        .commit();
                break;
            case R.id.buttom_setting:
                setButton(v);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(String fragmentname) {

        hideSoftInputView();
        switch (Integer.parseInt(fragmentname)) {
            case 1:
                currentFragment = "ConstactFragment";
                Flog.e(TAG, "constactfragment被点击了。。。。。。");
                setButton(mContact);
                mFragment = new ConstactFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_layout, mFragment)
                        .commit();
                break;
            case 2:
                Flog.e(TAG, "newsfragment被点击了。。。。。。");
                break;
            case 3:
                Flog.e(TAG, "settingfragment被点击了。。。。。。");
                break;
            case 4:
                currentFragment = "AddFriendFragment";
                Flog.e(TAG, "add---constactfragment被点击了。。。。。。");
                currentButton.setEnabled(true);
                mFragment = new AddFriendFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_layout, mFragment)
                        .commit();
                break;
        }
    }


    @Override
    public void getMsgData(MsgContent msgContent) {
        Flog.e(TAG, "getMsgData==" + msgContent.toString());
        if (msgContent.getMsgName() != null) {
            Toast.makeText(this,"你有新的消息",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void intoChatRoom(String username, String clientid) {
        Flog.e(TAG, "intoChatRoom===constactName" + username + "constactClientid==" + username);
        intoSessionFragment(username,clientid);
    }

    private void setButton(View v){
        if(currentButton!=null&&currentButton.getId()!=v.getId()){
            currentButton.setEnabled(true);
        }
        v.setEnabled(false);
        currentButton=v;
    }

    @Override
    public void intoSession(String constactName, String constactClientid) {
        Flog.e(TAG, "constactName" + constactName + "constactClientid==" + constactClientid);
        intoSessionFragment(constactName,constactClientid);
    }

    private void intoSessionFragment(String username,String Clientid){
        currentFragment = "SessionFragment";
        mFragment = new SessionFragment();
        if (username!=null&&!username.equals("")&&Clientid!=null&&!Clientid.equals("")) {
            Bundle data = new Bundle();
            data.putString("name", username);
            data.putString("clientid", Clientid);
            mFragment.setArguments(data);//通过Bundle向Activity中传递值
            fragmentManager.beginTransaction().replace(R.id.fragment_layout, mFragment)
                    .commit();
            currentButton.setEnabled(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Flog.e(TAG, "keyCode == KeyEvent.KEYCODE_BACK");
            hideSoftInputView();
            if (currentFragment.equals("ConstactFragment")) {
                moveTaskToBack(true);
                return false;
            }
            else{
                currentFragment = "ConstactFragment";
                setButton(mContact);
                mFragment = new ConstactFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_layout, mFragment)
                        .commit();
                return true;
            }
         }
        return super.onKeyDown(keyCode, event);
    }
}
