package medusa.theone.vibrator.lib;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiayong on 2015/4/29.
 */
public class Vibrator extends View implements ValueAnimator.AnimatorUpdateListener {
    private List<ShapeHolder> vibrators = new ArrayList<ShapeHolder>();
    private float mDensity;
    private AnimatorSet animation = null;
    private long duration = 1000;//default value
    private final static int BALL_NUMBER = 3;//小球数量
    private int lenght;//小球的摆动长度

    public Vibrator(Context context) {
        super(context);
        mDensity = getContext().getResources().getDisplayMetrics().density;
        createBalls();
    }

    public Vibrator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDensity = getContext().getResources().getDisplayMetrics().density;
        createBalls();
    }

    public Vibrator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = getContext().getResources().getDisplayMetrics().density;
        createBalls();
    }

    /**
     * 定义球的颜色、形状等，球的大小、位置等需要在OnLayout中定义
     */
    private void createBalls() {
        for (int i = 0; i < BALL_NUMBER; i++) {
            OvalShape circle = new OvalShape();
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            int red = (int) (100 + Math.random() * 155);
            int green = (int) (100 + Math.random() * 155);
            int blue = (int) (100 + Math.random() * 155);
            int color = 0xff000000 | red << 16 | green << 8 | blue;
            Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);
            int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f,
                    50f, color, darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            shapeHolder.setPaint(paint);
            vibrators.add(shapeHolder);
        }
    }

    /**
     * 将创建好的球s加入view布局中，
     *
     * @param width  容器宽度
     * @param height 容器高度
     */
    private void addBalls(final int width, final int height) {
        if (vibrators != null) {
            if (vibrators.size() != BALL_NUMBER) {
                throw new IllegalStateException("You can only add " + BALL_NUMBER + "balls!");
            }
            final float radius = height * 0.125f;
            addBall(0,radius * 0.5f, height * 0.5f, radius);
            addBall(1,width * 0.5f, height * 0.5f, radius);
            addBall(2,width, height * 0.5f, radius);
        }
    }

    /**
     * 添加小球
     * @param index 第index个小球
     * @param x 小球x坐标
     * @param y 小球y坐标
     * @param radius 小球半径
     */
    private void addBall(int index,float x, float y, float radius) {
        if(index >= BALL_NUMBER || index <0){
            throw new IndexOutOfBoundsException("we can't find the "+index+" ball");
        }
        ShapeHolder shapeHolder = vibrators.get(index);
        shapeHolder.resizeShape(radius * mDensity, radius * mDensity);
        shapeHolder.setX(x);
        shapeHolder.setY(y);

//        OvalShape circle = new OvalShape();
//        circle.resize(radius * mDensity, radius * mDensity);
//        ShapeDrawable drawable = new ShapeDrawable(circle);
//        ShapeHolder shapeHolder = new ShapeHolder(drawable);
//        shapeHolder.setX(x - radius);
//        shapeHolder.setY(y - radius);
//        int red = (int) (100 + Math.random() * 155);
//        int green = (int) (100 + Math.random() * 155);
//        int blue = (int) (100 + Math.random() * 155);
//        int color = 0xff000000 | red << 16 | green << 8 | blue;
//        Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);
//        int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
//        RadialGradient gradient = new RadialGradient(37.5f, 12.5f,
//                50f, color, darkColor, Shader.TileMode.CLAMP);
//        paint.setShader(gradient);
//        shapeHolder.setPaint(paint);
    }

    private void createAnimation() {
        if (animation == null) {
//            ObjectAnimator anim1 = ObjectAnimator.ofFloat(vibrators.get(0), "y",//改变object的y坐标
//                    0f, getHeight() - vibrators.get(0).getHeight()).setDuration(500);
            ValueAnimator vibrator = ValueAnimator.ofFloat(0, lenght);//TODO 这里使用getWidth()合适吗
            vibrator.setInterpolator(new AccelerateDecelerateInterpolator());
            vibrator.setDuration(duration);
            vibrator.setRepeatCount(ValueAnimator.INFINITE);
            vibrator.setRepeatMode(ValueAnimator.REVERSE);
            vibrator.addUpdateListener(this);
            animation = new AnimatorSet();
            animation.play(vibrator);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        //可能会被调用多次：当父容器中的布局变化之后，就会调用所有child的onLayout()重新布局
        super.onLayout(changed, left, top, right, bottom);
        final int width = getWidth();
        final int height = getHeight();
        lenght = width;
        //将小球加入布局
        addBalls(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < vibrators.size(); ++i) {
            ShapeHolder shapeHolder = vibrators.get(i);
            canvas.save();
            canvas.translate(shapeHolder.getX(), shapeHolder.getY());
            shapeHolder.getShape().draw(canvas);
            canvas.restore();
        }
    }

    /**
     * 注意：该方法的调用必须在onLayout()函数调用之后
     */
    public void startAnimation() {
        createAnimation();
        animation.start();
    }
    public void cancelAnimation(){
        if(animation != null){
            animation.cancel();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float x = (float) animation.getAnimatedValue();
        float fraction = animation.getAnimatedFraction();
        changeVibratorsX(x,fraction);
        invalidate();
    }
    //更改位置和颜色
    private void changeVibratorsX(float x,float fraction) {
        //不作合法性检查原因：在执行动画过程中该函数会非常频繁调用，因此这里我们选择信任vibrators！去掉多余的检查以提高程序性能
        /*if(vibrators == null || vibrators.size()!=BALL_NUMBER){
            throw new IllegalStateException("we don't have the right ball number");
        }*/
        ShapeHolder sh1 = vibrators.get(0);
        ShapeHolder sh2 = vibrators.get(1);
        ShapeHolder sh3 = vibrators.get(2);
        sh1.setX(x);
        sh3.setX(lenght - x);
        Log.e("xiayong", "xiayong:fraction = " + fraction);
        if(fraction == 0.5f){//TODO 检查浮点数比较是否合理
            //互换颜色
            int tempColor = sh1.getColor();
            sh1.setColor(sh2.getColor());
            sh2.setColor(sh3.getColor());
            sh3.setColor(tempColor);
        }
    }

}
