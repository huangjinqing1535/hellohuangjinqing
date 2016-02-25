package com.huang.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huang.database.util.DBDao;
import com.huang.entity.Constact;
import com.huang.getui.MainActivity;
import com.huang.getui.R;
import com.huang.listener.IntoSessionListener;
import com.huang.utils.Flog;
import com.huang.view.CustomImageView;

import java.util.List;

/**
 * Created by huang on 1/7/16.
 */
public class ConstactAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "ConstactAdapter";
    private Context context = null;
    List<String> groupData = null;

   private IntoSessionListener intoSessionListener = null;


    public ConstactAdapter(Context context, List<String> groupNames) {
        this.context = context;
        this.groupData = groupNames;
        Activity activity = (Activity) context;
        this.intoSessionListener = (IntoSessionListener)activity;
    }

    @Override
    public Object getChild(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * position与id一样，都是从0开始计数的，
     * 这里返回的id也是从0开始计数的
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        long id = 0;
        for (int i = 0; i < groupPosition; i++) {
            id += DBDao.getInstance(context).queryContantsByGroupName(groupData.get(groupPosition)).size();
        }
        id += childPosition;
        return id;
    }

    /**
     * ChildViewHolder内部类
     **/
    class ChildViewHolder {
        CustomImageView userImage = null;
        TextView userName = null;
        ImageView intoSession;
    }

    /**
     * 头像点击事件监听类
     **/
    class ImageClickListener implements View.OnClickListener {

        ChildViewHolder holder = null;

        public ImageClickListener(ChildViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Toast.makeText(context, holder.userName.getText() + " is clicked", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        List<Constact> mList = DBDao.getInstance(context).queryContantsByGroupName(groupData.get(groupPosition));
        /**
         * 这里isLastChild目前没用到，如果出现异常再说
         */
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.constact_child, null);
            //感觉这里需要把root设置成ViewGroup 对象
            /**
             * ERROR!!这里不能把null换成parent，否则会出现异常退出，原因不太确定，可能是inflate方法获得的这个item的View
             * 并不属于某个控件组，所以使用默认值null即可
             */
            holder = new ChildViewHolder();
            holder.userImage = (CustomImageView) convertView.findViewById(R.id.ct_photo);
            holder.userName = (TextView) convertView.findViewById(R.id.ct_name);
            holder.intoSession = (ImageView) convertView.findViewById(R.id.into_session);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        final String name = mList.get(childPosition).getName();
        final String clientid = mList.get(childPosition).getClientid();
        holder.userName.setText(name);

        holder.intoSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flog.e(TAG,"childPosition=="+childPosition);
                intoSessionListener.intoSession(name,clientid);
            }
        });
        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return DBDao.getInstance(context).queryContantsByGroupName(groupData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groupData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }


    class GroupViewHolder {
        ImageView image = null;
        TextView groupName = null;
        TextView childCount = null;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.constact_group, null);
            holder = new GroupViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.groupImage);
            holder.groupName = (TextView) convertView.findViewById(R.id.groupName);
            holder.childCount = (TextView) convertView.findViewById(R.id.childCount);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        if (isExpanded){
            holder.image.setBackgroundResource(R.drawable.group_close);
        }else{
            holder.image.setBackgroundResource(R.drawable.group_open);
        }

        holder.groupName.setText(groupData.get(groupPosition));
        holder.childCount.setText("23");
        return convertView;
        /**
         * 不要在适配器中调用适配器的内部方法，不然会出现奇怪的异常
         */
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}
