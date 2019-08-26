package com.the.ultimate.tournament.games.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.the.ultimate.tournament.games.JSONParser;
import com.the.ultimate.tournament.games.MatchResultActivity;
import com.the.ultimate.tournament.games.PrefManager;
import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.adapter.ResultAdapter;
import com.the.ultimate.tournament.games.config.config;
import com.the.ultimate.tournament.games.data.Play;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultFragment extends Fragment {

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    private ArrayList<HashMap<String, String>> offersList;

    // url to get all products list
    private static final String url = config.mainurl + "get_all_result.php?1=123";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RAJANR = "rajanr";

    //user
    private static final String TAG_USERID = "userid";
    private static final String TAG_USERNAME = "username";

    //playerjoinstatus
    private static final String TAG_PLAYERJOINSTATUS = "playerjoinstatus";

    //match
    private static final String TAG_MATCHID = "matchid";
    private static final String TAG_ROOMID = "roomid";
    private static final String TAG_ROOMPASSWORD = "roompassword";
    private static final String TAG_TYPE = "type";
    private static final String TAG_VERSION = "version";
    private static final String TAG_MAP = "map";
    private static final String TAG_MAINTOPBANNERIMG = "maintopbannerimg";
    private static final String TAG_ICONIMG = "iconimg";
    private static final String TAG_MATCHTYPE = "matchtype";
    private static final String TAG_TOTALPLAYER = "totalplayer";
    private static final String TAG_TOTALPLAYERJOINED = "totalplayerjoined";
    private static final String TAG_ENTRYFEE = "entryfee";
    private static final String TAG_WINPRICE = "winprice";
    private static final String TAG_PERKILL = "perkill";
    private static final String TAG_JOINSTATUS = "joinstatus";
    private static final String TAG_MATCHSTATUS = "matchstatus";
    private static final String TAG_MATCHSCHEDULE = "matchschedule";
    private static final String TAG_LOG_ENTDATE = "log_entdate";


    // products JSONArray
    private JSONArray jsonarray = null;

    //Prefrance
    private static PrefManager prf;

    private int success;

    //new
    private Context context;

    private List<Play> matches = new ArrayList<>();
    private RecyclerView recyclerView;
    private ResultAdapter mAdapter;

    private ShimmerFrameLayout mShimmerViewContainer;

    private LinearLayout noResults;
    private LinearLayout upcomingLinearLayout;
    private String username;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View rootViewone = inflater.inflate(R.layout.fragment_result, container, false);

        mShimmerViewContainer = (ShimmerFrameLayout) rootViewone.findViewById(R.id.shimmer_view_container);
        recyclerView = (RecyclerView) rootViewone.findViewById(R.id.recyclerView);

        mShimmerViewContainer.setVisibility(View.VISIBLE);

        matches = new ArrayList();

        mAdapter = new ResultAdapter(getActivity(), matches);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep match_result_list_rowist_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        // horizontal RecyclerView
        // keep match_result_list_rowist_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // row click listenerMyDividerItemDecoration
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Play play = matches.get(position);

                Intent in = new Intent(getActivity(), MatchResultActivity.class);
                in.putExtra(TAG_MATCHID, play.getMatchID());
                in.putExtra("matchTitle", play.getTitle());
                in.putExtra(TAG_WINPRICE, play.getWinPrize());
                in.putExtra(TAG_PERKILL, play.getPerKill());
                in.putExtra(TAG_ENTRYFEE, play.getEntryFee());
                in.putExtra(TAG_MATCHSCHEDULE, play.getTimeDate());
                startActivity(in);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        username = prf.getString(TAG_USERNAME);
        upcomingLinearLayout = (LinearLayout) rootViewone.findViewById(R.id.upcomingLL);
        noResults = (LinearLayout) rootViewone.findViewById(R.id.noResultsLL);

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

            // Check your log cat for JSON reponse
//            Log.d("All jsonarray: ", json.toString());

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
                        map.put(TAG_MATCHID, c.getString(TAG_MATCHID));
                        map.put(TAG_ROOMID, c.getString(TAG_ROOMID));
                        map.put(TAG_ROOMPASSWORD, c.getString(TAG_ROOMPASSWORD));
                        map.put(TAG_TYPE, c.getString(TAG_TYPE));
                        map.put(TAG_VERSION, c.getString(TAG_VERSION));
                        map.put(TAG_MAP, c.getString(TAG_MAP));
                        map.put(TAG_MAINTOPBANNERIMG, c.getString(TAG_MAINTOPBANNERIMG));
                        map.put(TAG_ICONIMG, c.getString(TAG_ICONIMG));
                        map.put(TAG_MATCHTYPE, c.getString(TAG_MATCHTYPE));
                        map.put(TAG_TOTALPLAYER, c.getString(TAG_TOTALPLAYER));
                        map.put(TAG_TOTALPLAYERJOINED, c.getString(TAG_TOTALPLAYERJOINED));
                        map.put(TAG_ENTRYFEE, c.getString(TAG_ENTRYFEE));
                        map.put(TAG_WINPRICE, c.getString(TAG_WINPRICE));
                        map.put(TAG_PERKILL, c.getString(TAG_PERKILL));
                        map.put(TAG_JOINSTATUS, c.getString(TAG_JOINSTATUS));
                        map.put(TAG_MATCHSTATUS ,c.getString(TAG_MATCHSTATUS));
                        map.put(TAG_MATCHSCHEDULE, c.getString(TAG_MATCHSCHEDULE));
                        map.put(TAG_LOG_ENTDATE, c.getString(TAG_LOG_ENTDATE));

                        //player join status
                        map.put(TAG_PLAYERJOINSTATUS, c.getString(TAG_PLAYERJOINSTATUS));

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

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
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

                            Play play = new Play();
                            play.setTitle(offersList.get(i).get(TAG_MATCHID));
                            play.setMatchID(offersList.get(i).get(TAG_MATCHID));
                            play.setType(offersList.get(i).get(TAG_TYPE));
                            play.setVersion(offersList.get(i).get(TAG_VERSION));
                            play.setMap(offersList.get(i).get(TAG_MAP));
                            play.setTopImage(offersList.get(i).get(TAG_MAINTOPBANNERIMG));
                            play.setImgURL(offersList.get(i).get(TAG_ICONIMG));
                            play.setMatchType(offersList.get(i).get(TAG_MATCHTYPE));
                            play.setTotalPeopleJoined(Integer.parseInt(offersList.get(i).get(TAG_TOTALPLAYERJOINED)));
                            play.setEntryFee(offersList.get(i).get(TAG_ENTRYFEE));
                            play.setWinPrize(offersList.get(i).get(TAG_WINPRICE));
                            play.setPerKill(offersList.get(i).get(TAG_PERKILL));
                            play.setJoin_status(offersList.get(i).get(TAG_PLAYERJOINSTATUS));
                            play.setTimeDate(offersList.get(i).get(TAG_MATCHSCHEDULE));

                            matches.add(play);

                            // notify adapter about data set changes
                            // so that it will render the list with new data
                            mAdapter.notifyDataSetChanged();

                        }

                        if (matches.size()<=0) {
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            noResults.setVisibility(View.VISIBLE);
                            return;
                        }

                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        noResults.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(context,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }
}
