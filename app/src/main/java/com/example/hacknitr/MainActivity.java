package com.example.hacknitr;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ImageView memeImageView;
    ProgressBar loadImage;
    String current_url = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memeImageView = (ImageView)findViewById(R.id.memeImageView);
        loadImage = (ProgressBar)findViewById(R.id.imageloader);
        Objects.requireNonNull(getSupportActionBar()).hide();
    loadmeme();
    }
    private void loadmeme(){
        current_url ="https://meme-api.herokuapp.com/gimme";
        // Request a string response from the provided URL.
        loadImage.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, current_url, null, response -> {
                    try {
                        String url1 = response.getString("url");
                        Glide.with(MainActivity.this).load(url1).listener(new RequestListener<Drawable>(){

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                loadImage.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                loadImage.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(memeImageView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(MainActivity.this,"Something went wrong", Toast.LENGTH_SHORT).show());
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void shareMeme(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey, checkout this meme I got from Reddit "+current_url+" ");
        Intent chooser = Intent.createChooser(shareIntent,"Share this meme using...");
        startActivity(chooser);
    }

    public void nextMeme(View view) {
        loadmeme();
    }
}