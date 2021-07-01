package com.example.financefinancer.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financefinancer.R;
import com.example.financefinancer.adapter.MemberAdapter;
import com.example.financefinancer.model.Member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SplitActivity extends AppCompatActivity {

    TextView tv;
    private MemberAdapter memberAdapter;
    private String tripId;
    private ArrayList < Member > listMember;
    private int noPerson;
    private String text = "";
    private String textSep = "------------------------------";
    Button share;

    static double minOfAmount(double amount, double amount2) {
        return (amount < amount2) ? amount : amount2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);

        initViews();

        Cursor cursor = memberAdapter.getMember(tripId);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            listMember.add(memberAdapter.cursorToMember(cursor));
            cursor.moveToNext();
        }
        noPerson = listMember.size();
        double[] amount = new double[noPerson];
        for (int i = 0; i < noPerson; i++) {
            amount[i] = listMember.get(i).getBalance();
        }
        minCashFlowRec(amount);
        tv = findViewById(R.id.tvSplit);
        tv.setText(text);

        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy' 'hh:mm aa", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                String timeStamp = (sdf.format(new Date()));
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Finance Financer  \n" +
                        textSep + "\n" + "Settlements" + "\n" + text + "\n" + textSep + "\n" + timeStamp);
                startActivity(Intent.createChooser(shareIntent, "Share using"));

            }
        });


    }

    private void initViews() {
        tripId = getIntent().getStringExtra("tripId");
        this.memberAdapter = new MemberAdapter(this);
        this.listMember = new ArrayList < > ();
    }

    int getMin(double[] amount) {
        int minInd = 0;
        for (int i = 1; i < noPerson; i++)
            if (amount[i] < amount[minInd])
                minInd = i;
        return minInd;
    }

    int getMax(double[] arr) {
        int maxInd = 0;
        for (int i = 1; i < noPerson; i++)
            if (arr[i] > arr[maxInd])
                maxInd = i;
        return maxInd;
    }

    // Greedy Algorithm
    void minCashFlowRec(double[] amount) {
        int mxCredit = getMax(amount), mxDebit = getMin(amount);
        // If both amounts are 0, then all amounts are settled
        if (amount[mxCredit] == 0 || amount[mxDebit] == 0)
            return;
        // Find the minimum of two amounts
        double min = minOfAmount((-amount[mxDebit]), amount[mxCredit]);
        amount[mxCredit] -= min;
        amount[mxDebit] += min;
        if (text.length() == 0) {
            text += "  - " + listMember.get(mxDebit).getName() + " pays Rs." + String.format("%.2f", min) + " to " +
                    listMember.get(mxCredit).getName();
        } else {
            text += "\n  - " + listMember.get(mxDebit).getName() + " pays Rs." + String.format("%.2f", min) + " to " +
                    listMember.get(mxCredit).getName();

        }
        minCashFlowRec(amount);
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }

}