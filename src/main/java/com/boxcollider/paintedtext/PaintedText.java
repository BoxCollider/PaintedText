package com.boxcollider.paintedtext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


/**
 * Created by aleksander on 2/19/15.
 */
public final class PaintedText extends View {

    //Reasonable default values when optional view state parameters missing
    private static final String DEFAULT_TEXT = "TEXT";
    private static final int DEFAULT_TEXT_SIZE = 64;
    private static final int DEFAULT_BITMAP_SIZE = 64;
    private static final LinearGradient DEFAULT_BITMAP_SHADER = new LinearGradient
            (0, 0, 0, DEFAULT_BITMAP_SIZE, Color.RED, Color.GREEN, Shader.TileMode.CLAMP);
    private static final Typeface DEFAULT_FONT=Typeface.DEFAULT;

    //Drawing related properties
    private Paint paint;
    private BitmapDrawable textBitmapDrawable;

    //Text related properties
    private int textSize;
    private String text;
    private String fontFileName;
    private Typeface font;

    //View related properties
    private int width;
    private int height;
    private PointF center;


    /**
     * Creates {@link com.boxcollider.paintedtext.PaintedText}.
     * <br/>
     * All parameters except context are optional.
     *<br/>
     * Reasonable default values are provided.
     * @param context The Context this view is running in
     * @param text Text value of this view
     * @param textBitmapDrawable Image that will be the drawing pattern for the text
     * @param textSizeInDP Text size expressed in DIP
     * @param fontFileName File name for custom font that resides in /assets folder
     */
    public PaintedText(Context context, String text, BitmapDrawable textBitmapDrawable, int textSizeInDP, String fontFileName) {
        super(context);
        Log.i("custom","CREATED");
        BitmapDrawable drawable=textBitmapDrawable;
        init(context, text, drawable, textSizeInDP, fontFileName);
    }

    /**
     * Creates {@link com.boxcollider.paintedtext.PaintedText}.
     * <br/>
     * All parameters except context are optional.
     *<br/>
     * Reasonable default values are provided.
     * @param context The Context the view is running in
     * @param text Text value of this view
     * @param textBitmapDrawableResourceID Image resource ID that will be the drawing pattern for the text
     * @param textSizeInDP Text size expressed in DIP
     * @param fontFileName File name for custom font that resides in /assets folder
     */
    public PaintedText(Context context, String text, int textBitmapDrawableResourceID, int textSizeInDP, String fontFileName) {
        super(context);
        Log.i("custom","CREATED");
        BitmapDrawable drawable=null;
        try {
             drawable = (BitmapDrawable) getResources().getDrawable(textBitmapDrawableResourceID);
        } catch (Exception e) {

        }
        init(context, text, drawable, textSizeInDP, fontFileName);
    }

    /**
     * Creates {@link com.boxcollider.paintedtext.PaintedText}.
     * <br/>
     * All parameters except context are optional.
     *<br/>
     * Reasonable default values are provided.
     * @param context The Context the view is running in
     * @param text Text value of this view
     * @param textBitmap Bitmap that will be the drawing pattern for the text
     * @param textSizeInDP Text size expressed in DIP
     * @param fontFileName File name for custom font that resides in /assets folder
     *
     */
    public PaintedText(Context context, String text, Bitmap textBitmap, int textSizeInDP, String fontFileName) {
        super(context);
        Log.i("custom","CREATED");
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), textBitmap);
        init(context, text, drawable, textSizeInDP, fontFileName);
    }

    public PaintedText(Context context, AttributeSet attrs) {
        super(context, attrs);
        intFromXml(context, attrs);
    }

    public PaintedText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intFromXml(context, attrs);
    }

    //Assign values for custom xml attributes of that view
    //Case when we make the view from layout editor
    private void intFromXml(Context context, AttributeSet attrs) {
        TypedArray customAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PaintedText, 0, 0);
        BitmapDrawable drawable = ((BitmapDrawable) customAttributes.getDrawable(R.styleable.PaintedText_textDrawable));
        int textSize = customAttributes.getDimensionPixelSize(R.styleable.PaintedText_textSize, DEFAULT_TEXT_SIZE);
        String text = customAttributes.getString(R.styleable.PaintedText_text);
        String fontFileName = customAttributes.getString(R.styleable.PaintedText_fontName);
        customAttributes.recycle();
        init(context, text, drawable, textSize, fontFileName);
    }

    //Case when we make the view in code
    private void init(Context context, String text, BitmapDrawable drawable, int textSizeInDP, String fontFileName) {
        textBitmapDrawable=drawable;
        checkValidDrawableOrAssignDefault();
        this.textSize = TypedValue.complexToDimensionPixelSize(textSizeInDP, context.getResources().getDisplayMetrics());
        checkTextSizePositiveOrAssignDefault();
        this.text = text;
        checkTextPresentOrAssignDefault();
        this.fontFileName = fontFileName;
        checkFontExistsOrAssignDefault(context);
        makePaint();
        calculateDimensions();
    }

    private void checkValidDrawableOrAssignDefault() {
        if (textBitmapDrawable == null) {
            textBitmapDrawable = generateDefaultBitmapDrawable();
        }
    }

    private BitmapDrawable generateDefaultBitmapDrawable() {

        Bitmap bitmap = Bitmap.createBitmap(DEFAULT_BITMAP_SIZE, DEFAULT_BITMAP_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint gradientPaint = new Paint();
        gradientPaint.setShader(DEFAULT_BITMAP_SHADER);
        canvas.drawPaint(gradientPaint);
        return new BitmapDrawable(getContext().getResources(), bitmap);
    }

    private void checkTextSizePositiveOrAssignDefault() {
        if (textSize <= 0) {
            textSize = DEFAULT_TEXT_SIZE;
        }
    }

    private void checkTextPresentOrAssignDefault() {
        if (text == null) {
            text = DEFAULT_TEXT;
        }
    }

    //Fallback to the system default font if custom font is not found or supplied
    private void checkFontExistsOrAssignDefault(Context context) {
        try {
            this.font = Typeface.createFromAsset(context.getAssets(), fontFileName);
        } catch (Exception fontNotFound) {
            this.font = DEFAULT_FONT;
        }
    }

    //Set paint for drawing
    private void makePaint() {
        Shader bitmapShader = new BitmapShader(textBitmapDrawable.getBitmap(), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint = new Paint();
        paint.setShader(bitmapShader);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        paint.setTypeface(font);
    }

    //Calculate and cache view dimensions based on supplied text
    //Calculate center of a view for later drawing
    public void calculateDimensions() {
        width = (int) (calculateRelativeWidth(text) * textSize);
        height = (int) (textSize);
        center = new PointF(width * 0.5f, height * 0.5f);
    }

    //Lowercase characters contribute by 0.5 relative units of width
    //Other characters contribute by 1 relative units of width
    private float calculateRelativeWidth(String text) {
        float characterRelativeWidthContribution = 0;
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLowerCase(text.codePointAt(i))) {
                characterRelativeWidthContribution += 0.6f;
            } else {
                characterRelativeWidthContribution += 1f;
            }
        }
        return characterRelativeWidthContribution;
    }

    //Draw text on screen
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawText(text, center.x, center.y + textSize * 0.25f, paint);
        canvas.drawText(text, center.x, center.y + textSize * 0.35f, paint);
    }

    //Measure pass performed by parent view
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

}
