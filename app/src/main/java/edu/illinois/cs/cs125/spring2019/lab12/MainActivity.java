package edu.illinois.cs.cs125.spring2019.lab12;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab12:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;
    /** List of items. */
    private ArrayList<String> itemList = new ArrayList<>();
    /** List View.*/
    private ListView listView;
    /** Missing item text. */
    private TextView missingItem = findViewById(R.id.text_missingItem);
    /** Missing number text. */
    private TextView missingNumber = findViewById(R.id.text_missingNumber);
    /**  Missing type text. */
    private TextView missingType = findViewById(R.id.text_missingType);
    /** Contains item text. */
    private TextView containsItem = findViewById(R.id.text_containsItem);
    /** Invalid Number text. */
    private TextView invalidNumber = findViewById(R.id.text_invalidNumber);
    /** Enter item box. */
    private EditText enterItem = findViewById(R.id.enterItem);
    /** Enter number box. */
    private EditText enterNumber = findViewById(R.id.enterNumber);
    /** Enter type box. */
    private EditText enterType = findViewById(R.id.enterType);
    /** Minimum number allowed. */
    private int MIN_NUMBER = 0;
    /** Maximum number allowed. */
    private int MAX_NUMBER = 100;
    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        // startAPICall("192.17.96.8");
        Button settings = (Button) findViewById(R.id.button_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                Intent myIntent = new Intent(view.getContext(), SettingsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
        Button add = (Button) findViewById(R.id.button_add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                add();
            }
        });
        ListView itemListView = (ListView) findViewById(R.id.item_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, itemList);
        itemListView.setAdapter(adapter);
    }
    /** Checks if is missing an item. */
    public boolean isMissingItem() {
        if (enterItem.toString().equals(null) || enterItem.toString().trim().equals("")) {
            return true;
        }
        return false;
    }
    /** Checks if is missing a number. */
    public boolean isMissingNumber() {
        if (enterNumber.toString().equals(null)) {
            return true;
        }
        return false;
    }
    /** Checks if is missing a type. */
    public boolean isMissingType() {
        if (enterType.toString().equals(null) || enterType.toString().trim().equals("")) {
            return true;
        }
        return false;
    }
    /** Checks if is already contained in the list. */
    public boolean containsItem() {
        if (itemList.contains(enterItem.toString())) {
            return true;
        }
        return false;
    }
    /** Checks if number entered is invalid. */
    public boolean isNumberInvalid() {
        String value = enterNumber.toString().trim();
        int number = Integer.parseInt(value);
        if (number <= MIN_NUMBER || number > MAX_NUMBER) {
            return true;
        }
        return false;
    }
    /** Attempts to add the item to the list. Otherwise, makes an error message visible. */
    public void add() {
        missingItem.setVisibility(View.INVISIBLE);
        missingNumber.setVisibility(View.INVISIBLE);
        missingType.setVisibility(View.INVISIBLE);
        containsItem.setVisibility(View.INVISIBLE);
        invalidNumber.setVisibility(View.INVISIBLE);
        if (isMissingItem()) {
            missingItem.setVisibility(View.VISIBLE);
            return;
        }
        if (isMissingNumber()) {
            missingNumber.setVisibility(View.VISIBLE);
            return;
        }
        if (isMissingType()) {
            missingType.setVisibility(View.VISIBLE);
            return;
        }
        if (containsItem()) {
            containsItem.setVisibility(View.VISIBLE);
            return;
        }
        if (isNumberInvalid()) {
            invalidNumber.setVisibility(View.VISIBLE);
            return;
        }
        String input = enterType.toString() + ": " + enterNumber.toString() + " " + enterItem.toString();
        itemList.add(input);
        ListView itemListView = (ListView) findViewById(R.id.item_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, itemList);
        itemListView.setAdapter(adapter);
    }
    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the IP geolocation API.
     *
     * @param ipAddress IP address to look up
     */
    void startAPICall(final String ipAddress) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://ipinfo.io/" + ipAddress + "/json",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the response from our IP geolocation API.
     *
     * @param response response from our IP geolocation API.
     */
    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object
            Log.i(TAG, response.get("hostname").toString());
        } catch (JSONException ignored) { }
    }
}
