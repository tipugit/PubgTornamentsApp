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
import com.the.ultimate.tournament.games.MatchDetailsActivity;
import com.the.ultimate.tournament.games.PrefManager;
import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.adapter.PlayAdapter;
import com.the.ultimate.tournament.games.config.config;
import com.the.ultimate.tournament.games.data.Play;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayFragment extends Fragment {

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    private ArrayList<HashMap<String, String>> offersList;
    private ArrayList<HashMap<String, String>> offersListUser;

    // url to get all products list
    private static final String url = config.mainurl + "get_all_match.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RAJANR = "rajanr";
    private static final String TAG_RAJANRUSER = "rajanruser";

    //user
    private static final String TAG_USERID = "userid";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PUBGUSERNAME = "pubgusername";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_MOBILE = "mobile";
    private static final String TAG_OTHER = "other";
    private static final String TAG_PROMOCODE = "promocode";

    //balance
    private static final String TAG_USERBALANCE = "balance";

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


    //matchdetail
    private static final String TAG_TOTALMATCHPLAYED = "totalmatchplayed";
    private static final String TAG_WONAMOUNT = "wonamount";
    private static final String TAG_KILLS = "kills";

    // products JSONArray
    private JSONArray jsonarray = null;
    private JSONArray jsonarrayuser = null;

    //Prefrance
    private static PrefManager prf;

    private int success;

    //new
    private Context context;

    private List<Play> playList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlayAdapter mAdapter;

    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout noMatchesLL;

    public PlayFragment() {
        // Required empty public constructor
    }

    public static PlayFragment newInstance(String param1, String param2) {
        PlayFragment fragment = new PlayFragment();
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
        offersListUser = new ArrayList<>();

        new OneLoadAllProducts().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViewone = inflater.inflate(R.layout.fragment_play, container, false);

        mShimmerViewContainer = (ShimmerFrameLayout) rootViewone.findViewById(R.id.shimmer_view_container);
        noMatchesLL = (LinearLayout) rootViewone.findViewById(R.id.noMatchesLL);

        mShimmerViewContainer.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) rootViewone.findViewById(R.id.recyclerView);

        playList = new ArrayList();

        mAdapter = new PlayAdapter(getActivity(), playList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep match_result_list_row.xmlrow.xml width to `match_parent`
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
                Play play = playList.get(position);

                Intent in = new Intent(getActivity(), MatchDetailsActivity.class);
                in.putExtra(TAG_MATCHID, play.getMatchID());
                in.putExtra("matchStatus", "upcoming");
                startActivity(in);
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

            // Check your log cat for JSON reponse
//            Log.d("All jsonarray: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if(success==1) {
                    // jsonarray found
                    // Getting Array of jsonarray
                    jsonarray = json.getJSONArray(TAG_RAJANR);
                    jsonarrayuser = json.getJSONArray(TAG_RAJANRUSER);

                    // looping through All jsonarray
                    for (int i = 0; i < jsonarrayuser.length(); i++) {
                        JSONObject c = jsonarrayuser.getJSONObject(i);

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
                        offersListUser.add(map);
                    }

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
            // dismiss the dialog after getting all products

            //error solved
            if(getActivity() == null)
                return;

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
                        for (int i = 0; i < offersListUser.size(); i++) {

                            // preference and set username for session
                            prf.setString(TAG_USERID, offersListUser.get(i).get(TAG_USERID));
                            prf.setString(TAG_FIRSTNAME, offersListUser.get(i).get(TAG_FIRSTNAME));
                            prf.setString(TAG_LASTNAME, offersListUser.get(i).get(TAG_LASTNAME));
                            prf.setString(TAG_USERNAME, offersListUser.get(i).get(TAG_USERNAME));
                            prf.setString(TAG_PUBGUSERNAME, offersListUser.get(i).get(TAG_PUBGUSERNAME));
                            prf.setString(TAG_GENDER, offersListUser.get(i).get(TAG_GENDER));
                            prf.setString(TAG_EMAIL, offersListUser.get(i).get(TAG_EMAIL));
                            prf.setString(TAG_MOBILE, offersListUser.get(i).get(TAG_MOBILE));
                            prf.setString(TAG_OTHER, offersListUser.get(i).get(TAG_OTHER));
                            prf.setString(TAG_PROMOCODE, offersListUser.get(i).get(TAG_PROMOCODE));

                            //balance
                            prf.setString(TAG_USERBALANCE, offersListUser.get(i).get(TAG_USERBALANCE));


                            //matchdetail
                            prf.setString(TAG_TOTALMATCHPLAYED, offersListUser.get(i).get(TAG_TOTALMATCHPLAYED));
                            prf.setString(TAG_WONAMOUNT, offersListUser.get(i).get(TAG_WONAMOUNT));
                            prf.setString(TAG_KILLS, offersListUser.get(i).get(TAG_KILLS));

                        }

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
                            play.setTotalplayer(Integer.parseInt(offersList.get(i).get(TAG_TOTALPLAYER)));
                            play.setTotalPeopleJoined(Integer.parseInt(offersList.get(i).get(TAG_TOTALPLAYERJOINED)));

                            int parseInt = Integer.parseInt(offersList.get(i).get(TAG_TOTALPLAYER)) - Integer.parseInt(offersList.get(i).get(TAG_TOTALPLAYERJOINED));
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Only ");
                            stringBuilder.append(String.valueOf(parseInt));
                            stringBuilder.append(" spots left");
                            play.setSpots(stringBuilder.toString());

                            String totalplayerjoined = offersList.get(i).get(TAG_TOTALPLAYERJOINED);
                            StringBuilder stringBuildersize = new StringBuilder();
                            stringBuildersize.append(totalplayerjoined);
                            stringBuildersize.append("/"+Integer.parseInt(offersList.get(i).get(TAG_TOTALPLAYER)));
                            play.setSize(stringBuildersize.toString());

                            play.setEntryFee(offersList.get(i).get(TAG_ENTRYFEE));
                            play.setWinPrize(offersList.get(i).get(TAG_WINPRICE));
                            play.setPerKill(offersList.get(i).get(TAG_PERKILL));
                            play.setJoin_status(offersList.get(i).get(TAG_PLAYERJOINSTATUS));
                            play.setTimeDate(offersList.get(i).get(TAG_MATCHSCHEDULE));

                            playList.add(play);

                            // notify adapter about data set changes
                            // so that it will render the list with new data
                            mAdapter.notifyDataSetChanged();

                        }

                        if (playList.size()<=0) {
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            noMatchesLL.setVisibility(View.VISIBLE);
                            return;
                        }

                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        noMatchesLL.setVisibility(View.GONE);


                    } else {
                        Toast.makeText(context,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }

}
