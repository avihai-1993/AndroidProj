package com.example.finalproject.DB;

import java.util.ArrayList;

public interface OrgenazerInteface {

    void OnGetOptions(ArrayList<String> optionWorkers);
    void OnGetNWAssignment(ArrayList<String> nextWeekCurrentAssignment);
    void OnAddedToNWAssigment(String worker);
}
