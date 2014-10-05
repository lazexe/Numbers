package com.lazexe.Numbers;

import android.app.Activity;
import android.os.Bundle;

public class ResultActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        DatabaseEngine dbEngine = new DatabaseEngine(getApplicationContext());
        dbEngine.showDigitsDialog(this);
    }
}