package com.the.ultimate.tournament.games;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.config.config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinMatchConfirmationActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    // url to get all products list
    private static final String url = config.mainurl + "join_match.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    //user
    private static final String TAG_USERID = "userid";
    private static final String TAG_USERNAME = "username";

    //balance
    private static final String TAG_USERBALANCE = "balance";

    //match
    private static final String TAG_MATCHID = "matchid";
    private static final String TAG_ENTRYFEE = "entryfee";


    private int success;

    private String accountBalance;
    private Button addMoney;
    private LinearLayout addMoneyLL;
    private TextView balance;
    private TextView balanceStatus;
    private Button cancel;
    private String entryFee;
    private TextView entryfee;
    private int fee;
    private String joinStatus;
    private String matchID;
    private String matchType;
    private int myBalance;
    private Button next;
    private LinearLayout nextButtonLL;
    private String privateStatus;
    private String username;

    //Prefrance
    private static PrefManager prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_match_confirmation);

        prf = new PrefManager(JoinMatchConfirmationActivity.this);

        next = (Button) findViewById(R.id.next);
        balance = (TextView) findViewById(R.id.balance);
        entryfee = (TextView) findViewById(R.id.entryFee);
        balanceStatus = (TextView) findViewById(R.id.statusTextView);
        nextButtonLL = (LinearLayout) findViewById(R.id.nextButtonLL);
        addMoneyLL = (LinearLayout) findViewById(R.id.addMoneyLL);
        addMoney = (Button) findViewById(R.id.addMoneyButton);
        cancel = (Button) findViewById(R.id.cancelButton);

        username = prf.getString(TAG_USERNAME);
        accountBalance = prf.getString(TAG_USERBALANCE);

        matchID = getIntent().getStringExtra("matchID");
        matchType = getIntent().getStringExtra("matchType");
        entryFee = getIntent().getStringExtra("entryFee");
        joinStatus = getIntent().getStringExtra("JoinStatus");
        privateStatus = getIntent().getStringExtra("isPrivate");
        if (matchType.equals("Free")) {
            entryfee.setText("Free");
            entryfee.setTextColor(Color.parseColor("#1E7E34"));
        } else {
            entryfee.setText(entryFee);
        }
        if (joinStatus.equals("1")) {
            next.setText("Add New Entry");
        }
        balance.setText(accountBalance);
        Matcher mbal = Pattern.compile("\\d+").matcher(accountBalance);
        while (mbal.find()) {
            myBalance = Integer.parseInt(mbal.group());
        }
        Matcher mentry = Pattern.compile("\\d+").matcher(entryFee);
        while (mentry.find()) {
            fee = Integer.parseInt(mentry.group());
        }
        if (myBalance >= fee) {
            balanceStatus.setText(R.string.sufficientBalanceText);
            next.setText("NEXT");
        } else {
            balanceStatus.setTextColor(Color.parseColor("#ff0000"));
            balanceStatus.setText(R.string.insufficientBalanceText);
            addMoneyLL.setVisibility(View.VISIBLE);
            nextButtonLL.setVisibility(View.GONE);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Join Player in Match in Background Thread
                new JoinMatch().execute();
            }
        });
        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinMatchConfirmationActivity.this, MyWalletActivity.class);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class JoinMatch extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(JoinMatchConfirmationActivity.this);
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
            System.out.println("Rjn_join_match"+matchID+Integer.toString(fee));
            params.put(TAG_USERID, prf.getString(TAG_USERID));
            params.put(TAG_USERNAME, prf.getString(TAG_USERNAME));
            params.put(TAG_MATCHID, matchID);
            params.put(TAG_ENTRYFEE, Integer.toString(fee));

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
                        //update balance
                        int bal = Integer.parseInt(prf.getString(TAG_USERBALANCE))-fee;
                        prf.setString(TAG_USERBALANCE,Integer.toString(bal));

                        Intent intent = new Intent(JoinMatchConfirmationActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toast.makeText(JoinMatchConfirmationActivity.this,"Match joined Succsessfully",Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(JoinMatchConfirmationActivity.this,"Joining match is failed",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }
}
