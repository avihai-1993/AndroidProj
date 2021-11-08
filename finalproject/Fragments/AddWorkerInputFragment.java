package com.example.finalproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;


public class AddWorkerInputFragment extends DialogFragment {



    private OnDone onDone;
    private TextView textView;
    private EditText nameEditText;
    private EditText idPasswordEditText;
    private Button donebutton;
    public AddWorkerInputFragment(){


    }

    public void setOnDone(OnDone onDone) {
        this.onDone = onDone;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.add_worker_fragment, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_title_promt);
        nameEditText = (EditText) view.findViewById(R.id.worker_name_input);
        idPasswordEditText = (EditText) view.findViewById(R.id.worker_idpassword_input);
        donebutton = (Button) view.findViewById(R.id.doneBtn);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDone.onDone(nameEditText.getText().toString()+","+idPasswordEditText.getText().toString());

            }
        });
        return view;
    }


    public void setOnDone(View.OnClickListener onClickListener) {
    }
}
