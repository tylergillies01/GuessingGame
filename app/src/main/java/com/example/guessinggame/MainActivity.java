package com.example.guessinggame;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // DECLARE THE VARIABLES FOR THE COMPONENTS ******************
    private EditText txtGuess;
    private Button btnGuess;
    private TextView lblOutput;
    private int theNumber;
    private int range = 100;
    private TextView lblRange;

    public void checkGuess(){
        String guessText = txtGuess.getText().toString();
        String message = "";
        try {
            int guess = Integer.parseInt(guessText);
            if (guess < theNumber)
                message = guess + " is too low. Try again.";
            else if (guess > theNumber)
                message = guess + " is too high. Try again.";
            else{
                message = guess + " is correct! You Win! Let's play again.";
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0) + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("gamesWon", gamesWon);
                editor.apply();
                newGame();
            }
        } catch (Exception e){
            message = "Enter a whole number between 1 and " + range + ".";
        } finally {
            lblOutput.setText(message);
            // CURSOR GOES TO IT AND TEN HIGHLIGHTS IT **********************
            txtGuess.requestFocus();
            txtGuess.selectAll();
        }
    }
    public void newGame() {
        theNumber = (int)(Math.random() * range + 1);
        lblRange.setText("Enter a number between 1 and " + range + ".");
        txtGuess.setText("" + range/2);
        txtGuess.requestFocus();
        txtGuess.selectAll();
    }


    @Override
    //DOES THIS STUFF ON STARTUP
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //IMPORTS THE XML STUFF TO JAVA *****************************
        txtGuess = (EditText) findViewById(R.id.txtGuess);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        lblOutput = (TextView) findViewById(R.id.lblOutput);
        lblRange = (TextView) findViewById(R.id.textView2);

        // ACTIVATE THE SHARED PREFERNECES
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        range = preferences.getInt("range", 100);
        newGame();
        // ^^^^^^

        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGuess();
            }
        });

        txtGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                checkGuess();
                // KEEPS THE KEYBOARD ON THE SCREEN
                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // DO THIS TO MAKE THE MENU ITEMS SHOW *********************
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //THIS IS THE ACTION LISTENERS FOR THE MENU ITEMS*******************
        switch (item.getItemId()){
            case R.id.action_settings:
                final CharSequence[] items = {"1 to 10", "1 to 100", "1 to 1000"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select the Range:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                range = 10;
                                storeRange(10);
                                newGame();
                                break;
                            case 1:
                                range = 100;
                                storeRange(100);
                                newGame();
                                break;
                            case 2:
                                range = 1000;
                                storeRange(1000);
                                newGame();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.action_newgame:
                newGame();
                return true;
            case R.id.action_gamestats:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0);
                AlertDialog statDialog = new AlertDialog.Builder(MainActivity.this).create();
                statDialog.setTitle("Guessing Game Stats");
                statDialog.setMessage("You have won " + gamesWon + " games!");
                statDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                statDialog.show();
                return true;
            case R.id.action_about:
                //CREATES A POPUP ABOUT PAGE
                AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
                aboutDialog.setTitle("About Guessing Game");
                aboutDialog.setMessage("(c) 2020 Tyler Gillies.");
                aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aboutDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void storeRange(int newRange) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("range",newRange);
        editor.apply();

    }
}