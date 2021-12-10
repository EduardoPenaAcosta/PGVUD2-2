package com.example.githubhunter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubhunter.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // Instanciamos lo que se va a exponer en la pantalla
    TextView urlDisplay;
    TextView searchResults;



    public class superHeroSearch extends AsyncTask<URL,Void, String> {

        //Sobreescribimos la clase donde obtendremos la respuesta de la URL
        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String toysURL = null;
            try {
                toysURL = NetworkUtils.getResponseFromHttpUrl(searchURL);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return toysURL;
        }

        //Sobreescribimos la clase donde obtendremos estableceremos el texto retornado...
        @Override
        protected void onPostExecute(String s) {
            if( s != null && !s.equals("")){
                searchResults.setText(s);
            }
        }
    }

    //Clase para poner los botones del menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Clase para hacer funcionar los botones, en caso de detectar que se pulsa uno
    // de los botones, se ejecutará dicha acción...
    // En caso de que se presione el botón "Launch" ejecutará mostrará la URL que se ha puesto y
    // se mostrará el resultado una vez cargue y en el botón "Clear" dejará la pnatalla libre.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.launch) {
            urlDisplay.setVisibility(View.VISIBLE);
            Log.i("MainActivity", "Se ha completado la búsqueda.");
            Context context = MainActivity.this;
            Toast.makeText(context, R.string.search_pressed, Toast.LENGTH_LONG).show();

            URL githubUrl = NetworkUtils.buildURL();
            urlDisplay.setText(githubUrl.toString());

            new superHeroSearch().execute(githubUrl);
            
        }

        if( itemId == R.id.clear){
            clearScreen();
        }
        return true;
    }

    // Clase para limpiar la pantalla...
    private void clearScreen(){
        searchResults.setVisibility(View.INVISIBLE);
        urlDisplay.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlDisplay = (TextView) findViewById(R.id.url_display);
        searchResults = (TextView) findViewById(R.id.github_search_result);

    }
}