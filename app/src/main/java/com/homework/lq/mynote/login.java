package com.homework.lq.mynote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class login extends AppCompatActivity {
    private EditText accoutEdit;
    private EditText passwordEdit;
    private Button register;
    private Button login;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private boolean isRemember;
    private String currentUser;
    private boolean judge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accoutEdit = (EditText)findViewById(R.id.accout);
        passwordEdit = (EditText)findViewById(R.id.password);
        rememberPass = (CheckBox)findViewById(R.id.remember_pass);
        register = (Button)findViewById(R.id.register);
        login = (Button)findViewById(R.id.login);
        isRemember = pref.getBoolean("remember_password", false);
        currentUser = pref.getString("currentUser","");
        judge = pref.getBoolean("judge",false);
        editor = pref.edit();
        editor.putString("20152005009","123456");
        editor.apply();
        editor.clear();
        accoutEdit.setText(currentUser);

        if ((isRemember)&&(judge)){
            String password = pref.getString(currentUser,"");
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accoutEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(account.equals(""))
                {
                    Toast.makeText(login.this,"Please enter your user name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<3)
                {
                    Toast.makeText(login.this,"Please enter your password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String confirm = pref.getString(account,"");
                editor = pref.edit();
                editor.putString("currentUser",account);

                if(password.equals(confirm)){

                    if(rememberPass.isChecked()){
                        editor.putBoolean("judge",true);
                        editor.putBoolean("remember_password",true);
                    }else {
                        editor.putBoolean("remember_password", false);
                    }
                    Toast.makeText(login.this,"Success!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(login.this,MainActivity.class);
                    startActivity(intent);

                    finish();

                } else {
                    editor.putBoolean("judge",false);
                    passwordEdit.setText("");
                    Toast.makeText(login.this,"accout or password is invalid",
                            Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    currentUser = data.getStringExtra("data_return");
                    accoutEdit.setText(currentUser);
                }
                break;
            default:
        }
    }
}