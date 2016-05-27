package com.xfzj.getbook.views.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.MyUtils;

import butterknife.Bind;

/**
 * Created by zj on 2016/3/25.
 */
public class NotificationImaheView extends ImageView {
    @Bind(R.id.iv)
    ImageView iv;
    private Context context;
    private int mRippleColor;// 水波颜色  

    private float mMaskAlpha;// 遮掩透明度  
    private float mRippleAlpha;// 水波透明度  

    private int mRippleTime;// 水波动画时间  
    private ObjectAnimator mRippleAnim;// 显示水波动画  

    private float mRadius;// 当前的半径  
    private float mMaxRadius;// 水波最大半径  
    private float mDownX; // 记录手指按下的位置  
    private float mDownY;

    private Path mPath;
    private boolean mIsMask=true;
    private boolean mIsAnimating;// 是否正在播放动画  
    private RectF mRect;
    private Paint mMaskPaint;
    private Paint mRipplePaint;

    public NotificationImaheView(Context context) {
        this(context, null);
    }

    public NotificationImaheView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationImaheView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public NotificationImaheView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setImageResource(R.mipmap.notifications);
        mRippleColor = Color.WHITE;
        mMaskAlpha = 0.4f;
        mRippleAlpha = 0.4f;
        mRippleTime = 4000;

        mPath = new Path();
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRipplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mMaskPaint.setColor(mRippleColor);
        mRipplePaint.setColor(mRippleColor);
        mMaskPaint.setAlpha((int) (mMaskAlpha * 255));
        mRipplePaint.setAlpha((int) (mRippleAlpha * 255));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.notification_shake);
        if (null != animation) {
            startAnimation(animation);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制遮掩  
//        if (mIsMask) {
//            canvas.save(Canvas.CLIP_SAVE_FLAG);
//            mPath.reset();
//            mPath.addRect(mRect, Path.Direction.CW);
//            canvas.clipPath(mPath);
//            canvas.drawRect(mRect, mMaskPaint);
//            canvas.restore();
//
//        }
//     
        if(mRadius>((2*mMaxRadius)/5)){
            return;
        }
        // 绘制水波  
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        mPath.reset();
        mPath.addCircle(getWidth()/2, getHeight()/2, mRadius, Path.Direction.CW);
        canvas.clipPath(mPath);
        canvas.drawCircle(getWidth()/2, getHeight()/2, mRadius, mRipplePaint);
        canvas.restore();
    }

    // 初始化动画  
    private void initAnim() {
        mRippleAnim = ObjectAnimator.ofFloat(this, "radius", 0, MyUtils.dp2px(context,24));
        mRippleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mRippleAnim.setRepeatCount(ValueAnimator.INFINITE);
        
        mRippleAnim.setDuration(mRippleTime);
        mRippleAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setRadius(0);
                mIsMask = false;
                mIsAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mRippleAnim.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取最大半径  
        mMaxRadius = (float)Math.min(w, h);
        // 获取该类布局范围  
        mRect = new RectF(0f, 0f, getMeasuredWidth() * 1.0f,
                getMeasuredHeight() * 1.0f);
        // 初始化动画  
        initAnim();
    }

    // 属性动画回调的方法  
    public void setRadius(final float radius) {
        mRadius = radius;
        invalidate();
    }
}
