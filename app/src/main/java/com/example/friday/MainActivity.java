package com.example.friday;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MainActivity extends FragmentActivity {
    private ImageButton btn1;
    public ImageButton btn2;
    public ImageButton btn3;
    private ImageButton btn4;
    //let fragment2 send data back to main activity
    public finalScore finalScore = new finalScore();

    private FragmentManager fm;
    private Fragment1 myFragment1;
    private Fragment2 myFragment2;
    private Fragment3 myFragment3;
    private ImageView index;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (ImageButton)findViewById(R.id.btn_1);
        btn1.setImageResource(R.mipmap.earth_y);
        btn2 = (ImageButton)findViewById(R.id.btn_2);
        btn3 = (ImageButton)findViewById(R.id.btn_3);
        btn4 = (ImageButton)findViewById(R.id.btn_4);
        linearLayout = (LinearLayout)findViewById(R.id.main);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                linearLayout.removeView(index);
            }
        }, 1800);

        myFragment1 = new Fragment1();
        FragmentTransaction trans;
        fm = getSupportFragmentManager();
        trans =  fm.beginTransaction();
        trans.add(R.id.fragmentContainer, myFragment1, "fragment1");
        trans.commit();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFragment1 = new Fragment1();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.fragmentContainer,myFragment1);
                trans.addToBackStack(null);
                trans.commit();
                btn1.setImageResource(R.mipmap.earth_y);
                btn2.setImageResource(R.mipmap.star);
                btn3.setImageResource(R.mipmap.bubble);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFragment2 = new Fragment2();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.fragmentContainer,myFragment2);
                trans.addToBackStack(null);
                trans.commit();
                btn1.setImageResource(R.mipmap.earth);
                btn2.setImageResource(R.mipmap.star_y);
                btn3.setImageResource(R.mipmap.bubble);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFragment3 = new Fragment3();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.fragmentContainer,myFragment3);
                trans.addToBackStack(null);
                trans.commit();
                btn1.setImageResource(R.mipmap.earth);
                btn2.setImageResource(R.mipmap.star);
                btn3.setImageResource(R.mipmap.bubble_y);
            }
        });
    }

    //final score is the user input score in fragment, show up in fragment3 to submit the score to server
    public void setFinalScore(int final_temp,int final_hmd,int final_spd,int final_rain){
        finalScore.setScore(final_temp, final_hmd, final_spd, final_rain);
    }
    public int getFinal_temp(){
        return finalScore.final_temp;
    }
    public int getFinal_hmd(){
        return finalScore.final_hmd;
    }
    public int getFinal_spd(){
        return finalScore.final_spd;
    }
    public int getFinal_rain(){
        return finalScore.final_rain;
    }
    public static class finalScore{
        private int final_temp,final_hmd,final_spd,final_rain;
        public finalScore(){
            this.final_hmd = 0;
            this.final_rain = 0;
            this.final_spd = 0;
            this.final_temp = 0;
        }
        public void setScore(int final_temp,int final_hmd,int final_spd,int final_rain){
            this.final_temp = final_temp;
            this.final_hmd = final_hmd;
            this.final_spd = final_spd;
            this.final_rain = final_rain;
        }
    }
}
