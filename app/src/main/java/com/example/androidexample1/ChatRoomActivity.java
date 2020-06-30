package com.example.androidexample1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {

    private ChatAdapter myAdapter = new ChatAdapter();
    private ArrayList<ChatMessage> msgList = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView chatList = findViewById(R.id.listviewChat);
        loadDataFromDatabase(); //get any previously saved Contact objects
        chatList.setAdapter(myAdapter);

        EditText editChat = findViewById(R.id.editChat);

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v->{
            insertDataToDatabase(true, editChat.getText().toString());
            editChat.setText("");
        });

        Button btnReceive = findViewById(R.id.btnReceive);
        btnReceive.setOnClickListener(v -> {
            insertDataToDatabase(false, editChat.getText().toString());
            editChat.setText("");
        });

        chatList.setOnItemLongClickListener(((parent, view, position, id) -> {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + (position) + "\nThe database id is:" + id)
                    .setPositiveButton("Yes", (click, arg)->{
                        deleteMsgFromDatabase(id);
                        msgList.remove(position);
                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, agr)->{})
                    //.setView(getLayoutInflater().inflate(R.layout.send_layout, null))
                    .create().show();

            return true;
        }));

    }

    //inner class
    private class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return msgList.size();
        }

        @Override
        public ChatMessage getItem(int position) {
            return msgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //get a layout for this row at position
            LayoutInflater inflater = getLayoutInflater();
            View newView;
            if(msgList.get(position).getIsSend()){
                newView = inflater.inflate(R.layout.send_layout, parent, false );
                TextView txtSent = newView.findViewById(R.id.sendMsg);
                txtSent.setText(getItem(position).getMsg());
            }
            else{
                newView = inflater.inflate(R.layout.receive_layout, parent, false );
                TextView txtReceived = newView.findViewById(R.id.receiveMsg);
                txtReceived.setText(getItem(position).getMsg());
            }

            return newView;
        }
    }

    private void loadDataFromDatabase() {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_SEND, MyOpener.COL_MSG};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        //Cursor results2 = db.rawQuery("Select * from ?", new String[]{MyOpener.TABLE_NAME});

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int sendColIndex = results.getColumnIndex(MyOpener.COL_SEND);
        int msgColIndex = results.getColumnIndex(MyOpener.COL_MSG);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            boolean isSend = (results.getInt(sendColIndex)==0)?false:true; //1: Send, 0: Receive
            String msg = results.getString(msgColIndex);

            //add the new Contact to the array list:
            msgList.add(new ChatMessage(id, isSend, msg));
        }//At this point, the contactsList array has loaded every row from the cursor.

        printCursor(results, db.getVersion());
    }

    private void insertDataToDatabase(boolean isSend, String msg) {

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();

        //Now provide a value for every database column defined in MyOpener.java:
        //put string name in the NAME column:
        newRowValues.put(MyOpener.COL_SEND, Integer.toString(isSend==true?1:0));
        //put string email in the EMAIL column:
        newRowValues.put(MyOpener.COL_MSG, msg);

        //Now insert in the database:
        long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

        //add the new contact to the list:
        msgList.add(new ChatMessage(newId, isSend, msg));
        //update the listView:
        myAdapter.notifyDataSetChanged();
    }

    private void deleteMsgFromDatabase(long id) {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(id)});
    }

    private void printCursor( Cursor c, int version){
        Log.i("DB Version number", Integer.toString(db.getVersion()));
        Log.i("Number of columns", Integer.toString(c.getColumnCount()));
        Log.i("Name of columns", Arrays.toString(c.getColumnNames()));
        Log.i("Number of rows", Integer.toString(c.getCount()));

        c.moveToFirst();
        while(!c.isAfterLast()){
            Log.i("ID " + c.getInt(0), c.getInt(1) + ", " + c.getString(2));
            c.moveToNext();
        }
    }
}