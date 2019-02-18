package com.homework.lq.mynote;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<Information> inforList = new ArrayList<>();
    private MyDatabaseHelper dbHelper;
    private TextView loginusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MyDatabaseHelper(this,"MyNote.db",null,2);
        dbHelper.getWritableDatabase();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initinfors();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final InforAdapter adapter = new InforAdapter(inforList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new InforAdapter.OnItemOnClickListener(){
            @Override
            public void onItemOnClick(View view, int pos) {
                int key1 = adapter.getKey1();
                SQLiteDatabase db= dbHelper.getWritableDatabase();
                String key2 = key1+"";
                Cursor cursor = db.query("note", new String[]{"degree,date,time,content"},"id = ?",new String[]{key2},null,null,null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String content = cursor.getString(3);
                        String date = cursor.getString(1);
                        String time = cursor.getString(2);
                        Intent intent = new Intent(MainActivity.this, xiugai.class);
                        intent.putExtra("id",key2);
                        intent.putExtra("content", content);
                        intent.putExtra("date", date);
                        intent.putExtra("time", time);
                        startActivity(intent);
                    }
                    }
                    cursor.close();
            }

            @Override
            public void onItemLongOnClick(final View view,final int pos) {

                int key =adapter.getKey();
                SQLiteDatabase db= dbHelper.getWritableDatabase();
                String key1 = key+"";
                db.delete("note","id = ?",new String[]{key1});
                Snackbar.make(view, "删除成功", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        Intent startIntent = new Intent(MainActivity.this, NotifyService.class);
        startService(startIntent);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WBeiWang.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = pref.getString("currentUser","");
        Log.d("MainActivity", username);

    }

    private void initinfors(){
        String content;
        String date;
        int degree;
        int id;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("note",null,null,null,null,null,"degree");
        if (cursor.moveToFirst())
        {
            do{
                content = cursor.getString(cursor.getColumnIndex("content"));
                 date = cursor.getString(cursor.getColumnIndex("date"));
                 id = cursor.getInt(cursor.getColumnIndex("id"));
                 //codestring = cursor.getString(cursor.getColumnIndex("degree"));
                degree= cursor.getInt(cursor.getColumnIndex("degree"));
                Information infor1 = new Information(content, degree, date,id);
                inforList.add(infor1);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
    private boolean isExit=false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(!isExit) {
                isExit = true;
                Toast.makeText(this, "在按一次退出程序", Toast.LENGTH_SHORT).show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,WBeiWang.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            Intent intent = new Intent(MainActivity.this,login.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}