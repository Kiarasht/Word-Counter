package com.pberger.word.counter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Ignore_List extends ListActivity implements OnClickListener
{
    private ListView ignoreList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ignore_list);

        View addButton = findViewById(R.id.addIgnore);
        addButton.setOnClickListener(this);

        ignoreList = (ListView) findViewById(android.R.id.list);

        populateThreadList();

        registerForContextMenu(ignoreList);
    }

    public void populateThreadList()
    {
        List<String> ignoredWords = new ArrayList<String>();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(this.getResources().openRawResource(
                        R.raw.ignore_list)));

        String aux = "";
        int count = 1;

        try
        {
            while ((aux = reader.readLine()) != null)
            {
                ignoredWords.add(count + ".    " + aux);
                count++;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        String[] ignoredListString = ignoredWords.toArray(new String[0]);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.contact_entry, R.id.contactEntryText,
                        ignoredListString);

        ignoreList.setAdapter(adapter);
    }

    public void removeWord(int word)
    {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Remove Word");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (item.getTitle() == "Remove Word")
        {
            removeWord(item.getItemId());
        } else
        {
            return false;
        }
        return true;
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.addIgnore:
            Intent w = new Intent(this, Word_Search.class);
            startActivity(w);
            break;
        //More buttons go here (if any)...
        }
    }
}
