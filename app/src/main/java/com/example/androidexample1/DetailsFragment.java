package com.example.androidexample1;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private AppCompatActivity parentActivity;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle dataFromActivity = getArguments();
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_details, container, false);
        //show the message
        TextView tvMsg = result.findViewById(R.id.frameMsg);
        tvMsg.setText("Message = " + dataFromActivity.getString("msg"));
        //show the id
        TextView tvId = result.findViewById(R.id.frameId);
        tvId.setText("ID = " + dataFromActivity.getString("id"));
        //if the message is from Send, then checked
        CheckBox cb = result.findViewById(R.id.frameCb);
        cb.setChecked(dataFromActivity.getBoolean("isSend"));
        //hide the fragment
        Button btnHide = result.findViewById(R.id.frameHide);
        btnHide.setOnClickListener(v -> {
            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            if(parentActivity instanceof EmptyActivity){
                //if use the phone, close fragment, back to chat room
                parentActivity.finish();
            }
        });

        return result;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //context will either be ChatRoomActivity for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}