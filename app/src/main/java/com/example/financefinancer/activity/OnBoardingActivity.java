package com.example.financefinancer.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.example.financefinancer.R;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Create Group", "Easy create your groups.", R.drawable.group);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Add Members", "Add your loved ones to groups.", R.drawable.member);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Add Bills", "Add bills details to share with.", R.drawable.bill);
        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("Split Bills", "Split bills on one tap.", R.drawable.pay);
        AhoyOnboarderCard ahoyOnboarderCard5 = new AhoyOnboarderCard("Share Results", "Share results via any platform.", R.drawable.share);

        ahoyOnboarderCard1.setBackgroundColor(R.color.onBoardColor);
        ahoyOnboarderCard2.setBackgroundColor(R.color.onBoardColor);
        ahoyOnboarderCard3.setBackgroundColor(R.color.onBoardColor);
        ahoyOnboarderCard4.setBackgroundColor(R.color.onBoardColor);
        ahoyOnboarderCard5.setBackgroundColor(R.color.onBoardColor);


        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);
        pages.add(ahoyOnboarderCard5);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
            page.setTitleTextSize(dpToPixels(12, this));
            page.setDescriptionTextSize(dpToPixels(8, this));
            //page.setIconLayoutParams(width, height, marginTop, marginLeft, marginRight, marginBottom);
        }

        setFinishButtonTitle("Finish");
        showNavigationControls(true);
        setImageBackground(R.drawable.on_board);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/poppins_regular.ttf");
        //Typeface fc = Typeface.createFromAsset(getAssets(),"font/poppins.ttf");
        setFont(face);

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.list_button));

        }


        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        Intent intent= new Intent(getBaseContext(), TripActivity.class);
        startActivity(intent);
    }
}