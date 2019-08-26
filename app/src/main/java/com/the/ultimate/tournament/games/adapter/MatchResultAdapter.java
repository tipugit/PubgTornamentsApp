package com.the.ultimate.tournament.games.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.data.MatchResult;

import java.util.List;

public class MatchResultAdapter extends RecyclerView.Adapter<MatchResultAdapter.MyViewHolder> {

    private final List<MatchResult> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView pKills;
        final TextView pName;
        final TextView pWinning;
        final TextView position;

        MyViewHolder(View view) {
            super(view);

            position = (TextView) view.findViewById(R.id.srNo);
            pName = (TextView) view.findViewById(R.id.playerName);
            pKills = (TextView) view.findViewById(R.id.totalKills);
            pWinning = (TextView) view.findViewById(R.id.totalAmountWon);
        }
    }


    public MatchResultAdapter(List<MatchResult> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_result_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MatchResult matchResult = moviesList.get(position);

        holder.position.setText(matchResult.getPosition());
        holder.pName.setText(matchResult.getPlayerName());
        holder.pKills.setText(matchResult.getPlayerKills());
        holder.pWinning.setText(matchResult.getPlayerWinning());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
