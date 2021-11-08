package com.example.minesweeper.Activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minesweeper.Fragments.InputFragment;
import com.example.minesweeper.Fragments.OnDone;
import com.example.minesweeper.Logic.Board;
import com.example.minesweeper.Logic.Record;
import com.example.minesweeper.Logic.Tile;
import com.example.minesweeper.R;
import com.example.minesweeper.Services.SensorServiceListener;
import com.example.minesweeper.Services.SensorsService;
import com.example.minesweeper.Views.TileAdapter;
import com.example.minesweeper.Views.TileView;

import java.util.ArrayList;
import java.util.Random;


public class PlayActivity extends AppCompatActivity implements SensorServiceListener, Animator.AnimatorListener {

    private GridView mGridView;
    private Board mBoard;
    private TileAdapter mTileAdapter;
    private TextView score;
    private Chronometer timer;
    private int level;
    private int countSec;
    private int timesThatTheUserTapOnboard;
    private boolean canContinue ;
    private Record currentGame;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private SensorsService.SensorServiceBinder mBinder;
    private boolean isBound = false;
    private volatile boolean animationIsFinished = false;
    private ImageView animationImageViewMsg;
    private int screenHigth;
    public static final String MESSAGE = "MESSAGE";
    public static final String SCORE = "SCORE";
    public static final String TIME = "TIME";
    public static final long ANIMATION_DURATION = 1000;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //get info from prevus activity
        Intent intent = getIntent();
        int size = intent.getIntExtra(MainActivity.DIFFICULTY, 0);
        int mines = intent.getIntExtra(MainActivity.MINES, 0);
        level = intent.getIntExtra(MainActivity.LEVEL,0);

        //get save and load objects
        sharedPref = getSharedPreferences(getString(R.string.shard),MODE_PRIVATE);
        editor = sharedPref.edit();

        //get higth of screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHigth = displayMetrics.heightPixels;


        //init the animated object
        animationImageViewMsg = (ImageView)findViewById(R.id.endTextMsg);
        animationImageViewMsg.setY(screenHigth + 80);

        //init game settings
        mBoard = new Board(size, mines);
        mTileAdapter = new TileAdapter(this, mBoard);
        mGridView = findViewById(R.id.Minefield);
        score = findViewById(R.id.score);
        timer = (Chronometer) findViewById(R.id.timer);
        mGridView.setNumColumns(size);
        mGridView.setAdapter(mTileAdapter);
        mTileAdapter.notifyDataSetChanged();
        timer.setBase(SystemClock.elapsedRealtime());
        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                countSec++;
            }
        });
        timer.start();

        //reveal tile  --- board tap
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int row = position / mBoard.getBoardSize();
                final int column = position % mBoard.getBoardSize();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mBoard.getTile(row, column).isRevealed() && !mBoard.getTile(row, column).isFlagged()) {
                            timesThatTheUserTapOnboard++;
                            mBoard.revealedTile(row, column);
                            if (mBoard.getTile(row, column).isHasMine()) {
                                updateTile(row,column);
                                onMineStep();
                            }

                        }
                        if (mBoard.isAllBoardRevealed()) {
                            refreshDisplay();
                            onGameFinish();
                        }
                        refreshDisplay();
                    }
                }).start();
                mTileAdapter.notifyDataSetChanged();
            }
        });

        //flag tile
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int row = i / mBoard.getBoardSize();
                final int column = i % mBoard.getBoardSize();
                final Tile tile = mBoard.getTile(row, column);

                if (!tile.isRevealed() && !tile.isFlagged()) {
                    mBoard.flagTile(row, column);
                } else if (tile.isFlagged()) {
                    tile.setFlagged(false);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        refreshDisplay();
                    }
                }).start();

                mTileAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
