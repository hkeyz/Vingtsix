package com.example.vingtsix;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllMusic extends Fragment {
    ListView allMusicList;
    ArrayAdapter<String> musicArraAdapter;
    String songs[];
    ArrayList<File> musics;


    public AllMusic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_music, container, false);

        allMusicList = view.findViewById(R.id.musicList);


        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                //display list of musique
                musics = findMusicFiles(Environment.getExternalStorageDirectory());
                songs = new String[musics.size()];

                for (int i =0; i<musics.size(); i++){
                    songs[i] = musics.get(i).getName().toString().replace(".mp3","").replace(".wav", "").replace(".mp4a","");

                }

                musicArraAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, songs);
                allMusicList.setAdapter(musicArraAdapter);

                //partir au niveau de lecture en cours quand on click sur une chanson dans la liste
                allMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent player = new Intent(getActivity(), Player.class);

                        //pour montrer les details de la chansons
                        player.putExtra("songFileList", musics);
                        player.putExtra("position", position);
                        startActivity(player);
                    }
                });



            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                //keep asking permission each time app is open untill granted
                token.continuePermissionRequest();

            }
        }).check();

        return view;
    }

    private ArrayList<File> findMusicFiles(File file){


        ArrayList<File> allMusicFilesObject = new ArrayList<>();
        File [] files = file.listFiles();

        for (File ocurrentFile: files){
            if (ocurrentFile.isDirectory() && !ocurrentFile.isHidden()){
                allMusicFilesObject.addAll(findMusicFiles(ocurrentFile));
            }
            else {
                //selection des fichiiers audio seulement
                if (ocurrentFile.getName().endsWith(".mp3") || ocurrentFile.getName().endsWith(".mp4a") || ocurrentFile.getName().endsWith(".wav") ){
                    allMusicFilesObject.add(ocurrentFile);
                }

            }
        }

        return allMusicFilesObject;

    }




}
