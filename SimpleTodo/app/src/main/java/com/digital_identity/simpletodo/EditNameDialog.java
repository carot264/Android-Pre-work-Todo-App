package com.digital_identity.simpletodo;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
/**
 * Created by namln on 2/28/2016.
 */
public class EditNameDialog extends DialogFragment {

    private EditText mEditText;
    private Button btnSave;
    private String taskName;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }
    public EditNameDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }
    public static EditNameDialog newInstance(String title, String oldName) {
        EditNameDialog frag = new EditNameDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("taskName",oldName);
        frag.setArguments(args);
        return frag;
    }
    EditNameDialogListener activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_edit_item, container);
        activity = (EditNameDialogListener) getActivity();
        btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               activity.onFinishEditDialog(mEditText.getText().toString());
               getDialog().dismiss();
           }
       });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.editText);
        // Fetch arguments from bundle and set title, taskName will be edited
        String title = getArguments().getString("title", "Title Name");
        String oldName = getArguments().getString("taskName","old Name" );
        getDialog().setTitle(title);
        mEditText.setText(oldName);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
