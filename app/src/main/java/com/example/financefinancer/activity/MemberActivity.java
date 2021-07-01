package com.example.financefinancer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financefinancer.R;
import com.example.financefinancer.adapter.Helper;
import com.example.financefinancer.adapter.MemberAdapter;
import com.example.financefinancer.model.Member;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MemberActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private String tripId;
    private ListView lvList;
    private MemberAdapter memberAdapter;
    private ArrayList < Long > listMember;
    private TextView tvEmpty;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference().child("tables").child("member");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        initViews();
        try {
            Cursor cursor = memberAdapter.getMember(tripId);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listMember.add(cursor.getLong(cursor.getColumnIndexOrThrow(Helper.COLUMN_MEMBER_ID)));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cursor result = memberAdapter.getMember(tripId);
        if (result.getCount() != 0) {
            tvEmpty.setVisibility(TextView.GONE);
            String[] columns = new String[] {
                    Helper.COLUMN_MEMBER_NAME
            };
            int[] to = new int[] {
                    R.id.tvItemMemberName
            };
            SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_item_member, result, columns, to, 0);
            lvList.setAdapter(dataAdapter);
        } else {
            tvEmpty.setVisibility(TextView.VISIBLE);
            tvEmpty.setText("Click on '+' to add new member");
        }

    }

    private void initViews() {
        tripId = getIntent().getStringExtra("tripId");
        memberAdapter = new MemberAdapter(this);
        this.tvEmpty = findViewById(R.id.tvMemberEmpty);
        this.listMember = new ArrayList < > ();
        this.lvList = findViewById(R.id.lvMemberList);
        this.lvList.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bAdd:
                addTrip();
                return true;
            case R.id.bNext:
                Intent intent = new Intent(getBaseContext(), BillActivity.class);
                intent.putExtra("tripId", tripId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addTrip() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MemberActivity.this);
        alertDialog.setTitle("Add Member");
        alertDialog.setMessage("What's the name?");
        final EditText input = new EditText(MemberActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().matches("")) {
                            Toast.makeText(MemberActivity.this, "Enter member name!", Toast.LENGTH_SHORT).show();
                        } else {
                            Member createdMember = memberAdapter.createMember(input.getText().toString(), Integer.parseInt(tripId));
                            Toast.makeText(MemberActivity.this, createdMember.getName() + " member added!", Toast.LENGTH_SHORT).show();

                            //TODO:
                            insertFirebase(createdMember);
                            //TODO:

                            Intent intent = new Intent(getBaseContext(), MemberActivity.class);
                            intent.putExtra("tripId", tripId);
                            startActivity(intent);
                        }
                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView < ? > parent, View view, final int position, long id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MemberActivity.this);
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are you sure?");
        alertDialog.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        memberAdapter.deleteMemberById(listMember.get(position).toString());
                        Intent intent = new Intent(getBaseContext(), MemberActivity.class);
                        intent.putExtra("tripId", tripId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        memberAdapter.close();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), TripActivity.class);
        intent.putExtra("tripId", tripId);
        startActivity(intent);
    }

    private void insertFirebase(Member member_name) {
        mRef.child(String.valueOf(member_name.getId())).setValue(member_name);
    }
}