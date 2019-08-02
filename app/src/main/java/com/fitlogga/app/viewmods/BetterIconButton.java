package com.fitlogga.app.viewmods;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.fitlogga.app.R;

/**
 * This class is used to better align an icon next to a button's text.
 * This is more applicable when the button's width is far greater than it's wrap_content
 */
public class BetterIconButton extends AppCompatButton {

    private Bitmap mIcon;
    private Paint mPaint;
    private Rect mSrcRect;
    private Rect mCachedRectDestRect;
    private int mIconPadding;
    private int mIconSize;

    public BetterIconButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public BetterIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BetterIconButton(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int shift = (mIconSize + mIconPadding) / 2;

        canvas.save();
        canvas.translate(shift, 0);

        super.onDraw(canvas);

        if (mIcon != null) {
            float textWidth = getPaint().measureText((String)getText());
            int left = (int)((getWidth() / 2f) - (textWidth / 2f) - mIconSize - mIconPadding);
            int top = getHeight()/2 - mIconSize/2;

            if (mCachedRectDestRect == null) {
                mCachedRectDestRect = new Rect(left, top, left + mIconSize, top + mIconSize);
            }

            canvas.drawBitmap(mIcon, mSrcRect, mCachedRectDestRect, mPaint);
        }

        canvas.restore();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BetterIconButton);

        for (int i = 0; i < array.getIndexCount(); ++i) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.BetterIconButton_iconSrc:
                    mIcon = drawableToBitmap(array.getDrawable(attr));
                    break;
                case R.styleable.BetterIconButton_iconPadding:
                    mIconPadding = array.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.BetterIconButton_iconSize:
                    mIconSize = array.getDimensionPixelSize(attr, 0);
                    break;
                default:
                    break;
            }
        }

        array.recycle();

        //If we didn't supply an icon in the XML
        if(mIcon != null){
            mPaint = new Paint();
            mSrcRect = new Rect(0, 0, mIcon.getWidth(), mIcon.getHeight());
        }



    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
