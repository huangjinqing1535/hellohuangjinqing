package com.huang.getui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.huang.entity.User;
import com.huang.utils.GeTuiUtils;
import com.huang.utils.PreferencesUtils;
import com.huang.utils.Worker;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText registerName,registerPassword;
    private Button register;
    private ImageView mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        initView();
    }


    private void initView(){
        registerName = (EditText)findViewById(R.id.name);
        registerPassword = (EditText)findViewById(R.id.password);
        register = (Button)findViewById(R.id.register_complete);
        register.setOnClickListener(this);
        mBack = (ImageView)findViewById(R.id.back_home);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_complete:
                String username = registerName.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();
                String clientid = PreferencesUtils.getSharePreString(this, "clientid");
                final User user = new User(username, password, clientid);
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "输入的内容有误", Toast.LENGTH_LONG).show();
                } else if (clientid != null && !clientid.equals("")) {
                    Message msg= new Message();
                    msg.what = 1;
                    msg.obj = user;
                    handler.sendMessage(msg);
                }else {
                    Toast.makeText(this,"个推启动失败，请重新启动应用",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back_home:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    Worker mWorker = new Worker("HttpUtils");
    Handler handler = new Handler(mWorker.getLooper()){
        @Override
        public void handleMessage(Message msg) {
            User user = (User)msg.obj;
            switch (msg.what){
                case 1:
                    if (GeTuiUtils.queryAilas(user)){
                        if (GeTuiUtils.queryAliasIsBind(user)) {
                            PreferencesUtils.putSharePreString(RegisterActivity.this, "username", user.getUsername());
                            PreferencesUtils.putSharePreString(RegisterActivity.this, "password", user.getPassword());
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(RegisterActivity.this, "已被注册，请尝试找回用户名", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };


}
