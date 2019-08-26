package com.the.ultimate.tournament.games.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.data.TopPlayer;

import java.util.List;

public class TopPlayerAdapter extends RecyclerView.Adapter<TopPlayerAdapter.MyViewHolder> {

    private final List<TopPlayer> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView name;
        final TextView position;
        final TextView winning;

        MyViewHolder(View view) {
            super(view);
            position = (TextView) view.findViewById(R.id.playerPosition);
            name = (TextView) view.findViewById(R.id.playerName);
            winning = (TextView) view.findViewById(R.id.playerWinning);
        }
    }


    public TopPlayerAdapter(List<TopPlayer> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_player_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TopPlayer topPlayer = moviesList.get(position);
//        holder.title.setText(topPlayer.getTitle());

        if (!(position == 0)) {
            holder.position.setText(topPlayer.getPosition());
            holder.name.setText(topPlayer.getPlayerName());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("₹ ");
            stringBuilder.append(topPlayer.getPlayerWinning());
            holder.winning.setText(stringBuilder.toString());
            return;
        }

        holder.position.setText(topPlayer.getPosition());
        holder.position.setTypeface(null, Typeface.BOLD);
        holder.name.setText(topPlayer.getPlayerName());
        holder.name.setTypeface(null, Typeface.BOLD);

        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("₹ ");
        stringBuilder2.append(topPlayer.getPlayerWinning());
        holder.winning.setText(stringBuilder2.toString());
        holder.winning.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
