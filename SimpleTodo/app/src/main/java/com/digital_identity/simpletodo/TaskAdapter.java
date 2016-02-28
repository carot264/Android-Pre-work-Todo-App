/**
 * Created by NamLe on 2/16/2016.
 */
package com.digital_identity.simpletodo;
import android.app.Activity;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;


class Task {
    private String name;
    private int isFinish;
    public Task(String name, int isFinish) {
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
    public int isFinish() {
        return isFinish;
    }
    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }
}

public class TaskAdapter extends ArrayAdapter<Task>{

    private List<Task> taskList;
    private Context context;

    CustomButtonEditListener customButtonEditListener;
    CustomButtonDeleteListener customButtonDeleteListener;


    public void setCustomButtonEditListener(CustomButtonEditListener customButtonListner)
    {
        this.customButtonEditListener = customButtonListner;
    }
    public void setCustomButtonDeleteListener(CustomButtonDeleteListener customButtonListner)
    {
        this.customButtonDeleteListener = customButtonListner;
    }

    public TaskAdapter(Context context,List<Task> tasks) {
        super(context, 0, tasks);
        this.taskList = tasks;
        this.context = context;
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
        public void setChecked(boolean isDone){
            chkIsFinish.setChecked(isDone);
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder ;
        boolean isDone = false;

        //LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Task t = taskList.get(position);
        if (t.isFinish() == 1)
            isDone = true;
        // inflate UI from XML file
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.activity_listview_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            holder.chkIsFinish.setChecked(isDone);
            // set tag for holder
            convertView.setTag(holder);
         }else{
             holder = (ViewHolder)convertView.getTag();
         }

        holder.chkIsFinish.setOnCheckedChangeListener((MainActivity) context);
        holder.chkIsFinish.setTag(position);// This line is important.

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
        return convertView;
        }


}

