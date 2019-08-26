package com.the.ultimate.tournament.games;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.config.config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    // url to get all products list
    private static final String url = config.mainurl + "create_user.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PUBGUSERNAME = "pubgusername";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_MOBILE = "mobile";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_OTHER = "other";
    private static final String TAG_PROMOCODE = "promocode";

    //Textbox
    private EditText firstname;
    private EditText lastname;
    private EditText username;
    private EditText pubgusername;
    private EditText email;
    private EditText mobile;
    private EditText password;
    private EditText promocode;
    private Button signup;
    private Button signin;

    private int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        username = (EditText) findViewById(R.id.username);
        pubgusername = (EditText) findViewById(R.id.pubgusername);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobileNumber);
        password = (EditText) findViewById(R.id.password);
        promocode = (EditText) findViewById(R.id.promocode);

        signup = (Button) findViewById(R.id.registerBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkdetails()) {
                    // Loading offers in Background Thread
                    new OneLoadAllProducts().execute();
                }
            }
        });

        signin = (Button) findViewById(R.id.loginFromRegister);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

            }
        });
    }

    private boolean checkdetails() {

        //special character checking


        Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher("I am a string"+username.getText().toString());
        boolean b = m.find();

        if (b)
            System.out.println("Rajan_There is a special character in my string");

        if (email.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter Value for Email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            Toast.makeText(MainActivity.this, "Enter valid Value for Email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return false;
        } else if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter Value for Password", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return false;
        } else if (firstname.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter Value for FirstName", Toast.LENGTH_SHORT).show();
            firstname.requestFocus();
            return false;
        } else if (lastname.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter Value for LastName", Toast.LENGTH_SHORT).show();
            lastname.requestFocus();
            return false;
        } else if (username.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter Value for Username", Toast.LENGTH_SHORT).show();
            username.requestFocus();
            return false;
        } else if (p.matcher(username.getText().toString()).find()) {
            Toast.makeText(MainActivity.this, "Enter Username without any special characters", Toast.LENGTH_SHORT).show();
            username.requestFocus();
            return false;
        } else if (pubgusername.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter Value for Pubg Username", Toast.LENGTH_SHORT).show();
            pubgusername.requestFocus();
            return false;
        } else if (mobile.getText().toString().trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter Value for Mobile", Toast.LENGTH_SHORT).show();
            mobile.requestFocus();
            return false;
        } else if (!Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches()) {
            Toast.makeText(MainActivity.this, "Enter Valid Value for MobileNumber", Toast.LENGTH_SHORT).show();
            mobile.requestFocus();
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
            pDialog = new ProgressDialog(MainActivity.this);
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
            params.put(TAG_FIRSTNAME, firstname.getText().toString().trim());
            params.put(TAG_LASTNAME, lastname.getText().toString().trim());
            params.put(TAG_USERNAME, username.getText().toString().trim());
            params.put(TAG_PUBGUSERNAME, pubgusername.getText().toString().trim());
            params.put(TAG_EMAIL, email.getText().toString().trim());
            params.put(TAG_MOBILE, mobile.getText().toString().trim());
            params.put(TAG_PASSWORD, password.getText().toString().trim());
            params.put(TAG_OTHER, "");
            params.put(TAG_PROMOCODE, promocode.getText().toString().trim());

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            // Check your log cat for JSON reponse
//            Log.d("All Offers: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
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
                        // offers found
                        // Getting Array of offers

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                        Toast.makeText(MainActivity.this,"Registration done Succsessfully",Toast.LENGTH_LONG).show();

                    } else if(success == 2){
                        // no offers found
                        Toast.makeText(MainActivity.this,"Email/mobile/username is already exist. change it and try again!",Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MainActivity.this,"User not created",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }
}
