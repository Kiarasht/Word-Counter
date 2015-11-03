package com.pberger.word.counter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class Word_Search extends Activity implements OnClickListener
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_search);

        TextView results = (TextView) findViewById(R.id.searchResult);
        results.setVisibility(View.GONE);

        View searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(this);
    }

    public void wordSearch(String text)
    {
        TextView results = (TextView) findViewById(R.id.searchResult);
        results.setText("Word was not found");

        List<String> smsBody = getMessages();
        String[] smsBodyString = smsBody.toArray(new String[0]);

        List<Word> words = sortWords(smsBodyString);

        for (int count = 0; count < words.size(); count++)
        {
            if (words.get(count).getWord().compareToIgnoreCase(text) == 0)
            {
                String result =
                        "\"" + words.get(count).getWord() + "\"" + " was used "
                                + words.get(count).getFrequency() + " times in your texts";
                results.setText(result);
                break;
            }
        }
        results.setVisibility(View.VISIBLE);
    }

    public List<String> getMessages()
    {
        Uri uri = Uri.parse("content://sms");
        Cursor c = null;

        List<String> smsBody = new ArrayList<String>();

        try
        {
            c = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            for (boolean hasData = c.moveToFirst(); hasData; hasData = c.moveToNext())
            {

                smsBody.add(c.getString(c.getColumnIndexOrThrow("body")));

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        c.close();

        return smsBody;
    }

    public List<Word> sortWords(String[] smsBodyString)
    {
        List<Word> words = new ArrayList<Word>();

        String[] temp;

        for (int count = 0; count < smsBodyString.length; count++)
        {
            temp = smsBodyString[count].split("[^a-zA-Z0-9]");

            if (temp.length > 1)
            {
                for (int count1 = 0; count1 < temp.length; count1++)
                {
                    int index = checkRepeat(words, temp[count1]);
                    if (index == -1)
                    {
                        words.add(new Word(temp[count1]));
                    } else
                    {
                        words.get(index).increaseFrequency();
                    }
                }
            }
        }

        return words;
    }

    public static int checkRepeat(List<Word> words, String word)
    {
        for (int counter = 0; counter < words.size(); counter++)
        {
            if (words.get(counter).getWord().compareToIgnoreCase(word) == 0)
            {
                return counter;
            }
        }
        return -1;
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.search:
            EditText btn = (EditText) findViewById(R.id.searchBar);
            String text = btn.getText().toString();

            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btn.getWindowToken(), 0);

            wordSearch(text);
            break;
        //More buttons go here (if any)...
        }
    }
}