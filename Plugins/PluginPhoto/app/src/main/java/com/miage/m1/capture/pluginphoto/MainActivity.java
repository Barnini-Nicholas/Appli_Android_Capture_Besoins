package com.miage.m1.capture.pluginphoto;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String AUTHORITY =
            BuildConfig.APPLICATION_ID + ".provider";
    private String nomProjet;
    private String cheminProjet;
    private String cheminFichier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout associé à l'activité
        setContentView(R.layout.activity_main);

        // On récupère le nom du projet sélectionné
        Bundle b = getIntent().getExtras();
        if (b != null) {
            nomProjet = b.getString("nomProjet");
            cheminProjet = b.getString("cheminProjet");
            cheminFichier = b.getString("cheminFichier");
        }

        // On change le label de l'Activity
        setTitle(nomProjet + " - Photo");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton take_photo = (FloatingActionButton) findViewById(R.id.take_photo);
        take_photo.setOnClickListener(this);

        if (cheminFichier != null) {

            // Open file with user selected app
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);

            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(cheminFichier));

            intent.setDataAndType(uri, "image/jpeg");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
    }

    private void lancerAppareilPhoto() {

        File dossierPhoto = new File(cheminProjet, nomProjet + "-" + getString(R.string.app_name));
        // Si il existe pas on le créé
        if (!dossierPhoto.exists())
            Log.e("AAAAAAAAA  ccc ", "Dossier Photo : " + dossierPhoto.mkdirs());
        //dossierTexte.mkdirs();


        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(dossierPhoto, getDateHeure() + ".jpg"));
        ;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        Snackbar.make(view, "Prise de photo", Snackbar.LENGTH_LONG).show();

        lancerAppareilPhoto();

    }

    public String getDateHeure() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String dateHeure = date + "_" + month + "_" + year;
        dateHeure += "-" + hour + "h" + minute + "m" + second + "s";

        return dateHeure;
    }


}
