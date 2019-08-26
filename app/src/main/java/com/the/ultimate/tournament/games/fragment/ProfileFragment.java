package com.the.ultimate.tournament.games.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.the.ultimate.tournament.games.EditProfileActivity;
import com.the.ultimate.tournament.games.LoginActivity;
import com.the.ultimate.tournament.games.MyWalletActivity;
import com.the.ultimate.tournament.games.PrefManager;
import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.TopPlayersActivity;
import com.the.ultimate.tournament.games.config.config;

public class ProfileFragment extends Fragment {

    //user
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_EMAIL = "email";

    //balance
    private static final String TAG_USERBALANCE = "balance";

    //matchdetail
    private static final String TAG_TOTALMATCHPLAYED = "totalmatchplayed";
    private static final String TAG_WONAMOUNT = "wonamount";
    private static final String TAG_KILLS = "kills";

    //Prefrance
    private static PrefManager prf;

    private CardView aboutUs;
    private CardView privacypolicy;
    private LinearLayout amountWonLayout;
    private TextView appVersion;
    private String balance;
    private CardView customerSupport;
    private String email;
    private CardView importantUpdates;
    private CardView logOut;
    private LinearLayout matchesPlayedLayout;
    private String matches_played;
    private TextView myAmountWon;
    private TextView myBalance;
    private TextView myKills;
    private TextView myMatchesNumber;
    private CardView myProfile;
    private CardView myStatistics;
    private CardView myWallet;
    private TextView myname;
    private TextView myusername;
    private String name;
    private CardView referEarn;
    private CardView shareApp;
    private CardView topPlayers;
    private LinearLayout totalKillsLayout;
    private String total_amount_won;
    private String total_kills;
    private String username;
    private CardView howtojoin;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prf = new PrefManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViewone = inflater.inflate(R.layout.fragment_profile, container, false);

        myname = (TextView) rootViewone.findViewById(R.id.name);
        myusername = (TextView) rootViewone.findViewById(R.id.myusername);
        myBalance = (TextView) rootViewone.findViewById(R.id.myBalance);
        myMatchesNumber = (TextView) rootViewone.findViewById(R.id.matchesPlayed);
        myKills = (TextView) rootViewone.findViewById(R.id.myKills);
        myAmountWon = (TextView) rootViewone.findViewById(R.id.amountWon);
        referEarn = (CardView) rootViewone.findViewById(R.id.referCard);
        myProfile = (CardView) rootViewone.findViewById(R.id.profileCard);
        myWallet = (CardView) rootViewone.findViewById(R.id.myWalletCard);
        myStatistics = (CardView) rootViewone.findViewById(R.id.statsCard);
        topPlayers = (CardView) rootViewone.findViewById(R.id.topPlayersCard);
        importantUpdates = (CardView) rootViewone.findViewById(R.id.impUpdates);
        howtojoin = (CardView) rootViewone.findViewById(R.id.howtoJoinCard);
        aboutUs = (CardView) rootViewone.findViewById(R.id.aboutUsCard);
        privacypolicy = (CardView) rootViewone.findViewById(R.id.privacypolicyCard);
        shareApp = (CardView) rootViewone.findViewById(R.id.shareCard);
        logOut = (CardView) rootViewone.findViewById(R.id.logOutCard);
        appVersion = (TextView) rootViewone.findViewById(R.id.appVersion);
        customerSupport = (CardView) rootViewone.findViewById(R.id.customerSupportCard);
        matchesPlayedLayout = (LinearLayout) rootViewone.findViewById(R.id.matchesPlayedLL);
        totalKillsLayout = (LinearLayout) rootViewone.findViewById(R.id.totalKillsLL);
        amountWonLayout = (LinearLayout) rootViewone.findViewById(R.id.amountWonLL);

        username = prf.getString(TAG_USERNAME);
        email = prf.getString(TAG_EMAIL);
        name = prf.getString(TAG_FIRSTNAME);
        balance = prf.getString(TAG_USERBALANCE);
        matches_played = prf.getString(TAG_TOTALMATCHPLAYED);
        total_kills = prf.getString(TAG_KILLS);
        total_amount_won = prf.getString(TAG_WONAMOUNT);
        myname.setText(name);
        myusername.setText(username);
        myBalance.setText("₹ "+balance);
        myMatchesNumber.setText(matches_played);
        myAmountWon.setText(total_amount_won);
        myKills.setText(total_kills);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), EditProfileActivity.class);
                getContext().startActivity(myIntent);
            }
        });
        myWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), MyWalletActivity.class);
                getContext().startActivity(myIntent);
            }
        });
        topPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), TopPlayersActivity.class);
                getContext().startActivity(myIntent);
            }
        });
        howtojoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(config.howtojoin));
                startActivity(browserIntent);
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(config.main));
                startActivity(browserIntent);
            }
        });
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(config.privacypolicy));
                startActivity(browserIntent);
            }
        });
        customerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(config.main));
                startActivity(browserIntent);
            }
        });

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent("android.intent.action.SEND");
                in.setType("text/plain");
                String string = "https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
                in.putExtra("android.intent.extra.SUBJECT", getString(R.string.shareSub));
                in.putExtra("android.intent.extra.TEXT", string);
                startActivity(Intent.createChooser(in, "Share using"));
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prf.clearall();
                getActivity().finish();
                Toast.makeText(getActivity(), "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return rootViewone;
    }
}
