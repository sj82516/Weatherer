package com.example.friday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.apache.http.HttpConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by zhengyuanjie on 15/6/17.
 */
public class CustomListAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Comment> cmtItems;
    String name="";


    public CustomListAdapter(Context context,List<Comment> cmtItems){
        this.context = context;
        this.cmtItems = cmtItems;
    }
    @Override
    public int getCount(){
        return cmtItems.size();
    }
    @Override
    public Object getItem(int location){
        return cmtItems.get(location);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position,View view, ViewGroup parent){
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_row, parent, false);
        }
        final TextView txt_name = (TextView)view.findViewById(R.id.txt_name);
        TextView txt_time = (TextView)view.findViewById(R.id.txt_time);
        TextView txt_cmt = (TextView)view.findViewById(R.id.txt_cmt);
        NetworkImageView img_icon = (NetworkImageView)view.findViewById(R.id.img_icon);

        final ImageButton btn_good = (ImageButton)view.findViewById(R.id.btn_good);

        final Comment cmt = cmtItems.get(position);
        //txt_name.setText(cmt.getName());
        //String uid = cmt.getName();
        String uid = cmt.getName();

        //get facebook name
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public  void onCompleted(
                            JSONObject object,
                            GraphResponse response){
                        if(object!=null) {
                            try {
                                txt_name.setText(object.getString("name"));
                            } catch (JSONException e) {
                                txt_name.setText("android");
                            }
                        }else{
                            txt_name.setText("android");
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("id",uid);
        request.setParameters(parameters);
        request.executeAsync();

        //load facebook account icon
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        String img_value ="http://graph.facebook.com/"+uid+"/picture?type=large";
        img_icon.setImageUrl(img_value, imageLoader);

        txt_time.setText(cmt.getTime());
        txt_cmt.setText(cmt.getCmt());
        btn_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cmt.getClicked()){
                    btn_good.setImageResource(R.mipmap.heart_not);
                    cmt.setClicked(false);
                }else{
                    btn_good.setImageResource(R.mipmap.heart_agree);
                    cmt.setClicked(true);
                }
            }
        });

        return view;
    }
}
