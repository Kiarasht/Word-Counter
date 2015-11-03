package com.pberger.word.counter;

import java.util.Set;
import java.util.TreeSet;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Generate_Top_5 extends ListActivity
{
    private ListView threadList;
    private String[] msgListString;

    private String[] msgListNumbers;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        threadList = (ListView) findViewById(android.R.id.list);

        populateThreadList();
    }

    public void populateThreadList()
    {
        Set<String> msgList = getSMSNumbers();

        msgListString = msgList.toArray(new String[0]);
        msgListNumbers = msgList.toArray(new String[0]);

        //Removes the 1 in front & all characters except 0-9
        for (int count = 0; count < msgListString.length; count++)
        {
            msgListString[count] = msgListString[count].replaceAll("[^0-9]", "");
            msgListString[count] = replace1(msgListString[count]);
            msgListString[count] = getSMSNames(msgListString[count]);
        }

        for (int count = 0; count < msgListNumbers.length; count++)
        {
            msgListNumbers[count] = msgListNumbers[count].replaceAll("[^0-9]", "");
            msgListNumbers[count] = replace1(msgListNumbers[count]);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.contact_entry, R.id.contactEntryText,
                        msgListString);

        TextView textView = (TextView) findViewById(R.id.Title);
        textView.setText("Inbox");

        threadList.setAdapter(adapter);
    }

    //removes the 1 in front of numbers
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

    public String getSMSNames(final String phoneNumber)
    {
        Cursor cursor =
                getContentResolver()
                        .query(Data.CONTENT_URI,
                                new String[]
                                { Data._ID, Data.DISPLAY_NAME, Phone.NUMBER, Data.CONTACT_ID, Phone.TYPE, Phone.LABEL },
                                Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", null,
                                Data.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = "No Contact Found - " + phoneNumber;

        while (cursor.moveToNext())
        {
            String phnNum;
            phnNum = cursor.getString(cursor.getColumnIndex("data1")).replaceAll("[^0-9]", "");
            phnNum = replace1(phnNum);

            if (phoneNumber.equals(phnNum))
            {
                name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
                cursor.close();
                return name;
            }
        }

        cursor.close();
        return name;

    }

    public Set<String> getSMSNumbers()
    {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = null;

        Set<String> smsNumbers = new TreeSet<String>();

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
                final String address = c.getString(c.getColumnIndex("address"));
                smsNumbers.add(address);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        c.close();

        return smsNumbers;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent t = new Intent(this, Top_5_List.class);
        int i = (int) id;

        t.putExtra("number", msgListNumbers[i]);
        t.putExtra("name", msgListString[i]);
        startActivity(t);
    }

}
