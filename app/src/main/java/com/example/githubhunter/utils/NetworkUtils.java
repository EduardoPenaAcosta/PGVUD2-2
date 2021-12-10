package com.example.githubhunter.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String URL_TOYS = "https://akabab.github.io/superhero-api/api/all.json";

    // Clase que devuelve la URL
    public static URL buildURL(){
        Uri builtUri = Uri.parse(URL_TOYS).buildUpon()
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return url;
    }

    // Clase que obtiene la respuesta desde la URL...
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = urlConnection.getInputStream();

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        try{
            boolean hasInput = scanner.hasNext();

            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }finally{
            urlConnection.disconnect();
        }


    }

}
