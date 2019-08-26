package com.the.ultimate.tournament.games.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.the.ultimate.tournament.games.R;
import com.the.ultimate.tournament.games.data.Transactions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.MyViewHolder> {

    private final List<Transactions> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        final TextView txnAmount;
        final TextView txnDate;
        final TextView txnRemark;
        final TextView txnType;

        MyViewHolder(View view) {
            super(view);

            txnType = (TextView) view.findViewById(R.id.txnType);
            txnRemark = (TextView) view.findViewById(R.id.txnRemark);
            txnDate = (TextView) view.findViewById(R.id.txnDate);
            txnAmount = (TextView) view.findViewById(R.id.txnAmount);
        }
    }


    public TransactionsAdapter(List<Transactions> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transactions_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transactions transactions = moviesList.get(position);

        //Input date in String format
        String input = transactions.getTxnDate();
        //Date/time pattern of input date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        Date date;
        String output = null;
        try {
            //Conversion of input String to date
            date = df.parse(input);
            //old date format to new date format
            output = outputformat.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        holder.txnDate.setText(output);
        holder.txnRemark.setText(transactions.getTxnRemark());
        CharSequence txnType = transactions.getTxnType();
        if (txnType.equals("1")) {
            holder.txnType.setText("CREDIT");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("+ ₹");
            stringBuilder.append(transactions.getTxnAmount());
            holder.txnAmount.setText(stringBuilder.toString());
        } else if (txnType.equals("0")) {
            holder.txnType.setText("DEBIT");
            holder.txnType.setTextColor(Color.parseColor("#ff0000"));

            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("- ₹");
            stringBuilder2.append((-1) * Integer.parseInt(transactions.getTxnAmount()));
            holder.txnAmount.setText(stringBuilder2.toString());
            holder.txnAmount.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
