package com.homework.lq.mynote;

import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView;
import android.widget.Toast;

public class WBeiWang extends Activity{
    private EditText mEditText;
    private EditText tEditText;

    private EditText beiwangcontent;
    private EditText datetext;
    private EditText timetext;
    private MyDatabaseHelper dbHelper;

    private String codeString;

    int hour=15;
    int minute=20;
    private Spinner spDown;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> dataList;
    private int[] icon = { R.drawable.blue,  R.drawable.purple,
            R.drawable.green,  R.drawable.yellow,  R.drawable.red};
    private int[] iconName = { 1, 2, 3, 4, 5 };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);


        beiwangcontent = (EditText) findViewById(R.id.mText);
        datetext = (EditText) findViewById(R.id.editText1);
        timetext = (EditText) findViewById(R.id.editText2);


        mEditText = (EditText) findViewById(R.id.editText1);
        tEditText = (EditText) findViewById(R.id.editText2);


        spDown = (Spinner) findViewById(R.id.spDown);
        spDown.setOnItemSelectedListener(new ProvOnItemSelectedListener());

        /*创建数据源*/
        dataList = new ArrayList<Map<String, Object>>();
        /*创建简单适配器*/
        adapter = new SimpleAdapter(this, getData(), R.layout.item,
                new String[] { "ivFruit", "tvFruit" }, new int[] { R.id.ivFruit,
                R.id.tvFruit });
        /*adapter设置一个下拉列表样式，参数为自己定义的子布局*/
        adapter.setDropDownViewResource(R.layout.item);
        /*spDown加载适配器*/
        spDown.setAdapter(adapter);


        mEditText.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        tEditText.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showTimePickDlg();
                    return true;
                }
                return false;
            }
        });
        tEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePickDlg();
                }
            }
        });


        dbHelper = new MyDatabaseHelper(this,"MyNote.db",null,2);
        dbHelper.getWritableDatabase();
        Button createDatabase = (Button) findViewById(R.id.set);
        createDatabase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String BWcontent = beiwangcontent.getText().toString();
                String BWdate = datetext.getText().toString();
                String BWtime = timetext.getText().toString();
                int key1=  Integer.parseInt(codeString);
                values.put("content",BWcontent);
                values.put("date",BWdate);
                values.put("time",BWtime);
                values.put("degree",key1);
                db.insert("note",null,values);
                values.clear();
                Intent intent = new Intent(WBeiWang.this,MainActivity.class);
                startActivity(intent);
            }
        });
        Button cancle = (Button) findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(WBeiWang.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //OnItemSelected监听器
    private class  ProvOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapter,View view,int position,long id) {
            TextView ProvinceTxt =  spDown.getSelectedView().findViewById(R.id.tvFruit); // 得到选中的选项Id
            codeString = ProvinceTxt.getText().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            String sInfo="什么也没选！";
            Toast.makeText(getApplicationContext(),sInfo, Toast.LENGTH_LONG).show();

        }
    }


    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(WBeiWang.this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear =monthOfYear+1;
                String TmonthOfYear = String.format("%02d",monthOfYear);
                String TdayOfMonth = String.format("%02d",dayOfMonth);
                WBeiWang.this.mEditText.setText(year + "-" + TmonthOfYear + "-" + TdayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    protected void showTimePickDlg() {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourofday, int minuteofhour) {
                hour=hourofday;
                minute=minuteofhour;
                WBeiWang.this.tEditText.setText(hour+"时"+minute+"分");
            }
        }, 15, 20, true).show();
    }
    /*设置数据源*/
    private List<Map<String, Object>> getData() {
        for(int i=0;i<icon.length;i++){      //循环添加图片文字信息
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("ivFruit", icon[i]);
            map.put("tvFruit",iconName[i]);
            dataList.add(map);
        }
        return dataList;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent();
            myIntent = new Intent(WBeiWang.this, MainActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
