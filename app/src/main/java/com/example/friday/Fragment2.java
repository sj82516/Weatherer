package com.example.friday;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


/**
 * Created by zhengyuanjie on 15/6/14.
 */
public class Fragment2 extends Fragment {
    private ImageButton btn_temp;
    private ImageButton btn_hmd;
    private ImageButton btn_spd;
    private ImageButton btn_rain;
    private int flag=1;
    private Score score;
    private ImageButton btn_plus;
    private ImageButton btn_minus;
    private ImageButton btn_next;
    private int hmd=0,temp=0,spd=0,rain=0;
    private View view;
    private ImageView img;
    private TextView txt_t;
    private TextView txt_h;
    private TextView txt_w;
    private TextView txt_r;
    private TextView txt_avg_title;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.fragment2,container,false);

        btn_hmd = (ImageButton)view.findViewById(R.id.btn_hmd);
        btn_temp = (ImageButton)view.findViewById(R.id.btn_temp);
        btn_spd = (ImageButton)view.findViewById(R.id.btn_spd);
        btn_rain = (ImageButton)view.findViewById(R.id.btn_rain);
        btn_plus = (ImageButton)view.findViewById(R.id.btn_plus);
        btn_minus = (ImageButton)view.findViewById(R.id.btn_minus);
        btn_next = (ImageButton)view.findViewById(R.id.btn_next);
        txt_t = (TextView)view.findViewById(R.id.txt_t);
        txt_h = (TextView)view.findViewById(R.id.txt_h);
        txt_w = (TextView)view.findViewById(R.id.txt_w);
        txt_r = (TextView)view.findViewById(R.id.txt_r);
        txt_avg_title = (TextView)view.findViewById(R.id.txt_avg_title);
        DisplayMetrics metrics;
        metrics = getResources().getDisplayMetrics();
        float TZ_score =txt_t.getTextSize()/metrics.density;
        txt_t.setTextSize(TZ_score);
        txt_h.setTextSize(TZ_score);
        txt_w.setTextSize(TZ_score);
        txt_r.setTextSize(TZ_score);
        float TZ_title = txt_avg_title.getTextSize()/metrics.density;
        txt_avg_title.setTextSize(TZ_title);
        img = (ImageView)view.findViewById(R.id.img_score);
        score = new Score();
        callWeatherAvg();

        flag = 1;
        createBitMap(scoreColor(score.getScr_temp()));
        btn_temp.setBackgroundColor(Color.parseColor("#4F818D"));


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWeatherAvg();
                MainActivity activity = (MainActivity)getActivity();
                activity.btn3.performClick();
            }
        });

        btn_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                createBitMap(scoreColor(score.getScr_temp()));
                btn_temp.setBackgroundColor(Color.parseColor("#4F818D"));
                btn_hmd.setBackgroundColor(Color.parseColor("#4BACC6"));
                btn_spd.setBackgroundColor(Color.parseColor("#4BACC6"));
                btn_rain.setBackgroundColor(Color.parseColor("#4BACC6"));
            }
        });
        btn_hmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                createBitMap(scoreColor(score.getScr_hmd()));
                btn_hmd.setBackgroundColor(Color.parseColor("#4F818D"));
                btn_temp.setBackgroundColor(Color.parseColor("#4BACC6"));
                btn_spd.setBackgroundColor(Color.parseColor("#4BACC6"));
                btn_rain.setBackgroundColor(Color.parseColor("#4BACC6"));
            }
        });
        btn_spd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 3;
                createBitMap(scoreColor(score.getScr_spd()));
                btn_spd.setBackgroundColor(Color.parseColor("#4F818D"));
                btn_hmd.setBackgroundColor(Color.parseColor("#4BACC6"));
                btn_temp.setBackgroundColor(Color.parseColor("#4BACC6"));
                btn_rain.setBackgroundColor(Color.parseColor("#4BACC6"));
            }
        });
        btn_rain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 4;
                createBitMap(scoreColor(score.getScr_rain()));
                btn_rain.setBackgroundColor(Color.parseColor("#4F818D"));
                btn_hmd.setBackgroundColor(Color.parseColor("#4BACC6"));
                btn_spd.setBackgroundColor(Color.parseColor("#4BACC6"));
                btn_temp.setBackgroundColor(Color.parseColor("#4BACC6"));
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(flag){
                    case 1:
                        score.addScr_temp();
                        Log.d(Integer.toString(score.getScr_temp()),"Msg");
                        createBitMap(scoreColor(score.getScr_temp()));
                        break;
                    case 2:
                        score.addScr_hmd();
                        Log.d(Integer.toString(score.getScr_hmd()),"Msg");
                        createBitMap(scoreColor(score.getScr_hmd()));
                        break;
                    case 3:
                        score.addScr_spd();
                        Log.d(Integer.toString(score.getScr_spd()),"Msg");
                        createBitMap(scoreColor(score.getScr_spd()));
                        break;
                    case 4:
                        score.addScr_rain();
                        Log.d(Integer.toString(score.getScr_rain()),"Msg");
                        createBitMap(scoreColor(score.getScr_rain()));
                        break;
                }
            }
        });
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(flag){
                    case 1:
                        score.minusScr_temp();
                        Log.d(Integer.toString(score.getScr_temp()),"Msg");
                        createBitMap(scoreColor(score.getScr_temp()));
                        break;
                    case 2:
                        score.minusScr_hmd();
                        Log.d(Integer.toString(score.getScr_hmd()),"Msg");
                        createBitMap(scoreColor(score.getScr_hmd()));
                        break;
                    case 3:
                        score.minusScr_spd();
                        Log.d(Integer.toString(score.getScr_spd()),"Msg");
                        createBitMap(scoreColor(score.getScr_spd()));
                        break;
                    case 4:
                        score.minusScr_rain();
                        Log.d(Integer.toString(score.getScr_rain()),"Msg");
                        createBitMap(scoreColor(score.getScr_rain()));
                        break;
                }
            }
        });
        return view;
    }
    //draw the score circle
    private void createBitMap(String color) {
        // Create a mutable bitmap
        Bitmap bitMap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_4444);
        bitMap = bitMap.copy(bitMap.getConfig(), true);
        // Construct a canvas with the specified bitmap to draw into
        Canvas canvas = new Canvas(bitMap);
        // Create a new paint with default settings.
        Paint paint = new Paint();
        // smooths out the edges of what is being drawn
        paint.setAntiAlias(true);
        // set color
        paint.setColor(Color.WHITE);
        // set style
        canvas.drawCircle(100, 100, 85, paint);
        int mycolor = Color.parseColor(color);
        paint.setColor(mycolor);
        canvas.drawCircle(100, 100, 70, paint);
        // set on ImageView or any other view
        img.setAdjustViewBounds(true);
        img.setImageBitmap(bitMap);
    }

    //score compare to color
    private String scoreColor(int score){
        String scoreColor="";
        //wind and rain color
        if(flag==4 || flag ==3)
            score = score * -1;
        switch(score){
            case 1:
                scoreColor = "#FFFF66";
                break;
            case 2:
                scoreColor = "#FFCC00";
                break;
            case 3:
                scoreColor = "#FF9933";
                break;
            case 4:
                scoreColor = "#FF6600";
                break;
            case 5:
                scoreColor = "#FF0000";
                break;
            case 0:
                scoreColor = "#60c855";
                break;
            case -1:
                scoreColor = "#C6D9F1";
                break;
            case -2:
                scoreColor = "#8EB4E3";
                break;
            case -3:
                scoreColor = "#558ED5";
                break;
            case -4:
                scoreColor = "#376092";
                break;
            case -5:
                scoreColor = "#17375E";
                break;
            default:
                scoreColor = "#000000";
        }
        return scoreColor;
    }
    //score class
    public class Score{
        private int scr_temp,scr_hmd,scr_rain,scr_spd;
        public Score(){
            this.scr_hmd = ((MainActivity) getActivity()).getFinal_hmd();
            this.scr_rain = ((MainActivity) getActivity()).getFinal_rain();
            this.scr_spd = ((MainActivity) getActivity()).getFinal_spd();
            this.scr_temp = ((MainActivity) getActivity()).getFinal_temp();
        };
        public int getScr_temp(){
            return scr_temp;
        }
        public int getScr_hmd(){
            return scr_hmd;
        }
        public int getScr_rain(){
            return scr_rain;
        }
        public int getScr_spd(){
            return scr_spd;
        }
        public void addScr_temp(){
            this.scr_temp ++;
            if(this.scr_temp >= 5)
                this.scr_temp = 5;
        }
        public void addScr_hmd(){
            this.scr_hmd ++;
            if(this.scr_hmd >= 5)
                this.scr_hmd = 5;
        }
        public void addScr_rain(){
            this.scr_rain ++;
            if(this.scr_rain >= 5)
                this.scr_rain = 5;
        }
        public void addScr_spd(){
            this.scr_spd ++;
            if(this.scr_spd >= 5)
                this.scr_spd = 5;
        }
        public void minusScr_temp(){
            this.scr_temp --;
            if(this.scr_temp <= -5)
                this.scr_temp = -5;
        }
        public void minusScr_hmd(){
            this.scr_hmd --;
            if(this.scr_hmd <= 0)
                this.scr_hmd = 0;

        }
        public void minusScr_rain(){
            this.scr_rain --;
            if(this.scr_rain <= 0)
                this.scr_rain = 0;
        }
        public void minusScr_spd(){
            this.scr_spd --;
            if(this.scr_spd <= 0)
                this.scr_spd = 0;
        }

    }

    //get the weather avg score from server
    private void callWeatherAvg(){
        String url="http://128.199.200.67:8080/weather_info/avg/Taipei";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url ,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {

                            hmd = response.getInt("hmd");
                            spd = response.getInt("spd");
                            rain = response.getInt("rain");
                            temp = response.getInt("temp");
                            showWeatherAvg(temp,hmd,spd,rain);
                            Log.d(response.toString(),"Avg:");

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    private void showWeatherAvg(int t_temp,int t_hmd,int t_spd,int t_rain){
        txt_t.setTextColor(Color.parseColor(scoreColor(t_temp)));
        txt_h.setTextColor(Color.parseColor(scoreColor(t_hmd)));
        txt_w.setTextColor(Color.parseColor(scoreColor(-1*t_spd)));
        txt_r.setTextColor(Color.parseColor(scoreColor(-1*t_rain)));
    }
    //set weather avg score store in the Mainactivity
    private void sendWeatherAvg() {
        ((MainActivity) getActivity()).setFinalScore(score.getScr_temp(), score.getScr_hmd(), score.getScr_spd(), score.getScr_rain());
    }
}