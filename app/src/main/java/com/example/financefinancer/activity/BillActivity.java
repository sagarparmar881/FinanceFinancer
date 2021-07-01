package com.example.financefinancer.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;


import com.example.financefinancer.R;
import com.example.financefinancer.adapter.Helper;
import com.example.financefinancer.adapter.BillAdapter;


public class BillActivity extends AppCompatActivity {

    private String tripId;

    private ListView lvList;
    private BillAdapter billAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        initViews();

        Cursor result = billAdapter.getBill(Integer.parseInt(tripId));
        String[] columns = new String[] {
                Helper.COLUMN_BILL_NAME, Helper.COLUMN_BILL_AMOUNT, Helper.COLUMN_BILL_SHARE, Helper.COLUMN_BILL_MEMBERS
        };
        int[] to = new int[] {
                R.id.tvBillName, R.id.tvBillAmount, R.id.tvBillShare, R.id.tvBillMembers
        };
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_item_bill, result, columns, to, 0);
        lvList = findViewById(R.id.lvBillList);
        lvList.setAdapter(dataAdapter);
    }

    private void initViews() {
        tripId = getIntent().getStringExtra("tripId");
        this.billAdapter = new BillAdapter(this);
        this.lvList = findViewById(R.id.lvBillList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bBillAdd:
                Intent intent = new Intent(getApplicationContext(), AddBillActivity.class);
                intent.putExtra("tripId", tripId);
                startActivity(intent);
                return true;
            case R.id.bBillCalc:
                Intent intent1 = new Intent(getApplicationContext(), SplitActivity.class);
                intent1.putExtra("tripId", tripId);
                startActivity(intent1);
                return true;
            case R.id.bExit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        billAdapter.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }

}