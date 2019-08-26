package com.the.ultimate.tournament.games.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.config.config;
import com.the.ultimate.tournament.games.data.Ongoing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OngoingAdapter extends RecyclerView.Adapter<OngoingAdapter.MyViewHolder> {

    private final Context ctx;

    private final List<Ongoing> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final CardView cardView;
        final TextView fee;
        final ImageView img;
        final TextView map;
        final TextView perkill;
        final Button playBtn;
        final LinearLayout playbtnLL;
        final TextView prize;
        TextView size;
        final Button spectateBtn;
        final TextView sponsorText;
        final RelativeLayout sponsorTextArea;
        TextView spot;
        final TextView timedate;
        final TextView title;
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
            playBtn = (Button) view.findViewById(R.id.playButton);
            spectateBtn = (Button) view.findViewById(R.id.spectateButton);
            sponsorTextArea = (RelativeLayout) view.findViewById(R.id.sponsorTextArea);
            sponsorText = (TextView) view.findViewById(R.id.sponsorText);
            type = (TextView) view.findViewById(R.id.matchType);
            version = (TextView) view.findViewById(R.id.matchVersion);
            map = (TextView) view.findViewById(R.id.matchMap);
            playbtnLL = (LinearLayout) view.findViewById(R.id.playButtonLL);
            topImage = (ImageView) view.findViewById(R.id.mainTopBanner);
        }
    }


    public OngoingAdapter(Context context, List<Ongoing> moviesList) {
        ctx = context;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ongoing_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Ongoing ongoing = moviesList.get(position);

        holder.title.setText("Match#"+ongoing.getTitle());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Time: ");

        //Input date in String format
        String input = ongoing.getTimeDate();
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
        stringBuilder.append(output);
        holder.timedate.setText(stringBuilder.toString());

        holder.prize.setText(ongoing.getWinPrize());
        holder.perkill.setText(ongoing.getPerKill());

        String matchType = ongoing.getMatchType();
        if (matchType.equals("Free")) {
            holder.fee.setText("FREE");
            holder.fee.setTextColor(Color.parseColor("#1E7E34"));
        } else if (matchType.equals("Sponsored")) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Sponsored by ");
            stringBuilder.append(ongoing.getSponsoredby());
            holder.sponsorText.setText(stringBuilder.toString());
            holder.sponsorTextArea.setVisibility(View.VISIBLE);

            holder.fee.setText("FREE");
            holder.fee.setTextColor(Color.parseColor("#1E7E34"));
        } else if (matchType.equals("Giveaway")) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Giveaway by ");
            stringBuilder.append(ongoing.getSponsoredby());
            holder.sponsorText.setText(stringBuilder.toString());
            holder.sponsorTextArea.setVisibility(View.VISIBLE);

            holder.fee.setText("FREE");
            holder.fee.setTextColor(Color.parseColor("#1E7E34"));
        } else {
            holder.fee.setText(ongoing.getEntryFee());
        }
        holder.type.setText(ongoing.getType());
        holder.version.setText(ongoing.getVersion());
        holder.map.setText(ongoing.getMap());
        if (ongoing.getImgURL().contains("png") || ongoing.getImgURL().contains("jpg")) {
            String img = config.mainimg + ongoing.getImgURL();
            Glide.with(ctx).load(img).placeholder(R.drawable.circlesmall).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img);
        }
        if (ongoing.getTopImage().contains("png") || ongoing.getTopImage().contains("jpg")) {
            holder.topImage.setVisibility(View.VISIBLE);
            String img = config.mainimg + ongoing.getTopImage();
            Glide.with(ctx).load(img).placeholder(R.drawable.wp).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.topImage);
        }

        if (Integer.parseInt(ongoing.getJoin_status()) == 0) {
            holder.playbtnLL.setVisibility(View.GONE);
        }
        holder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = ctx.getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage("com.tencent.ig");
                try {
                    if (intent != null) {
                        ctx.startActivity(intent);
                    } else {
                        // Bring user to the market or let them choose an app?
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("market://details?id=" + "com.tencent.ig"));
                        if (intent != null) {
                            ctx.startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.spectateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PackageManager pm = ctx.getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage("com.google.android.youtube");
                try {
                    if (intent != null) {
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(config.youtubechannel));
                        ctx.startActivity(intent);
                    } else {
                        // Bring user to the market or let them choose an app?
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("market://details?id=" + "com.google.android.youtube"));
                        if (intent != null) {
                            ctx.startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
