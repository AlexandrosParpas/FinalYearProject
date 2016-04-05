package com.example.alex.projectui;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends BaseActivity implements TranslateAPI.Callbacks {

    public ImageButton keyboardButton;
    public ImageButton micButton;
    public ImageButton selectStarters;
    public ImageButton repeat;
    public ImageButton reveal;
    public EditText userInputView;
    public TextView userOutputView;
    public ImageButton swapInputLang;
    public ImageButton listenInput;
    public ImageView visualCue;
    ImageButton lang1;
    ImageButton lang2;
    ListView convStarters;
    private TranslateAPI translateAPI;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    int soundC;
    String chosenScenario;
    String targetLanguage = "";
    String inputLanguage = "en";
    String swapLanguage1 = "en";
    String swapLanguage2;
    final Context context = this;
    ChatBot chatBot;
    TextToSpeech tts;
    String cbMatch = "";
    String translatedOutput = "";
    Locale TTSOutputLanguage;
    Locale TTSInputLanguage = Locale.ENGLISH;
    String userInput = "";
    ArrayAdapter<String> greetingsAdapter;
    ArrayAdapter<String> travelAdapter;
    private boolean soundEnabled = true;
    boolean swapLang = false;
    Dialog langDialog;
    Dialog startersDialog;
    int settLock = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Components
        userInputView = (EditText) findViewById(R.id.text_input);
        swapInputLang = (ImageButton) findViewById(R.id.swap_langinput);
        listenInput = (ImageButton) findViewById(R.id.listen_input);
        userOutputView = (TextView) findViewById(R.id.text_output);
        visualCue = (ImageView) findViewById(R.id.visualCue);
        repeat = (ImageButton) findViewById(R.id.imageRepeat);
        reveal = (ImageButton) findViewById(R.id.imageReveal);
        keyboardButton = (ImageButton) findViewById(R.id.keyboard_button);
        micButton = (ImageButton) findViewById(R.id.mic_button);
        selectStarters = (ImageButton) findViewById(R.id.starters_button);
        translateAPI = new TranslateAPI(this);
        chatBot = new ChatBot();

        //Retrieve state of component from the previous activity and assign the target language
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            targetLanguage = extras.getString("targetLanguage");
            chosenScenario = extras.getString("chosenScenario");
        }
        swapLanguage2 = targetLanguage;

        //Create scenarios dialog and its components
        startersDialog = new Dialog(context);
        startersDialog.setContentView(R.layout.starters_dialog);
        startersDialog.setTitle("Select Starter");
        convStarters = (ListView) startersDialog.findViewById(R.id.scenarios);
        System.out.println("chosenScenario is: " + chosenScenario);

        //Create language dialog and its components
        langDialog = new Dialog(context);
        langDialog.setContentView(R.layout.lang_dialog);
        langDialog.setTitle("Choose Language");
        lang1 = (ImageButton) langDialog.findViewById(R.id.lang1);
        lang2 = (ImageButton) langDialog.findViewById(R.id.lang2);

        //Initializing Text To Speech
        tts = new
                TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            tts.setLanguage(Locale.ENGLISH);
                        } else {
                            Intent installIntent = new Intent();
                            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                            startActivity(installIntent);

                        }
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();

        userInputView.setText("");
        userOutputView.setText("");
        visualCue.setBackgroundResource(R.drawable.transparent);

        //Access settings
        if (settLock == 1) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            Boolean b = sp.getBoolean("speech_only_mode", false);
            String katoLang = sp.getString("bot_lang", "fr");
            String katoScen = sp.getString("select_scen", "Greetings");
            speechOnlyMode(b);
            targetLanguage = katoLang;
            settLock = 0;
            swapLanguage2 = targetLanguage;
            chosenScenario = katoScen;
        }

        switch (targetLanguage) {
            case "fr":
                lang2.setBackgroundResource(R.drawable.french);
                break;
            case "it":
                lang2.setBackgroundResource(R.drawable.italian);
                break;
            case "es":
                lang2.setBackgroundResource(R.drawable.spanish);
                break;
        }

        switch (chosenScenario) {
            case "Greetings":
                greetingsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, chatBot.getGreetingsInputs());
                convStarters.setAdapter(greetingsAdapter);
                break;
            case "Travel":
                travelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, chatBot.getTravelInputs());
                convStarters.setAdapter(travelAdapter);
                break;
            default:
        }

        //Handle Click Listeners
        keyboardButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userInputView.setFocusable(true);
                        userInputView.setFocusableInTouchMode(true);
                        userInputView.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(userInputView, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
        );

        userInputView.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            userInput = userInputView.getText().toString();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(userInputView.getWindowToken(), 0);
                            userInputView.setFocusable(false);
                            return true;
                        }
                        return false;
                    }
                }
        );

        userInputView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!userInputView.getText().toString().matches("")) {
                            visualCue.setBackgroundResource(R.drawable.transparent);
                            userOutputView.setText("...");
                            if (inputLanguage.equals("en")) {
                                prepChatBot();
                            } else {
                                //Translate input to English
                                translateOutput(userInput, inputLanguage, "en");
                            }
                        }
                    }
                }
        );

        listenInput.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (soundEnabled) {
                            ttsResponse(userInputView.getText().toString(), TTSInputLanguage);
                        } else {
                            Toast.makeText(getApplicationContext(), "Sound is disabled. Enable sound to hear the message.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        swapInputLang.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swapLang = true;
                        userInputView.setText("...");
                        System.out.println("inputLanguage variable is: " + swapLanguage1);
                        System.out.println("inputLanguage2 variable is: " + swapLanguage2);
                        if (swapLanguage1.equals("en")) {
                            switch (swapLanguage2) {
                                case "fr":
                                    translateAPI.translate(userInput, TranslateAPI.ENGLISH, TranslateAPI.FRENCH);
                                    swapLanguage1 = "fr";
                                    TTSInputLanguage = Locale.FRENCH;
                                    break;
                                case "es":
                                    translateAPI.translate(userInput, TranslateAPI.ENGLISH, TranslateAPI.SPANISH);
                                    swapLanguage1 = "es";
                                    TTSInputLanguage = new Locale("es");
                                    break;
                                case "it":
                                    translateAPI.translate(userInput, TranslateAPI.ENGLISH, TranslateAPI.ITALIAN);
                                    swapLanguage1 = "it";
                                    TTSInputLanguage = Locale.ITALIAN;
                                    break;
                            }
                            swapLanguage2 = "en";
                        } else {
                            switch (swapLanguage1) {
                                case "fr":
                                    translateAPI.translate(userInput, TranslateAPI.FRENCH, TranslateAPI.ENGLISH);
                                    swapLanguage2 = "fr";
                                    break;
                                case "es":
                                    translateAPI.translate(userInput, TranslateAPI.SPANISH, TranslateAPI.ENGLISH);
                                    swapLanguage2 = "es";
                                    break;
                                case "it":
                                    translateAPI.translate(userInput, TranslateAPI.ITALIAN, TranslateAPI.ENGLISH);
                                    swapLanguage2 = "it";
                                    break;
                            }
                            swapLanguage1 = "en";
                            TTSInputLanguage = Locale.ENGLISH;
                        }
                    }
                }
        );


        selectStarters.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startersDialog.show();
                    }
                }
        );

        convStarters.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
                        inputLanguage = "en";
                        switch (chosenScenario) {
                            case "Greetings":
                                userInput = greetingsAdapter.getItem(position).toString();
                                break;
                            case "Travel":
                                userInput = travelAdapter.getItem(position).toString();
                                break;
                            default:
                        }
                        userInputView.setText(userInput);
                        userOutputView.setText("");
                        visualCue.setBackgroundResource(R.drawable.transparent);
                        userInputView.setFocusable(false);
                        startersDialog.dismiss();
