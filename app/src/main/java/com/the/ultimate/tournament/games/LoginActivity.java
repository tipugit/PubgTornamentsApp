package com.the.ultimate.tournament.games;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.config.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    private ArrayList<HashMap<String, String>> offersList;

    // url to get all products list
    private static final String url = config.mainurl + "signin_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RAJANR = "rajanr";

    //user
    private static final String TAG_USERID = "userid";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PUBGUSERNAME = "pubgusername";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_MOBILE = "mobile";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_OTHER = "other";
    private static final String TAG_PROMOCODE = "promocode";

    //balance
    private static final String TAG_USERBALANCE = "balance";

    //matchdetail
    private static final String TAG_TOTALMATCHPLAYED = "totalmatchplayed";
    private static final String TAG_WONAMOUNT = "wonamount";
    private static final String TAG_KILLS = "kills";

    // products JSONArray
    private JSONArray jsonarray = null;

    //Prefrance
    private static PrefManager prf;

    //Textbox
    private EditText username;
    private EditText password;
    private Button signup;
    private Button signin;
    private TextView resetnow;

    private int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prf = new PrefManager(LoginActivity.this);

        // Hashmap for ListView
        offersList = new ArrayList<>();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        resetnow = (TextView) findViewById(R.id.resetnow);

        signup = (Button) findViewById(R.id.registerFromLogin);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signin = (Button) findViewById(R.id.signinbtn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkdetails()) {
                    // Loading jsonarray in Background Thread
                    new OneLoadAllProducts().execute();
                }
            }
        });

        resetnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"Email us from your registed email id",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkdetails() {

        if (username.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Enter Value for Username", Toast.LENGTH_SHORT).show();
            username.requestFocus();
            return false;
        }  else if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Enter Value for Password", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return false;
        }


        return true;
    }

    class OneLoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            Map<String, String> params = new HashMap<>();
            params.put(TAG_USERNAME, username.getText().toString());
            params.put(TAG_PASSWORD, password.getText().toString());

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            // Check your log cat for JSON reponse
//            Log.d("All jsonarray: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                // jsonarray found
                // Getting Array of jsonarray
                jsonarray = json.getJSONArray(TAG_RAJANR);

                // looping through All jsonarray
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject c = jsonarray.getJSONObject(i);

//                    // Storing each json item in variable

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_USERID, c.getString(TAG_USERID));
                    map.put(TAG_FIRSTNAME, c.getString(TAG_FIRSTNAME));
                    map.put(TAG_LASTNAME, c.getString(TAG_LASTNAME));
                    map.put(TAG_USERNAME, c.getString(TAG_USERNAME));
                    map.put(TAG_PUBGUSERNAME, c.getString(TAG_PUBGUSERNAME));
                    map.put(TAG_GENDER, c.getString(TAG_GENDER));
                    map.put(TAG_EMAIL, c.getString(TAG_EMAIL));
                    map.put(TAG_MOBILE, c.getString(TAG_MOBILE));
                    map.put(TAG_OTHER, c.getString(TAG_OTHER));
                    map.put(TAG_PROMOCODE, c.getString(TAG_PROMOCODE));

                    //balance
                    map.put(TAG_USERBALANCE, c.getString(TAG_USERBALANCE));


                    //matchdetail
                    map.put(TAG_TOTALMATCHPLAYED, c.getString(TAG_TOTALMATCHPLAYED));
                    map.put(TAG_WONAMOUNT, c.getString(TAG_WONAMOUNT));
                    map.put(TAG_KILLS, c.getString(TAG_KILLS));


                    // adding HashList to ArrayList
                    offersList.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Rjn_login_error"+e.getMessage());
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /*
                      Updating parsed JSON data into ListView
                     */
                    if (success == 1) {
                        // jsonarray found
                        // Getting Array of jsonarray
                        System.out.println("Rjn_user_created");

                        /*
                          Updating parsed JSON data into ListView
                         */
                        for (int i = 0; i < offersList.size(); i++) {

                            // preference and set username for session
                            prf.setString(TAG_USERID, offersList.get(i).get(TAG_USERID));
                            prf.setString(TAG_FIRSTNAME, offersList.get(i).get(TAG_FIRSTNAME));
                            prf.setString(TAG_LASTNAME, offersList.get(i).get(TAG_LASTNAME));
                            prf.setString(TAG_USERNAME, offersList.get(i).get(TAG_USERNAME));
                            prf.setString(TAG_PUBGUSERNAME, offersList.get(i).get(TAG_PUBGUSERNAME));
                            prf.setString(TAG_GENDER, offersList.get(i).get(TAG_GENDER));
                            prf.setString(TAG_EMAIL, offersList.get(i).get(TAG_EMAIL));
                            prf.setString(TAG_MOBILE, offersList.get(i).get(TAG_MOBILE));
                            prf.setString(TAG_OTHER, offersList.get(i).get(TAG_OTHER));
                            prf.setString(TAG_PROMOCODE, offersList.get(i).get(TAG_PROMOCODE));

                            //balance
                            prf.setString(TAG_USERBALANCE, offersList.get(i).get(TAG_USERBALANCE));


                            //matchdetail
                            prf.setString(TAG_TOTALMATCHPLAYED, offersList.get(i).get(TAG_TOTALMATCHPLAYED));
                            prf.setString(TAG_WONAMOUNT, offersList.get(i).get(TAG_WONAMOUNT));
                            prf.setString(TAG_KILLS, offersList.get(i).get(TAG_KILLS));

                        }

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toast.makeText(LoginActivity.this,"Signin done Succsessfully",Toast.LENGTH_LONG).show();

                    } else if(success == 2){
                        // no jsonarray found
                        Toast.makeText(LoginActivity.this,"Username or password is not valid",Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(LoginActivity.this,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }
}
