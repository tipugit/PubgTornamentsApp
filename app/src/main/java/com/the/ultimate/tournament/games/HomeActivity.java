package com.the.ultimate.tournament.games;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.config.config;
import com.the.ultimate.tournament.games.fragment.OngoingFragment;
import com.the.ultimate.tournament.games.fragment.ResultFragment;
import com.the.ultimate.tournament.games.fragment.PlayFragment;
import com.the.ultimate.tournament.games.fragment.ProfileFragment;
import com.the.ultimate.tournament.games.fragment.EarnFragment;
import com.the.ultimate.tournament.games.helper.BottomNavigationBehavior;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    // Creating JSON Parser object
    private final JSONParser jsonParser = new JSONParser();

    // url to get all products list
    private static final String url = config.mainurl + "app_updater.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    //user
    private static final String TAG_USERID = "userid";

    //Prefrance
    private static PrefManager prf;

    //back
    private static int backbackexit=1;

    private int success;
    private String newversion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        prf = new PrefManager(HomeActivity.this);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        //off shift mode
//        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        loadFragment(new PlayFragment());
        navigation.setSelectedItemId(R.id.navigation_play);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_earn:
                    fragment = new EarnFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_ongoing:
                    fragment = new OngoingFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_play:
                    fragment = new PlayFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_result:
                    fragment = new ResultFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.navigation_play != seletedItemId) {
            setHomeItem(HomeActivity.this);
        } else {
            if (backbackexit >= 2) {

                    // Creating alert Dialog with three Buttons

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            HomeActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle(getResources().getString(R.string.app_name));

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want to exit??");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.icon);

                    // Setting Positive Yes Button
                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            });
                    // Setting Positive Yes Button
                    alertDialog.setNeutralButton("NO",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    // Showing Alert Message
                    alertDialog.show();
//					super.onBackPressed();
            } else {
                backbackexit++;
                Toast.makeText(getBaseContext(), "Press again to Exit", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_play);
    }

    private class OneLoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                newversion = json.getString("newversion");

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

                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            String version = pInfo.versionName;
                            if(version.equalsIgnoreCase(newversion)) {

                            } else {
                                Intent intent = new Intent(HomeActivity.this, AppUpdaterActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
//                    else if(success == 2){
//                        // no jsonarray found
////                        Toast.makeText(HomeActivity.this,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();
//
//                    }
                    else {
                        Toast.makeText(HomeActivity.this,"Something went wrong. Try again!",Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }

}
