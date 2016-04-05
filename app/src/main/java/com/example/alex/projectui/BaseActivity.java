package com.example.alex.projectui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class BaseActivity extends ActionBarActivity {

    final Context context = this;
    Dialog scenariosDialog;
    Dialog languagesDialog;
    Dialog welcomeDialog;
    ImageButton btnScenarios;
    ImageButton btnLanguages;
    ImageButton btnStart;
    ImageButton selectFrench;
    ImageButton selectSpanish;
    ImageButton selectItalian;
    ImageButton selectGreetings;
    ImageButton selectTourism;
    String targetLanguage;
    String chosenScenario;
    private int langLock = -1;
    private int scenLock = -1;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


        //Initializing Buttons
        btnStart = (ImageButton) findViewById(R.id.btn_start);
        btnScenarios = (ImageButton) findViewById(R.id.sel_scenario);
        btnLanguages = (ImageButton) findViewById(R.id.sel_lang);

        //Creating Scenarios Dialog
        scenariosDialog = new Dialog(context);
        scenariosDialog.setContentView(R.layout.scenarios_dialog);
        scenariosDialog.setTitle("Choose a Scenario");

        //Initializing scenario dialog components
        selectGreetings = (ImageButton) scenariosDialog.findViewById(R.id.greetings);
        selectTourism = (ImageButton) scenariosDialog.findViewById(R.id.tourism);

        //Set action listener for the scenario icons
        selectGreetings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chosenScenario = "Greetings";
                        btnScenarios.setBackgroundResource(R.drawable.greetings);
                        scenLock = 1;
                        scenariosDialog.dismiss();
                    }
                }
        );

        selectTourism.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chosenScenario = "Travel";
                        btnScenarios.setBackgroundResource(R.drawable.tourism);
                        scenLock = 1;
                        scenariosDialog.dismiss();
                    }
                }
        );

        //Creating Languages Dialog
        languagesDialog = new Dialog(context);
        languagesDialog.setContentView(R.layout.selectlang_dialog);
        languagesDialog.setTitle("Select Language");

        //Initializing Language Dialog Components
        selectFrench = (ImageButton) languagesDialog.findViewById(R.id.french);
        selectSpanish = (ImageButton) languagesDialog.findViewById(R.id.spanish);
        selectItalian = (ImageButton) languagesDialog.findViewById(R.id.italian);

        //Set action listener for the language icons
        selectFrench.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        targetLanguage = "French";
                        btnLanguages.setBackgroundResource(R.drawable.french);
                        langLock = 1;
                        languagesDialog.dismiss();
                    }
                }
        );

        selectSpanish.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        targetLanguage = "Spanish";
                        btnLanguages.setBackgroundResource(R.drawable.spanish);
                        langLock = 1;
                        languagesDialog.dismiss();
                    }
                }
        );

        selectItalian.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        targetLanguage = "Italian";
                        btnLanguages.setBackgroundResource(R.drawable.italian);
                        langLock = 1;
                        languagesDialog.dismiss();
                    }
                }
        );

        //Set action listeners for the buttons on the activity
        btnScenarios.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scenariosDialog.show();
                    }
                }
        );

        btnLanguages.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        languagesDialog.show();
                    }
                }
        );

        btnStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (scenLock != -1 && langLock != -1) {
                            //Create the Intent
                            i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("targetLanguage", getLangValue(targetLanguage));
                            i.putExtra("chosenScenario", chosenScenario);
                            startActivity(i);
                        } else {
                            //Make assistant ask to choose language and scenario
                        }
                    }
                }
        );
    }

    private void createWelcomeDialog() {
        //Creating Welcome Dialog
        welcomeDialog = new Dialog(context);
        welcomeDialog.setContentView(R.layout.welcome_dialog);
        welcomeDialog.setTitle("Welcome");

        ImageButton btnSelectScenario = (ImageButton) welcomeDialog.findViewById(R.id.sel_scenario);
        ImageButton btnSelectLanguage = (ImageButton) welcomeDialog.findViewById(R.id.sel_lang);
        ImageButton btnWelcomeStart = (ImageButton) welcomeDialog.findViewById(R.id.start);

        welcomeDialog.show();

        btnSelectScenario.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scenariosDialog.show();
                    }
                }
        );

        btnSelectLanguage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        languagesDialog.show();
                    }
                }
        );

        btnWelcomeStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (scenLock != -1 && langLock != -1) {
                            welcomeDialog.dismiss();
                        } else {
                            //Make assistant ask to choose language and scenario
                        }
                    }
                }
        );
    }

    private String getLangValue(String targetLanguage) {
        String value;
        switch (targetLanguage) {
            case "French":
                value = "fr";
                break;
            case "Spanish":
                value = "es";
                break;
            case "Italian":
                value = "it";
                break;
            default:
                value = "en";
        }
        return value;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
}
