package com.huang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huang.entity.MsgContent;
import com.huang.getui.R;
import com.huang.utils.Constants;
import com.huang.utils.ExpressionUtil;

import java.util.List;

/**
 * Created by huang on 1/5/16.
 */
public class MsgListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MsgContent> mMsgLists;

    public MsgListAdapter(Context context,List<MsgContent> msgLists) {
        this.mContext = context;
        this.mMsgLists = msgLists;
    }

    @Override
    public int getCount() {
        return mMsgLists.size();
    }

    @Override
    public MsgContent getItem(int position) {
        return mMsgLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {

        if (getItem(position).getType().equals(Constants.ADD_FRIEND)){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.msg_list_item, null);
            holder.msgName = (TextView) convertView
                    .findViewById(R.id.msg_name);
            holder.msgContent = (TextView) convertView
                    .findViewById(R.id.msg_content);
            holder.msgDate = (TextView) convertView
                    .findViewById(R.id.msg_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.msgName.setText(mMsgLists.get(position).getMsgName());
        if (mMsgLists.get(position).getType().equals(Constants.ADD_FRIEND)){
            holder.msgContent.setText("对方请求加你为好友");
        }else{
            holder.msgContent.setText(ExpressionUtil.prase(mContext,holder.msgContent,mMsgLists.get(position).getContent()));
        }

        holder.msgDate.setText(mMsgLists.get(position).getDate());
        return convertView;
    }
    ViewHolder holder;
    private class ViewHolder {
        private TextView msgName,msgContent,msgDate;
        private ImageView showMenu, localPlayFlag;
        private LinearLayout musicMenu;

        private LinearLayout addlist, say, delete, share, love;
    }
}
