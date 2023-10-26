package com.example.duckjumpgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GameManager extends AppCompatActivity{
    private DuckPlayer duckPlayer;
    private Handler winHandler = new Handler();
    int screenWidth;
    int screenHeight;
    PlatformManager initialPlatform1;
    PlatformManager initialPlatform2;
    PlatformManager initialPlatform3;
    PlatformManager hiddenPlatform1;
    PlatformManager hiddenPlatform2;
    PlatformManager hiddenPlatform3;
    PlatformManager hiddenPlatform4;
    PlatformManager hiddenPlatform5;
    PlatformManager hiddenPlatform6;
    public int score = 0; // Need way to implement score


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manager);

        // Get the player icon
        ImageView theDuck = findViewById(R.id.theDuck);
        ConstraintLayout background = findViewById(R.id.background);

        // Getting screen width so there is a max on how far left and right the player icon can move
        this.screenWidth = getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = getResources().getDisplayMetrics().heightPixels;

        // Set up the DuckPlayer instance
        duckPlayer = new DuckPlayer(theDuck, screenHeight);

        // Start the bounce animation for the duck player
        duckPlayer.startBounceAnimation();

        // Start the platform animation
        managePlatforms();
        // Start check for win
        winHandler.postDelayed(winChecker, 100);

        // Adding touch listener to move duck
        background.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return onTouchEvent(event);
            }
        });
    }
    public boolean onTouchEvent(MotionEvent event) {
        // Subtract to center duck on pointer
        int newX = (int) event.getRawX() - duckPlayer.getDuckWidth()/2;
        // Getting duck params so we can change them
        ViewGroup.MarginLayoutParams params = duckPlayer.getDuckLayoutParams();
        // Adding the change
        // as long as the new location will be within the screen make the change
        if (newX >= 0 && newX + duckPlayer.getDuckWidth() <= screenWidth) {
            params.leftMargin = newX;
            duckPlayer.setDuckLayoutParams(params);
        }
        return true;
    }



    public void managePlatforms(){
        ImageView platform1 = findViewById(R.id.platform1);
        ImageView platform2 = findViewById(R.id.platform2);
        ImageView platform3 = findViewById(R.id.platform3);

        ImageView TopPlatform1 = findViewById(R.id.platformTop1);
        ImageView TopPlatform2 = findViewById(R.id.platformTop2);
        ImageView TopPlatform3 = findViewById(R.id.platformTop3);
        ImageView TopPlatform4 = findViewById(R.id.platformTop4);
        ImageView TopPlatform5 = findViewById(R.id.platformTop5);
        ImageView TopPlatform6 = findViewById(R.id.platformTop6);

        // These platforms are the ones that start on the screen. Dont want them to respawn on screen so make delay huge

        initialPlatform1 = new PlatformManager(platform1, screenWidth, screenHeight, duckPlayer, 4000, 100000);
        initialPlatform2 = new PlatformManager(platform2, screenWidth, screenHeight, duckPlayer, 3000, 100000);
        initialPlatform3 = new PlatformManager(platform3, screenWidth, screenHeight, duckPlayer, 2000, 100000);

        hiddenPlatform1 = new PlatformManager(TopPlatform1, screenWidth, screenHeight, duckPlayer, 6000, 6000);
        hiddenPlatform2 = new PlatformManager(TopPlatform2, screenWidth, screenHeight, duckPlayer, 5000, 5000);
        hiddenPlatform3 = new PlatformManager(TopPlatform3, screenWidth, screenHeight, duckPlayer, 5500, 5500);
        hiddenPlatform4 = new PlatformManager(TopPlatform4, screenWidth, screenHeight, duckPlayer, 7000, 7000);
        hiddenPlatform5 = new PlatformManager(TopPlatform5, screenWidth, screenHeight, duckPlayer, 10000, 10000);
        hiddenPlatform6 = new PlatformManager(TopPlatform6, screenWidth, screenHeight, duckPlayer, 6000, 6000);

    }

    public void endGame(){
        // Open the winPage when game ends
        Intent intent = new Intent(this, WinPage.class);
        startActivity(intent);
    }


    // Runnable is running every 100 milliseconds checking for game end condition
    // Learned how to use runnable and handlers from examples online
    Runnable winChecker = new Runnable(){
        public void run(){
            // Check for if duck is too low
            if (duckPlayer.getDuckY() >= screenHeight){
                endGame();
                return;
            }
            winHandler.postDelayed(this, 100); //execute again in 100 millis
        }
    };

}
