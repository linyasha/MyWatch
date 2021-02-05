package com.example.home.socialnetwork;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class FbWorkClass {

    Context context;
    ArrayList<String> name, url, id_album;

    public FbWorkClass(Context context, ArrayList<String> name, ArrayList<String> url, ArrayList<String> id_album) {
        this.name = name;
        this.url = url;
        this.id_album = id_album;
        this.context = context;
    }

    void getFBAlbums() {
        FacebookSdk.sdkInitialize(context);
        Bundle params = new Bundle();
        params.putString("fields", "id,name");
        GraphRequest.Callback callback = new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                String albumID = null;
                try {
                    JSONObject json = response.getJSONObject();
                    JSONArray jarray = json.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject oneAlbum = jarray.getJSONObject(i);
                        albumID = oneAlbum.getString("id");
                        id_album.add(albumID);
                        name.add(oneAlbum.getString("name"));
                        Bundle params = new Bundle();
                        params.putBoolean("redirect", false);
                        GraphRequest.Callback gCallback = new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                JSONObject json = response.getJSONObject();
                                try {
                                    JSONObject jjj = json.getJSONObject("data");
                                    url.add(jjj.getString("url"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        final GraphRequest graphRequest = new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/" + albumID + "/picture",
                                params,
                                HttpMethod.GET, gCallback);
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                GraphResponse graphResponse = graphRequest.executeAndWait();
                            }
                        });
                        t.start();
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (
                        JSONException e) {
                    Log.v("ddddd", "something wrong");
                }
            }
        };
        final GraphRequest gRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/albums",
                params,
                HttpMethod.GET, callback);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GraphResponse graphResponse = gRequest.executeAndWait();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void getFBAlbumPhoto(String albumID) {
        FacebookSdk.sdkInitialize(context);
        Bundle params = new Bundle();
        params.putString("fields", "images,name,created_time");
        GraphRequest.Callback gcallback = new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                JSONObject json = response.getJSONObject();
                Log.v("photos",json.toString());
                try {
                    JSONArray jarray = json.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject oneAlbum = jarray.getJSONObject(i);
                        String created_time = oneAlbum.getString("created_time");
                        id_album.add(created_time);
                        String names = oneAlbum.optString("name");
                        name.add(names);
                        JSONArray ss = oneAlbum.getJSONArray("images");
                        for (int j = 0; j < 1; j++) {
                            JSONObject onephoto = ss.getJSONObject(j);
                            String sourse = onephoto.getString("source");
                            url.add(sourse);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        final GraphRequest graphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumID + "/photos",
                params,
                HttpMethod.GET, gcallback);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GraphResponse graphResponse = graphRequest.executeAndWait();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public ArrayList<String> getId_album() {
        return id_album;
    }

}


