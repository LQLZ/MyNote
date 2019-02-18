package com.homework.lq.mynote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText accountEdit;
    private EditText passwordEdit;
    private EditText confirmEdit;
    private Button register;
    private Button cancel;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText)findViewById(R.id.accout);
        passwordEdit = (EditText)findViewById(R.id.password);
        confirmEdit =  (EditText)findViewById(R.id.confirm);
        register = (Button)findViewById(R.id.register);
        cancel = (Button)findViewById(R.id.cancel);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String confirm = confirmEdit.getText().toString();
                String name = pref.getString(account,"1");
                if(account.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please enter your name!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(name.equals("1"))) {
                    Toast.makeText(RegisterActivity.this, "User name already exists!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<3){
                    Toast.makeText(RegisterActivity.this, "Password length is too short!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(password.equals(confirm))) {
                    Toast.makeText(RegisterActivity.this, "Entered passwords differ!",
                        Toast.LENGTH_SHORT).show();
                    return;
                }
                editor = pref.edit();
                editor.putString(account,password);
                editor.apply();
                Intent intent = new Intent();
                intent.putExtra("data_return",account);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("data_return","Hello");
        setResult(RESULT_OK,intent);
        finish();
    }

}