//                        for(int i=0; i<travelAdapter.getCount(); i++){
//                            System.out.println(travelAdapter.getItem(i).toString());
//                        }
                    }
                }
        );

        micButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        langDialog.show();
                    }
                }
        );

        repeat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (soundEnabled) {
                            ttsResponse(translatedOutput, TTSOutputLanguage);
                        } else {
                            Toast.makeText(getApplicationContext(), "Sound is disabled. Enable sound to hear the message.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        reveal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userOutputView.getText().toString().equals(translatedOutput)) {
                            userOutputView.setText(cbMatch);
                        } else {
                            userOutputView.setText(translatedOutput);
                        }
                    }
                }
        );

        lang1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        langDialog.dismiss();
                        promptSpeechInput("en_US");
                        inputLanguage = "en";
                    }
                }
        );

        lang2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        langDialog.dismiss();
                        promptSpeechInput(targetLanguage);
                        inputLanguage = targetLanguage;
                    }
                }
        );
//        chosenScenario = katoScen;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sound:
                soundC++;
                if (soundC % 2 != 0) { //If the sound counter is odd
                    item.setIcon(R.drawable.ic_volume_up_white_48dp);
                    soundEnabled = false;
                    Toast.makeText(this, "Sound Disabled", Toast.LENGTH_SHORT).show();
                } else {
                    item.setIcon(R.drawable.ic_volume_off_white_48dp);
                    soundEnabled = true;
                    Toast.makeText(this, "Sound Enabled", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_settings:
                settLock = 1;
                Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.action_about:
                //Creating About Dialog
                Dialog aboutDialog = new Dialog(context);
                aboutDialog.setContentView(R.layout.about_dialog);
                aboutDialog.setTitle("About Kato");
                aboutDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput(String targetLanguage) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, targetLanguage);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void ttsResponse(String output, Locale language) {
        if (soundEnabled) {
            tts.setLanguage(language);
            tts.speak(output, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void setVisualCue(String match) {
        switch (match) {
            case "airport":
                visualCue.setBackgroundResource(R.drawable.airport);
                break;
            case "ambulance":
                visualCue.setBackgroundResource(R.drawable.ambulance);
                break;
            case "blue":
                visualCue.setBackgroundResource(R.drawable.blue);
                break;
            case "brothers":
                visualCue.setBackgroundResource(R.drawable.brothers);
                break;
            case "bye":
                visualCue.setBackgroundResource(R.drawable.bye);
                break;
            case "cash":
                visualCue.setBackgroundResource(R.drawable.cash);
                break;
            case "cats":
                visualCue.setBackgroundResource(R.drawable.cats);
                break;
            case "confused":
                visualCue.setBackgroundResource(R.drawable.confused);
                break;
            case "credit":
                visualCue.setBackgroundResource(R.drawable.credit);
                break;
            case "cyprus":
                visualCue.setBackgroundResource(R.drawable.cyprus);
                break;
            case "dogcat":
                visualCue.setBackgroundResource(R.drawable.dogcat);
                break;
            case "fivefifty":
                visualCue.setBackgroundResource(R.drawable.fivefifty);
                break;
            case "forward":
                visualCue.setBackgroundResource(R.drawable.forward);
                break;
            case "germanitalian":
                visualCue.setBackgroundResource(R.drawable.germanitalian);
                break;
            case "greece":
                visualCue.setBackgroundResource(R.drawable.greece);
                break;
            case "greekfrench":
                visualCue.setBackgroundResource(R.drawable.greekfrench);
                break;
            case "green":
                visualCue.setBackgroundResource(R.drawable.green);
                break;
            case "happy":
                visualCue.setBackgroundResource(R.drawable.happy);
                break;
            case "hello":
                visualCue.setBackgroundResource(R.drawable.hello);
                break;
            case "irritated":
                visualCue.setBackgroundResource(R.drawable.irritated);
                break;
            case "left":
                visualCue.setBackgroundResource(R.drawable.left);
                break;
            case "meet":
                visualCue.setBackgroundResource(R.drawable.meet);
                break;
            case "movie":
                visualCue.setBackgroundResource(R.drawable.movie);
                break;
            case "museum":
                visualCue.setBackgroundResource(R.drawable.museum);
                break;
            case "music":
                visualCue.setBackgroundResource(R.drawable.music);
                break;
            case "neutral":
                visualCue.setBackgroundResource(R.drawable.neutral);
                break;
            case "nine":
                visualCue.setBackgroundResource(R.drawable.nine);
                break;
            case "no":
                visualCue.setBackgroundResource(R.drawable.no);
                break;
            case "park":
                visualCue.setBackgroundResource(R.drawable.park);
                break;
            case "red":
                visualCue.setBackgroundResource(R.drawable.red);
                break;
            case "repeat":
                visualCue.setBackgroundResource(R.drawable.repeat);
                break;
            case "right":
                visualCue.setBackgroundResource(R.drawable.right);
                break;
            case "russianswedish":
                visualCue.setBackgroundResource(R.drawable.russianswedish);
                break;
            case "sisters":
                visualCue.setBackgroundResource(R.drawable.sisters);
                break;
            case "six":
                visualCue.setBackgroundResource(R.drawable.six);
                break;
            case "student":
                visualCue.setBackgroundResource(R.drawable.student);
                break;
            case "teacher":
                visualCue.setBackgroundResource(R.drawable.teacher);
                break;
            case "teneuro":
                visualCue.setBackgroundResource(R.drawable.teneuro);
                break;
            case "tennis":
                visualCue.setBackgroundResource(R.drawable.tennis);
                break;
            case "three":
                visualCue.setBackgroundResource(R.drawable.three);
                break;
            case "thumbdown":
                visualCue.setBackgroundResource(R.drawable.thumbdown);
                break;
            case "thumbup":
                visualCue.setBackgroundResource(R.drawable.thumbup);
                break;
            case "onehundredm":
                visualCue.setBackgroundResource(R.drawable.onehundredm);
                break;
            case "tv":
                visualCue.setBackgroundResource(R.drawable.tv);
                break;
            case "twentypound":
                visualCue.setBackgroundResource(R.drawable.twentypound);
                break;
            case "twohundredm":
                visualCue.setBackgroundResource(R.drawable.twohundredm);
                break;
            case "volley":
                visualCue.setBackgroundResource(R.drawable.volley);
                break;
            case "why":
                visualCue.setBackgroundResource(R.drawable.why);
                break;
            case "yes":
                visualCue.setBackgroundResource(R.drawable.yes);
                break;
            default:
                visualCue.setBackgroundResource(R.drawable.transparent);
        }
    }

    private void translateOutput(String input, String inputLanguage, String targetLanguage) {
        if (targetLanguage.equals("fr")) {
            translateAPI.translate(input, TranslateAPI.ENGLISH, TranslateAPI.FRENCH);
            TTSOutputLanguage = Locale.FRENCH;
        } else if (targetLanguage.equals("es")) {
            translateAPI.translate(input, TranslateAPI.ENGLISH, TranslateAPI.SPANISH);
            TTSOutputLanguage = new Locale("es");
        } else if (targetLanguage.equals("it")) {
            translateAPI.translate(input, TranslateAPI.ENGLISH, TranslateAPI.ITALIAN);
            TTSOutputLanguage = Locale.ITALIAN;
        } else if (targetLanguage.equals("en")) {
            translateAPI.translate(input, inputLanguage, TranslateAPI.ENGLISH);
            TTSOutputLanguage = Locale.ENGLISH;
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    userInput = result.get(0).toString();
                    userInputView.setText(userInput);
                }
                break;
            }
        }
    }

    public void prepChatBot() {
        chatBot.findMatch(chatBot.preProcessInput(userInput), chosenScenario);
        chatBot.selectMatch();
        cbMatch = chatBot.getMatch();
        translateOutput(cbMatch, inputLanguage, targetLanguage);
    }

    public void runChatBot() {
        translatedOutput = translateAPI.getTranslation();
        userOutputView.setText(translatedOutput);
        ttsResponse(translatedOutput, TTSOutputLanguage);
        setVisualCue(chatBot.getVisualMatch());
        chatBot.clearResults();
    }

    @Override
    public void onTranslationReceived() {
        System.out.println("swapLang value is: " + swapLang);
        if (swapLang) {
            userInputView.setText(translateAPI.getTranslation());
            System.out.println("userInput variable is: " + userInput);
            swapLang = false; //Reset swap lang lock variable to false
        } else {
            if (inputLanguage.equals("en")) {
                runChatBot();
            } else {
                userInput = translateAPI.getTranslation();
                inputLanguage = "en";
                prepChatBot();
            }
        }
    }

    @Override
    public void onTranslationFailed() {
        //Handle failure
    }

    public void speechOnlyMode(Boolean b) {
        if (b) {
            repeat.setClickable(false);
            repeat.setEnabled(false);
            reveal.setClickable(false);
            reveal.setEnabled(false);
        } else {
            repeat.setClickable(true);
            repeat.setEnabled(true);
            reveal.setClickable(true);
            reveal.setEnabled(true);
        }
    }
}