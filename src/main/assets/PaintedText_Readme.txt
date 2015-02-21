PaintedText

What is that?
PaintedText is an Android View that draws text using pattern from an image.
Just like making a text mask in your favorite image editing software.
Supplied as Android Studio library module

What problem does it solve?
It makes you flexible against constant changes in requirements.
It makes separation of elements so they can vary independently.

Typical scenario:
You have great text logo and you have supplied it as .png or .jpg image in your project files.
Today you  have new requirement to change only colors - you make new images but with different color.
Tomorrow you have another requirement to change font size - same thing happens.
The day after tomorrow you must change font type - same thing over and over again...
Can we supply different parts of our text logo as separate elements?
Can that be done faster in code?
I think yes.

How can I use it?
PaintedText can be created in code or in XML layout file

Can I have more details?
PaintedText is a simple View that draws text mask.

At creation we can supply:
-Font Name for custom font as String
-Image for text painting as  BitmapDrawable, Bitmap or Image resource identifier.
-Text for the screen as String.
-Text size in DP as int value.
All above parameters are optional and default reasonable values are supplied.

Can you show some code?

//Reference layout that will hold the PaintedText View
LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);

//Instantiate PaintedText
PaintedText myLogo = new PaintedText(this,"NEW DAY",R.drawable.cellular_red ,50,"kinifed.ttf");

//Put on screen
parentLayout.addView(myLogo);

How can I make it from XML?

    <com.boxcollider.paintedtext.PaintedText
        xmlns:custom="http://schemas.android.com/apk/res-auto"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        custom:fontName="PirataOne.ttf"
        custom:text="GREENY"
        custom:textDrawable="@drawable/cellular_green"
        custom:textSize="60dp"
        />


