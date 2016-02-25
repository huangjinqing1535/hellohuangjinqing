package com.huang.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.huang.adapter.MsgListAdapter;
import com.huang.database.util.DBDao;
import com.huang.entity.Constact;
import com.huang.entity.MsgContent;
import com.huang.getui.R;
import com.huang.listener.OnFragmentInteractionListener;
import com.huang.swipemenulistview.SwipeMenu;
import com.huang.swipemenulistview.SwipeMenuCreator;
import com.huang.swipemenulistview.SwipeMenuItem;
import com.huang.swipemenulistview.SwipeMenuListView;
import com.huang.utils.Constants;
import com.huang.utils.Flog;
import com.huang.utils.HttpUtils;
import com.huang.utils.JsonUtils;
import com.huang.utils.PreferencesUtils;

import java.util.List;

/**
 * 消息fragment
 */
public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";
    private OnFragmentInteractionListener mListener;
    private View mView;
    private SwipeMenuListView mListView;
    private List<MsgContent> mMsgLists;
    private MsgListAdapter msgListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_message, container, false);
        initView();
        return mView;
    }

    private void initView(){
        mListView = (SwipeMenuListView)mView.findViewById(R.id.message_list);

        mMsgLists = DBDao.getInstance(getActivity()).queryAllMsgList();
        for (MsgContent msgContent:mMsgLists){
            Flog.e(TAG,"initData=="+msgContent.toString());
        }
        if (mMsgLists.size()>=1){
            msgListAdapter = new MsgListAdapter(getActivity(),mMsgLists);
            mListView.setAdapter(msgListAdapter);
        }

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                    case 1:
                        createMenu2(menu);
                        break;
                }
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getActivity());
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0x18,
                        0x5E)));
                item1.setWidth(dp2px(60));
                item1.setTitle("同意");
                item1.setTitleSize(16);
                item1.setTitleColor(Color.BLACK);
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(
                        getActivity());
                item2.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                item2.setWidth(dp2px(60));
                item2.setTitleColor(Color.BLACK);
                item2.setTitleSize(16);
                item2.setTitle("拒绝");
                menu.addMenuItem(item2);
                SwipeMenuItem item3 = new SwipeMenuItem(
                        getActivity());
                item3.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                item3.setWidth(dp2px(60));
                item3.setTitleColor(Color.BLACK);
                item3.setTitle("删除");
                item3.setTitleSize(16);
                menu.addMenuItem(item3);
            }

            private void createMenu2(SwipeMenu menu) {
                SwipeMenuItem item2 = new SwipeMenuItem(
                        getActivity());
                item2.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                item2.setWidth(dp2px(60));
                item2.setTitleColor(Color.BLACK);
                item2.setTitle("删除");
                item2.setTitleSize(16);
                menu.addMenuItem(item2);
            }


        };
        // set creator
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                swipeMenuType(position,menu,index);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMsgLists.get(position).getType().equals(Constants.TEXT)) {
                    String fromClientid = mMsgLists.get(position).getFromClientid();
                    String toClientid = mMsgLists.get(position).getToClientid();
                    String myclientid = PreferencesUtils.getSharePreString(getActivity(), "clientid");
                    if (myclientid.equals(fromClientid)) {
                        mListener.intoChatRoom(mMsgLists.get(position).getMsgName(), toClientid);
                        Flog.e(TAG, "setOnItemClickListener===" + "name==" + mMsgLists.get(position).getMsgName() + "toClientid==" + toClientid);
                    } else {
                        mListener.intoChatRoom(mMsgLists.get(position).getMsgName(), fromClientid);
                        Flog.e(TAG, "setOnItemClickListener===" + "name==" + mMsgLists.get(position).getMsgName() + "fromClientid==" + fromClientid);
                    }
                }
            }
        });

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


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }



    private void swipeMenuType(int position, SwipeMenu menu, int index){
        String type = mMsgLists.get(position).getType();
        switch (Integer.parseInt(type)){
            case Constants.TYPE_ADD_FRIEND:
                swipeAddFriend(position,menu,index);
                break;
            case Constants.TYPE_TEXT:
                swipeText(position,menu,index);
                break;
        }

    }
    /**
     * 消息删除操作
     * @param position
     * @param menu
     * @param index
     */
    private void swipeText(int position, SwipeMenu menu, int index){
        MsgContent msgContent = mMsgLists.get(position);
        switch (index) {
            case 0://删除
                DBDao.getInstance(getActivity()).deleteMsgListByName(msgContent);
                mMsgLists.remove(position);
                msgListAdapter.notifyDataSetChanged();
                break;
        }
    }


    /**
     * 加好友菜单操作
     * @param position
     * @param menu
     * @param index
     */
    private void swipeAddFriend(int position, SwipeMenu menu, int index){
        MsgContent msgContent = mMsgLists.get(position);
        switch (index) {
            case 0://加为好友
                MsgContent newMsgContent = buildMsgContent(msgContent,"01");
                if (DBDao.getInstance(getActivity()).queryContantByName(newMsgContent.getToName())) {
                    Toast.makeText(getActivity(),"对方已是你的好友",Toast.LENGTH_SHORT).show();
                }else{
                    Constact mConstact = new Constact(newMsgContent.getToName(),newMsgContent.getToClientid(),"好友","online");
                    DBDao.getInstance(getActivity()).insertConstact(mConstact);
                }
                break;
            case 1://拒绝好友
                MsgContent mContent = buildMsgContent(msgContent,"02");
                Flog.e(TAG, "swipeAddFriend==拒绝" + mContent.toString());
                break;
        }
    }

    public MsgContent buildMsgContent(MsgContent msgContent,String content){
        MsgContent newMsgContent = new MsgContent();
        newMsgContent.setMsgName(msgContent.getToName());
        newMsgContent.setType(Constants.ADD_FRIEND);
        newMsgContent.setContent(content);
        newMsgContent.setIsRead("false");
        newMsgContent.setToName(msgContent.getFromName());
        newMsgContent.setFromClientid(msgContent.getToClientid());
        newMsgContent.setDate(JsonUtils.getCurrentDate());
        newMsgContent.setToClientid(msgContent.getFromClientid());
        newMsgContent.setFromName(msgContent.getToName());
        Flog.e(TAG, "swipeAddFriend==同意" + newMsgContent.toString());
        HttpUtils.getInstance().postAddFriend(newMsgContent, Constants.TYPE_ADD_FRIEND);
        DBDao.getInstance(getActivity()).deleteMsgListByName(msgContent);
        return  newMsgContent;
    }

}
