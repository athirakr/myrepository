package com.example.test.todotask.views.view;/*
   Auther        :  Athira K. R.
   Created on    :  22/7/15.
   Time          :  3:06 PM
   Project Name  :  TodoTask
   Package Name  :  com.example.test.todotask.views.view
   
*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.todotask.R;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

   private EditText userName;
   private EditText passWord;
   private Button login;
   private String userId;
   private Firebase ref;
   private int flag;
   private Button signUp;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.login);
      Firebase.setAndroidContext(this);

      //initialize controlls
      initialize();


      login.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            //Getting the values from layout
            getValue();

         }
      });

      signUp.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
         }
      });

   }



   private void initialize(){

      userName=(EditText)findViewById(R.id.editText_username);
      passWord=(EditText)findViewById(R.id.editText_password);
      login=(Button)findViewById(R.id.btnLogin);
      signUp=(Button)findViewById(R.id.btnFirebase_signin);

   }


   private void getValue(){
      String nameString;
      String usernameString;
      String passwordString;


      usernameString=userName.getText().toString();
      passwordString=passWord.getText().toString();

      fireBaseLogin(usernameString, passwordString);

   }



   private void fireBaseLogin(final String userName, final String passWord){

      if(userName.equals("")||passWord.equals("")){

         Toast.makeText(this, "Please enter values properly", Toast.LENGTH_LONG).show();
      }
      else {

         ref = new Firebase("https://todolista.firebaseio.com/android/saving-data/fireblog");

         ref.addChildEventListener(new ChildEventListener() {
          //   Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {


               outerLoop:

               for (DataSnapshot child1 : snapshot.getChildren()) {

                  for (DataSnapshot child2 : child1.getChildren()) {

                     String nameString=child1.child("Name").getValue(String.class);
                     String userNameString=child1.child("Username").getValue(String.class);
                     String passWordString=child1.child("Password").getValue(String.class);

                     if((userNameString.equals(userName))&&(passWordString.equals(passWord))){

                        flag=1;
                        validUser(nameString);
                        break outerLoop;
                     }
                     else{
                        flag=0;
                     }

                  }

              }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

         });




    }
   }



   public  void validUser(String userName){
      if(flag==1){

         Toast.makeText(this, "Loggined Successfully ", Toast.LENGTH_LONG).show();
         Intent intent=new Intent(getApplication(),ToDoMainActivity.class);
         Bundle bundle=new Bundle();
         bundle.putString("name",userName);
         intent.putExtras(bundle);
         startActivity(intent);

      }
      else{

         Toast.makeText(this, "No such user exist.. Please sign up", Toast.LENGTH_LONG).show();
         finish();
      }
   }


}