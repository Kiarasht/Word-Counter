package com.pberger.word.counter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Top_5_List extends Activity implements OnClickListener
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_5_list);

        Bundle bundle = getIntent().getExtras();
        String number = bundle.getString("number");
        String name = bundle.getString("name");

        TextView subTitle = (TextView) findViewById(R.id.top_5_SubTitle);
        subTitle.setText("w/ " + name);

        List<String> smsBody = findMessages(number);
        String[] smsBodyString = smsBody.toArray(new String[0]);

        Vector<Word> topFiveList = sortWords(smsBodyString);

        printList(topFiveList);

        View addButton = findViewById(R.id.shareTopFive);
        addButton.setOnClickListener(this);
    }

    public void printList(Vector<Word> topFiveList)
    {
        TextView textView1 = (TextView) findViewById(R.id.word1);
        textView1.setText("1. " + topFiveList.get(0).getWord() + " ("
                + topFiveList.get(0).getFrequency() + ")");

        TextView textView2 = (TextView) findViewById(R.id.word2);
        textView2.setText("2. " + topFiveList.get(1).getWord() + " ("
                + topFiveList.get(1).getFrequency() + ")");

        TextView textView3 = (TextView) findViewById(R.id.word3);
        textView3.setText("3. " + topFiveList.get(2).getWord() + " ("
                + topFiveList.get(2).getFrequency() + ")");

        TextView textView4 = (TextView) findViewById(R.id.word4);
        textView4.setText("4. " + topFiveList.get(3).getWord() + " ("
                + topFiveList.get(3).getFrequency() + ")");

        TextView textView5 = (TextView) findViewById(R.id.word5);
        textView5.setText("5. " + topFiveList.get(4).getWord() + " ("
                + topFiveList.get(4).getFrequency() + ")");
    }

    public List<String> findMessages(String number)
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
                String address = c.getString(c.getColumnIndex("address"));

                address = address.replaceAll("[^0-9]", "");
                address = replace1(address);

                if (address.equals(number))
                {
                    smsBody.add(c.getString(c.getColumnIndexOrThrow("body")));
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        c.close();

        return smsBody;
    }

    public String replace1(String num)
    {
        if (num.substring(0, 1).equals("1"))
        {
            num = num.substring(1, num.length());
            return num;
        } else
        {
            return num;
        }
    }

    //*********************************************************************************************

    public Vector<Word> sortWords(String[] smsBodyString)
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

        for (int count = 0; count < words.size(); count++)
        {
            if (words.get(count).getWord().equals(""))
            {
                words.remove(count);
            }
        }

        Vector<Word> vector = findMostCommon(words);

        return vector;
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

    public boolean checkIgnoreList(String word)
    {
        List<String> ignoredWords = new ArrayList<String>();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(this.getResources().openRawResource(
                        R.raw.ignore_list)));

        String aux = "";

        try
        {
            while ((aux = reader.readLine()) != null)
            {
                ignoredWords.add(aux);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        for (int count = 0; count < ignoredWords.size(); count++)
        {
            if (ignoredWords.get(count).equalsIgnoreCase(word))
            {
                return true;
            }
        }

        return false;
    }

    public Vector<Word> findMostCommon(List<Word> words)
    {
        Vector<Word> vector = new Vector<Word>();

        for (int count = 0; count < words.size(); count++)
        {
            if (checkIgnoreList(words.get(count).getWord())
                    || (words.get(count).getWord().length() < 2))
            {

            } else
            {
                if (vector.isEmpty())
                {
                    vector.add(words.get(count));
                } else
                {
                    //if the last element's frequency is >= current word's frequency
                    //  -add to end
                    if (vector.get(vector.size() - 1).getFrequency() >= words.get(count)
                            .getFrequency())
                    {
                        vector.add(words.get(count));
                    }
                    //if the first elements's frequency is <= current word's frequency
                    //  -add to beginning  
                    else if (vector.get(0).getFrequency() <= words.get(count).getFrequency())
                    {
                        vector.insertElementAt(words.get(count), 0);
                    }
                    //if the second to last element's frequency is >= current word's frequency
                    //  -add to second to last 
                    else if (vector.get(vector.size() - 2).getFrequency() >= words.get(count)
                            .getFrequency())
                    {
                        vector.insertElementAt(words.get(count), (vector.size() - 1));
                    }
                    //if the third to last element's frequency is >= current word's frequency
                    //  -add to third to last 
                    else if (vector.get(vector.size() - 3).getFrequency() >= words.get(count)
                            .getFrequency())
                    {
                        vector.insertElementAt(words.get(count), (vector.size() - 2));
                    }
                    //if the forth to last element's frequency is >= current word's frequency
                    //  -add to forth to last 
                    else if (vector.get(vector.size() - 4).getFrequency() >= words.get(count)
                            .getFrequency())
                    {
                        vector.insertElementAt(words.get(count), (vector.size() - 3));
                    }
                    //if the fifth to last element's frequency is >= current word's frequency
                    //  -add to fifth to last 
                    else if (vector.get(vector.size() - 5).getFrequency() >= words.get(count)
                            .getFrequency())
                    {
                        vector.insertElementAt(words.get(count), (vector.size() - 4));
                    }
                    if (vector.size() > 5)
                    {
                        vector.subList(5, vector.size()).clear();
                    }
                }
            }
        }

        return vector;
    }

    public void onClick(View v)
    {
        TextView textViewTitle = (TextView) findViewById(R.id.top_5_Title);
        String title = textViewTitle.getText().toString();

        TextView textViewSubTitle = (TextView) findViewById(R.id.top_5_SubTitle);
        String subTitle = textViewSubTitle.getText().toString();

        TextView textView1 = (TextView) findViewById(R.id.word1);
        String word1 = "  " + textView1.getText().toString();

        TextView textView2 = (TextView) findViewById(R.id.word2);
        String word2 = "  " + textView2.getText().toString();

        TextView textView3 = (TextView) findViewById(R.id.word3);
        String word3 = "  " + textView3.getText().toString();

        TextView textView4 = (TextView) findViewById(R.id.word4);
        String word4 = "  " + textView4.getText().toString();

        TextView textView5 = (TextView) findViewById(R.id.word5);
        String word5 = "  " + textView5.getText().toString();

        String smsBody =
                title + " \n" + subTitle + "\n" + word1 + "\n" + word2 + "\n" + word3 + "\n"
                        + word4 + "\n" + word5 + "\nCreated with Word Counter";
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", smsBody);
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
    }
}
