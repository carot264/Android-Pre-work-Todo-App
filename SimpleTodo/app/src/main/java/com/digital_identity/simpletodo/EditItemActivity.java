package com.digital_identity.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Console;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditText;
    private String oldname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditText = (EditText)findViewById(R.id.editText);
        oldname = getIntent().getStringExtra("old_name").toString();
        etEditText.setText(oldname);
        etEditText.setSelection(etEditText.getText().length());
    }
    public void onSave(View view) {
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("new_name", etEditText.getText().toString());
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        this.finish(); // closes the activity, pass data to parent
    }

}
