package com.example.minesweeper.Fragments;

import android.content.DialogInterface;
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

import com.example.minesweeper.R;

public class InputFragment extends DialogFragment {
    private String promet;
    private OnDone onDone;
    private TextView textView;
    private EditText editText;
    private Button donebutton;
    public InputFragment(String promet){
       this.promet = promet;
    }

    public void setOnDone(OnDone onDone) {
        this.onDone = onDone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.input_fragment, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_title_promt);
        editText = (EditText) view.findViewById(R.id.plain_text_input);
        donebutton = (Button) view.findViewById(R.id.doneBtn);
        textView.setText(promet);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDone.onDone(editText.getText().toString());

            }
        });
        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        onDone.onDone(editText.getText().toString());
    }

}
