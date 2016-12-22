package shubham.finalmedia;

import android.content.Intent;
import android.os.Environment;




import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

import static shubham.finalmedia.R.id.lvPlaylist;


public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView) findViewById(R.id.lvPlaylist);
        final ArrayList<File> mySongs= findSongs(Environment.getExternalStorageDirectory());
        items = new String[ mySongs.size()];
        for(int i=0;i<mySongs.size();i++) {
           // toast(mySongs.get(i).getName().toString());
            items[i]=mySongs.get(i).getName().toString();

        }
        ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                  startActivity(new Intent(getApplicationContext(),player.class).putExtra("pos",position).putExtra("songlist",mySongs));

            }
        }

        );


    }
    public ArrayList<File> findSongs(File root){
        ArrayList<File> al= new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singleFile : files){
            if(singleFile.isDirectory()&&!singleFile.isHidden()) {
                al.addAll(findSongs(singleFile));
        }
            else {
              if(singleFile.getName().endsWith(".mp3")){
                  al.add(singleFile);
              }
            }
        }
         return al;
    }
    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();

    }
}
