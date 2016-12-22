package shubham.finalmedia;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class player extends AppCompatActivity implements View.OnClickListener {
   static MediaPlayer mp;
    int progressval;
    Handler myHandler = new Handler();
    ArrayList<File> mySongs;
    Button b5,b1,b2,b3,b4;
    SeekBar seekBar;
    int position;
    double startTime = 0;
    double finalTime = 0;
    public static int oneTimeOnly = 0;
    SeekBar seekbar;
    TextView tx1,tx2,tx3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        b5= (Button)findViewById(R.id.button);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);
        seekbar=(SeekBar)findViewById(R.id.seekBar);
        tx1 = (TextView)findViewById(R.id.textView2);
        tx2 = (TextView)findViewById(R.id.textView3);
        //tx3 = (TextView)findViewById(R.id.textView4);
        seekbar.setClickable(true);

        b5.setOnClickListener(this);
        b4.setOnClickListener(this);
        b3.setOnClickListener(this);
        b2.setOnClickListener(this);
        b1.setOnClickListener(this);
        if(mp!=null){
            mp.stop();
            mp.release();
        }
        Intent i=getIntent();
        Bundle b= i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);
        Uri u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);
        tx2.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );

        tx1.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );
        //mp.start();
        finalTime = mp.getDuration();
        seekbar.setMax((int) finalTime);

        seekbar.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener() {


                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressval = progress;

                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {

                        mp.seekTo((int) progressval);
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {

                        mp.seekTo((int) progressval);
                    }

                });

    }




    @Override
   public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.button2:
                if(mp.isPlaying())
                {
                    b2.setText(">");
                    mp.pause();
                    finalTime = mp.getDuration();
                    startTime = mp.getCurrentPosition();


                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }
                    seekbar.setProgress((int)startTime);
                    myHandler.postDelayed(UpdateSongTime,100);

                }
                else {
                    b2.setText("||");
                    mp.start();
                    finalTime = mp.getDuration();
                    startTime = mp.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }
                    seekbar.setProgress((int)startTime);
                    myHandler.postDelayed(UpdateSongTime,100);

                }
                tx2.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );

                tx1.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                );
                    break;
            case R.id.button3:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.button1:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.button4:
                mp.stop();
                mp.release();
                position=(position+1)%mySongs.size();
                Uri u = Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                tx2.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );

                tx1.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                );
                mp.start();
                finalTime = mp.getDuration();
                seekbar.setMax((int) finalTime);
                break;
            case R.id.button:
                mp.stop();
                mp.release();
                position=(position-1<0)?mySongs.size()-1:position-1;
                u = Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                tx2.setText(String.format("%2d:%2d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );

                tx1.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                );
                mp.start();
                finalTime = mp.getDuration();
                seekbar.setMax((int) finalTime);
                break;
        }

    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mp.getCurrentPosition();
            tx1.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

}
