package com.example.finalproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;

public class AddShiftInputFragment extends DialogFragment {


    private OnDone onDone;
    private EditText shiftNameEditText;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private Button donebutton;

    public AddShiftInputFragment() { }

    public void setOnDone(OnDone onDone) {
        this.onDone = onDone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.add_shift_input_fragment, container, false);
        shiftNameEditText = (EditText) view.findViewById(R.id.shift_name_input);
        startTimePicker = (TimePicker) view.findViewById(R.id.time_start_input);
        endTimePicker = (TimePicker) view.findViewById(R.id.time_end_input);

        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

        donebutton = (Button) view.findViewById(R.id.doneBtn);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDone.onDone(getAllInput());

            }
        });
        return view;
    }
    private String getAllInput(){
        String start;
        String end;

        if(startTimePicker.getCurrentMinute() < 10 ){
            start = startTimePicker.getCurrentHour().toString()+":"+"0"+startTimePicker.getCurrentMinute().toString();
        }else {
            start = startTimePicker.getCurrentHour().toString()+":"+startTimePicker.getCurrentMinute().toString();
        }


        if(endTimePicker.getCurrentMinute() < 10 ){
            end = endTimePicker.getCurrentHour().toString()+":"+"0"+endTimePicker.getCurrentMinute().toString();
        }else {
            end = endTimePicker.getCurrentHour().toString()+":"+endTimePicker.getCurrentMinute().toString();
        }

         if(shiftNameEditText.getText().toString().compareTo("".trim()) == 0 ){
             shiftNameEditText.setText("NO NAME SHIFT");
         }

        return shiftNameEditText.getText().toString()+","+start+"-"+end;
    }
}
