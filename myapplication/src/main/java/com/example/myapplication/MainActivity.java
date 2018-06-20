package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ProgressCircle circle;
    private Button load;
    private  int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circle = (ProgressCircle) findViewById(R.id.circle);
        load = (Button) findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(myrunable).start();
            }
        });
    }

    Runnable myrunable = new Runnable() {
        @Override
        public void run() {
            setdata();
            Timedata();
        }
    };

    private void setdata(){
        for (int j = 0; j < 101; j++) {
            circle.setprogress(j);
            Log.e("tag", "run: "+j );
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void Timedata(){
        Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                circle.setprogress(i);
                Log.e("tag", "run: "+i);
                if (i>99){
                    cancel();
                }
                i++;
            }
        };
        timer.schedule(task,10,100);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
