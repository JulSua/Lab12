package edu.illinois.cs.cs125.spring2019.lab12;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import com.android.volley.toolbox.Volley;


import java.util.ArrayList;
import java.util.Collections;

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
    private TextView missingItem;
    /** Missing number text. */
    private TextView missingNumber;
    /**  Missing type text. */
    private TextView missingType;
    /** Contains item text. */
    private TextView containsItem;
    /** Invalid Number text. */
    private TextView invalidNumber;
    /** Enter item box. */
    private EditText enterItem;
    /** Enter number box. */
    private EditText enterNumber;
    /** Enter type box. */
    private EditText enterType;
    /** Minimum number allowed. */
    private int MIN_NUMBER = 0;
    /** Maximum number allowed. */
    private int MAX_NUMBER = 100;
    /**
     * sensor manager.
     */
    private SensorManager sensorManager;
    /**
     * accelerometer.
     */
    private Sensor accelerometer;
    /**
     * shake detector.
     */
    private ShakeDetector shakeDetector;
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
        missingItem = findViewById(R.id.text_missingItem);
        missingType = findViewById(R.id.text_missingType);
        missingNumber = findViewById(R.id.text_missingNumber);
        containsItem = findViewById(R.id.text_containsItem);
        invalidNumber = findViewById(R.id.text_invalidNumber);
        enterItem = findViewById(R.id.enterItem);
        enterNumber = findViewById(R.id.enterNumber);
        enterType = findViewById(R.id.enterType);
        Button settings = (Button) findViewById(R.id.button_trip);
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
        Button clear = findViewById(R.id.button_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                clearList();
            }
        });
        ListView itemListView = (ListView) findViewById(R.id.item_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, itemList);
        itemListView.setAdapter(adapter);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });
    }

    /** Checks if is missing an item.
     *
     * @return true if missing an item.
     */
    public boolean isMissingItem() {
        String stringItem = enterItem.getText().toString();
        if (enterItem.toString().equals(null) || stringItem.trim().equals("")) {
            return true;
        }
        return false;
    }

    /** Checks if is missing a number.
     *
     * @return true if missing a number.
     */
    public boolean isMissingNumber() {
        String stringNumber = enterNumber.getText().toString();
        if (stringNumber.equals(null) || stringNumber.equals("") || stringNumber.equals(" ")) {
            return true;
        }
        return false;
    }

    /** Checks if is missing a type.
     *
     * @return true if missing a type.
     */
    public boolean isMissingType() {
        String stringType = enterType.getText().toString();
        if (enterType.toString().equals(null) || stringType.trim().equals("")) {
            return true;
        }
        return false;
    }

    /** Checks if number entered is invalid.
     *
     * @return true if the number entered is invalid.
     */
    public boolean isNumberInvalid() {
        String value = enterNumber.getText().toString();
        if (value.equals(null) || value.equals("") || value.equals(" ")) {
            return true;
        }
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
        if (isNumberInvalid()) {
            invalidNumber.setVisibility(View.VISIBLE);
            return;
        }
        int value = Integer.parseInt(enterNumber.getText().toString());
        if (value > 1) {
            String input = enterType.getText().toString() + ": " + enterNumber.getText().toString() + " " + enterItem.getText().toString() + "s";
            if (itemList.contains(input)) {
                containsItem.setVisibility(View.VISIBLE);
                return;
            }
            itemList.add(input);
            alphabetizeList(itemList);
        } else {
            String input = enterType.getText().toString() + ": " + enterNumber.getText().toString() + " " + enterItem.getText().toString();
            if (itemList.contains(input)) {
                containsItem.setVisibility(View.VISIBLE);
                return;
            }
            itemList.add(input);
            alphabetizeList(itemList);
        }
        ListView itemListView = (ListView) findViewById(R.id.item_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, itemList);
        itemListView.setAdapter(adapter);
    }
    /** Creates a new list/clear the existing one. */
    public void clearList() {
        itemList = new ArrayList<>();
        ListView itemListView = (ListView) findViewById(R.id.item_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, itemList);
        itemListView.setAdapter(adapter);
    }

    /** Alphabetizes an ArrayList of strings.
     *
     * @param list to alphabetize
     * @return an alphabetized list.
     */
    public void alphabetizeList(final ArrayList<String> list) {
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
    }
    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(shakeDetector);
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeDetector, accelerometer, sensorManager.SENSOR_DELAY_UI);
    }

    /**
     * clears list when shakes twice.
     * @param count number of shakes.
     */
    public void handleShakeEvent(final int count) {
        if (count >= 2) {
            clearList();
        }
    }
}
