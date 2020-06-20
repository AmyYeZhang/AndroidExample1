package com.example.androidexample1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private ChatAdapter myAdapter = new ChatAdapter();
    private ArrayList<ChatMessge> elements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView chatList = findViewById(R.id.listviewChat);
        chatList.setAdapter(myAdapter);

        EditText editChat = findViewById(R.id.editChat);

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(v->{
            elements.add(new ChatMessge(true, editChat.getText().toString()));
            editChat.setText("");
            myAdapter.notifyDataSetChanged();
        });

        Button btnReceive = findViewById(R.id.btnReceive);
        btnReceive.setOnClickListener(v -> {
            elements.add(new ChatMessge(false, editChat.getText().toString()));
            editChat.setText("");
            myAdapter.notifyDataSetChanged();
        });

        chatList.setOnItemLongClickListener(((parent, view, position, id) -> {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + (position+1) + "\nThe database id is:" + id)
                    .setPositiveButton("Yes", (click, arg)->{
                        elements.remove(position);
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
            return elements.size();
        }

        @Override
        public ChatMessge getItem(int position) {
            return elements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long)position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //get a layout for this row at position
            LayoutInflater inflater = getLayoutInflater();
            View newView;
            if(elements.get(position).getIsSend()){
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

    class ChatMessge {
        private boolean isSend;
        private String msg;

        public ChatMessge(boolean isSend, String msg){
            setIsSend(isSend);
            setMsg(msg);
        }

        public boolean getIsSend(){
            return isSend;
        }

        private void setIsSend(boolean isSend){
            this.isSend = isSend;
        }

        public String getMsg(){
            return msg;
        }

        private void setMsg(String msg){
            this.msg = msg;
        }
    }
}