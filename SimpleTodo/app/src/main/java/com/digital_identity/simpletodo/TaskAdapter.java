/**
 * Created by NamLe on 2/16/2016.
 */
package com.digital_identity.simpletodo;
import android.app.Activity;
import android.content.Context;

import java.io.Serializable;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


class Task {

    private String name;
    private boolean isFinish = false;
    //private int rowNumber;

    public Task(String name, boolean isFinish) {
        super();
        this.name = name;
        this.isFinish = isFinish;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isFinish() {
        return isFinish;
    }
    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }
   }

public class TaskAdapter extends ArrayAdapter<Task>{

    private List<Task> taskList;
    private Activity activity;
    private int selectedPosition = -1;
    CustomButtonEditListener customButtonEditListener;
    CustomButtonDeleteListener customButtonDeleteListener;


    public void setCustomButtonEditListener(CustomButtonEditListener customButtonListner)
    {
        this.customButtonEditListener = customButtonListner;
    }
    public void CustomButtonDeleteListener(CustomButtonDeleteListener customButtonListner)
    {
        this.customButtonDeleteListener = customButtonListner;
    }

    public TaskAdapter(Activity context, int resource, List<Task> tasks) {
        super(context, resource, tasks);
        this.taskList = tasks;
        this.activity = context;
    }

    private static class ViewHolder {
        public TextView txtTaskName;
        public CheckBox chkIsFinish;
        public ImageButton btnEdit;
        public ImageButton btnDelete;
        public ViewHolder(View v) {
            txtTaskName = (TextView) v.findViewById(R.id.name);
            chkIsFinish = (CheckBox) v.findViewById(R.id.check);
            btnEdit = (ImageButton) v.findViewById(R.id.btnEdit);
            btnDelete = (ImageButton) v.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.activity_listview_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        holder.chkIsFinish.setTag(position); // This line is important.
        holder.txtTaskName.setText(getItem(position).getName());
        holder.btnEdit.setTag(position);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customButtonEditListener != null) {
                    customButtonEditListener.onButtonEditClickListener(position, holder.txtTaskName);
                }
            }
        });

        holder.btnDelete.setTag(position);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customButtonDeleteListener != null) {
                    customButtonDeleteListener.onButtonDeleteClickListener(position);
                }
            }
        });
        if (position == selectedPosition) {
            holder.chkIsFinish.setChecked(true);
        } else holder.chkIsFinish.setChecked(false);



        return convertView;
    }


}

