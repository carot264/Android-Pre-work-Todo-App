package com.digital_identity.simpletodo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CustomButtonEditListener ,CustomButtonDeleteListener
 {

     private ArrayList<String> todoItems;
     private ArrayAdapter<String> aToDoAdapter;
     private ArrayList<Task> taskList;

     private ListView lvItems;
     private EditText etEditText;


     private TaskAdapter adapter;
     private int currentItemPosition;
     private boolean isCurrentItemDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        populateArrayItems();
        etEditText = (EditText)findViewById(R.id.etEditText);
       /* lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               // taskList.remove(position);
               // adapter.notifyDataSetChanged();
                //writeItems();
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

      lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditItemActivity.class);
                intent.putExtra("old_name", taskList.get(position).getName());
                currentItemPosition = position;
                startActivityForResult(intent,2);
            }
        });*/
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
    private void readFromJSON(){

        taskList = new ArrayList<Task>();
        File fileDir = getFilesDir();
        FileReader fReader= null;
        try {
          // fReader = new FileReader(fileDir.getPath()+ "todo.json");
          //  JSONArray arr = (JSONArray) new JSONParser().parse(fReader);
           //JSONArray arr = (JSONArray)obj.getJSONArray("data");
            JSONArray arr = new JSONArray(fileDir.getPath() + "todo.json");
            for ( int i = 0; i < arr.length(); i++){
               JSONObject o = arr.getJSONObject(i);
                Task t = new Task(o.getString("name"),o.getBoolean("isDone"));
                taskList.add(t);
            }

        /*} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void writeToJSON(){

        File fileDir = getFilesDir();
        JSONArray objArr = new JSONArray();
        FileWriter fWrite = null;
        try {
            fWrite = new FileWriter(fileDir.getPath()+ "todo.json");

            for(int i = 0; i < taskList.size();i++) {

                JSONObject obj= new JSONObject();
                try {
                    obj.put("isDone", taskList.get(i).isFinish());
                    obj.put("name", taskList.get(i).getName());
                    fWrite.write(obj.toString());

                }catch (JSONException e) {
                    e.printStackTrace();

                }}
            fWrite.flush();
            fWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        //todoItems = new ArrayList<String>();
        //readItems();
        //aToDoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,todoItems);
        readFromJSON();
        adapter = new TaskAdapter(this,R.layout.activity_listview_item,taskList);
        lvItems.setAdapter(adapter);
        adapter.setCustomButtonEditListener(this);
        adapter.CustomButtonDeleteListener(this);

    }
    /*Functions*/
    //Fired  when the button is clicked
    public void onSubmit(View view) {
        //Add item from editText to the listView
        //aToDoAdapter.add(etEditText.getText().toString());
        //etEditText.setText("");
        //writeItems();
        taskList.add(new Task(etEditText.getText().toString(), false));
        etEditText.setText("");
        writeToJSON();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // Extract name value from result extras
            String name = data.getExtras().getString("new_name");
            taskList.set(currentItemPosition,new Task(name,isCurrentItemDone));
            adapter.notifyDataSetChanged();
            writeToJSON();
        }
    }
    @Override
    public void onButtonEditClickListener(int position,TextView text) {
        Intent intent = new Intent(MainActivity.this,EditItemActivity.class);
        intent.putExtra("old_name", taskList.get(position).getName());
        currentItemPosition = position;
        isCurrentItemDone = taskList.get(position).isFinish();
        startActivityForResult(intent, 2);
    }
     @Override
     public void onButtonDeleteClickListener(int position) {
          taskList.remove(position);
          adapter.notifyDataSetChanged();
          writeToJSON();
     }

 }
