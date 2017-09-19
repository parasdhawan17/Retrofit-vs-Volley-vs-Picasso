package com.example.parasdhawan.retrofitexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private final static String API_KEY = "648ce826279b307038d2edd2a6cbdf3b";
    ProgressBar progressBarRetro;
    ProgressBar progressBarVolley;
    RecyclerView recyclerView;
    RecyclerView recyclerViewVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBarRetro=(ProgressBar)findViewById(R.id.progressRetro);
        progressBarVolley=(ProgressBar)findViewById(R.id.progressVolley);

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerViewVolley = (RecyclerView) findViewById(R.id.movies_recycler_view_volley);

        loadData();

        Button btReload=(Button)findViewById(R.id.btReload);
        btReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }

    public void loadData(){

        recyclerView.setAdapter(null);
        recyclerViewVolley.setAdapter(null);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService =
                ApiController.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        progressBarRetro.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                progressBarRetro.setVisibility(View.GONE);
                int statusCode = response.code();
                List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                progressBarRetro.setVisibility(View.GONE);
                // Log error here since request failed
                Log.e("Logs", t.toString());
            }
        });



        recyclerViewVolley.setLayoutManager(new LinearLayoutManager(this));

        progressBarVolley.setVisibility(View.VISIBLE);
        final StringRequest request = new StringRequest(Request.Method.GET,
                "http://api.themoviedb.org/3/movie/top_rated?api_key=648ce826279b307038d2edd2a6cbdf3b",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBarVolley.setVisibility(View.GONE);
                        Gson gson=new Gson();
                        MoviesResponse moviesResponse=gson.fromJson(response,MoviesResponse.class);
                        List<Movie> movies = moviesResponse.getResults();
                        recyclerViewVolley.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarVolley.setVisibility(View.GONE);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.photo){
            Intent intent=new Intent(this,ImageCaching.class);;
            MainActivity.this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
