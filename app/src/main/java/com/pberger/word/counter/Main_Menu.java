package com.pberger.word.counter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class Main_Menu extends Activity implements OnClickListener
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);
        //Set up click listeners for all the buttons
        View top5Button = findViewById(R.id.top_5_list_button);
        top5Button.setOnClickListener(this);
        View searchButton = findViewById(R.id.word_search_button);
        searchButton.setOnClickListener(this);
        View ignoreButton = findViewById(R.id.ignore_button);
        ignoreButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main__menu, menu);
        return true;
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.top_5_list_button:
            Intent t = new Intent(this, Generate_Top_5.class);
            startActivity(t);
            break;
        case R.id.about_button:
            Intent a = new Intent(this, About.class);
            startActivity(a);
            break;
        case R.id.ignore_button:
            Intent i = new Intent(this, Ignore_List.class);
            startActivity(i);
            break;
        case R.id.word_search_button:
            Intent w = new Intent(this, Word_Search.class);
            startActivity(w);
            break;
        //More buttons go here (if any)...
        }
    }
}