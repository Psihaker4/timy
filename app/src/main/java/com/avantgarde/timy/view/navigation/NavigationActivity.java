package com.avantgarde.timy.view.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.avantgarde.timy.R;
import com.avantgarde.timy.view.main.MainActivity;

public abstract class NavigationActivity extends AppCompatActivity {

    public static int START_PAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        NavigationBar navBar = findViewById(R.id.navigation_bar);

        navBar.configure(new int[]{
                R.drawable.ic_nav_helper,
                R.drawable.ic_nav_social,
                R.drawable.ic_nav_dashboard,
                R.drawable.ic_nav_schedule,
                R.drawable.ic_nav_account
        }, START_PAGE);
        navBar.setOnNavItemSelectedListener(position -> {
            Log.d("NavActivity", "onCreate: " + position);

            if (this instanceof MainActivity) {
                ((MainActivity) this).openPage(position);
                return;
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", position);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });
        inflate();
    }

    protected abstract void inflate();

    protected void configureToolbar(){

    }

}
