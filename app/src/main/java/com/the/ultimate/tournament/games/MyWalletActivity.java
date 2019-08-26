package com.the.ultimate.tournament.games;

import android.app.ProgressDialog;
//import instamojo.library.InstapayListener;
//import instamojo.library.InstamojoPay;

import org.json.JSONException;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.widget.Toast;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.config.config;
import com.the.ultimate.tournament.games.fragment.AddMoneyFragment;
import com.the.ultimate.tournament.games.fragment.TransactionsFragment;
import com.the.ultimate.tournament.games.fragment.WithdrawFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyWalletActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    // url to get all products list
    private static final String url = config.mainurl + "payment.php";
    private static final String urlpaytmchecksum = config.paytmchecksum + "generateChecksum.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    //user
    private static final String TAG_USERID = "userid";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_MOBILE = "mobile";

    //balance
    private static final String TAG_USERBALANCE = "balance";

    //instamojo
    private static final String TAG_INSTA_ORDERID = "instaorderid";
    private static final String TAG_INSTA_TXNID = "instatxnid";
    private static final String TAG_INSTA_PAYMENTID = "instapaymentid";
    private static final String TAG_INSTA_TOKEN = "instatoken";

    private String balance;
    private String email;
    private LinearLayout main;
    private String number;
    private TabLayout tabLayout;
    private String username;
    private ViewPager viewPager;
    private TextView walletBalance;

    //Prefrance
    private static PrefManager prf;

    //paytm
    private String paytmemail;
    private String paytmphone;
    private String paytmamount;
    private String paytmpurpose;
    private String paytmbuyername;
    private String paytmorder_id;
    private String paytmchecksumhash;

    //instamojo
