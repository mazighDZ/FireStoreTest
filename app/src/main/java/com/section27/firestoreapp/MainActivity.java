package com.section27.firestoreapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private     Button saveBTN , getSavedBTN , updateBTN , deleteBTN ,newUserBTN;
    private EditText nameET,emailET ;
    private TextView textView;


    //firebase store
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
   //instance reading proUsers in fireStore
    private DocumentReference proUserRef = db.collection("Users")
            .document("ProUsers");

    private CollectionReference collectionReference = db.collection("Users");

    //keys
    public static final String KEY_NAME= "name";
    public static final String KEY_EMAIL = "email";
    /**
     1-saving simple data on Firebase(creating data) -> saveDataToFireStore();
     2-reading simple data from Firebase(retrieving  data) ->  readDataFromFireStore();
     2.1-Listening to snapshot changes -> onStar()--> proUserRef.addSnapshotListener
     3-updating simple data                         ->    updateData();
     4-deleting data(key-value pairs) from firebase -> deleteDataFromFireStore();

     5-saving custom object data (java POJO) into FireStore -> saveObjectToFireStore();

     6-Creating multiple documents and retrieving Multiple document -> saveDataToNewDocument();
     7- Retrieving multiple documents into  Log --> getAllDocumentInCollection();
     8-retrieving Multiple Documents into Objects (ProUser) ->  getAllDocumentInCollection()


     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameET = findViewById(R.id.etName);
        emailET = findViewById(R.id.etEmail);
        saveBTN = findViewById(R.id.btnSave);
        getSavedBTN = findViewById(R.id.btngetData);
        textView = findViewById(R.id.textv);
        updateBTN = findViewById(R.id.btnupdate);
        deleteBTN = findViewById(R.id.bntDelete);
        newUserBTN = findViewById(R.id.btnSavenew);

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveDataToFireStore();
                saveObjectToFireStore();
            }
        });

        //get saved data on btn clicked

        getSavedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                readDataFromFireStore();
                getAllDocumentInCollection();
            }
        });

        //update data on btn clicked
        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        //delete btn

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // method to delete specific key
//                deleteDataFromFireStore();
                // delete all
           deleteAll();
            }
        });

        // new document (means new user)
        newUserBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToNewDocument();
            }
        });

    }

    private void getAllDocumentInCollection() {

    collectionReference.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    // lop through all the document inside Users collection
                       for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots ){

                           //Log.v("MyTag" , documentSnapshot.getString(KEY_NAME));
                           // transforming dataSnapshot into object of ProUser
                           ProUser user = documentSnapshot.toObject(ProUser.class);
                           Log.v("MyTag" , user.getName());
                       }
                }
            });
    }

    private void deleteDataFromFireStore() {

    //delete pairs according to name
        proUserRef.update(KEY_NAME, FieldValue.delete());
    //delete value according to email
        proUserRef.update(KEY_NAME, FieldValue.delete());

    }


    private  void deleteAll(){
        proUserRef.delete();
    }
    private void updateData() {
        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        Map<String ,Object> data = new HashMap<>();

        data.put(KEY_NAME , name);
        data.put(KEY_EMAIL,email);

        proUserRef.update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Updated  successfully ",Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void readDataFromFireStore() {
   proUserRef.get()
           .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
               public void onSuccess(DocumentSnapshot documentSnapshot) {
                   //reteive data into textview
                   if(documentSnapshot.exists()){

                       String pname = documentSnapshot.getString(KEY_NAME);
                       String pemail = documentSnapshot.getString(KEY_EMAIL);

                       textView.setText("user name: "+pname + "\nuser Email: " +pemail );
                   }

               }
           });

    }

    private void saveDataToFireStore() {

        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        //saving data as key-value pairs (MAP data structure)
        Map<String ,Object> data = new HashMap<>();

        data.put(KEY_NAME , name);
        data.put(KEY_EMAIL,email);

        //saving in collections
        db.collection("Users")
                .document("ProUsers")
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"successfully created",Toast.LENGTH_SHORT).show();
                    }
                })
                //we can add more other listeners
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_SHORT).show();

                    }
                });

    }
    private  void saveDataToNewDocument(){

        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        ProUser user =  new ProUser(name,email);

        collectionReference.add(user);


    }

    private void saveObjectToFireStore(){
        ProUser user1 = new ProUser();
        user1.setName(nameET.getText().toString());
        user1.setEmail(emailET.getText().toString());


        proUserRef.set(user1);

    }

    @Override
    protected void onStart() {
        super.onStart();
    //listening all the time during the app lifecycle
        proUserRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Toast.makeText(getApplicationContext() , "Error found " ,Toast.LENGTH_SHORT).show();
                }
                if (value != null && value.exists()){
                    //getting data(key-value) from FireStore
//                    String pname = value.getString(KEY_NAME);
//                    String pemail = value.getString(KEY_EMAIL);
                    //getting Object data from FireStore
               ProUser user = value.toObject(ProUser.class);

                    textView.setText("user name: "+user.getName() + "\nuser Email: " +user.getEmail() );
                }
                else {
                    textView.setText("user name: no user"+  "\nuser Email: no Email"  );

                }
            }
        });

    }
}