package com.sanverrigo.nevsquik;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Vzhuh Durdle on 10.06.2017.
 */

public class TextSpeecher {

    private TextToSpeech textToSpeech;

    public TextSpeecher(Context context) {
       textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    Locale locale = new Locale("ru");
                    textToSpeech.setLanguage(locale);
//                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
    }

    public void speak(String speech) {
        textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

}
