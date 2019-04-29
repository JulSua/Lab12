package edu.illinois.cs.cs125.spring2019.lab12;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab12:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;
    /** List for the grocery list items. */
    private ArrayList<String> itemList;
    /** txt. */
    private TextView txt;
    /** List. */
    private RecyclerView listView;
    /** Item. */
    private String item;
    /** The item that's trying to be added. */
    private TextView inputItem;
    /** The number of items that's trying to be added. */
    private TextView inputNumber;
    /** The variable that'll display if there's an invalid item entered. */
    private TextView invalidItemMessage = findViewById(R.id.text_missingItem);

    /** Sets the item as a string.
     *
     * @param setItem string to set.
     */
    public void setItem(final String setItem) {
        this.item = setItem;
    }

    /** Gets the current item.
     *
     * @return the item.
     */
    public String getItem() {
        return this.item;
    }
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
                String input =
                invalidItemMessage.setVisibility(View.INVISIBLE);
                if (invalidItem()) {
                    invalidItemMessage.setVisibility(View.VISIBLE);
                    return;
                } else {
                    setItem(inputNumber.toString() + " " + inputItem.toString());
                    itemList.add(this.item);
                }
            }
        });
        txt = (TextView) findViewById(R.id.txt);
        listView = (RecyclerView) findViewById(R.id.list_view);

    }
    /** Function that figures out if the item is invalid or not. Returns true if it is invalid.
     *
     * @return true if the item is invalid.
     */
    public boolean invalidItem() {
        inputItem = (TextView) findViewById(R.id.text_enterItem);
        String input = inputItem.toString().trim();
        if (input.length() == 0 || input.equals("Enter Item:")) {
            return true;
        }
        return false;
    }

    /** Returns true if missing a number in the box.
     *
     * @return true if missing a number in the box.
     */
    public boolean missingNumber() {
        inputNumber = (TextView) findViewById(R.id.text_number);
        String value = inputNumber.toString().trim();
        if (value.length() == 0) {
            return true;
        }
        return false;
    }

    /** Returns true if the number entered is invalid.
     *
     * @return true if the number entered is invalid.
     */
    public boolean  invalidNumber() {
        inputNumber = (TextView) findViewById(R.id.text_number);
        String value = inputNumber.toString().trim();
        int number = Integer.parseInt(value);
        if (number <= 0 || number >= 100) {
            return true;
        }
        return false;
    }
    /** Attempts to add an item to the list. */
    public void addItem() {
        invalidItemMessage.setVisibility(View.INVISIBLE);
        missingNumberMessage.setVisibility(View.INVISIBLE);
        invalidNumberMessage.setVisibility(View.INVISIBLE);
        if (invalidItem()) {
            invalidItemMessage.setVisibility(View.VISIBLE);
            return;
        } else if (missingNumber()) {
            missingNumberMessage.setVisibility(View.VISIBLE);
            return;
        } else if (invalidNumber()) {
            invalidNumberMessage.setVisibility(View.VISIBLE);
            return;
        } else {
            setItem(inputNumber.toString() + " " + inputItem.toString());
            ((TextView) findViewById(R.id.txt)).setText(this.item);
            itemList.add(this.item);
        }
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
