package com.the.ultimate.tournament.games.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.the.ultimate.tournament.games.JSONParser;
import com.the.ultimate.tournament.games.PrefManager;
import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.adapter.TransactionsAdapter;
import com.the.ultimate.tournament.games.config.config;
import com.the.ultimate.tournament.games.data.Transactions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransactionsFragment extends Fragment {

    // Progress Dialog
//    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    private ArrayList<HashMap<String, String>> offersList;

    // url to get all products list
    private static final String url = config.mainurl + "get_all_transactions.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RAJANR = "rajanr";

    //user
    private static final String TAG_USERID = "userid";
    private static final String TAG_USERNAME = "username";

    //usertransactions
    private static final String TAG_TRNID = "trnid";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_TRNTYPE = "type";
    private static final String TAG_PAYTMNUMBER = "paytmnumber";
    private static final String TAG_TRNSTATUS = "status";

    //match
    private static final String TAG_LOG_ENTDATE = "log_entdate";


    // products JSONArray
    private JSONArray jsonarray = null;

    //Prefrance
    private static PrefManager prf;

    private int success;

    //new
    private Context context;

    private List<Transactions> transactionsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionsAdapter mAdapter;

    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout noTxnsLayout;
    private String username;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        prf = new PrefManager(context);

        // Hashmap for ListView
        offersList = new ArrayList<>();

        new OneLoadAllProducts().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViewone = inflater.inflate(R.layout.fragment_transactions, container, false);

        mShimmerViewContainer = (ShimmerFrameLayout) rootViewone.findViewById(R.id.shimmer_container);
        noTxnsLayout = (LinearLayout) rootViewone.findViewById(R.id.noTxnLayout);

        mShimmerViewContainer.setVisibility(View.VISIBLE);

        username = prf.getString(TAG_USERNAME);
        transactionsList = new ArrayList();

        recyclerView = (RecyclerView) rootViewone.findViewById(R.id.txnListRecyclerView);

        mAdapter = new TransactionsAdapter(transactionsList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep transactions_list_rowist_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);

        // horizontal RecyclerView
        // keep transactions_list_rowist_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // row click listenerMyDividerItemDecoration
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Transactions transactions = transactionsList.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return rootViewone;
    }

    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

    /**
     * Prepares sample data to provide data set to adapter
     */

    class OneLoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            Map<String, String> params = new HashMap<>();
            params.put(TAG_USERID, prf.getString(TAG_USERID));

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            System.out.println("Rjn_transactions_json"+json);
            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if(success==1) {
                    // jsonarray found
                    // Getting Array of jsonarray
                    jsonarray = json.getJSONArray(TAG_RAJANR);

                    // looping through All jsonarray
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        //match
                        map.put(TAG_TRNID, c.getString(TAG_TRNID));
                        map.put(TAG_USERID, c.getString(TAG_USERID));
                        map.put(TAG_AMOUNT, c.getString(TAG_AMOUNT));
                        map.put(TAG_TRNTYPE, c.getString(TAG_TRNTYPE));
                        map.put(TAG_PAYTMNUMBER, c.getString(TAG_PAYTMNUMBER));
                        map.put(TAG_TRNSTATUS, c.getString(TAG_TRNSTATUS));
                        map.put(TAG_LOG_ENTDATE, c.getString(TAG_LOG_ENTDATE));

                        // adding HashList to ArrayList
                        offersList.add(map);
                    }
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

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
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
                        System.out.println("Rjn_offersList.size()"+offersList.size());
                        for (int i = 0; i < offersList.size(); i++) {

                            System.out.println("Rjn_userid"+offersList.get(i).get(TAG_USERID));
                            Transactions transactions = new Transactions();
                            transactions.setTxnAmount(offersList.get(i).get(TAG_AMOUNT));
                            transactions.setTxnDate(offersList.get(i).get(TAG_LOG_ENTDATE));
                            transactions.setTxnRemark(offersList.get(i).get(TAG_TRNSTATUS));
                            transactions.setTxnType(offersList.get(i).get(TAG_TRNTYPE));

                            transactionsList.add(transactions);

                            // notify adapter about data set changes
                            // so that it will render the list with new data
                            mAdapter.notifyDataSetChanged();

                        }

                        if (transactionsList.size() <= 0) {
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            noTxnsLayout.setVisibility(View.VISIBLE);
                            return;
                        }

                        System.out.println("Rjn_mShimmerViewContainer.stopShimmer()");
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        noTxnsLayout.setVisibility(View.GONE);

                    } else if(success == 2){
                        // no jsonarray found
                        System.out.println("Rjn_mobile_already_exist");
//                        Toast.makeText(context,"Userid is not valid",Toast.LENGTH_LONG).show();

                    } else {
                        System.out.println("Rjn_user_not_created");
                        Toast.makeText(context,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }

}
