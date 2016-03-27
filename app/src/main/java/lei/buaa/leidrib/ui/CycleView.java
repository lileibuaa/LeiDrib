package lei.buaa.leidrib.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import lei.buaa.leidrib.R;

/**
 * Created by lei on 3/25/16.
 * email: lileibh@gmail.com
 */
public class CycleView extends View {
    private int mStartArc = 90;
    private int mStopArc = 90;
    private boolean mIsAccelerated = true;
    private int mMaxArcInterval = 270;
    private Paint mPaint;
    private RectF mContainer;
    private float mStrokeWidth;

    public CycleView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.colorPrimary));
        mPaint.setStyle(Paint.Style.STROKE);
        mStrokeWidth = context.getResources().getDimensionPixelOffset(R.dimen.dimen_3);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    public CycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CycleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mStartArc += mIsAccelerated ? 3 : 10;
        mStopArc += mIsAccelerated ? 10 : 3;
        mStartArc %= 360;
        mStopArc %= 360;
        int arcInterval = mStopArc - mStartArc;
        if (arcInterval < 0)
            arcInterval += 360;
        if (arcInterval > mMaxArcInterval) {
            mIsAccelerated = false;
        } else if (arcInterval < 10) {
            mIsAccelerated = true;
        }
        if (mContainer == null)
            mContainer = new RectF(mStrokeWidth, mStrokeWidth,
                    getWidth() - mStrokeWidth, getHeight() - mStrokeWidth);
        canvas.drawArc(mContainer, mStartArc, arcInterval, false, mPaint);
        invalidate();
    }

}
