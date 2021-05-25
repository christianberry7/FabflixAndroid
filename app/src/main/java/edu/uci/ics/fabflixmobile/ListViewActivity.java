package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewActivity extends Activity {
    public static final String SINGLE_MOVIE = "";

    private final String host = "18.222.142.175";
    private final String port = "8443";
    private final String domain = "fabflix";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    private String page;
    private String total_pages;
    public static final String JSON_DATA = "";
    private String query = "";
    public static int internal = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        // TODO: this should be retrieved from the backend server
        Intent intent = getIntent();
        String[] json_data;

        json_data = intent.getStringArrayExtra(ListViewActivity.JSON_DATA);
        query = json_data[0];
        final ArrayList<Movie> movies = new ArrayList<>();
        try{
            JSONArray arr = new JSONArray(json_data[1]);

            for (int i = 0; i < arr.length() - 1; i++)
            {
                String movie_id = arr.getJSONObject(i).getString("movie_id");
                String movie_title = arr.getJSONObject(i).getString("movie_title");
                String year = arr.getJSONObject(i).getString("movie_year");
                String director = arr.getJSONObject(i).getString("movie_director");
                String actors = arr.getJSONObject(i).getString("actors");
                String genres = arr.getJSONObject(i).getString("genres");
                movies.add(new Movie(movie_title, (short) Integer.parseInt(year), movie_id, director, actors, genres));
                // grab current page and make put it in a session variable to keep track of the page
                // when we click "next page" send a post request with current page
            }
            total_pages = arr.getJSONObject(arr.length() - 1).getString("numPages");
            page = arr.getJSONObject(arr.length() - 1).getString("page");
            System.out.println("Total Pages: " + total_pages);
            System.out.println("Current page: " + page);

        }
        catch (JSONException j){
            System.out.println(j);
        }
//        movies.add(new Movie("The Terminal", (short) 2004));
//        movies.add(new Movie("The Final Season", (short) 2007));



        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);




        ListView listView = findViewById(R.id.list);
        TextView page_disp = findViewById(R.id.pageNum);
        if (Integer.parseInt(total_pages) == 0){
            page_disp.setText("No Results");
        }
        else{
            page_disp.setText("Page " + page + " of " + total_pages);
        }
        Button next = findViewById(R.id.next);
        Button prev = findViewById(R.id.prev);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(view -> goBack());
        if (Integer.parseInt(page) >= Integer.parseInt(total_pages)){
            next.setClickable(false);
            next.setVisibility(View.INVISIBLE);

        }
        else{
            next.setOnClickListener(view -> goNext());
        }
        if (Integer.parseInt(page) == 1){
            prev.setClickable(false);
            prev.setVisibility(View.INVISIBLE);

        }
        else{
            prev.setOnClickListener(view -> goPrev());
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = movies.get(position);
            String movieId = movie.getId();
            Intent singleMoviePage = new Intent(this,SingleMovieActivity.class);
            singleMoviePage.putExtra(SINGLE_MOVIE, movieId);
            startActivity(singleMoviePage);
        });

    }

    public void goNext() {
        if (Integer.parseInt(page) >= Integer.parseInt(total_pages)){
            return;
        }
        // TODO: this should be retrieved from the backend server
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/android_movies",
                response -> {
                    this.internal = 1;
                    Intent intent = new Intent(this, ListViewActivity.class);
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    Log.d("here is the json: ", response);
                    String[] pass = new String[2];
                    pass[0] = query;
                    pass[1] = response;
                    intent.putExtra(JSON_DATA, pass);
                    startActivity(intent);

                },
                error -> {
                    // error
                    Log.d("android_movies.error", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("stitle", query);
                params.put("Page_Number", (Integer.parseInt(page) + 1) + "");
                return params;
            }
        };

        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);

    }

    public void goPrev() {
        if(Integer.parseInt(page) == 1){
            return;
        }
        // TODO: this should be retrieved from the backend server
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/android_movies",
                response -> {
                    this.internal = 1;
                    Intent intent = new Intent(this, ListViewActivity.class);
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    Log.d("here is the json: ", response);
                    String[] pass = new String[2];
                    pass[0] = query;
                    pass[1] = response;
                    intent.putExtra(JSON_DATA, pass);
                    startActivity(intent);

                },
                error -> {
                    // error
                    Log.d("android_movies.error", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("stitle", query);
                params.put("Page_Number", (Integer.parseInt(page) - 1) + "");
                return params;
            }
        };

        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);

    }

    public void goBack() {
        Intent listPage = new Intent(this, SearchActivity.class);
        startActivity(listPage);
    }
}


// add some move functions to A: do a post request on a 'next page' button click and B: Links for single movie pages.