package br.elois.maktaba.button;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.elois.maktaba.R;

public class ButtonFlat extends RelativeLayout{


    final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    // Complete in child class
    int minWidth;
    int minHeight;
    int background;
    float rippleSpeed = 12f;
    int rippleSize = 3;
    Integer rippleColor;
    OnClickListener onClickListener;
    boolean clickAfterRipple = true;
    int backgroundColor = Color.parseColor("#1E88E5");
    TextView textButton;

    final int disabledBackgroundColor = Color.parseColor("#E2E2E2");
    int beforeBackground;

    // Indicate if user touched this view the last time
    public boolean isLastTouch = false;

    public ButtonFlat(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultProperties();
        clickAfterRipple = attrs.getAttributeBooleanValue(MATERIALDESIGNXML,
                "animate", true);
        setAttributes(attrs);
        beforeBackground = backgroundColor;
        if (rippleColor == null)
            rippleColor = makePressColor();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(enabled)
            setBackgroundColor(beforeBackground);
        else
            setBackgroundColor(disabledBackgroundColor);
        invalidate();
    }

    boolean animation = false;

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        animation = true;
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        animation = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(animation)
            invalidate();
        if (x != -1) {

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            canvas.drawCircle(x, y, radius, paint);
            if(radius > getHeight()/rippleSize)
                radius += rippleSpeed;
            if(radius >= getWidth()){
                x = -1;
                y = -1;
                radius = getHeight()/rippleSize;
                if(onClickListener != null&& clickAfterRipple)
                    onClickListener.onClick(this);
            }
            invalidate();
        }
    }

    protected void setDefaultProperties() {
        // Min size
        setMinimumHeight(dpToPx(minHeight, getResources()));
        setMinimumWidth(dpToPx(minWidth, getResources()));
        // Background shape
        setBackgroundResource(background);
        setBackgroundColor(backgroundColor);
        setBackgroundResource(R.drawable.bf_background_transparent);
        minHeight = 36;
        minWidth = 88;
        rippleSize = 3;
    }

    protected void setAttributes(AttributeSet attrs) {
        // Set text button
        String text = null;
        int textResource = attrs.getAttributeResourceValue(ANDROIDXML,"text",-1);
        if(textResource != -1){
            text = getResources().getString(textResource);
        }else{
            text = attrs.getAttributeValue(ANDROIDXML,"text");
        }
        if(text != null){
            textButton = new TextView(getContext());
            textButton.setText(text.toUpperCase());
            textButton.setTextColor(backgroundColor);
            textButton.setTypeface(null, Typeface.BOLD);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            textButton.setLayoutParams(params);
            addView(textButton);
        }
        int backgroundColor = attrs.getAttributeResourceValue(ANDROIDXML,"background",-1);
        if(backgroundColor != -1){
            setBackgroundColor(getResources().getColor(backgroundColor));
        }else{
            // Color by hexadecimal
            // Color by hexadecimal
            background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            if (background != -1)
                setBackgroundColor(background);
        }
    }

    // ### RIPPLE EFFECT ###

    float x = -1, y = -1;
    float radius = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        if (isEnabled()) {
            isLastTouch = true;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                radius = getHeight() / rippleSize;
                x = event.getX();
                y = event.getY();
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                radius = getHeight() / rippleSize;
                x = event.getX();
                y = event.getY();
                if (!((event.getX() <= getWidth() && event.getX() >= 0) && (event
                        .getY() <= getHeight() && event.getY() >= 0))) {
                    isLastTouch = false;
                    x = -1;
                    y = -1;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if ((event.getX() <= getWidth() && event.getX() >= 0)
                        && (event.getY() <= getHeight() && event.getY() >= 0)) {
                    radius++;
                    if (!clickAfterRipple && onClickListener != null) {
                        onClickListener.onClick(this);
                    }
                } else {
                    isLastTouch = false;
                    x = -1;
                    y = -1;
                }
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                isLastTouch = false;
                x = -1;
                y = -1;
            }
        }
        return true;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!gainFocus) {
            x = -1;
            y = -1;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // super.onInterceptTouchEvent(ev);
        return true;
    }

    public Bitmap makeCircle() {
        Bitmap output = Bitmap.createBitmap(
                getWidth() - dpToPx(6, getResources()), getHeight()
                        - dpToPx(7, getResources()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(rippleColor);
        canvas.drawCircle(x, y, radius, paint);
        if (radius > getHeight() / rippleSize)
            radius += rippleSpeed;
        if (radius >= getWidth()) {
            x = -1;
            y = -1;
            radius = getHeight() / rippleSize;
            if (onClickListener != null && clickAfterRipple)
                onClickListener.onClick(this);
        }
        return output;
    }

    /**
     * Make a dark color to ripple effect
     *
     * @return
     */
    protected int makePressColor() {
        int r = (this.backgroundColor >> 16) & 0xFF;
        int g = (this.backgroundColor >> 8) & 0xFF;
        int b = (this.backgroundColor >> 0) & 0xFF;
        r = (r - 30 < 0) ? 0 : r - 30;
        g = (g - 30 < 0) ? 0 : g - 30;
        b = (b - 30 < 0) ? 0 : b - 30;
        int color =  Color.rgb(r, g, b);
        return Color.parseColor("#88DDDDDD");
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        onClickListener = l;
    }

    // Set color of background
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        if(isEnabled())
            beforeBackground = backgroundColor;
        if (textButton!=null)
            textButton.setTextColor(color);
    }

    public void setRippleSpeed(float rippleSpeed) {
        this.rippleSpeed = rippleSpeed;
    }

    public float getRippleSpeed() {
        return this.rippleSpeed;
    }

    public void setText(String text) {
        textButton.setText(text);
    }

    public void setTextColor(int color) {
        textButton.setTextColor(color);
    }

    public TextView getTextView() {
        return textButton;
    }

    public String getText() {
        return textButton.getText().toString();
    }

    private int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
}