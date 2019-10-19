package com.deco.moodify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StatsPage extends AppCompatActivity {

    private int love_count = MainActivity.love_count;
    private int joy_count = MainActivity.joy_count;
    private int happy_count = MainActivity.happy_count;
    private int anger_count = MainActivity.anger_count;
    private int sad_count = MainActivity.sad_count;
    private int total_count = love_count + joy_count + happy_count + anger_count + sad_count;
    public static final String SHARED_PREFS = "sharedPrefs";





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.statsNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(StatsPage.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_stats:
                        break;
                }
                return false;
            }
        });

        TextView tv1 = (TextView)findViewById(R.id.love_count);
        TextView tv2 = (TextView)findViewById(R.id.joy_count);
        TextView tv3 = (TextView)findViewById(R.id.happy_count);
        TextView tv4 = (TextView)findViewById(R.id.anger_count);
        TextView tv5 = (TextView)findViewById(R.id.sad_count);

        tv1.setText(Integer.toString(love_count));
        tv2.setText(Integer.toString(joy_count));
        tv3.setText(Integer.toString(happy_count));
        tv4.setText(Integer.toString(anger_count));
        tv5.setText(Integer.toString(sad_count));


    }

    public void reset_button(View view){
        save_data();

        TextView tv1 = (TextView)findViewById(R.id.love_count);
        TextView tv2 = (TextView)findViewById(R.id.joy_count);
        TextView tv3 = (TextView)findViewById(R.id.happy_count);
        TextView tv4 = (TextView)findViewById(R.id.anger_count);
        TextView tv5 = (TextView)findViewById(R.id.sad_count);
        tv1.setText("0");
        tv2.setText("0");
        tv3.setText("0");
        tv4.setText("0");
        tv5.setText("0");
    }

    public void save_data(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("LOVE_COUNT", 0);
        editor.putInt("JOY_COUNT", 0);
        editor.putInt("HAPPY_COUNT", 0);
        editor.putInt("ANGER_COUNT", 0);
        editor.putInt("SAD_COUNT", 0);

        editor.apply();

        Toast.makeText(this, "Counts Reset!", Toast.LENGTH_SHORT).show();
    }

}
