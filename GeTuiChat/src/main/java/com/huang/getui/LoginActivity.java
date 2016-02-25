package com.huang.getui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huang.entity.User;
import com.huang.utils.Flog;
import com.huang.utils.GeTuiUtils;
import com.huang.utils.PreferencesUtils;
import com.huang.utils.Worker;
import com.igexin.sdk.PushManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "LoginActivity";
    private EditText usernameText;
    private EditText passwordText;
    private Button login, register,findUser;

    // SDK参数，会自动从Manifest文件中读取，第三方无需修改下列变量，请修改AndroidManifest.xml文件中相应的meta-data信息。
    // 修改方式参见个推SDK文档
    private String appkey = "";
    private String appsecret = "";
    private String appid = "";

    /**
     * SDK服务是否启动
     */
    private boolean isServiceRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (EditText) findViewById(R.id.account);
        passwordText = (EditText) findViewById(R.id.password);
        String userName = PreferencesUtils.getSharePreString(LoginActivity.this,"username");
        if (userName!=null){
            if (!userName.equals("")){
                usernameText.setText(userName);
            }
        }
        String password = PreferencesUtils.getSharePreString(LoginActivity.this,"password");
        if (password!=null){
            if (!password.equals("")){
                passwordText.setText(password);
            }
        }
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        findUser = (Button)findViewById(R.id.find_user);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        findUser.setOnClickListener(this);

        initData();
    }


    private void initData() {
        // 从AndroidManifest.xml的meta-data中读取SDK配置信息
        String packageName = getApplicationContext().getPackageName();
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                appid = appInfo.metaData.getString("PUSH_APPID");
                appsecret = appInfo.metaData.getString("PUSH_APPSECRET");
                appkey = (appInfo.metaData.get("PUSH_APPKEY") != null) ? appInfo.metaData.get("PUSH_APPKEY").toString() : null;
                Flog.e(TAG, "appid" + appid + "appsecret===" + appsecret + "appkey==" + appkey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 重新初始化sdk
        PushManager manager = PushManager.getInstance();
        manager.initialize(this.getApplicationContext());
        String clientid = manager.getClientid(LoginActivity.this);
        Flog.e(TAG, "clientid=====" + manager.getClientid(LoginActivity.this));



    }

    Worker mWorker = new Worker("HttpUtils");
    Handler handler = new Handler(mWorker.getLooper()){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    User user = (User)msg.obj;
                    if (GeTuiUtils.queryAilas(user)){
                        if (GeTuiUtils.queryAliasIsBind(user)) {
                            PreferencesUtils.putSharePreString(LoginActivity.this, "username", user.getUsername());
                            PreferencesUtils.putSharePreString(LoginActivity.this, "password", user.getPassword());
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    User mUser = (User)msg.obj;
                    String mClientid = GeTuiUtils.queryClientId(mUser);
                    if (mUser.getClientid().equals(mClientid)){
                        PreferencesUtils.putSharePreString(LoginActivity.this, "username", mUser.getUsername());
                        PreferencesUtils.putSharePreString(LoginActivity.this, "password", mUser.getPassword());
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                    }
                  break;
                case 3:
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    String clientId = PreferencesUtils.getSharePreString(LoginActivity.this,"clientid");
                    String userName = GeTuiUtils.queryAilasByClientid(clientId);
                    String password = PreferencesUtils.getSharePreString(LoginActivity.this,"password");
                    if (userName!=null){
                        Toast.makeText(LoginActivity.this, "用户名为："+userName+",密码为："+password, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(LoginActivity.this, "没有找到相应的用户", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String username = usernameText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                String clientid = PreferencesUtils.getSharePreString(this, "clientid");
                final User user = new User(username, password, clientid);
//                if (login.getText().equals("注册")) {
//                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
//                        Toast.makeText(this, "输入的内容有误", Toast.LENGTH_LONG).show();
//                    } else if (clientid != null && !clientid.equals("")) {
//                        Message msg= new Message();
//                        msg.what = 1;
//                        msg.obj = user;
//                        handler.sendMessage(msg);
//                    }
//                }else if(login.getText().equals("登录") ){
                    String name = PreferencesUtils.getSharePreString(this,"username");
                    String mPassword = PreferencesUtils.getSharePreString(this,"password");
                    if (name==null||name.equals("")||mPassword==null||mPassword.equals("")){
                        Message msg= new Message();
                        msg.what = 2;
                        msg.obj = user;
                        handler.sendMessage(msg);
                    } else if(name.equals(user.getUsername())&&mPassword.equals(user.getPassword())){
                        Message msg= new Message();
                        msg.what = 2;
                        msg.obj = user;
                        handler.sendMessage(msg);
                    }else {
                        Message msg= new Message();
                        msg.what = 3;
                        msg.obj = user;
                        handler.sendMessage(msg);
                    }
//                }
                break;
            case R.id.register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.find_user:
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
                break;
        }
    }
}
