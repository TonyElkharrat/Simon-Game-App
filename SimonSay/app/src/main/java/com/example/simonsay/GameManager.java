package com.example.simonsay;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GameManager extends AppCompatActivity
{
    final String m_tableScore = "Record_Score";
    final String CREATE_TABLE_CMD=" CREATE TABLE IF NOT EXISTS "+m_tableScore+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, Level TEXT, Score TEXT);";
    private SQLiteDatabase database;

    public ArrayList<eColors.Color> getArrayOfColors() {
        return m_ArrayOfColors;
    }

    private ArrayList<eColors.Color> m_ArrayOfColors ;
    public ArrayList<View> getListOfChildren()
    {
        return ListOfChildren;
    }
    private  ArrayList<View> ListOfChildren;
    private int m_SleepOfThread=500;
    private MusicService m_musicService;
    private  boolean v_isClickable=false;

    public int getNumberRequest()
    {
        return numberRequest;
    }

    private int numberRequest=0;

    public GameManager(RelativeLayout i_pannel, MusicService i_MusicService)
    {
        m_ArrayOfColors= new ArrayList<eColors.Color>();
        numberRequest=0;
        ListOfChildren= new ArrayList<View>();
        ListOfChildren = getAllChildren(i_pannel);
        m_musicService = i_MusicService;
    }

    public  void CreateLevel()
    {
        m_ArrayOfColors.add(eColors.Color.Blue);
        m_ArrayOfColors.add(eColors.Color.Red);
        m_ArrayOfColors.add(eColors.Color.Blue);
        m_ArrayOfColors.add(eColors.Color.Green);
        m_ArrayOfColors.add(eColors.Color.Green);
        m_ArrayOfColors.add(eColors.Color.Red);
        m_ArrayOfColors.add(eColors.Color.Blue);
        m_ArrayOfColors.add(eColors.Color.Yellow);
        m_ArrayOfColors.add(eColors.Color.Green);
        m_ArrayOfColors.add(eColors.Color.Blue);
        m_ArrayOfColors.add(eColors.Color.Green);


    }

    public void TurnOfComputer( final TextView numbeOfRequestTv)
    {
        numberRequest++;
        numbeOfRequestTv.setText(""+numberRequest);


        new Thread()
        {
            int i=0;
            @Override
            public void run() {
                super.run();
                try{
                    v_isClickable = false;
                    DesactivateOtherTouchWhenComputerArePlaying();

                    Thread.sleep(600);
                    while(i<numberRequest)
                    {


                        m_musicService.PlayMusicOfTouch(m_ArrayOfColors.get(i).toString());
                        ImageButton button = CheckButtonColor(m_ArrayOfColors.get(i),ListOfChildren);
                        button.setPressed(true);
                        Thread.sleep(300);
                        button.setPressed(false);
                        i++;
                    }

                    numbeOfRequestTv.setText("Your Turn");
                    v_isClickable = true;
                    DesactivateOtherTouchWhenComputerArePlaying();
                    Thread.sleep(200);



                }catch (InterruptedException e)
                {

                }
            }
        }.start();

    }

    public  ImageButton CheckButtonColor (eColors.Color i_color, ArrayList<View> i_ListOfAllChildren)
    {

        for (int i = 0; i < i_ListOfAllChildren.size(); i++)
        {
            View childOfRelativeLayout = i_ListOfAllChildren.get(i);

            if (childOfRelativeLayout instanceof ImageButton)
            {
                if(CheckIdEqualToView(i_color,childOfRelativeLayout) )
                {
                    return (ImageButton)childOfRelativeLayout;
                }
            }

        }
        return null;
    }

    public  ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    public  void AddScore (ContentValues i_contentValues, eLevel.Level i_Level , int i_Score)
    {
        String todayString = MakeDate();

        database  = openOrCreateDatabase("dataOfScore",MODE_PRIVATE,null);
        database.execSQL(CREATE_TABLE_CMD);

        i_contentValues.put("Date", todayString);
        i_contentValues.put("Score", i_Score);
        i_contentValues.put("Level", i_Level.toString());
        database.insert(m_tableScore, null, i_contentValues);
    }

    public  boolean CheckIdEqualToView(eColors.Color i_color, View buttonColor)
    {
        boolean v_isEqual = false;
        String id=buttonColor.getResources().getResourceName(buttonColor.getId());
        String nameOfTheId = id.substring(id.indexOf("/")+1);
        String s =i_color.toString();
        if(s.equalsIgnoreCase(nameOfTheId))
        {
            v_isEqual = true;

        }
        return v_isEqual;

    }

    public String MakeDate()
    {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return  formatter.format(todayDate);
    }

    public void DesactivateOtherTouchWhenComputerArePlaying()
    {
        for (View v: ListOfChildren)
        {
            if( v instanceof ImageButton)
            {
                v.setClickable(v_isClickable);
            }

        }
    }

}