//    InstapayListener listener;
//    InstamojoPay instamojoPay;

    private String addamount;
    private String instaorderid;
    private String instatoken;
    private String instapaymentid;
    private String instatxnid;

    private int success;

    public void PaytmAddMoney(String email, String phone, String amount, String purpose, String buyername) {

        paytmemail = email;
        paytmphone = phone;
        paytmamount = amount;
        paytmpurpose = purpose;
        paytmbuyername = buyername;

        final int min = 1000;
        final int max = 10000;
        final int random = new Random().nextInt((max - min) + 1) + min;
        paytmorder_id = prf.getString(TAG_USERID) +random;

        // Join Player in Match in Background Thread
        new GetChecksum().execute();

    }

    class GetChecksum extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyWalletActivity.this);
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
            params.put( "MID" , config.MID);
            params.put( "ORDER_ID" , paytmorder_id);
            params.put( "CUST_ID" , prf.getString(TAG_USERID));
            params.put( "MOBILE_NO" , paytmphone);
            params.put( "EMAIL" , paytmemail);
            params.put( "CHANNEL_ID" , "WAP");
            params.put( "TXN_AMOUNT" , paytmamount);
            params.put( "WEBSITE" , config.WEBSITE);
            params.put( "INDUSTRY_TYPE_ID" , config.INDUSTRY_TYPE_ID);
            params.put( "CALLBACK_URL", config.CALLBACK_URL + paytmorder_id);

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(urlpaytmchecksum, "POST", params);

            // Check your log cat for JSON reponse

            if(json != null){
                try {

                    paytmchecksumhash=json.has("CHECKSUMHASH")?json.getString("CHECKSUMHASH"):"";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    try {

                        Map<String, String> paramMap = new HashMap<>();
                        paramMap.put( "MID" , config.MID);
                        // Key in your staging and production MID available in your dashboard

                        paramMap.put( "ORDER_ID" , paytmorder_id);
                        paramMap.put( "CUST_ID" , prf.getString(TAG_USERID));
                        paramMap.put( "MOBILE_NO" , paytmphone);
                        paramMap.put( "EMAIL" , paytmemail);
                        paramMap.put( "CHANNEL_ID" , "WAP");
                        paramMap.put( "TXN_AMOUNT" , paytmamount);
                        paramMap.put( "WEBSITE" , config.WEBSITE);
                        paramMap.put( "INDUSTRY_TYPE_ID" , config.INDUSTRY_TYPE_ID);
                        paramMap.put( "CALLBACK_URL", config.CALLBACK_URL + paytmorder_id);
                        paramMap.put( "CHECKSUMHASH" , paytmchecksumhash);
                        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);

                        // For Staging environment:
//                        PaytmPGService Service = PaytmPGService.getStagingService();

                        // For Production environment:
                         PaytmPGService Service = PaytmPGService.getProductionService();

                        Service.initialize(Order, null);

                        Service.startPaymentTransaction(MyWalletActivity.this, true, true, MyWalletActivity.this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }

    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {

        // getting JSON string from URL
        JSONObject json = null;
        try {
            String resstatus=inResponse.getString("STATUS");

            if(resstatus.equalsIgnoreCase("TXN_SUCCESS")) {

                instaorderid = inResponse.getString("ORDERID");
                instatxnid = inResponse.getString("TXNID");
                addamount = inResponse.getString("TXNAMOUNT");
                instapaymentid = inResponse.getString("CHECKSUMHASH");
                instatoken = inResponse.getString("MID");

                // Loading jsonarray in Background Thread
                new OneLoadAllProducts().execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
    }

//    public void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
//        final Activity activity = this;
//        instamojoPay = new InstamojoPay();
//        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
//        registerReceiver(instamojoPay, filter);
//        JSONObject pay = new JSONObject();
//        try {
//            pay.put("email", email);
//            pay.put("phone", phone);
//            pay.put("purpose", purpose);
//            addamount=amount;
//            pay.put("amount", amount);
//            pay.put("name", buyername);
//            pay.put("send_sms", true);
//            pay.put("send_email", true);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            System.out.println("Rjn_instamojo_error"+e.getMessage());
//        }
//        initListener();
//        instamojoPay.start(activity, pay, listener);
//    }
//
//    private void initListener() {
//        listener = new InstapayListener() {
//            @Override
//            public void onSuccess(String response) {
//                System.out.println("Rjn_payment"+response);
//
//                String[] str = response.split(":");
//                String[] split = str[1].split("=");
//                instaorderid = split[1];
//                split = str[2].split("=");
//                instatxnid = split[1];
//                split = str[3].split("=");
//                instapaymentid = split[1];
//                str = str[4].split("=");
//                instatoken = str[1];
//
//                // Loading jsonarray in Background Thread
//                new OneLoadAllProducts().execute();
//            }
//
//            @Override
//            public void onFailure(int code, String reason) {
//                System.out.println("Rjn_payment_error"+"code:"+code+"reason:"+reason);
//                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
//                        .show();
//            }
//        };
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Call the function callInstamojo to start payment here

        prf = new PrefManager(MyWalletActivity.this);

        walletBalance = (TextView) findViewById(R.id.walletBalance);
        main = (LinearLayout) findViewById(R.id.mainLayout);
        balance = prf.getString(TAG_USERBALANCE);
        username = prf.getString(TAG_USERNAME);
        email = prf.getString(TAG_EMAIL);
        number = prf.getString(TAG_MOBILE);
        walletBalance.setText("₹ "+balance);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddMoneyFragment(), "Add Money");
        adapter.addFragment(new WithdrawFragment(), "Withdraw");
        adapter.addFragment(new TransactionsFragment(), "Transactions");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    class OneLoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyWalletActivity.this);
            pDialog.setMessage("Loading Please wait...");
            pDialog.setIndeterminate(true);
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
            params.put("addamount", addamount);
            params.put(TAG_INSTA_ORDERID, instaorderid);
            params.put(TAG_INSTA_TXNID, instatxnid);
            params.put(TAG_INSTA_PAYMENTID, instapaymentid);
            params.put(TAG_INSTA_TOKEN, instatoken);
            params.put("status", "Add Money Success");

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            // Check your log cat for JSON reponse
//            Log.d("All jsonarray: ", json.toString());

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
                        String s = addamount;
                        double d = Double.parseDouble(s);
                        int i = (int) d;

                        int bal = Integer.parseInt(prf.getString(TAG_USERBALANCE))+ i;
                        prf.setString(TAG_USERBALANCE,Integer.toString(bal));

                        Intent intent = new Intent(MyWalletActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                        Toast.makeText(MyWalletActivity.this,"Payment done. Now join match",Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MyWalletActivity.this,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }

}
