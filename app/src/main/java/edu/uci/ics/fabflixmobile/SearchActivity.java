package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends Activity {
    private EditText title;
    private TextView message;
    private Button searchButton;
    public static final String JSON_DATA = "";
    public static final String QUERY = "";




    private final String host = "10.0.2.2";
    private final String port = "8080";
    private final String domain = "fabflix";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching);
        title = findViewById(R.id.searchBar);
        message = findViewById(R.id.message);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> searchMovie());
        // TODO: this should be retrieved from the backend server

        searchButton.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    searchButton.performClick();
                }
                return false;
            }
        });
//        final ArrayList<Movie> movies = new ArrayList<>();
//        movies.add(new Movie("The Terminal", (short) 2004));
//        movies.add(new Movie("The Final Season", (short) 2007));
//
//        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);
//
//        ListView listView = findViewById(R.id.list);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            Movie movie = movies.get(position);
//            String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//        });
    }

    public void searchMovie() {
        // TODO: this should be retrieved from the backend server
        message.setText("Searching for: " + title.getText().toString());
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST
        final StringRequest searchRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/android_movies",
                response -> {
                    Intent intent = new Intent(this, ListViewActivity.class);
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                        Log.d("here is the json: ", response);
                        String[] pass = new String[2];
                        pass[0] = title.getText().toString();
                        pass[1] = response;
                        intent.putExtra(JSON_DATA, pass);
                        //intent.putExtra(QUERY, title.getText().toString());

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
                params.put("stitle", title.getText().toString());
                params.put("Page_Number", "1");
                return params;
            }
        };

        // important: queue.add is where the login request is actually sent
        queue.add(searchRequest);

    }
}