//other Methods-----------------------------------------------------------------------------------

    private void refreshDisplay(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTiles();
                score.setText(timesThatTheUserTapOnboard + "");

            }
        });

    }

   private void animation(final int idrawble){

       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               animationImageViewMsg.setVisibility(View.VISIBLE);
               animationImageViewMsg.setImageDrawable(getDrawable(idrawble));
               ObjectAnimator animator = ObjectAnimator.ofFloat(animationImageViewMsg,"Y",animationImageViewMsg.getY(),(float) screenHigth/4);
               animator.setDuration(ANIMATION_DURATION);
               animator.addListener(PlayActivity.this);
               animator.start();
           }
       });
   }
    //lose the game
    public void onMineStep() {
        timer.stop();
        mBinder.stopSensors();
        animation(R.drawable.boom);
       while (!animationIsFinished){ }
        passOn(getString(R.string.lost));
    }

    private void checkIfANewRecordInLevelAndUpdateIfNeeded(int level){
        int place ;
        switch (level){
            case 1:
                place = checkNewEasyRecordInPlace();
                if(place == -1){
                   return;
                }else {
                    getNameFromUser(getString(R.string.congrats));
                     while (!canContinue){}
                    updateRecordInLevelOne(place);
                }
                break;
            case 2:
                place = checkNewMediumRecordInPlace();
                if(place == -1){
                    return;
                }else
                    {
                    getNameFromUser(getString(R.string.congrats));
                     while (!canContinue){}
                    updateRecordInLevelTwo(place);
                }
                break;
            case 3:
                place = checkNewHardRecordInPlace();
                if(place == -1){
                    return;
                }else {
                    getNameFromUser(getString(R.string.congrats));
                    while (!canContinue){}
                    updateRecordInLevelThree(place);
                }
                break;
        }



    }

    private void updateRecordInLevelOne(int place) {

        switch (place){
            case 1:
                editor.putString(getString(R.string.easy_third_place),sharedPref.getString(getString(R.string.easy_second_place),getString(R.string.defult)));
                editor.putString(getString(R.string.easy_second_place),sharedPref.getString(getString(R.string.easy_first_place),getString(R.string.defult)));
                editor.putString(getString(R.string.easy_first_place),currentGame.toString());
                break;
            case 2:
                editor.putString(getString(R.string.easy_third_place),sharedPref.getString(getString(R.string.easy_second_place),getString(R.string.defult)));
                editor.putString(getString(R.string.easy_second_place),currentGame.toString());
                break;
            case 3:
                editor.putString(getString(R.string.easy_third_place),currentGame.toString());
                break;
        }
        editor.commit();

    }
    private void updateRecordInLevelTwo(int place) {
        switch (place){
            case 1:

                editor.putString(getString(R.string.medium_third_place),sharedPref.getString(getString(R.string.medium_second_place),getString(R.string.defult)));
                editor.putString(getString(R.string.medium_second_place),sharedPref.getString(getString(R.string.medium_first_place),getString(R.string.defult)));
                editor.putString(getString(R.string.medium_first_place),currentGame.toString());
                break;
            case 2:
                editor.putString(getString(R.string.medium_third_place),sharedPref.getString(getString(R.string.medium_second_place),getString(R.string.defult)));
                editor.putString(getString(R.string.medium_second_place),currentGame.toString());
                break;
            case 3:
                editor.putString(getString(R.string.medium_third_place),currentGame.toString());
                break;
        }
        editor.apply();

    }
    private void updateRecordInLevelThree(int place) {
        switch (place){
            case 1:
                editor.putString(getString(R.string.hard_third_place),sharedPref.getString(getString(R.string.hard_second_place),getString(R.string.defult)));
                editor.putString(getString(R.string.hard_second_place),sharedPref.getString(getString(R.string.hard_first_place),getString(R.string.defult)));
                editor.putString(getString(R.string.hard_first_place),currentGame.toString());
                break;
            case 2:
                editor.putString(getString(R.string.hard_third_place),sharedPref.getString(getString(R.string.hard_second_place),getString(R.string.defult)));
                editor.putString(getString(R.string.hard_second_place),currentGame.toString());
                break;
            case 3:
                editor.putString(getString(R.string.hard_third_place),currentGame.toString());
                break;
        }
        editor.apply();
    }

    private int checkNewEasyRecordInPlace() {
       String first = sharedPref.getString(getString(R.string.easy_first_place),getString(R.string.defult));
       String second = sharedPref.getString(getString(R.string.easy_second_place),getString(R.string.defult));
       String third = sharedPref.getString(getString(R.string.easy_third_place),getString(R.string.defult));
       if (first.equals("null") || currentGame.compareTo(new Record(first)) >= 0){
          return 1;
       }
        if (second.equals("null") || currentGame.compareTo(new Record(second)) >= 0){
            return 2;
        }
        if (third.equals("null") || currentGame.compareTo(new Record(third)) >= 0){
            return 3;
        }
        return -1;
    }
    private int checkNewMediumRecordInPlace() {
        String first = sharedPref.getString(getString(R.string.medium_first_place),getString(R.string.defult));
        String second = sharedPref.getString(getString(R.string.medium_second_place),getString(R.string.defult));
        String third = sharedPref.getString(getString(R.string.medium_third_place),getString(R.string.defult));
        if (first.equals("null") || currentGame.compareTo(new Record(first)) >= 0){
            return 1;
        }
        if (second.equals("null") || currentGame.compareTo(new Record(second)) >= 0){
            return 2;
        }
        if (third.equals("null") || currentGame.compareTo(new Record(third)) >= 0){
            return 3;
        }
        return -1;
    }
    private int checkNewHardRecordInPlace() {
        String first = sharedPref.getString(getString(R.string.hard_first_place),getString(R.string.defult));
        String second = sharedPref.getString(getString(R.string.hard_second_place),getString(R.string.defult));
        String third = sharedPref.getString(getString(R.string.hard_third_place),getString(R.string.defult));
        if (first.equals("null") || currentGame.compareTo(new Record(first)) >= 0){
            return 1;
        }
        if (second.equals("null") || currentGame.compareTo(new Record(second)) >= 0){
            return 2;
        }
        if (third.equals("null") || currentGame.compareTo(new Record(third)) >= 0){
            return 3;
        }
        return -1;

    }

 //get user name with promte
    private void getNameFromUser(String promt) {

        final InputFragment inputFragment = new InputFragment(promt);
        inputFragment.setOnDone(new OnDone() {
            @Override
            public void onDone(String input) {
                currentGame.setName(input);
                canContinue = true;
                inputFragment.dismiss();
            }
        });
        inputFragment.show(getSupportFragmentManager(),"");

    }

    //win the game
    public void onGameFinish() {
        timer.stop();
        mBinder.stopSensors();
        currentGame = new Record("",timesThatTheUserTapOnboard,timer.getText().toString(),level+"",countSec-1);
        animation(R.drawable.trophy);
        while (!animationIsFinished){}
        checkIfANewRecordInLevelAndUpdateIfNeeded(level);
        passOn(getString(R.string.won));
    }

    private void updateTile(final int r ,final int c){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBoard.getTile(r,c).notifyTileView();
            }
        });


    }


    //pass on to the end screen
    public void passOn(String message) {
        Intent intent = new Intent(PlayActivity.this, EndActivity.class);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(SCORE, timesThatTheUserTapOnboard+"");
        intent.putExtra(TIME, timer.getText());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //update tile views
    public void updateTiles() {
        for (int i = 0; i < mBoard.getBoardSize() * mBoard.getBoardSize(); i++) {
            int x = i / mBoard.getBoardSize();
            int y = i % mBoard.getBoardSize();
            Tile tile = mBoard.getTile(x, y);
            tile.notifyTileView();
        }
    }

    //service handling
    @Override
    public void alarmStateChanged(ALARM_STATE state) {
       mBinder.stopSensors();
       punishment();
       mBinder.startSensors();
    }

    private int getInt(int id){
        return Integer.parseInt(getString(id));
    }

    private void punishment() {
        for (int i = 0; i <mBoard.getBoardSize() ; i++) {
            for (int j = 0; j <mBoard.getBoardSize() ; j++) {
                if(mBoard.getTile(i,j).isRevealed()){
                    if(chanseForApplyPunishment(getInt(R.integer.Want_For_Hiding),getInt(R.integer.Found_For_Hiding))){
                        mBoard.getTile(i,j).setRevealed(false);
                        if(chanseForApplyPunishment(getInt(R.integer.Want_For_Add_Mine),getInt(R.integer.Found_For_Add_Mine))){
                            mBoard.getTile(i,j).setHasMine(true);
                            mBoard.increamentNumOfMines();
                        }
                    }
                }

            }
        }

        mBoard.resetBoardAdjMineCount();
        mBoard.calNewBoardFromMines();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTiles();
            }
        });
    }

    private boolean chanseForApplyPunishment(int numWanted , int found ) {
        boolean [] ratio = new boolean[found];
        ArrayList<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < found ; i++) {
            indexs.add(i);
        }
        while (numWanted != 0){
            int randomIndex = new Random().nextInt(found-1);
            ratio[randomIndex] = true;
            indexs.remove(randomIndex);
            found--;
            numWanted--;
        }

        return ratio[new Random().nextInt(found-1)];
    }

    //life cycele for service control
    @Override
    protected void onResume() {
        super.onResume();

        if(isBound) {
            mBinder.startSensors();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if(isBound) {
            mBinder.stopSensors();
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SensorsService.class);
        Log.d("On start", "binding to service...");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }
    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }

    }
    //for service Control
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Service Connection", "bound to service");
            mBinder = (SensorsService.SensorServiceBinder) service;
            mBinder.registerListener(PlayActivity.this);
            Log.d("Service Connection", "registered as listener");
            isBound = true;
            mBinder.startSensors();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            isBound = false;
        }
    };
    //animation control
    @Override
    public void onAnimationStart(Animator animation) {
        animationIsFinished = false;
    }
    @Override
    public void onAnimationEnd(Animator animation) {
        animationIsFinished = true;
    }
    @Override
    public void onAnimationCancel(Animator animation) {

    }
    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}


