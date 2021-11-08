package com.example.minesweeper.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.minesweeper.Logic.Record;
import com.example.minesweeper.R;

import java.util.ArrayList;
import java.util.List;


public class MyRecordListFragment extends DialogFragment {

    private ArrayList<String> recordArrayList;

    public MyRecordListFragment(){

    }

    @SuppressLint("ValidFragment")
    public MyRecordListFragment(ArrayList <String> records){
        this.recordArrayList = records;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.record_fragment_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, recordArrayList);
        listView.setAdapter(adapter);
        return view;
    }

}
