package com.example.financefinancer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.financefinancer.adapter.TripAdapter;
import com.example.financefinancer.model.Trip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TripActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private boolean doubleBackToExitPressedOnce;
    private ListView lvList;
    private TextView tvEmpty;
    private TripAdapter tripAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference().child("tables").child("trip");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        initViews();

        Cursor result = tripAdapter.getTrip();
        if (result.getCount() != 0) {
            tvEmpty.setVisibility(TextView.GONE);
            String[] columns = new String[] {
                    Helper.COLUMN_TRIP_NAME
            };
            int[] to = new int[] {
                    R.id.tvItemTripName
            };
            SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.list_item_trip, result, columns, to, 0);
            lvList.setAdapter(dataAdapter);
        } else {
            tvEmpty.setVisibility(TextView.VISIBLE);
            tvEmpty.setText("Click on 'ADD GROUP'\n to add new group!");
        }

  /*if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        }
*/
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstRun", false);
        editor.commit();
    }


    private void initViews() {
        this.doubleBackToExitPressedOnce = false;
        this.tripAdapter = new TripAdapter(this);
        this.tvEmpty = findViewById(R.id.tvTripEmpty);
        this.lvList = findViewById(R.id.lvTripList);
        this.lvList.setOnItemClickListener(this);
        this.lvList.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView < ? > parent, View view, int position, long id) {
        Cursor cursor = (Cursor) lvList.getItemAtPosition(position);
        String curId = cursor.getString(cursor.getColumnIndexOrThrow(Helper.COLUMN_TRIP_ID));
        Intent intent = new Intent(getBaseContext(), MemberActivity.class);
        intent.putExtra("tripId", curId);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView < ? > parent, View view, int position, long id) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bAdd:
                addTrip();
                return true;
            case R.id.bExit:
                finish();
                return true;
            case R.id.bTutorial:
                Intent intent1 = new Intent(getBaseContext(), OnBoardingActivity.class);
                startActivity(intent1);
                return true;
            case R.id.bAbout:
                Intent intent2 = new Intent(getBaseContext(), AboutUs.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addTrip() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TripActivity.this);
        alertDialog.setTitle("Add New Group");
        alertDialog.setMessage("What's the group name?");
        final EditText input = new EditText(TripActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().matches("")) {
                            Toast.makeText(TripActivity.this, "Enter group name!", Toast.LENGTH_SHORT).show();
                        } else {
                            Trip createdTrip = tripAdapter.createTrip(input.getText().toString());
                            Toast.makeText(TripActivity.this, createdTrip.getName() + " group created!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), TripActivity.class);
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
    public void onDestroy() {
        super.onDestroy();
        tripAdapter.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}