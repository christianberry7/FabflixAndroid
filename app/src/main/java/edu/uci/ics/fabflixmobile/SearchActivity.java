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


    private final String host = "18.222.142.175";
    private final String port = "8443";
    private final String domain = "fabflix";
    private final String baseURL = "https://" + host + ":" + port + "/" + domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching);
        title = findViewById(R.id.searchBar);
        message = findViewById(R.id.message);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> searchMovie());
        //EditText edit_txt = (EditText) findViewById(R.id.searchBar);

        title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    searchButton.performClick();
                }
                return false;
            }
        });
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