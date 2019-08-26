package com.the.ultimate.tournament.games;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.adapter.MatchResultAdapter;
import com.the.ultimate.tournament.games.config.config;
import com.the.ultimate.tournament.games.data.MatchResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MatchResultActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    private ArrayList<HashMap<String, String>> offersList;
    private ArrayList<HashMap<String, String>> offersListWon;

    // url to get all products list
    private static final String url = config.mainurl + "get_result_details.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RAJANR = "rajanr";
    private static final String TAG_RAJANRWON = "rajanrwon";

    //user
    private static final String TAG_USERID = "userid";
    private static final String TAG_FIRSTNAME = "firstname";

    //playerjoinstatus
    private static final String TAG_PLAYERRRANK = "playerrrank";

    //match
    private static final String TAG_MATCHID = "matchid";
    private static final String TAG_ENTRYFEE = "entryfee";
    private static final String TAG_WINPRICE = "winprice";
    private static final String TAG_PERKILL = "perkill";
    private static final String TAG_MATCHSCHEDULE = "matchschedule";

    //matchdetail
    private static final String TAG_WONAMOUNT = "wonamount";
    private static final String TAG_KILLS = "kills";

    // products JSONArray
    private JSONArray jsonarray = null;
    private JSONArray jsonarraywon = null;

    //Prefrance
    private static PrefManager prf;

    private int success;

    private List<MatchResult> matchResultList = new ArrayList<>();
    private List<MatchResult> matchResultListWon = new ArrayList<>();
    private RecyclerView recyclerView;
    private MatchResultAdapter mAdapter;
    private MatchResultAdapter mAdapterWon;

    private TextView dateTime;
    private TextView entryFee;
    private String matchDateTime;
    private String matchEntryFee;
    private String matchID;
    private String matchPerKill;
    private String matchTitle;
    private String matchWinPrize;
    private CardView notesCard;
    private TextView notesText;
    private TextView notesTitle;
    private TextView perKill;
    private RecyclerView recyclerViewWinner;
    private TextView title;
    private TextView winPrize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);

        prf = new PrefManager(MatchResultActivity.this);

        matchID = getIntent().getStringExtra(TAG_MATCHID);
        matchTitle = getIntent().getStringExtra("matchTitle");
        matchWinPrize = getIntent().getStringExtra(TAG_WINPRICE);
        matchPerKill = getIntent().getStringExtra(TAG_PERKILL);
        matchEntryFee = getIntent().getStringExtra(TAG_ENTRYFEE);
        matchDateTime = getIntent().getStringExtra(TAG_MATCHSCHEDULE);

        // Hashmap for ListView
        offersList = new ArrayList<>();
        offersListWon = new ArrayList<>();

        title = (TextView) findViewById(R.id.matchTitle);
        dateTime = (TextView) findViewById(R.id.datetime);
        winPrize = (TextView) findViewById(R.id.winPrize);
        perKill = (TextView) findViewById(R.id.perKill);
        entryFee = (TextView) findViewById(R.id.entryFee);

        title.setText("PUBG Mobile Match#"+matchID);

        //Input date in String format
        String input = matchDateTime;
        //Date/time pattern of input date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputtimeformat = new SimpleDateFormat("hh:mm aa");
        Date date;
        String output = null;
        try{
            //Conversion of input String to date
            date= df.parse(input);
            //old date format to new date format
            output = outputformat.format(date) + " at " + outputtimeformat.format(date);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        dateTime.setText(output);
        winPrize.setText(matchWinPrize);
        perKill.setText(matchPerKill);
        entryFee.setText(matchEntryFee);

        matchResultList = new ArrayList();
        matchResultListWon = new ArrayList();

        //1st recycler
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new MatchResultAdapter(matchResultList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep match_result_list_row.xmlrow.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep match_result_list_row.xmlrow.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        //2st recycler
        recyclerViewWinner = (RecyclerView) findViewById(R.id.recyclerViewWinner);

        mAdapterWon = new MatchResultAdapter(matchResultListWon);

        recyclerViewWinner.setHasFixedSize(true);

        // vertical RecyclerView
        // keep top_player_list_row.xmlw.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManagerWon = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep top_player_list_row.xmlw.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewWinner.setLayoutManager(mLayoutManagerWon);

        // adding inbuilt divider line
        recyclerViewWinner.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerViewWinner.setItemAnimator(new DefaultItemAnimator());

        recyclerViewWinner.setAdapter(mAdapterWon);

        title = (TextView) findViewById(R.id.matchTitle);
        dateTime = (TextView) findViewById(R.id.datetime);
        notesCard = (CardView) findViewById(R.id.specialNotesCard);
        notesText = (TextView) findViewById(R.id.notesText);
        notesTitle = (TextView) findViewById(R.id.noteTitle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle((CharSequence) "Match Result");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewWinner.setHasFixedSize(true);
        recyclerViewWinner.setLayoutManager(new LinearLayoutManager(this));

        new OneLoadAllProducts().execute();

    }

    /**
     * Prepares sample data to provide data set to adapter
     */

    class OneLoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MatchResultActivity.this);
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
            params.put(TAG_MATCHID, matchID);
            Random r = new Random();
            int i1 = r.nextInt(100);
            params.put("random",""+i1);

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url+"?rnd="+i1, "POST", params);

            // Check your log cat for JSON reponse
