package com.example.financefinancer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.financefinancer.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        simulateDayNight(1);
        Element developersName = new Element();
        developersName.setTitle("App Developed By : ");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logo)
                .addItem(new Element().setTitle("Version 1.0"))
                .setDescription("Split expenses with friends, roommates, group trips, and more\n" +
                        "\n" +
                        "Finance Financer is the easiest way to share expenses with friends and family and stop stressing about “who owes who.”")
                .addItem(developersName)

                .addGroup("17103451 | Kartik Nerkar")
                .addEmail("17103451@nuv.ac.in","Contact Me")
                .addInstagram("kartik25._","Follow me on Instagram")


                .addGroup("17103470 | Jinesh Kansara")
                .addEmail("17103470@nuv.ac.in","Contact Me")
                .addInstagram("jinesh_511","Follow me on Instagram")


                .addGroup("17103508 | Sagar Parmar")
                .addEmail("17103508@nuv.ac.in","Contact Me")
                .addInstagram("its.mr_editor","Follow me on Instagram")

                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);

    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + " Finance-Financer Inc.";
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.copyright_logo);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUs.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}
