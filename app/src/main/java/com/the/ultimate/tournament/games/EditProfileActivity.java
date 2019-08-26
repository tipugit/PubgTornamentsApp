package com.the.ultimate.tournament.games;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class EditProfileActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    private ArrayList<HashMap<String, String>> offersList;

    // url to get all products list
    private static final String url = config.mainurl + "edit_profile.php";
    private static final String urlresetpassword = config.mainurl + "reset_password.php";

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
    private static final String TAG_OLDPASSWORD = "oldpassword";
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

    private int success;

    private RadioGroup Gender;
    private TextInputEditText birthdate;
    private TextInputEditText eMail;
    private String email;
    private RadioButton female;
    private String firstname;
    private TextInputEditText fname;
    private String gender;
    private String lastname;
    private TextInputEditText lname;
    private TextInputEditText mNumber;
    private RadioButton male;
    private String mnumber;
    private TextInputEditText newPass;
    private TextInputEditText oldPass;
    private Button resetPassButton;
    private TextInputEditText retypeNewPass;
    private Button saveButton;
    private TextView successText;
    private TextView successTextPassword;
    private TextInputEditText uname;
    private TextInputEditText pubgusername;
    private String username;
    private String pubguname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        prf = new PrefManager(EditProfileActivity.this);

        // Hashmap for ListView
        offersList = new ArrayList<>();

        fname = (TextInputEditText) findViewById(R.id.firstname);
        lname = (TextInputEditText) findViewById(R.id.lastname);
        uname = (TextInputEditText) findViewById(R.id.username);
        pubgusername = (TextInputEditText) findViewById(R.id.pubgusername);
        eMail = (TextInputEditText) findViewById(R.id.email);
        mNumber = (TextInputEditText) findViewById(R.id.mobileNumber);
        birthdate = (TextInputEditText) findViewById(R.id.dob);
        Gender = (RadioGroup) findViewById(R.id.gender);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        saveButton = (Button) findViewById(R.id.saveBtn);
        oldPass = (TextInputEditText) findViewById(R.id.oldpass);
        newPass = (TextInputEditText) findViewById(R.id.newpass);
        retypeNewPass = (TextInputEditText) findViewById(R.id.retypeNewPass);
        resetPassButton = (Button) findViewById(R.id.changePassBtn);
        successText = (TextView) findViewById(R.id.messageView);
        successTextPassword = (TextView) findViewById(R.id.passwordMessageView);

        username = prf.getString(TAG_USERNAME);
        pubguname = prf.getString(TAG_PUBGUSERNAME);
        firstname = prf.getString(TAG_FIRSTNAME);
        lastname = prf.getString(TAG_LASTNAME);
        email = prf.getString(TAG_EMAIL);
        mnumber = prf.getString(TAG_MOBILE);
        gender = prf.getString(TAG_GENDER);

        fname.setText(firstname);
        lname.setText(lastname);
        uname.setText(username);
        pubgusername.setText(pubguname);
        eMail.setText(email);
        mNumber.setText(mnumber);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname.getText().length() >1 && lname.getText().length()>1 && pubgusername.getText().length()>1 && mNumber.getText().length() >8) {
                    // Loading jsonarray in Background Thread
                    new OneLoadAllProducts().execute();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please enter value for all field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        resetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPass.getText().length() >1 && newPass.getText().length()>1 && retypeNewPass.getText().length()>1) {
                    if (newPass.equals(retypeNewPass)) {
                        // Loading jsonarray in Background Thread
                        new ResetPassword().execute();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "NewPassword And RetypePass is not Same", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please enter value for all field", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class OneLoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProfileActivity.this);
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
            params.put(TAG_USERID, prf.getString(TAG_USERID));
            params.put(TAG_USERNAME, prf.getString(TAG_USERNAME));
            params.put(TAG_PUBGUSERNAME, pubgusername.getText().toString());
            params.put(TAG_FIRSTNAME, fname.getText().toString());
            params.put(TAG_LASTNAME, lname.getText().toString());
            params.put(TAG_MOBILE, mNumber.getText().toString());

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);


            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                // jsonarray found
                // Getting Array of jsonarray
                jsonarray = json.getJSONArray(TAG_RAJANR);

                // looping through All jsonarray
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject c = jsonarray.getJSONObject(i);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

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

                        /*
                          Updating parsed JSON data into ListView
                         */
                        for (int i = 0; i < offersList.size(); i++) {

                            System.out.println("Rjn_userid"+offersList.get(i).get(TAG_USERID));

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

                        Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toast.makeText(EditProfileActivity.this,"Profile Updated Succsessfully",Toast.LENGTH_LONG).show();

                    } else if(success == 2){
                        // no jsonarray found
                        Toast.makeText(EditProfileActivity.this,"Something went wrong. Email Us.",Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(EditProfileActivity.this,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }

    class ResetPassword extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProfileActivity.this);
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
            params.put(TAG_USERID, prf.getString(TAG_USERID));
            params.put(TAG_USERNAME, prf.getString(TAG_USERNAME));
            params.put(TAG_PASSWORD, newPass.getText().toString());
            params.put(TAG_OLDPASSWORD, oldPass.getText().toString());

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(urlresetpassword, "POST", params);

            // Check your log cat for JSON reponse

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
                        // jsonarray found
                        // Getting Array of jsonarray

                        Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toast.makeText(EditProfileActivity.this,"Password Updated Succsessfully",Toast.LENGTH_LONG).show();

                    } else if(success == 2){
                        // no jsonarray found
                        Toast.makeText(EditProfileActivity.this,"Old Password is wrong",Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(EditProfileActivity.this,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }
}
