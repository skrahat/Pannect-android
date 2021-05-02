package com.example.bondhu;


import android.graphics.Color;

import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;



public class BuilderManager {

    private static final int[] imageResources1 = new int[]{
            R.drawable.happy,
            R.drawable.friendship,
            R.drawable.blushing,
            R.drawable.interest,
            R.drawable.satisfaction,
            R.drawable.romantic,
            R.drawable.awww,
            R.drawable.surprise,
            R.drawable.trust,


    };
    private static final int[] imageResources2 = new int[]{
            R.drawable.fighting,
            R.drawable.sad,
            R.drawable.scared,
            R.drawable.disgust,
            R.drawable.lonely,
            R.drawable.angry,
            R.drawable.awkwardness,
            R.drawable.annoyed,
            R.drawable.anxiety,

    };
    private static final int[] imageResources3 = new int[]{
            R.drawable.cooking,
            R.drawable.withfamily,
            R.drawable.music,
            R.drawable.walking,
            R.drawable.reading,
            R.drawable.yoga,
            R.drawable.swimming,
            R.drawable.singing,
            R.drawable.painting,


    };
    private static final int[] imageResources4 = new int[]{
            R.drawable.bat,
            R.drawable.bear,
            R.drawable.bee,
            R.drawable.butterfly,
            R.drawable.cat,
            R.drawable.deer,
            R.drawable.dolphin,
            R.drawable.eagle,
            R.drawable.horse,

    };
    private static final int[] imageResources5 = new int[]{
            R.drawable.bath,
            R.drawable.bored,
            R.drawable.gym,
            R.drawable.browsing,
            R.drawable.gaming,
            R.drawable.movie,
            R.drawable.cycling,
            R.drawable.eating,
            R.drawable.sleeping,


    };
    private static final int[] textResources1= new int[]{
            R.string.happy,
            R.string.friendship,
            R.string.blushing,
            R.string.interest,
            R.string.satisfaction,
            R.string.romantic,
            R.string.awww,
            R.string.surprise,
            R.string.trust,



    };
    private static final int[] textResources2 = new int[]{
            R.string.fighting,
            R.string.sad,
            R.string.scared,
            R.string.disgust,
            R.string.lonely,
            R.string.angry,
            R.string.awkwardness,
            R.string.annoyed,
            R.string.anxiety,


    };
    private static final int[] textResources3 = new int[]{
            R.string.cooking,
            R.string.withfamily,
            R.string.music,
            R.string.walking,
            R.string.reading,
            R.string.yoga,
            R.string.swimming,
            R.string.singing,
            R.string.painting,


    };
    private static final int[] textResources4 = new int[]{
            R.string.bath,
            R.string.bored,
            R.string.gym,
            R.string.browsing,
            R.string.gaming,
            R.string.movie,
            R.string.cycling,
            R.string.eating,
            R.string.sleeping,


    };
    private static final int[] textResources5 = new int[]{
            R.string.bath,
            R.string.bored,
            R.string.gym,
            R.string.browsing,
            R.string.gaming,
            R.string.movie,
            R.string.cycling,
            R.string.eating,
            R.string.sleeping,


    };
    public static int[] getTextResourceArray1() {
        return textResources1.clone();
    }
    public static int[] getTextResourceArray2() {
        return textResources2.clone();
    }
    public static int[] getTextResourceArray3() {
        return textResources3.clone();
    }
    public static int[] getTextResourceArray4() {
        return textResources4.clone();
    }
    public static int[] getTextResourceArray5() {
        return textResources5.clone();
    }
    private static int imageResourceIndex = 0;
    private static int TextIndex = 0;

    static int getImageResource1() {
        if (imageResourceIndex >= imageResources1.length) imageResourceIndex = 0;
        return imageResources1[imageResourceIndex++];
    }
    static int getImageResource2() {
        if (imageResourceIndex >= imageResources2.length) imageResourceIndex = 0;
        return imageResources2[imageResourceIndex++];
    }
    static int getImageResource3() {
        if (imageResourceIndex >= imageResources3.length) imageResourceIndex = 0;
        return imageResources3[imageResourceIndex++];
    }
    static int getImageResource4() {
        if (imageResourceIndex >= imageResources4.length) imageResourceIndex = 0;
        return imageResources4[imageResourceIndex++];
    }
    static int getImageResource5() {
        if (imageResourceIndex >= imageResources5.length) imageResourceIndex = 0;
        return imageResources5[imageResourceIndex++];
    }


    static int getTextResource1() {
        if (TextIndex >= textResources1.length) TextIndex = 0;
        return textResources1[TextIndex++];
    }
    static int getTextResource2() {
        if (TextIndex >= textResources2.length) TextIndex = 0;
        return textResources2[TextIndex++];
    }
    static int getTextResource3() {
        if (TextIndex >= textResources3.length) TextIndex = 0;
        return textResources3[TextIndex++];
    }
    static int getTextResource4() {
        if (TextIndex >= textResources4.length) TextIndex = 0;
        return textResources4[TextIndex++];
    }
    static int getTextResource5() {
        if (TextIndex >= textResources5.length) TextIndex = 0;
        return textResources5[TextIndex++];
    }


    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilder1() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource1())
                .normalTextRes(getTextResource1());

    }
    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilder2() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource2())
                .normalTextRes(getTextResource2());

    }
    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilder3() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource3())
                .normalTextRes(getTextResource3());

    }
    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilder4() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource4())
                .normalTextRes(getTextResource4());

    }
    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilder5() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource5())
                .normalTextRes(getTextResource5());

    }




    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilderWithDifferentPieceColor() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource1())
                .normalTextRes(R.string.text_inside_circle_button_text_normal)
                .pieceColor(Color.WHITE);
    }


    private static BuilderManager ourInstance = new BuilderManager();

    public static BuilderManager getInstance() {
        return ourInstance;
    }

    private BuilderManager() {
    }
}