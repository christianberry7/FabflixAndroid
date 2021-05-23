package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SingleMovieActivity extends Activity{
    private final String host = "10.0.2.2";
    private final String port = "8080";
    private final String domain = "fabflix";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.singlemovie);
        Intent intent = getIntent();
        String movieId = intent.getStringExtra(ListViewActivity.SINGLE_MOVIE);

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/android_single-movie",
                response -> {
                    Log.d("here is the sm json: ", response);

                    //PARSE THE JSON
                    try{
                        JSONArray arr = new JSONArray(response);
                        String movie_title = arr.getJSONObject(0).getString("movie_title");
                        Log.d("movie_title", movie_title);
                        String movie_year = arr.getJSONObject(0).getString("movie_year");
                        Log.d("movie_year", movie_year);
                        String movie_director = arr.getJSONObject(0).getString("movie_director");
                        Log.d("movie_director", movie_director);
                        String stars = "";
                        String genres = "";
                        int c = 0;
                        int d = 0;
                        for(int i = 0; i < arr.length(); i++){
                            if(arr.getJSONObject(i).has("star_name") && !arr.getJSONObject(i).isNull("star_name")){
                                if(d == 0){
                                    stars += arr.getJSONObject(i).getString("star_name");
                                    d++;
                                }else{
                                    stars += ", " + arr.getJSONObject(i).getString("star_name");
                                }
                            }
                            if(arr.getJSONObject(i).has("genre_name") && !arr.getJSONObject(i).isNull("genre_name")){
                                if(c == 0) {
                                    genres+= arr.getJSONObject(i).getString("genre_name");
                                    c++;
                                }else{
                                    genres+= ", " + arr.getJSONObject(i).getString("genre_name");
                                }
                            }
                        }
                        Log.d("stars", stars);
                        Log.d("genres", genres);

                        TextView titleView = findViewById(R.id.title2);
                        TextView yearView = findViewById(R.id.year2);
                        TextView directorView = findViewById(R.id.director2);
                        TextView starsView = findViewById(R.id.stars2);
                        TextView genresView = findViewById(R.id.genres2);

                        titleView.setText(movie_title);
                        yearView.setText("Year: " + movie_year + "");
                        directorView.setText("Director: " + movie_director);
                        starsView.setText("Stars: " + stars);
                        genresView.setText("Genres: " + genres);

                    }catch(JSONException j){
                        System.out.println(j);
                    }
                },
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams () {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("id", movieId);

                return params;
            }
        };
        queue.add(searchRequest);
    }
}

