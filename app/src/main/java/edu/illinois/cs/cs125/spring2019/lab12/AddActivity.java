package edu.illinois.cs.cs125.spring2019.lab12;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/** For adding items. */
public class AddActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));
    }
    private TextView inputItem;
    private TextView inputNumber;
    private TextView invalidItemMessage = findViewById(R.id.text_missingItem);
    private TextView missingNumberMessage = findViewById(R.id.text_mssingNumber);
    private TextView invalidNumberMessage = findViewById(R.id.text_invalidNumber);
    public boolean invalidItem() {
        inputItem = (TextView) findViewById(R.id.text_enterItem);
        String input = inputItem.toString().trim();
        if (input.length() == 0 || input.equals("Enter Item:")) {
            return true;
        }
        return false;
    }
    public boolean missingNumber() {
        inputNumber = (TextView) findViewById(R.id.text_number);
        String value = inputNumber.toString().trim();
        if (value.length() == 0) {
            return true;
        }
        return false;
    }
    public boolean  invalidNumber() {
        inputNumber = (TextView) findViewById(R.id.text_number);
        String value = inputNumber.toString().trim();
        int number = Integer.parseInt(value);
        if (number <= 0 || number >= 100) {
            return true;
        }
        return false;
    }
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
            String item = inputNumber.toString() + " " + inputItem.toString();
            addToList(item);
        }
        public List<String> addToList(String input) {

        }
    }
}
