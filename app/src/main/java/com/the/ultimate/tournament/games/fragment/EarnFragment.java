package com.the.ultimate.tournament.games.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.ultimate.tournament.games.PrefManager;
import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.config.config;

public class EarnFragment extends Fragment {

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RAJANR = "rajanr";

    //user
    private static final String TAG_USERNAME = "username";

    //Prefrance
    private static PrefManager prf;

    //new
    private Context context;

    private ImageView howrefer;
    private TextView referCode;
    private TextView referDesc;
    private Button referNow;
    private LinearLayout referralOfferLL;

    public EarnFragment() {
        // Required empty public constructor
    }

    public static EarnFragment newInstance(String param1, String param2) {
        EarnFragment fragment = new EarnFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        prf = new PrefManager(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootViewone = inflater.inflate(R.layout.fragment_earn, container, false);

        referNow = (Button) rootViewone.findViewById(R.id.referButton);
        referDesc = (TextView) rootViewone.findViewById(R.id.refMessage);
        referCode = (TextView) rootViewone.findViewById(R.id.referCode);
        howrefer = (ImageView) rootViewone.findViewById(R.id.howrefer);
        referralOfferLL = (LinearLayout) rootViewone.findViewById(R.id.referralLL);

        referCode.setText(prf.getString(TAG_USERNAME));

        referNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent("android.intent.action.SEND");
                in.setType("text/plain");
                String string = "Download Apk From " +config.main + " Promo code is: " +prf.getString(TAG_USERNAME);
                in.putExtra("android.intent.extra.SUBJECT", getString(R.string.shareSub));
                in.putExtra("android.intent.extra.TEXT", string);
                startActivity(Intent.createChooser(in, "Share using"));
            }
        });

        return rootViewone;
    }
}
