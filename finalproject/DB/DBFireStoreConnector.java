package com.example.finalproject.DB;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject.Logic.Shift;
import com.example.finalproject.Logic.ShiftManager;
import com.example.finalproject.Logic.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DBFireStoreConnector {

    private FirebaseFirestore db;
    private EmploeeValidtionListener emploeeValidtionListener;
    private WorkerMangementInterface workerMangementInterface;
    private ShiftPolicyMangmentInterface shiftPolicyMangmentInterface;
    private ManagerShiftPolicyMangmentInterface managerShiftPolicyMangmentInterface;
    private OrgenazerInteface orgenazerInteface;
    private ThisWeekArrangmentInterface thisWeekArrangmentInterface;
    private NextWeekArrangmentInterface nextWeekArrangmentInterface;
    private DBMessageInterface dbMessageInterface;
    private final String DBFS_AllWorkers_collection= "workPlace1/workers/AllWorkers";
    private final String DBFS_AllManagers_collection= "workPlace1/managers/AllManagers";
    private final String DBFS_Days_collection= "workPlace1/WeekShiftPolicy/DaysShiftPolicy";
    private final String DBFS_shifts_collection= "DayShifts";
    private final String DBFS_worker_name_key_field_doc = "name";
    private final String DBFS_worker_password_key_field_doc = "ID";
    private final String DBFS_shift_name_key_field_doc= "name";
    private final String DBFS_shift_timeFrame_key_field_doc= "timeFrame";
    private final String DBFS_NO_OPTIONS_FOR_SHIFT= "NO OPTIONS FOR THIS SHIFT";
    private final String DBFS_shift_NWAssignWorkers_key_field_doc= "NWAssignWorkers";
    private final String DBFS_shift_ThisWeekWorkers_key_field_doc= "ThisWeekWorkers";
    private final String DBFS_shift_WorkersNWOptionsForShift_key_field_doc= "WorkersNWOptionsForShift";
    private final String WORKER_EXISTS_MESSAGE= "There is already a worker with this name and ID";
    private final String FAIL= "Server request failed";
    private final String REMOVE_FROM_OPTIONS_LIST= "You are not in the options list of this shift";
    private final String IN_ALREADY_OPTIONS_LIST= "you are already in the Option list in this shift";
    private final String ADDED_OPTIONS_LIST= "you are added to option list";
    private final String ALREADY_IN_NW_ASSINGMENT= "The Worker Is already in Next Week current Assingnment";
    private final String START_AUTO_ASSIGMENT_MES= "Starting Auto Assigment wait For all days to Finish";
    private final String FINISH_DAY_AUTO_ASSIGMENT= "Finished to do Auto Assiment to ";
    private final String WEEK_CHANGE= "The exchange of arrangements begins For all days to Finish ";
    private final String DAY_CHANGE= "The exchange of arrangements was successful in ";
    private final String NEW_NW_LIST= "The list of workers for next week has been cleared in ";
    private final String [] daysOfweek = new String[]{"Sunday","Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday"};

    //constarctor
    public DBFireStoreConnector(){
        db = FirebaseFirestore.getInstance();

    }

    //interfaces sets
    public void setNextWeekArrangmentInterface(NextWeekArrangmentInterface nextWeekArrangmentInterface) {
        this.nextWeekArrangmentInterface = nextWeekArrangmentInterface;
    }
    public void setManagerShiftPolicyMangmentInterface(ManagerShiftPolicyMangmentInterface managerShiftPolicyMangmentInterface) {
        this.managerShiftPolicyMangmentInterface = managerShiftPolicyMangmentInterface;
        setShiftPolicyMangmentInterface(managerShiftPolicyMangmentInterface);
    }
    public void setThisWeekArrangmentInterface(ThisWeekArrangmentInterface thisWeekArrangmentInterface) {
        this.thisWeekArrangmentInterface = thisWeekArrangmentInterface;
    }
    public void setEmploeeValidtionListener(EmploeeValidtionListener emploeeValidtionListener) {
        this.emploeeValidtionListener = emploeeValidtionListener;
    }
    public void setWorkerMangementInterface(WorkerMangementInterface workerMangementInterface) {
        this.workerMangementInterface = workerMangementInterface;
    }
    public void setShiftPolicyMangmentInterface(ShiftPolicyMangmentInterface shiftPolicyMangmentInterface) {
        this.shiftPolicyMangmentInterface = shiftPolicyMangmentInterface;
    }
    public void setOrgenazerInteface(OrgenazerInteface orgenazerInteface) {
        this.orgenazerInteface = orgenazerInteface;
    }
    public void setDbMessageInterface(DBMessageInterface dbMessageInterface) {
        this.dbMessageInterface = dbMessageInterface;
    }


    //login
    public void valideteWorker(final Worker worker){
        db.collection(DBFS_AllWorkers_collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if(document.getData().get(DBFS_worker_name_key_field_doc).toString().compareTo(worker.getName())==0 &&
                                document.getData().get(DBFS_worker_password_key_field_doc).toString().compareTo(worker.getId())==0)
                        {
                                emploeeValidtionListener.onSuccessWorkerValidtion();
                        }
                    }
                }else
                {

                    emploeeValidtionListener.onFailedWorkerValidtion();

                }
            }
        });
    }
    public  void valideteManeger(final ShiftManager shiftManager){

       db.collection(DBFS_AllManagers_collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
       {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()){
                     for (QueryDocumentSnapshot document : task.getResult()){
                      if(document.getData().containsKey(shiftManager.getId()) ){
                           if(document.getData().get(shiftManager.getId()).toString().compareTo(shiftManager.getName()) == 0 ){
                               emploeeValidtionListener.onSuccessManagerValidtion();

                           }
                       }
                    }
                }else
                    {
                    emploeeValidtionListener.onFailedManagerValidtion();

                }
            }
        });


    }

    //workers mangment methods
    public void addWorker(final Worker worker){
        db.collection(DBFS_AllWorkers_collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult())
                    {

                        if(document.getData().get(DBFS_worker_name_key_field_doc).toString().compareTo(worker.getName())==0 &&
                                document.getData().get(DBFS_worker_password_key_field_doc).toString().compareTo(worker.getId())==0)
                        {

                           dbMessageInterface.onGetBDMessages(WORKER_EXISTS_MESSAGE);
                           return;

                        }


                    }
                    Map<String, Object> workerMap= new HashMap<>();
                    workerMap.put(DBFS_worker_name_key_field_doc,worker.getName());
                    workerMap.put(DBFS_worker_password_key_field_doc,worker.getId());
                    db.collection(DBFS_AllWorkers_collection).add(workerMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            workerMangementInterface.OnWorkerAdded(worker);
                        }
                    });

                }else
                {
                    dbMessageInterface.onGetBDMessages(FAIL);


                }
            }
        });

    }
    public void deleteWorker(final Worker worker){
        db.collection(DBFS_AllWorkers_collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult())
                    {

                        if(document.getData().get(DBFS_worker_name_key_field_doc).toString().compareTo(worker.getName())==0 &&
                                document.getData().get(DBFS_worker_password_key_field_doc).toString().compareTo(worker.getId())==0)
                        {

                            db.document(DBFS_AllWorkers_collection+"/"+document.getId()).delete();
                            return;

                        }


                    }

                }else
                {
                    dbMessageInterface.onGetBDMessages(FAIL);


                }
            }
        });
    }
    public void getAllWorkers(){
        final ArrayList<Worker>workerArrayList = new ArrayList<Worker>();
        db.collection(DBFS_AllWorkers_collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult())
                    {

                        workerArrayList.add(new Worker(document.getData().get(DBFS_worker_name_key_field_doc).toString(),document.getData().get(DBFS_worker_password_key_field_doc).toString()));

                    }
                    workerMangementInterface.OnGetWorkersList(workerArrayList);
                }else
                {
                    dbMessageInterface.onGetBDMessages("FAIL");


                }
            }
        });


    }

    //shifts policy methods
    public void deleteShiftFromPolicy(final String day, final String shiftDocID){

        db.collection(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()){


                            db.document(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection+"/"+shiftDocID).delete();


                }
                else
                {

                   dbMessageInterface.onGetBDMessages(FAIL);

                }
            }
        });

    }
    public void addShiftToPolicy(String day, final Shift shift){
        Map<String, Object> shiftMap= new HashMap<>();
        shiftMap.put(DBFS_shift_name_key_field_doc,shift.getKindOfShift());
        shiftMap.put(DBFS_shift_timeFrame_key_field_doc,shift.getTimeFrame());
        ArrayList<Map<String,Object>> nwa =new ArrayList<Map<String,Object>>();
        ArrayList<Map<String,Object>> tww =new ArrayList<Map<String,Object>>();
        ArrayList<Map<String,Object>> options =new ArrayList<Map<String,Object>>();
        shiftMap.put(DBFS_shift_NWAssignWorkers_key_field_doc,nwa);
        shiftMap.put(DBFS_shift_ThisWeekWorkers_key_field_doc,tww);
        shiftMap.put(DBFS_shift_WorkersNWOptionsForShift_key_field_doc,options);
        db.collection(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection).add(shiftMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    managerShiftPolicyMangmentInterface.onShiftAdd(shift,task.getResult().getId());
                }
                else {
                    dbMessageInterface.onGetBDMessages(FAIL);
                }

            }
        });

    }
    public void getAllShiftPolicyByDay(String day){
        final ArrayList<String> shiftsInday = new ArrayList<>();
        final ArrayList<String> shiftsdocumentIds = new ArrayList<>();
        db.collection(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        if(document.getData().get(DBFS_shift_name_key_field_doc) != null
                                && document.getData().get(DBFS_shift_timeFrame_key_field_doc)!= null)
                        {
                            shiftsInday.add(document.getData().get(DBFS_shift_name_key_field_doc).toString() + "\n" +
                                    document.getData().get(DBFS_shift_timeFrame_key_field_doc).toString()  );
                            shiftsdocumentIds.add(document.getId());
                        }



                    }

                    shiftPolicyMangmentInterface.OnGetAllShiftOfDay(shiftsInday,shiftsdocumentIds);
                }else
                {
                    dbMessageInterface.onGetBDMessages(FAIL);


                }
            }
        });

    }


     //orginazer
    public void getWorkerOptions(String day , String docShiftId){
        final ArrayList<String> dataOfString = new ArrayList<>();
        DocumentReference dref = db.document(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection+"/"+docShiftId);
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>)task.getResult().get(DBFS_shift_WorkersNWOptionsForShift_key_field_doc);
                    for (int i = 0; i <data.size() ; i++) {
                        dataOfString.add(data.get(i).get(DBFS_worker_name_key_field_doc).toString()+"\n"+data.get(i).get(DBFS_worker_password_key_field_doc).toString());

                    }
                    orgenazerInteface.OnGetOptions(dataOfString);
                }else {

                    dbMessageInterface.onGetBDMessages(FAIL);

                }
            }
        });

    }
    public void getNextWeekAssignWorkers(String dayOfShift, String docIdOfShift) {
        final ArrayList<String> dataOfString = new ArrayList<>();
        DocumentReference dref = db.document(DBFS_Days_collection+"/"+dayOfShift+"/"+DBFS_shifts_collection+"/"+docIdOfShift);
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>)task.getResult().get(DBFS_shift_NWAssignWorkers_key_field_doc);
                    for (int i = 0; i <data.size() ; i++) {
                        dataOfString.add(data.get(i).get(DBFS_worker_name_key_field_doc).toString()+"\n"+data.get(i).get(DBFS_worker_password_key_field_doc).toString());

                    }
                    orgenazerInteface.OnGetNWAssignment(dataOfString);
                }else {
                     dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });
    }

    //orginazer
    public void removeWorkerFromNWAssignment(String dayOfShift, String docIdOfShift, final Worker worker)
    {
        final ArrayList<String> dataOfString = new ArrayList<>();
        final DocumentReference dref = db.document(DBFS_Days_collection+"/"+dayOfShift+"/"+DBFS_shifts_collection+"/"+docIdOfShift);
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>)task.getResult().get(DBFS_shift_NWAssignWorkers_key_field_doc);
                    for (int i = 0; i <data.size() ; i++) {
                        if(data.get(i).get(DBFS_worker_name_key_field_doc).toString().compareTo(worker.getName())==0 &&
                                data.get(i).get(DBFS_worker_password_key_field_doc).toString().compareTo(worker.getId())== 0)
                        {
                            data.remove(i);
                            break;

                        }
                    }
                    dref.update(DBFS_shift_NWAssignWorkers_key_field_doc,data);
                }else {
                    dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });


    }


    //orginazer
    public void addWorkerToNWAssignment(String dayOfShift, String docIdOfShift, final Worker worker) {
        final ArrayList<String> dataOfString = new ArrayList<>();
        final DocumentReference dref = db.document(DBFS_Days_collection+"/"+dayOfShift+"/"+DBFS_shifts_collection+"/"+docIdOfShift);
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>)task.getResult().get(DBFS_shift_NWAssignWorkers_key_field_doc);
                    for (int i = 0; i <data.size() ; i++) {
                        if(data.get(i).get(DBFS_worker_name_key_field_doc).toString().compareTo(worker.getName())==0 &&
                                data.get(i).get(DBFS_worker_password_key_field_doc).toString().compareTo(worker.getId())== 0)
                        {
                            dbMessageInterface.onGetBDMessages(ALREADY_IN_NW_ASSINGMENT);
                            return;

                        }
                    }
                    HashMap<String,Object> workerData = new HashMap<>();
                    workerData.put(DBFS_worker_name_key_field_doc,worker.getName());
                    workerData.put(DBFS_worker_password_key_field_doc,worker.getId());
                    data.add(workerData);
                    dref.update(DBFS_shift_NWAssignWorkers_key_field_doc,data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                orgenazerInteface.OnAddedToNWAssigment(worker.getName()+"\n"+worker.getId());
                            }
                            else{
                                dbMessageInterface.onGetBDMessages(FAIL);
                            }
                        }
                    });
                }else {
                    dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });
    }

    //read arregments
    public void getThisWeekArregment() {
        CollectionReference cref = db.collection(DBFS_Days_collection);
        cref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (int i = 0; i < task.getResult().getDocuments().size() ; i++) {
                        getThisWeekArregmentDay(daysOfweek[i]);
                    }
                }
                else {
                    dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });


    }

    private void getThisWeekArregmentDay(final String day) {
        db.collection(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()){
                    String log = "";
                    for (QueryDocumentSnapshot document : task.getResult())
                    {

                        if(document.getData().get(DBFS_shift_name_key_field_doc) != null
                           && document.getData().get(DBFS_shift_timeFrame_key_field_doc)!= null)
                        {

                            sendToInterfacerByDay(day,document.getData().get(DBFS_shift_name_key_field_doc).toString()+"\n"+document.getData().get(DBFS_shift_ThisWeekWorkers_key_field_doc).toString(),false);

                        }


                    }



                }
                else
                {
                    dbMessageInterface.onGetBDMessages(FAIL);

                }
            }
        });
    }

    private void sendToInterfacerByDay(String day, String log,boolean next) {
        switch (day){
            case "Sunday":
                if (next){
                    nextWeekArrangmentInterface.OnGetSunday(log);
                }else {
                    thisWeekArrangmentInterface.OnGetSunday(log);
                }

                break;
            case "Monday":
                if (next){
                    nextWeekArrangmentInterface.OnGetMonday(log);
                }else {
                    thisWeekArrangmentInterface.OnGetMonday(log);
                }
                break;
            case "Tuesday":
                if (next){
                    nextWeekArrangmentInterface.OnGetTuesday(log);
                }else {
                    thisWeekArrangmentInterface.OnGetTuesday(log);
                }
                break;
            case "Wednesday":
                if (next){
                    nextWeekArrangmentInterface.OnGetWednesday(log);
                }else {
                    thisWeekArrangmentInterface.OnGetWednesday(log);
                }
                break;
            case "Thursday":
                if (next){
                    nextWeekArrangmentInterface.OnGetThursday(log);
                }else {
                    thisWeekArrangmentInterface.OnGetThursday(log);
                }
                break;
            case "Friday":
                if (next){
                    nextWeekArrangmentInterface.OnGetFriday(log);
                }else {
                    thisWeekArrangmentInterface.OnGetFriday(log);
                }
                break;
            case "Saturday":
                if (next){
                    nextWeekArrangmentInterface.OnGetsaturday(log);
                }else {
                    thisWeekArrangmentInterface.OnGetsaturday(log);
                }
                break;
            default:
                break;
        }

    }

    public void getNexWeekArregment() {

        CollectionReference cref = db.collection(DBFS_Days_collection);
        cref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (int i = 0; i < task.getResult().getDocuments().size() ; i++) {
                        getNextWeekArregmentDay(daysOfweek[i]);
                    }
                }
                else {
                    dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });
    }

    private void getNextWeekArregmentDay(final String day) {

        db.collection(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful()){
                    String log = "";
                    for (QueryDocumentSnapshot document : task.getResult())
                    {

                        if(document.getData().get(DBFS_shift_name_key_field_doc) != null
                                && document.getData().get(DBFS_shift_timeFrame_key_field_doc)!= null)
                        {

                            sendToInterfacerByDay(day,document.getData().get(DBFS_shift_name_key_field_doc).toString()+"\n"+document.getData().get(DBFS_shift_NWAssignWorkers_key_field_doc).toString(),true);

                        }


                    }



                }
                else
                {
                    dbMessageInterface.onGetBDMessages(FAIL);

                }
            }
        });
    }


    //worker Options
    public void removeWorkerFromNWOptions(String day, String docShiftId, final String workerName, final String workerID)
    {
        final ArrayList<String> dataOfString = new ArrayList<>();
        final DocumentReference dref = db.document(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection+"/"+docShiftId);
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>)task.getResult().get(DBFS_shift_WorkersNWOptionsForShift_key_field_doc);
                    for (int i = 0; i <data.size() ; i++) {
                        if(data.get(i).get(DBFS_worker_name_key_field_doc).toString().compareTo(workerName)==0 &&
                                data.get(i).get(DBFS_worker_password_key_field_doc).toString().compareTo(workerID)== 0)
                        {
                            data.remove(i);
                            break;

                        }
                    }
                    dref.update(DBFS_shift_WorkersNWOptionsForShift_key_field_doc,data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dbMessageInterface.onGetBDMessages(REMOVE_FROM_OPTIONS_LIST);
                        }
                    });
                }else
                    {
                     dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });
    }


    //worker Options
    public void addWorkerToNWOptions(String day, String docShiftId, final String workerName, final String workerID)
    {
        final ArrayList<String> dataOfString = new ArrayList<>();
        final DocumentReference dref = db.document(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection+"/"+docShiftId);
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>)task.getResult().get(DBFS_shift_WorkersNWOptionsForShift_key_field_doc);
                    for (int i = 0; i <data.size() ; i++) {
                        if(data.get(i).get(DBFS_worker_name_key_field_doc).toString().compareTo(workerName)==0 &&
                                data.get(i).get(DBFS_worker_password_key_field_doc).toString().compareTo(workerID)== 0)
                        {
                            dbMessageInterface.onGetBDMessages(IN_ALREADY_OPTIONS_LIST);
                          return;

                        }
                    }
                    HashMap<String,Object> workerData = new HashMap<>();
                    workerData.put(DBFS_worker_name_key_field_doc,workerName);
                    workerData.put(DBFS_worker_password_key_field_doc,workerID);
                    data.add(workerData);
                    dref.update(DBFS_shift_WorkersNWOptionsForShift_key_field_doc,data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dbMessageInterface.onGetBDMessages(ADDED_OPTIONS_LIST);
                            }else {
                                dbMessageInterface.onGetBDMessages(FAIL);
                            }
                        }
                    });
                }else {
                    dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });

    }

    //manger auto assignment
    public void makeAutoAssimentForNWFromOptions() {

        CollectionReference cref = db.collection(DBFS_Days_collection);
        cref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    dbMessageInterface.onGetBDMessages(START_AUTO_ASSIGMENT_MES);
                    for (int i = 0; i < task.getResult().getDocuments().size() ; i++) {
                        makeAutoAssimentForNWFromOptionsByDay(daysOfweek[i]);
                    }
                }
                else {
                    dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });
    }

    private void makeAutoAssimentForNWFromOptionsByDay(final String day)
    {

      CollectionReference shiftsOfDay =  db.collection(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection);
      shiftsOfDay.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {

              if(task.isSuccessful()){
                  for (QueryDocumentSnapshot document : task.getResult())
                  {

                      if(document.getData().get(DBFS_shift_name_key_field_doc) != null
                              && document.getData().get(DBFS_shift_timeFrame_key_field_doc)!= null)
                      {
                          DocumentReference documentReference = document.getReference();
                         //we look now on a shift documentSnapshot
                          ArrayList<HashMap<String,Object>> nwa = new ArrayList<HashMap<String,Object>>();
                          ArrayList<HashMap<String,Object>> options = (ArrayList<HashMap<String,Object>>) document.getData().get(DBFS_shift_WorkersNWOptionsForShift_key_field_doc);
                          if(options !=null && options.size() != 0)
                          {
                              int randIndex = new Random().nextInt(options.size() - 1);
                              if(options.get(randIndex) != null)
                              {
                                  nwa.add(options.get(randIndex));
                                  documentReference.update(DBFS_shift_NWAssignWorkers_key_field_doc,nwa).addOnCompleteListener(new OnCompleteListener<Void>()
                                  {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task)
                                      {
                                          if(task.isSuccessful())
                                          {
                                                  dbMessageInterface.onGetBDMessages(FINISH_DAY_AUTO_ASSIGMENT+ day);
                                          }else
                                          {
                                                dbMessageInterface.onGetBDMessages(FAIL);
                                          }
                                      }
                                  });
                              }
                          }
                          dbMessageInterface.onGetBDMessages(DBFS_NO_OPTIONS_FOR_SHIFT);

                      }


                  }

              }else
                  {
                  dbMessageInterface.onGetBDMessages(FAIL);
              }
          }
      });
    }


    //week replaceing
    public void changeWeeks() {
        CollectionReference cref = db.collection(DBFS_Days_collection);
        cref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    dbMessageInterface.onGetBDMessages(WEEK_CHANGE);
                    for (int i = 0; i < task.getResult().getDocuments().size() ; i++) {
                        changeWeekByDay(daysOfweek[i]);
                    }
                }
                else {
                    dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });

    }

    private void changeWeekByDay(final String day) {
        CollectionReference shiftsOfDay =  db.collection(DBFS_Days_collection+"/"+day+"/"+DBFS_shifts_collection);
        shiftsOfDay.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult())
                    {

                        if(document.getData().get(DBFS_shift_name_key_field_doc) != null
                                && document.getData().get(DBFS_shift_timeFrame_key_field_doc)!= null)
                        {
                            DocumentReference documentReference = document.getReference();
                            //we look now on a shift documentSnapshot
                            ArrayList<HashMap<String,Object>> newNWA = new ArrayList<HashMap<String,Object>>();
                            ArrayList<HashMap<String,Object>> currentWeekToBe = (ArrayList<HashMap<String,Object>>) document.getData().get(DBFS_shift_NWAssignWorkers_key_field_doc);
                            if(currentWeekToBe !=null)
                            {

                                documentReference.update(DBFS_shift_ThisWeekWorkers_key_field_doc,currentWeekToBe).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            dbMessageInterface.onGetBDMessages(DAY_CHANGE+ day);
                                        }else
                                        {
                                            dbMessageInterface.onGetBDMessages(FAIL);
                                        }
                                    }
                                });

                                    documentReference.update(DBFS_shift_NWAssignWorkers_key_field_doc,newNWA).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                dbMessageInterface.onGetBDMessages(NEW_NW_LIST+ day);
                                            }else
                                            {
                                                dbMessageInterface.onGetBDMessages(FAIL);
                                            }
                                        }
                                    });

                            }

                        }


                    }

                }else
                {
                    dbMessageInterface.onGetBDMessages(FAIL);
                }
            }
        });
    }
}


