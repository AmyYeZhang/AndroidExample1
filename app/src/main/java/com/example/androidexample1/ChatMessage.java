package com.example.androidexample1;

public class ChatMessage {
    private long id;
    private boolean isSend; //1: Send, 0: Receive
    private String msg;

    //public ChatMessage(boolean isSend, String msg){
     //   this(0, isSend, msg);
    //}

    public ChatMessage(long id, boolean isSend, String msg){
        setId(id);
        setIsSend(isSend);
        setMsg(msg);
    }

    public void updateMessage(boolean isSend, String msg){
        setIsSend(isSend);
        setMsg(msg);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
