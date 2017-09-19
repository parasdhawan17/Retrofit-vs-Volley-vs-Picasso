package com.example.parasdhawan.retrofitexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class ImageCaching extends AppCompatActivity {

    ProgressBar progressBarRetro;
    ProgressBar progressBarVolley;
    ImageView ivRetro;
    NetworkImageView ivVolley;
    private static LruBitmapCache lruBitmapCache;
    EditText etCustomUrl;
    TextView tvLoad;
;

    String URL="https://s3.ap-south-1.amazonaws.com/vehicollate-dev/wallpaper.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_caching);

        progressBarRetro = (ProgressBar) findViewById(R.id.progressRetro);
        progressBarVolley = (ProgressBar) findViewById(R.id.progressVolley);
        ivRetro = (ImageView) findViewById(R.id.ivRetro);
        ivVolley = (NetworkImageView) findViewById(R.id.ivVolley);
        etCustomUrl=(EditText)findViewById(R.id.etCustomUrl);
        tvLoad=(TextView)findViewById(R.id.tvLoad);

        loadImages(URL);

        tvLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etCustomUrl.getText().toString().trim().equals("")) {
                    loadImages(etCustomUrl.getText().toString().trim());
                }else {

                }
            }});

    }

    public void loadImages(String url){
        Picasso.with(this)
                .load(url)
                .into(ivRetro);

        RequestQueue queue = Volley.newRequestQueue(this);

        lruBitmapCache = new LruBitmapCache();

        ImageLoader mImageLoader = new ImageLoader(queue,
                lruBitmapCache);
        ivVolley.setImageUrl(url,mImageLoader);
    }
}
