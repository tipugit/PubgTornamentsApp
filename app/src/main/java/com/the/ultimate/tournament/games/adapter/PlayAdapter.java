package com.the.ultimate.tournament.games.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.the.ultimate.tournament.games.JoinMatchConfirmationActivity;
import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.config.config;
import com.the.ultimate.tournament.games.data.Play;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class PlayAdapter extends RecyclerView.Adapter<PlayAdapter.MyViewHolder> {

    private final Context ctx;

    private final List<Play> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView title;

        final CardView cardView;
        final TextView fee;
        final ImageView img;
        final Button joinBtn;
        final TextView map;
        final MaterialProgressBar materialProgressBar;
        final TextView perkill;
        final RelativeLayout privateTextArea;
        final TextView prize;
        final TextView size;
        final TextView sponsorText;
        final RelativeLayout sponsorTextArea;
        final TextView spot;
        final TextView timedate;
        final ImageView topImage;
        final TextView type;
        final TextView version;

        MyViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.mainCard);
            img = (ImageView) view.findViewById(R.id.img);
            title = (TextView) view.findViewById(R.id.title);
            timedate = (TextView) view.findViewById(R.id.timedate);
            prize = (TextView) view.findViewById(R.id.winPrize);
            perkill = (TextView) view.findViewById(R.id.perKill);
            fee = (TextView) view.findViewById(R.id.entryFee);
            spot = (TextView) view.findViewById(R.id.spots);
            size = (TextView) view.findViewById(R.id.size);
            materialProgressBar = (MaterialProgressBar) view.findViewById(R.id.progressBar);
            joinBtn = (Button) view.findViewById(R.id.joinButton);
            sponsorTextArea = (RelativeLayout) view.findViewById(R.id.sponsorTextArea);
            sponsorText = (TextView) view.findViewById(R.id.sponsorText);
            type = (TextView) view.findViewById(R.id.matchType);
            version = (TextView) view.findViewById(R.id.matchVersion);
            map = (TextView) view.findViewById(R.id.matchMap);
            topImage = (ImageView) view.findViewById(R.id.mainTopBanner);
            privateTextArea = (RelativeLayout) view.findViewById(R.id.privateTextArea);
        }
    }


    public PlayAdapter(Context context, List<Play> moviesList) {
        ctx = context;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Play play = moviesList.get(position);

        holder.title.setText(play.getTitle());
        holder.title.setText("Match#" + play.getTitle());
        TextView textView = holder.timedate;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Time: ");

        //Input date in String format
        String input = play.getTimeDate();
        //Date/time pattern of input date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputtimeformat = new SimpleDateFormat("hh:mm aa");
        Date date;
        String output = null;
        try {
            //Conversion of input String to date
            date = df.parse(input);
            //old date format to new date format
            output = outputformat.format(date) + " at " + outputtimeformat.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        stringBuilder.append(output);
        textView.setText(stringBuilder.toString());

        holder.prize.setText(play.getWinPrize());
        holder.perkill.setText(play.getPerKill());
        String matchType = play.getMatchType();
        if (matchType.equals("Free")) {
            holder.fee.setText("FREE");
            holder.fee.setTextColor(Color.parseColor("#1E7E34"));
        } else if (matchType.equals("Sponsored")) {
            textView = holder.sponsorText;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Sponsored by ");
            stringBuilder.append(play.getSponsoredby());
            textView.setText(stringBuilder.toString());
            holder.sponsorTextArea.setVisibility(View.VISIBLE);
            holder.fee.setText("FREE");
            holder.fee.setTextColor(Color.parseColor("#1E7E34"));
        } else if (matchType.equals("Giveaway")) {
            textView = holder.sponsorText;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Giveaway by ");
            stringBuilder.append(play.getSponsoredby());
            textView.setText(stringBuilder.toString());
            holder.sponsorTextArea.setVisibility(View.VISIBLE);
            holder.fee.setText("FREE");
            holder.fee.setTextColor(Color.parseColor("#1E7E34"));
        } else {
            holder.fee.setText(play.getEntryFee());
        }

        holder.type.setText(play.getType());
        holder.version.setText(play.getVersion());
        holder.map.setText(play.getMap());
        holder.size.setText(play.getSize());
        if (play.getImgURL().contains("png") || play.getImgURL().contains("jpg")) {
            String img = config.mainimg + play.getImgURL();
            Glide.with(ctx).load(img).placeholder(R.drawable.circlesmall).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img);
        }
        if (play.getTopImage().contains("png") || play.getTopImage().contains("jpg")) {
            holder.topImage.setVisibility(View.VISIBLE);
            String img = config.mainimg + play.getTopImage();
            Glide.with(ctx).load(img).placeholder(R.drawable.wp).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.topImage);
        }
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(ctx, MatchDetailsActivity.class);
//                intent.putExtra(TAG_MATCHID, play.getMatchID());
//                intent.putExtra("matchStatus", "upcoming");
//                ctx.startActivity(intent);
//                return;
//            }
//        });
        final int totalPeopleJoined = play.getTotalPeopleJoined();

        holder.joinBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Integer.parseInt(play.getJoin_status()) == 1) {
                    Toast.makeText(ctx, "Match Already Joinned", Toast.LENGTH_LONG).show();
                    return;
                }
                if (totalPeopleJoined >= play.getTotalplayer()) {
                    Toast.makeText(ctx, "No Spots Left! Match is Full.", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(ctx, JoinMatchConfirmationActivity.class);
                intent.putExtra("matchType", play.getMatchType());
                intent.putExtra("matchID", play.getMatchID());
                intent.putExtra("entryFee", play.getEntryFee());
                intent.putExtra("JoinStatus", play.getJoin_status());
                intent.putExtra("isPrivate", play.getIsPrivateMatch());
                ctx.startActivity(intent);
            }
        });

        holder.materialProgressBar.setProgress(play.getTotalPeopleJoined());
        if (totalPeopleJoined >= play.getTotalplayer()) {
            holder.spot.setTextColor(Color.parseColor("#ff0000"));
            holder.spot.setText("No Spots Left! Match is Full.");
            holder.joinBtn.setText("MATCH FULL");
            holder.joinBtn.setTextColor(Color.parseColor("#ffffff"));
            holder.joinBtn.setBackgroundResource(R.drawable.buttonbackactive);
            holder.joinBtn.setClickable(false);
        } else {
            holder.spot.setText(play.getSpots());
        }
        if (Integer.parseInt(play.getJoin_status()) == 0 && totalPeopleJoined < play.getTotalplayer()) {
            holder.joinBtn.setText("Join");
        } else if (Integer.parseInt(play.getJoin_status()) == 1 && totalPeopleJoined < play.getTotalplayer()) {
            holder.joinBtn.setText("Joinned");
            holder.joinBtn.setTextColor(Color.parseColor("#ffffff"));
            holder.joinBtn.setBackgroundResource(R.drawable.buttonbackactive);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
