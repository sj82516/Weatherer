package com.example.friday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment3 extends Fragment {
    private TextView txt_date;
    private TextView txt_hmd;
    private TextView txt_spd;
    private TextView txt_temp;
    private TextView date;
    private TextView hmd;
    private TextView spd;
    private TextView txt_loc_ch;
    private TextView txt_loc_en;
    private ListView listView;
    private List<Comment> cmtList = new ArrayList<Comment>();
    private CustomListAdapter adapter;
    private EditText edt_cmt;
    private String name = "Android";
    private Button btn_sbt;
    private CallbackManager callbackManager;
    private AccessToken accessToken=null;
    private String UID = null;
    //record current listview
    private int listcount = 0;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
        new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("FB", "onSuccess");
            accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Log.d("FB", profile.getName());
            name = profile.getName();
            UID = profile.getId();
            callWeatherComment(0);
        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onError(FacebookException exception) {
            Log.d("FB", exception.toString());
        }
        });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment3,container,false);
        date = (TextView)view.findViewById(R.id.date);
        hmd = (TextView)view.findViewById(R.id.hmd);
        spd = (TextView)view.findViewById(R.id.spd);
        txt_date = (TextView)view.findViewById(R.id.txt_date);
        txt_hmd = (TextView)view.findViewById(R.id.txt_hmd);
        txt_spd = (TextView)view.findViewById(R.id.txt_spd);
        txt_temp = (TextView)view.findViewById(R.id.txt_temp);
        txt_loc_ch= (TextView)view.findViewById(R.id.txt_loc_ch);
        txt_loc_en = (TextView)view.findViewById(R.id.txt_loc_en);

        adapter = new CustomListAdapter(getActivity(),cmtList);
        listView = (ListView)view.findViewById(R.id.listView);
        //load 5 comment per time
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if((firstVisibleItem + visibleItemCount) ==  totalItemCount)
                {
                    Log.d("Onscroll",Integer.toString(firstVisibleItem)+":"+Integer.toString(visibleItemCount));
                    listcount += 5;
                    callWeatherComment(listcount);
                }
            }
        });
        edt_cmt = (EditText)view.findViewById(R.id.edt_cmt);
        btn_sbt = (Button)view.findViewById(R.id.btn_sbt);

        btn_sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWeatherInfo();
                fbProfile();
            }
        });
        callWeatherInfo();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //listView.setAdapter(adapter);
    }


    //for facebook login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //set upper weather info bar
    private void callWeatherInfo(){
        String url="http://128.199.200.67:8080/weather_cdt/Taipei";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url ,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            Log.d(response.toString(),"Msg:");
                            float temp = Float.parseFloat(response.getString("temp"));
                            String  hmd = response.getString("hmd"),
                                    spd = response.getString("spd"),
                                    date = response.getString("time");
                            temp = temp/10;
                            StringBuilder sb = new StringBuilder();
                            sb.append(temp);
                            String strI = sb.toString();

                            txt_hmd.setText(hmd.substring(0,2)+"%");
                            txt_temp.setText(strI.substring(0,4));
                            txt_spd.setText(spd.substring(0,3)+"M/s");
                            txt_date.setText(date.substring(4,8));
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

    //get the comment data and set up the listview
    private void callWeatherComment(final int item){
        String url2 = "http://128.199.200.67:8080/weather_info/cmt/Taipei";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url2,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(item<response.length()) {
                            for (int i = item; i < item + 5 && i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    Comment cmt = new Comment();
                                    cmt.setCmt(obj.getString("cmt"));
                                    cmt.setName(obj.getString("name"));
                                    cmt.setId(obj.getInt("id"));
                                    cmt.setTime(obj.getString("time").substring(0, 1) + "/" + obj.getString("time").substring(1, 3) + " " + obj.getString("time").substring(3, 5) + ":" + obj.getString("time").substring(5));
                                    cmt.setGood(obj.getInt("good"));
                                    cmtList.add(cmt);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            listView.setAdapter(adapter);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(error.toString(),"Msg2:");
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    //send data back to server
    private void sendWeatherInfo(){
        JSONObject js = new JSONObject();
        try {
            js.put("temp",((MainActivity) getActivity()).getFinal_temp());
            js.put("hmd",((MainActivity) getActivity()).getFinal_hmd());
            js.put("spd",((MainActivity) getActivity()).getFinal_spd());
            js.put("rain",((MainActivity) getActivity()).getFinal_temp());
            js.put("cmt","'"+ edt_cmt.getText() + "'");
            js.put("name", "'" + UID + "'");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        String url="http://128.199.200.67:8080/weather_info/cmt/Taipei";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            Log.e("Err:",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
        callWeatherComment(0);
    }

    //strat facebook login process
    private void fbProfile(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }

}