package com.huang.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
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
import com.huang.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by huang on 1/8/16.
 */
public class SessionListAdapter extends BaseAdapter {

    private List<MsgContent> msgContentList;

    private Context mContext;
    private static final String TAG = "SessionListAdapter";

    public SessionListAdapter(Context context, List<MsgContent> msgContentList) {
        this.msgContentList = msgContentList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return msgContentList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        holder = new ViewHolder();
        if (msgContentList.get(position).getFromName().equals(PreferencesUtils.getSharePreString(mContext, "username"))) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.session_list_right_item, null);
        } else {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.session_list_left_item, null);
        }
        holder.msgName = (TextView) convertView
                .findViewById(R.id.name);
        holder.msgContent = (TextView) convertView
                .findViewById(R.id.say_text);
        holder.msgName.setText(msgContentList.get(position).getFromName());
        SpannableStringBuilder sb = ExpressionUtil.prase(mContext, holder.msgContent, msgContentList.get(position).getContent());// 对内容做处理
        holder.msgContent.setText(sb);

        return convertView;
    }

    private ViewHolder holder;

    private class ViewHolder {
        private TextView msgName, msgContent, msgDate;
    }
}
