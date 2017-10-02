package com.example.andre.chronothread3;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);

        Button btna = findViewById(R.id.buttonaddominaledx1);
        Button btn = findViewById(R.id.buttonpectodx);
        btna.setHighlightColor(0xFFFF0000);
        btn.setHighlightColor(0xFFFF0000);
        //costruisco l'array contenente i campioni
        String[]arr1 = new String[0];
        String[]arr2 = new String[0];
        try {
            arr1 = SecondColumn("emg_healthy1.txt");
            arr2 = SecondColumn("emg_healthy2.txt");
        } catch (IOException e) {   e.printStackTrace();    }


        final String[] finalArr1 = arr1; // per poterla utilizare dentro il Listener
        final String[] finalArr2 = arr2;
        MyHandler1 handler1 = new MyHandler1();
        MyHandler2 handler2 = new MyHandler2();
        final Printer P1 = new Printer(handler1, finalArr1);
        final Printer P2 = new Printer(handler2, finalArr2);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (P1.getState() == Thread.State.NEW) {P1.start();}
                else {    P1.suspendresume();     }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (P2.getState() == Thread.State.NEW) {P2.start();}
                else {    P2.suspendresume();     }
            }
        });
    }

    private class MyHandler1 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //System.out.println("DEBUG: Handler(Main): - " +msg);

            Bundle bundle = msg.getData(); //costruisco il bundle
            if (bundle.containsKey("refresh")) { // controllo che sia stato inserito il campione
                String value = bundle.getString("refresh"); //ne pesco il valore
                Button btn = findViewById(R.id.button1);
                System.out.println("DEBUG: Handler1(Main): - " +value);
                btn.setText(value + " mV"); //setto il testo che ho messo nel layout che ho creato
            }
        }
    }

    private class MyHandler2 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //System.out.println("DEBUG: Handler(Main): - " +msg);

            Bundle bundle = msg.getData(); //costruisco il bundle
            if (bundle.containsKey("refresh")) { // controllo che sia stato inserito il campione
                String value = bundle.getString("refresh"); //ne pesco il valore
                Button btn = findViewById(R.id.button2);
                System.out.println("DEBUG: Handler2(Main): - " +value);
                btn.setText(value + " mV"); //setto il testo che ho messo nel layout che ho creato
            }
        }
    }

    //Metodo fatto da me che presa in ingresso il nome di un file txt produce un array di stringhe contentente solo la seconda colonna del file
    private String[] SecondColumn(String directory) throws IOException {


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(directory)));
        } catch (IOException e) {   e.printStackTrace();    }

        String line = "a";
        String[] arr = new String[0];
        int i = 0;
        while (!(line == null)) {

            assert reader != null;
            line = reader.readLine();
            if (line != null) {
                String[] columns = line.split(" ");
                //System.out.println("COLONNA1 = " + columns[0b1]);


                int aLen = arr.length;
                int bLen = columns.length;
                String[] c = new String[aLen + bLen-1];
                while (i < aLen) {
                    c[i] = arr[i];
                    i++;
                }
                c[i] = columns[1];
                i = 0;

                arr = c;
            }
        } System.out.println("FUNZIONA" + arr.length);
        return arr;
    }
}