package com.example.finalproject.Views;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkerView extends LinearLayout
{
    private TextView mTextView;

    public WorkerView(Context context) {
        super(context);
        mTextView = new TextView(context);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTextView.setLayoutParams(layoutParams);
        mTextView.setTextSize(15);
        mTextView.setTextColor(Color.BLACK);
        setBackgroundColor(Color.CYAN);
        addView(mTextView);
    }



    public void setText(CharSequence charSequence) {
        mTextView.setText(charSequence);
    }
}
