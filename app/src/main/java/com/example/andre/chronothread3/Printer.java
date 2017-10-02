package com.example.andre.chronothread3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by andre on 02/09/2017.
 */

public class Printer extends Thread {


    private Handler handler;
    private String[] Lead;
    private int i = 0;
    boolean suspendFlag;

    public Printer(Handler handler, String [] Lead) {
        this.handler = handler;
        this.Lead = Lead;
        suspendFlag = false;
    }

    public void run() {
        System.out.println("DEBUG: method run() is running");
        try {
            while( i<Lead.length) {

                notifyMessage(Lead[i] + " ");
                Thread.sleep(500);
                System.out.println("DEBUG: while n': " + i +" - " +Lead[i]);
                i=i+1;
                synchronized (this){    while(suspendFlag){wait();}    }
            }
        }catch(InterruptedException ex) {}
    }

    synchronized void suspendresume() {
        if (!suspendFlag) {  suspendFlag = true;
            System.out.println("Printer is suspended");}
        else {  suspendFlag = false; notify();
                System.out.println("Printer is ongoing");}
    }

    private void notifyMessage(String str) {
        Message msg = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("refresh", ""+str); //inserico il campione nel bundle
        System.out.println("DEBUG: notify str n': " +i+ " - " + str);
        msg.setData(b); //salvo il bundle nel Message
        //System.out.println("DEBUG: notify bundle n': " +i+ " - " + b);
        handler.sendMessage(msg); //passo al MainThread
    }
}
