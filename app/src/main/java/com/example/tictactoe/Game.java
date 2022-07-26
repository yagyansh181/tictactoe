package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Game extends AppCompatActivity {

    private static Tictactoeboard Tictactoeboard;
    private Button playAgainButton;
    private Button homeButton;
    private TextView playerTurn;
    private String[] playerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        playAgainButton = findViewById(R.id.button4);
        homeButton = findViewById(R.id.button3);
        playerTurn = findViewById(R.id.textView2);

        playAgainButton.setVisibility(View.GONE);
        homeButton.setVisibility(View.GONE);

      Tictactoeboard = findViewById(R.id.tictactoeboard);

        playerNames = getIntent().getStringArrayExtra("PLAYER_NAMES");

        if (playerNames != null) {
            playerTurn.setText((playerNames[0] + "'s turn"));
        }

      Tictactoeboard.setUpGame(playAgainButton, homeButton, playerTurn, playerNames);
    }

    public static void playAgainPress(View view){
        Tictactoeboard.resetGame();
    Tictactoeboard.invalidate();
    }

    public void homePress(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}