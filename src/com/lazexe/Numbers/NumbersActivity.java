package com.lazexe.Numbers;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NumbersActivity extends Activity implements View.OnLongClickListener, View.OnClickListener {

    private static final String TAG = NumbersActivity.class.getName();
    private TextView inputTextView;
    private DatabaseEngine dbEngine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initControls();
        dbEngine = new DatabaseEngine(getApplicationContext());
    }


    private void initControls() {
        Button oneButton = (Button) findViewById(R.id.one);
        Button twoButton = (Button) findViewById(R.id.two);
        Button threeButton = (Button) findViewById(R.id.three);
        Button fourButton = (Button) findViewById(R.id.four);
        Button fiveButton = (Button) findViewById(R.id.five);
        Button sixButton = (Button) findViewById(R.id.six);
        Button sevenButton = (Button) findViewById(R.id.seven);
        Button eightButton = (Button) findViewById(R.id.eight);
        Button nineButton = (Button) findViewById(R.id.nine);
        Button zeroButton = (Button) findViewById(R.id.zero);

        Button saveButton = (Button) findViewById(R.id.save);
        Button viewButton = (Button) findViewById(R.id.view);
        Button clearButton = (Button) findViewById(R.id.clear);

        inputTextView = (TextView) findViewById(R.id.input_textview);

        oneButton.setOnClickListener(this);
        twoButton.setOnClickListener(this);
        threeButton.setOnClickListener(this);
        fourButton.setOnClickListener(this);
        fiveButton.setOnClickListener(this);
        sixButton.setOnClickListener(this);
        sevenButton.setOnClickListener(this);
        eightButton.setOnClickListener(this);
        nineButton.setOnClickListener(this);
        zeroButton.setOnClickListener(this);

        saveButton.setOnClickListener(this);
        viewButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        clearButton.setOnLongClickListener(this);
    }


    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.clear) {
            inputTextView.setText("");
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;
        switch (view.getId()) {
            case R.id.one:
            case R.id.two:
            case R.id.three:
            case R.id.four:
            case R.id.five:
            case R.id.six:
            case R.id.seven:
            case R.id.eight:
            case R.id.nine:
            case R.id.zero:
                inputTextView.append(clickedButton.getText());
                break;
            case R.id.clear:
                removeLastSymbol();
                break;
            case R.id.view:
                dbEngine.showDigitsDialog(this);
                break;
            case R.id.save:
                dbEngine.saveToDatabaseDigit(inputTextView.getText().toString());
                break;
            default:
                Toast.makeText(this, "Default!", Toast.LENGTH_LONG).show();
                break;
        }
    }



    private void removeLastSymbol() {
        String input = inputTextView.getText().toString();
        String result = removeLastSymbolFromString(input);
        inputTextView.setText(result);
    }

    public static String removeLastSymbolFromString(String input) {
        if (input.length() > 0) {
            input = input.substring(0, input.length() - 1);
        }
        return input;
    }
}