//            Log.d("All jsonarray: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if(success==1) {
                    // jsonarray found
                    // Getting Array of jsonarray
                    jsonarray = json.getJSONArray(TAG_RAJANR);
                    jsonarraywon = json.getJSONArray(TAG_RAJANRWON);

                    // looping through All jsonarray
                    for (int i = 0; i < jsonarraywon.length(); i++) {
                        JSONObject c = jsonarraywon.getJSONObject(i);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        //match
                        map.put(TAG_MATCHID, c.getString(TAG_MATCHID));
                        map.put(TAG_PLAYERRRANK, c.getString(TAG_PLAYERRRANK));
                        map.put(TAG_FIRSTNAME, c.getString(TAG_FIRSTNAME));
                        map.put(TAG_KILLS, c.getString(TAG_KILLS));
                        map.put(TAG_WONAMOUNT, c.getString(TAG_WONAMOUNT));

                        // adding HashList to ArrayList
                        offersListWon.add(map);
                    }

                    // looping through All jsonarray
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject c = jsonarray.getJSONObject(i);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        //match
                        map.put(TAG_MATCHID, c.getString(TAG_MATCHID));
                        map.put(TAG_PLAYERRRANK, c.getString(TAG_PLAYERRRANK));
                        map.put(TAG_FIRSTNAME, c.getString(TAG_FIRSTNAME));
                        map.put(TAG_KILLS, c.getString(TAG_KILLS));
                        map.put(TAG_WONAMOUNT, c.getString(TAG_WONAMOUNT));

                        // adding HashList to ArrayList
                        offersList.add(map);
                    }
                }
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

                        /*
                          Updating parsed JSON data into ListView
                         */
                        for (int i = 0; i < offersListWon.size(); i++) {

                            MatchResult matchresult = new MatchResult();

                            matchresult.setPosition(offersListWon.get(i).get(TAG_PLAYERRRANK));
                            matchresult.setPlayerName(offersListWon.get(i).get(TAG_FIRSTNAME));
                            matchresult.setPlayerKills(offersListWon.get(i).get(TAG_KILLS));

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("₹ ");
                            stringBuilder.append(offersListWon.get(i).get(TAG_WONAMOUNT));
                            matchresult.setPlayerWinning(stringBuilder.toString());

                            matchResultListWon.add(matchresult);

                            // notify adapter about data set changes
                            // so that it will render the list with new data
                            mAdapterWon.notifyDataSetChanged();

                        }

                        for (int i = 0; i < offersList.size(); i++) {

                            MatchResult matchresult = new MatchResult();

                            matchresult.setPosition(offersList.get(i).get(TAG_PLAYERRRANK));
                            matchresult.setPlayerName(offersList.get(i).get(TAG_FIRSTNAME));
                            matchresult.setPlayerKills(offersList.get(i).get(TAG_KILLS));

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("₹ ");
                            stringBuilder.append(offersList.get(i).get(TAG_WONAMOUNT));
                            matchresult.setPlayerWinning(stringBuilder.toString());

                            matchResultList.add(matchresult);

                            // notify adapter about data set changes
                            // so that it will render the list with new data
                            mAdapter.notifyDataSetChanged();

                        }

                    } else {
                        Toast.makeText(MatchResultActivity.this,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }
}
