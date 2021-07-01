package com.example.financefinancer.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financefinancer.R;
import com.example.financefinancer.adapter.BillAdapter;
import com.example.financefinancer.adapter.Helper;
import com.example.financefinancer.adapter.MemberAdapter;
import com.example.financefinancer.model.Member;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddBillActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static boolean addedBill = false;
    SparseBooleanArray array;
    int noPerson;
    private String tripId;
    private EditText etName, etAmount;
    private ListView lvList;
    private MemberAdapter memberAdapter;
    private ArrayList < Member > listMember;

    private Spinner membersSpinner;
    private int memberPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        initViews();
        Cursor cursor = memberAdapter.getMember(tripId);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            listMember.add(new Member(cursor.getString(cursor.getColumnIndexOrThrow(Helper.COLUMN_MEMBER_NAME)), Integer.parseInt(tripId)));
            cursor.moveToNext();
        }

        Cursor result = memberAdapter.getMember(tripId);
        String[] columns = new String[] {
                Helper.COLUMN_MEMBER_NAME
        };
        int[] to = new int[] {
                R.id.tvItemAddBillName
        };
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_item_add_bill, result, columns, to, 0);

        //New EMPTY LIST for member list Drop Down
        List < String > membersDropDown = new ArrayList < String > ();
        ArrayAdapter < String > memberDataAdapter = new ArrayAdapter < String > (this, R.layout.spinner, membersDropDown);
        for (int i = 0; i < listMember.size(); i++) {
            String memberName = listMember.get(i).getName();
            membersDropDown.add(memberName);
        }
        //TODO:
        memberDataAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        membersSpinner.setAdapter(memberDataAdapter);
        membersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView < ? > parent, View view, int position, long id) {
                memberPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView < ? > parent) {
                // If an option is removed then what to do
                // or anything else
            }

        });

        //TODO:
        lvList.setAdapter(dataAdapter);
    }



    private void initViews() {
        tripId = getIntent().getStringExtra("tripId");
        this.memberAdapter = new MemberAdapter(this);
        this.listMember = new ArrayList < > ();
        this.etName = findViewById(R.id.etAddBillName);
        this.etAmount = findViewById(R.id.etAddBillAmount);



        //TODO: Check Code here
        this.membersSpinner = findViewById(R.id.spinner3);
        Button bAdd = findViewById(R.id.bAddBillAdd);
        this.lvList = findViewById(R.id.lvAddBillMember);

        this.lvList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        bAdd.setOnClickListener(this);
        this.lvList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAddBillAdd:
                if (etName.getText().toString().matches("") || etAmount.getText().toString().matches("")) {
                    Toast.makeText(this, "one or more fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    addNewBill();
                }

                if (addedBill)
                    showBill();
                else
                    Toast.makeText(this, "add member bill", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void insertMember() {
        noPerson = listMember.size();
        double total = 0;
        int noPay = array.size();
        double[] amount = new double[noPerson];
        double share;
        for (int j = 0; j < noPerson; j++)
            amount[j] = -1;
        for (int j = 0; j < noPerson; j++)
            amount[j] = listMember.get(j).getAmount();
        for (int j = 0; j < noPerson; j++)
            if (amount[j] != -1)
                total += amount[j];
        share = total / noPay;
        for (int j = 0; j < noPerson; j++)
            if (amount[j] != -1) {
                if (amount[j] - share > 0)
                    listMember.get(j).setCredit(listMember.get(j).getCredit() + amount[j] -
                            share);
                else
                    listMember.get(j).setDebit(listMember.get(j).getDebit() + share -
                            amount[j]);
            }
        String members = "";
        for (int i = 0; i < noPerson; i++) {
            double temp = memberAdapter.getBalance(tripId, listMember.get(i).getName());
            amount[i] = temp + listMember.get(i).getCredit() - listMember.get(i).getDebit();
            listMember.get(i).setBalance(amount[i]);
            memberAdapter.updateBalance(tripId, listMember.get(i).getName(), listMember.get(i).getBalance());
            members += listMember.get(i).getName();
            if (i < noPerson - 1)
                members += ", ";
        }
        insertBill(share, members);
    }

    private void insertBill(double share, String members) {

        double roundShare = share;
        DecimalFormat df = new DecimalFormat("#.##");
        share = Double.valueOf(df.format(roundShare));

        BillAdapter billAdapter = new BillAdapter(this);
        billAdapter.createBill(etName.getText().toString(), etAmount.getText().toString(), share, members, tripId);
        Toast.makeText(AddBillActivity.this, etName.getText().toString() + " bill added!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), BillActivity.class);
        intent.putExtra("tripId", tripId);
        startActivity(intent);
    }

    private void showBill() {
        if (etName.getText().toString().matches("") || etAmount.getText().toString().matches("")) {
            Toast.makeText(this, "one or more fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }
        int billAmount = 0;
        for (int i = 0; i < listMember.size(); i++)
            if (listMember.get(i).getAmount() != -1)
                billAmount += listMember.get(i).getAmount();
        if (!etAmount.getText().toString().matches(billAmount + "")) {
            Toast.makeText(this, "member bill don't add up to total bill", Toast.LENGTH_SHORT).show();
            addedBill = false;
        } else
            insertMember();
    }

    private void addNewBill() {
        //TODO: Select all members to split the current bill
        //This functionality can be changed
        //It can split with selected members in the list also
        for (int i = 0; i < listMember.size(); i++) {
            lvList.setItemChecked(i, true);
        }
        //TODO: SPLIT BILL WITH ALL MEMBERS [DEFAULT]

        array = lvList.getCheckedItemPositions();
        int i = memberPosition;
        listMember.get(array.keyAt(i)).setAmount(Double.parseDouble(etAmount.getText().toString()));

        addedBill = true;

        for (int j = 0; j < array.size(); j++) {
            if (j != memberPosition) {
                listMember.get(array.keyAt(j)).setAmount(Double.parseDouble("00000"));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView < ? > parent, View view, int position, long id) {

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