package com.example.finalproject.DB;

import com.example.finalproject.Logic.Shift;

public interface ManagerShiftPolicyMangmentInterface extends ShiftPolicyMangmentInterface {

    void  onShiftAdd(Shift shift , String shiftDocId);
}
