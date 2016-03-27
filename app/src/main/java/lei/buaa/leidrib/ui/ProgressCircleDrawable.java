package lei.buaa.leidrib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import lei.buaa.leidrib.R;

/**
 * Created by lei on 3/25/16.
 * email: lileibh@gmail.com
 */
public class ProgressCircleDrawable extends Drawable {
    private Paint mPaint;
    private float mProgress;
    private float mRadius;

    public ProgressCircleDrawable(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(context.getResources().getColor(R.color.dark_blue));
        mPaint.setStrokeWidth(context.getResources().getDimensionPixelOffset(R.dimen.dimen_5));
        mRadius = context.getResources().getDimensionPixelOffset(R.dimen.dimen_20);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        RectF newRect = new RectF(rect.centerX() - mRadius,
                rect.centerY() - mRadius,
                rect.centerX() + mRadius,
                rect.centerY() + mRadius);
        canvas.drawArc(newRect, 0, 360 * mProgress, false, mPaint);
    }

    @Override
    protected boolean onLevelChange(int level) {
        mProgress = level / 10000f;
        invalidateSelf();
        return true;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
