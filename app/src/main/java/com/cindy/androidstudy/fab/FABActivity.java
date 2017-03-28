package com.cindy.androidstudy.fab;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cindy.androidstudy.R;

public class FABActivity extends AppCompatActivity {

    boolean sIsFabMenuOpen = false;
    View dim;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    FloatingActionButton fab4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_action_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.tv_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FABActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });

        dim = findViewById(R.id.dim);
        dim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMenu();
            }
        });
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG.setAction("Action", null).show();
                toggleFabMenu();
            }
        });
    }

    private void toggleFabMenu() {
        if (sIsFabMenuOpen) {
            fab1.animate().translationYBy(300).rotation(-360).setDuration(300);
            fab2.animate().translationYBy(550).rotation(-360).setDuration(300);
            fab3.animate().translationYBy(800).rotation(-360).setDuration(300);
            fab4.animate().translationYBy(1050).rotation(-360).setDuration(300);
            dim.setVisibility(View.INVISIBLE);
        } else {
            dim.setVisibility(View.VISIBLE);
            fab1.animate().translationYBy(-1 * 300).rotation(360).setDuration(300);
            fab2.animate().translationYBy(-1 * 550).rotation(360).setDuration(300);
            fab3.animate().translationYBy(-1 * 800).rotation(360).setDuration(300);
            fab4.animate().translationYBy(-1 * 1050).rotation(360).setDuration(300);
        }
        sIsFabMenuOpen = !sIsFabMenuOpen;
    }

    @Override
    public void onBackPressed() {
        if (sIsFabMenuOpen) {
            toggleFabMenu();
            return;
        }
        super.onBackPressed();
    }
}
