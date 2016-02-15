package com.digital_identity.simpletodo;

//import android.support.v7.app.ActionBarActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Int4;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    private EditText etEditText;
    private int currentItemPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText)findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditItemActivity.class);
                intent.putExtra("old_name",todoItems.get(position));
                currentItemPosition = position;
                startActivityForResult(intent,2);

            }
        });

    }

    /*Functions*/
    private void readItems(){
        File fileDir = getFilesDir();
        File file = new File(fileDir,"todo.txt");
        try{
             todoItems = new ArrayList<String>(FileUtils.readLines(file));
        }catch (IOException e){
       }
    }
    private void writeItems(){
        File fileDir = getFilesDir();
        File file = new File(fileDir,"todo.txt");
        try{
           FileUtils.writeLines(file,todoItems);
        }catch (IOException e){

        }

    }
    public void populateArrayItems(){
        todoItems = new ArrayList<String>();
        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,todoItems);
    }
    /*Functions*/
    //Fired  when the button is clicked
    public void onSubmit(View view) {
        //Add item from editText to the listView
        aToDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // Extract name value from result extras
            String name = data.getExtras().getString("new_name");
            todoItems.set(currentItemPosition,name);
            aToDoAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
