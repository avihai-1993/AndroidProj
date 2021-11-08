package com.example.minesweeper.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minesweeper.Fragments.MyRecordListFragment;
import com.example.minesweeper.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button startButton;
    private Button watchRecordsButton;
    public static final String DIFFICULTY = "DIFFICULTY";
    public static final String MINES = "MINES";
    public static final String LEVEL = "LEVEL";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedPref = getSharedPreferences(getString(R.string.shard),MODE_PRIVATE);
        editor = sharedPref.edit();

        int level = sharedPref.getInt(getString(R.string.lastLevel), getInt(R.integer.Level_easy_difficulty));
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        resolveLastDifficulty(level);
        startButton = (Button) findViewById(R.id.new_game_button);
        watchRecordsButton = (Button)findViewById(R.id.watch_records);

        watchRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first =getString(R.string.defult);
                String second =getString(R.string.defult);
                String third =getString(R.string.defult);
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButton_easy_difficulty:
                        Toast.makeText(MainActivity.this,"Records :"+1,Toast.LENGTH_LONG).show();
                        first = sharedPref.getString(getString(R.string.easy_first_place),first);
                        second = sharedPref.getString(getString(R.string.easy_second_place),second);
                        third = sharedPref.getString(getString(R.string.easy_third_place),third);
                        break;
                    case R.id.radioButton_medium_difficulty:
                        Toast.makeText(MainActivity.this,"Records :"+2,Toast.LENGTH_LONG).show();
                        first = sharedPref.getString(getString(R.string.medium_first_place),first);
                        second = sharedPref.getString(getString(R.string.medium_second_place),second);
                        third = sharedPref.getString(getString(R.string.medium_third_place),third);
                        break;
                    case R.id.radioButton_hard_difficulty:
                        Toast.makeText(MainActivity.this,"Records :"+3,Toast.LENGTH_LONG).show();
                        first = sharedPref.getString(getString(R.string.hard_first_place),first);
                        second = sharedPref.getString(getString(R.string.hard_second_place),second);
                        third = sharedPref.getString(getString(R.string.hard_third_place),third);
                        break;
                }

                ArrayList <String> records = new  ArrayList<>();
                records.add(first);
                records.add(second);
                records.add(third);
                new MyRecordListFragment(records).show(getSupportFragmentManager(),"");
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_easy_difficulty:
                        editor.putInt(getString(R.string.lastLevel), getInt(R.integer.Level_easy_difficulty));
                        break;
                    case R.id.radioButton_medium_difficulty:
                        editor.putInt(getString(R.string.lastLevel),getInt(R.integer.Level_medium_difficulty));
                        break;
                    case R.id.radioButton_hard_difficulty:
                        editor.putInt(getString(R.string.lastLevel),getInt(R.integer.Level_Hard_difficulty));
                        break;
                }
                editor.commit();
            }
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButton_easy_difficulty:
                        playGame(getInt(R.integer.easy_difficulty_dim),getInt(R.integer.easy_difficulty_mines), getInt(R.integer.Level_easy_difficulty));
                        break;
                    case R.id.radioButton_medium_difficulty:
                        playGame(getInt(R.integer.medium_difficulty_dim),getInt(R.integer.medium_difficulty_mines),getInt(R.integer.Level_medium_difficulty));
                        break;
                    case R.id.radioButton_hard_difficulty:
                        playGame(getInt(R.integer.Hard_difficulty_dim),getInt(R.integer.Hard_difficulty_mines),getInt(R.integer.Level_Hard_difficulty));
                        break;
                }
            }
        });
        
    }

    private int getInt(int id){
        return Integer.parseInt(getString(id));
    }
    //play the minesweeper game
    private void playGame(int difficulty, int mines,int level) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra(DIFFICULTY, difficulty);
        intent.putExtra(MINES, mines);
        intent.putExtra(LEVEL,level);
        startActivity(intent);
        finish();
    }

    private void resolveLastDifficulty(int level) {
        Toast.makeText(this,"Level :" + level,Toast.LENGTH_LONG).show();
        switch(level) {
            case 2:
                radioGroup.check(R.id.radioButton_medium_difficulty);
                break;
            case 3:
                radioGroup.check(R.id.radioButton_hard_difficulty);
                break;
            default:
                radioGroup.check(R.id.radioButton_easy_difficulty);
                break;
        }

        
    }
}

