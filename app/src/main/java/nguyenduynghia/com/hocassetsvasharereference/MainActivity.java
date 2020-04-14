package nguyenduynghia.com.hocassetsvasharereference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import nguyenduynghia.com.hocassetsvasharereference.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayAdapter<String>fontAdapter;
    int lastSelected=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadFontsFromAssets();
        addEvents();
    }

    private void addEvents() {
        binding.lvFonts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doiFont(position);
                lastSelected=position;
                playMusics();
            }
        });
    }

    private void playMusics() {
        try {
            AssetFileDescriptor afd=getAssets().openFd("musics/Tiengchimhot-Dangcapnhat_mtjj.mp3");
            MediaPlayer player=new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            afd.close();
            player.prepare();
            player.start();


        }catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void doiFont(int position) {
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/"+fontAdapter.getItem(position));
        binding.txtFonts.setTypeface(typeface);
    }

    private void loadFontsFromAssets() {
        try{
            AssetManager assetManager=getAssets();
            String []fonts=assetManager.list("fonts");
            fontAdapter=new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,fonts);
            binding.lvFonts.setAdapter(fontAdapter);

        }catch (Exception ex){
            Toast.makeText(MainActivity.this,ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences=getSharedPreferences("MY_SHARE",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("LAST_SELECTED",lastSelected);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences=getSharedPreferences("MY_SHARE",MODE_PRIVATE);
        lastSelected=preferences.getInt("LASTSELECTED",-1);
        if(lastSelected!=-1)
            binding.lvFonts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    parent.getChildAt(lastSelected).setBackgroundColor(Color.TRANSPARENT);
                    view.setBackgroundColor(Color.BLUE);
                }
            });
           //binding.lvFonts.setSelection(lastSelected);
//        SharedPreferences preferences=getSharedPreferences("MY_SHARE",MODE_PRIVATE);
//        lastSelected=preferences.getInt("LASTSELECTED",-1);
//        if(lastSelected!=-1)
//        {
//            binding.lvFonts.getChildAt(lastSelected).setBackgroundColor(Color.BLUE);
//        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences=getSharedPreferences("MY_SHARE",MODE_PRIVATE);
        lastSelected=preferences.getInt("LASTSELECTED",-1);
        if(lastSelected!=-1)
        {
            binding.lvFonts.getChildAt(lastSelected).setBackgroundColor(Color.BLUE);
        }
    }
}
