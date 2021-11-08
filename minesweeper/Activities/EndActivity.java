package com.example.minesweeper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.minesweeper.R;

public class EndActivity extends AppCompatActivity {

    private TextView gameVerdict;
    private Button mainMenuButton;
    private Button quitButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Intent intent = getIntent();
        String scoreS = intent.getStringExtra(PlayActivity.SCORE);
        String timer = intent.getStringExtra(PlayActivity.TIME);
        String verdict = intent.getStringExtra(PlayActivity.MESSAGE);

        gameVerdict = findViewById(R.id.end_game_verdict);
        gameVerdict.setText(verdict + " with  " + scoreS + "  taps  and time of " + timer);
        mainMenuButton = (Button) findViewById(R.id.end_return_main_menu);

        //return to the main menu to start a new game
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        quitButton = (Button) findViewById(R.id.end_quit_game);

        //quit the app
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndActivity.this.finish();
            }
        });
    }
}
