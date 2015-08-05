package com.example.test.todotask.views.view;/*
   Auther        :  Athira K. R.
   Created on    :  22/7/15.
   Time          :  3:08 PM
   Project Name  :  TodoTask
   Package Name  :  com.example.test.todotask.views.view
   
*/

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.test.todotask.R;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends Activity {

   private EditText name;
   private EditText userName;
   private EditText passWord;
   private Button submit;
   private ImageButton back_btn;

   private Firebase ref;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.sign_up);
      Firebase.setAndroidContext(this);


      //initialize controlls
      initialize();


      submit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {


            //Getting the values from layout
            getValue();

         }
      });


      back_btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            finish();
         }
      });


   }




   private void fireBaseSignUp(String name,String userName,String passWord){

      if(name.equals("")||userName.equals("")||passWord.equals("")){

         Toast.makeText(this, "Please enter values properly", Toast.LENGTH_LONG).show();
      }
      else{

         Firebase usersRef = ref.child("users");
         Map<String, String> users = new HashMap<String, String>();
         users.put("Name", name);
         users.put("Username", userName);
         users.put("Password", passWord);
         usersRef.push().setValue(users);

         ref.addAuthStateListener(new Firebase.AuthStateListener() {

            @Override
            public void onAuthStateChanged(AuthData authData) {


            }
         });

         finish();
      }

   }



   private void getValue(){
      String nameString;
      String usernameString;
      String passwordString;

      nameString=name.getText().toString();
      usernameString=userName.getText().toString();
      passwordString=passWord.getText().toString();

      fireBaseSignUp(nameString, usernameString, passwordString);

   }


   private void initialize(){

      name=(EditText)findViewById(R.id.name);
      userName=(EditText)findViewById(R.id.username);
      passWord=(EditText)findViewById(R.id.password);
      submit=(Button)findViewById(R.id.btnSubmit);
      back_btn=(ImageButton)findViewById(R.id.image_back);

   }
}

