package com.example.simonsay;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SimonGameActivity extends AppCompatActivity implements View.OnClickListener
{

    private TextView numbeOfRequestTv;
    private  RelativeLayout pannel;
    private int countOfTouch;
    private boolean gameover=false;
    private int m_SleepOfThread=0;
    private boolean doubleBackpresssed=false;
    private SharedPreferences sharedPreferences;
    private GameManager gameManager;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        String level = getIntent().getStringArrayExtra("level")[0];
//        if(level=="Commando")
//        {
            setContentView(R.layout.game4);
//        }
//        else
//        {
//
//        }
        Initialize();
        gameManager.CreateLevel();
        m_SleepOfThread=800;
        gameManager.TurnOfComputer(numbeOfRequestTv);
    }

    public void Initialize()
    {

        numbeOfRequestTv = findViewById(R.id.numberOfRequestTV);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        numbeOfRequestTv.setTypeface(typeface);
        pannel = findViewById(R.id.mainRelativeLayout);
        ImageButton restartgame = findViewById(R.id.restartGame);
        sharedPreferences = getSharedPreferences("details",MODE_PRIVATE);
        gameManager = new GameManager(pannel);
        SetonClickListener();

    }

    public void SetonClickListener()
    {
        for (View viewInTheLayout : gameManager.getListOfChildren())
        {
            if(viewInTheLayout instanceof ImageButton)
            {
                viewInTheLayout.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        if(!gameManager.CheckIdEqualToView(GameManager.Arr.get(countOfTouch),v))
        {
            gameover=true;
            GameOver();
        }

        countOfTouch++;
        if(countOfTouch  == gameManager.getNumberRequest()&&!gameover)
        {
            countOfTouch=0;
            gameManager.TurnOfComputer(numbeOfRequestTv);

        }
    }



    public void PlayMusicOfTouch(String i_color)
    {
        switch (i_color)
        {
            case "Red":
        }
    }

    public void GameOver()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogview = getLayoutInflater().inflate(R.layout.game_over_dialog,null);
        Button saveButton = dialogview.findViewById(R.id.SaveButton);
        final EditText nameOfTheUser = dialogview.findViewById(R.id.inputOfTheUserName);
        final TextView recordOfTheUserTv = dialogview.findViewById(R.id.recordTextOfTheUser);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        ImageButton restartGame = dialogview.findViewById(R.id.restartGame);
        ImageButton homeButton = dialogview.findViewById(R.id.HomeButton);
        ImageView trophyicone = dialogview.findViewById(R.id.TrophyId);
        int temp =sharedPreferences.getInt("record_of_the_user",0);
        gameManager.AddScore(new ContentValues(),eLevel.Level.Easy,Integer.parseInt(numbeOfRequestTv.getText().toString())-1);


        if (sharedPreferences.getInt("record_of_the_user",0)< gameManager.getNumberRequest()-1)
        {
            recordOfTheUserTv.setText("You Have A New Record!! :"+(gameManager.getNumberRequest()-1));
            editor.putInt("record_of_the_user",gameManager.getNumberRequest()-1);
            editor.commit();
            saveButton.setVisibility(View.VISIBLE);
            nameOfTheUser.setVisibility(View.VISIBLE);
            trophyicone.setVisibility(View.VISIBLE);
        }

        else
        {
            recordOfTheUserTv.setText("Your score is :"+(gameManager.getNumberRequest()-1));
            saveButton.setVisibility(View.INVISIBLE);
            nameOfTheUser.setVisibility(View.INVISIBLE);
            trophyicone.setVisibility(View.INVISIBLE);
        }

        saveButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                editor.putString("user_name",nameOfTheUser.getText().toString());
                editor.putInt("record_of_the_user",gameManager.getNumberRequest()-1);

                editor.commit();
                int temp = sharedPreferences.getInt("record_of_the_user",0);

            }
        });

        restartGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        builder.setView(dialogview).show();
    }

    @Override
    public void onBackPressed()
    {
        if(doubleBackpresssed)
        {
            super.onBackPressed();
        }
        else
            {
                doubleBackpresssed=true;
                Toast.makeText(SimonGameActivity.this,"Click again to exit",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        doubleBackpresssed=false;
                    }
                },2000);
        }          }

    }





