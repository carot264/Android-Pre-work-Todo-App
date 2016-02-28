package com.digital_identity.simpletodo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements android.widget.CompoundButton.OnCheckedChangeListener, CustomButtonDeleteListener,
                                                    CustomButtonEditListener,EditNameDialog.EditNameDialogListener
 {
     DatabaseHelper myData;
     private ArrayList<Task> taskList;
     private TaskAdapter adapter;

     private ListView lvItems;
     private EditText etEditText;

     private int currentItemPosition;
     private int isCurrentItemDone;
     private String currentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        etEditText = (EditText)findViewById(R.id.etEditText);
        myData = new DatabaseHelper(this);
        populateArrayItems();
    }
    /*Functions*/
    public void populateArrayItems(){
        //load data from database
        taskList = myData.getTaskList();
        adapter = new TaskAdapter(this,taskList);
        lvItems.setAdapter(adapter);
        adapter.setCustomButtonEditListener(this);
        adapter.setCustomButtonDeleteListener(this);
    }
    private void showEditDialog(String oldName) {
         FragmentManager fm = getSupportFragmentManager();
         EditNameDialog editNameDialog = EditNameDialog.newInstance("Edit Item",oldName);
         editNameDialog.show(fm, "activity_listview_item");
    }
     /*Functions*/

    //Fired  when the button is clicked
    public void onSubmit(View view) {
        Task t = new Task(etEditText.getText().toString(), 0);
        taskList.add(t);
        etEditText.setText("");
        myData.addTask((t));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Extract name value from result extras
            String name = data.getExtras().getString("new_name");
            Task t = new Task(name, isCurrentItemDone);
            taskList.set(currentItemPosition, t);
            myData.updateTask(currentName,t);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onButtonEditClickListener(int position,TextView text) {
        /*Intent intent = new Intent(MainActivity.this,EditItemActivity.class);
        intent.putExtra("old_name", taskList.get(position).getName());
        currentItemPosition = position;
        isCurrentItemDone = taskList.get(position).isFinish();
        currentName = taskList.get(position).getName();
        startActivityForResult(intent, 2);*/
        currentItemPosition = position;
        isCurrentItemDone = taskList.get(position).isFinish();
        currentName = taskList.get(position).getName();
        showEditDialog(taskList.get(position).getName());
    }
     @Override
     public void onButtonDeleteClickListener(int  position) {
         String nameTask = taskList.get(position).getName();
         myData.deleteTask(nameTask);
         taskList.remove(position);
         adapter.notifyDataSetChanged();

     }
     @Override
     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
         int pos = lvItems.getPositionForView(buttonView);
         if (pos != ListView.INVALID_POSITION) {
             Task t = taskList.get(pos);
             int isDone = 0;
             if(isChecked)
                 isDone = 1;
             t.setIsFinish(isDone);
             myData.updateTask(t.getName(),t);
         }
     }
     @Override
     public void onFinishEditDialog(String newName) {
         Task t = new Task(newName, isCurrentItemDone);
         taskList.set(currentItemPosition, t);
         myData.updateTask(currentName,t);
         adapter.notifyDataSetChanged();
     }
 }
