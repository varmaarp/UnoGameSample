package com.example.arpit_pc.sampleproject.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        // Get the winner of game from previous activity.
        String winner = getIntent().getStringExtra("Winner");

        // Display the winners name on screen.
        TextView winnerText = findViewById(R.id.winner);
        winnerText.setText(winner);

        // Add event listener for onClick on play again button.
        TextView playAgain = findViewById(R.id.play_again);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take to mainActivity
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
