package com.sanverrigo.nevsquik;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_IMAGE = "image";
    private static final String STATE_QUOTE = "quote";

    private Button buttonGenerateNew;
    private TextView textQuote;
    private ImageView imageFace;
    private TextSpeecher textSpeecher;

    private int chosenImageInd = -1;
    private int chosenQuoteInd = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonGenerateNew = (Button) findViewById(R.id.btn_generate_new);
        textQuote = (TextView) findViewById(R.id.text_quote);
        imageFace = (ImageView) findViewById(R.id.image_of_mr_nevsky);
        buttonGenerateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewQuote();
            }
        });
        textSpeecher = new TextSpeecher(getApplicationContext());
        if (savedInstanceState != null) {
            chosenImageInd = savedInstanceState.getInt(STATE_IMAGE);
            chosenQuoteInd = savedInstanceState.getInt(STATE_QUOTE);
            updateImageAndQuote();
        } else {
            generateNewQuote();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_IMAGE, chosenImageInd);
        outState.putInt(STATE_QUOTE, chosenQuoteInd);
    }

    private static final int[] images = {
            R.drawable.nevsky0,
            R.drawable.nevsky1,
            R.drawable.nevsky2,
            R.drawable.nevsky3,
            R.drawable.nevsky4,
            R.drawable.nevsky5,
    };

    private static final String[] quotes = {
            "Absolutely!",
            "It's so deadable",
            "Vot tak vot",
            "Where are you, where are you disappear?",
            "I have take a lot of tituls",
            "HOLLYWOOD right now needs new heroes",
            "You wanna play? Let's play!",
            "Водка внутри, а снаружи бутылка",
            "Я трижды Мистер Вселенная!",
            "Mr. World — это не 10%, это 5% моего успеха",
            "Стероиды — это не путь к Невскому и не путь к Шварценеггеру",
            "Я ел куриные грудки, я ел салаты",
            "Я снялся во многих голливудских фильмах"

    };

    private void generateNewQuote() {
        Random random = new Random();
        if (chosenImageInd == -1) {
            chosenImageInd = random.nextInt(images.length);
            chosenQuoteInd = random.nextInt(quotes.length);
        } else {
            int randImage = random.nextInt(images.length - 1) + 1;
            int randQuote = random.nextInt(quotes.length - 1) + 1;
            chosenImageInd = (chosenImageInd + randImage) % images.length;
            chosenQuoteInd = (chosenQuoteInd + randQuote) % quotes.length;
        }
        String quote = quotes[chosenQuoteInd];
        updateImageAndQuote();
        final Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        imageFace.startAnimation(rotateAnimation);
        textSpeecher.speak(quote);
    }

    private void updateImageAndQuote() {
        if (chosenImageInd < 0 || chosenImageInd >= images.length ||
                chosenQuoteInd < 0 || chosenQuoteInd >= quotes.length) {
            return;
        }
        setCurrentQuoteAndImage(images[chosenImageInd], quotes[chosenQuoteInd]);
    }

    private void setCurrentQuoteAndImage(int imageResource, String quote) {
        imageFace.setImageResource(imageResource);
        textQuote.setText(quote);

        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), imageResource);
        if (myBitmap != null && !myBitmap.isRecycled()) {
            Palette palette = Palette.from(myBitmap).generate();
            int defColor = 0x000000;
            int mutedColorLight = palette.getLightMutedColor(defColor);
            animateBackgroundTo(mutedColorLight);
            findViewById(R.id.root_view).setBackgroundColor(mutedColorLight);
        }
    }

    private void animateBackgroundTo(int mutedColorLight) {
        final View rootView = findViewById(R.id.root_view);
        int colorFrom = ((ColorDrawable)rootView.getBackground()).getColor();
        int colorTo = mutedColorLight;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(180);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                rootView.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

}
