package com.reyhane.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.reyhane.note.Adaptor.NoteAdaptor;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<Integer> id = new ArrayList<Integer>();
    ArrayList<String> t = new ArrayList<String>();
    ArrayList<String> d = new ArrayList<String>();
    ArrayList<Integer> img = new ArrayList<Integer>();
    SqlHelper Data = new SqlHelper(this);

    public NoteAdaptor adaptor;

    FloatingActionButton addButton;
    RecyclerView rcv;
    DrawerLayout drawerLayout;
    ImageButton menu;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //setting up views
        SetUpViews();

        //adding data to recycler view and making it
        GettingDataFromSQL();

        //setting uo the recyclerview
        SetRecyclerView();

        //adding new item
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewNote.class);
                startActivity(intent);
            }
        });

        //menu and navigation view
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i("click_log", "logged click");
                Toast.makeText(MainActivity.this, "hi", Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case (R.id.favorite): {
                        Toast.makeText(MainActivity.this, "favorite", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(intent);
                    }
                    break;
                    case (R.id.deleted): {
                        Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, DeletedActivity.class);
                        startActivity(intent);
                    }
                    break;
                    case (R.id.setting): {
                        Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
                return true;
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });
    }
    //setting up views
    public void SetUpViews(){
        drawerLayout = findViewById(R.id.drawer_menu);
        menu = findViewById(R.id.menu_button);
        navigationView = findViewById(R.id.nav_view);
        addButton = findViewById(R.id.AddButton);
        addButton.setImageResource(R.drawable.ic_add_24);
    }
    //setting uo the recyclerview
    public void SetRecyclerView(){
        rcv = findViewById(R.id.RecyclerView);
        adaptor = new NoteAdaptor(this, id,t, d, img);
        rcv.setAdapter(adaptor);
        rcv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
    //adding data to recycler view and making it
    public void GettingDataFromSQL(){
        Cursor AllData = Data.GetData();
        if(AllData.getCount() != 0){
            while (AllData.moveToNext()){
                id.add(AllData.getInt(0));
                t.add(AllData.getString(1));
                d.add(AllData.getString(2));
                img.add(AllData.getInt(3));
            }
        }
    }
    //navigation item click
    //@Override
    /*public boolean onNavigationItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.favorite){
            Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, FavoriteActivity.class);
            this.startActivity(intent);
        }
        else if(item.getItemId() == R.id.deleted){
            Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DeletedActivity.class);
            this.startActivity(intent);
        }
        else if(item.getItemId() == R.id.setting){
            Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SettingActivity.class);
            this.startActivity(intent);
        }
        else
            Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();

        return true;
    }*/
}