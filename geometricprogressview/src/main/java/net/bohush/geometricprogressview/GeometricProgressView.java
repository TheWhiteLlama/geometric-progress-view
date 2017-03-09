package net.bohush.geometricprogressview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import net.bohush.geometricprogressview.figure.GeometricFigure;
import net.bohush.geometricprogressview.figure.factories.GeometricFigureFactory;
import net.bohush.geometricprogressview.figure.factories.GeometricKiteFigureFactory;
import net.bohush.geometricprogressview.figure.factories.GeometricTriangleFigureFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class GeometricProgressView extends View {

    private static final int DEFAULT_NUMBER_OF_ANGLES = 6;
    private static final int DEFAULT_SIZE = 64;
    private static final int DEFAULT_DURATION = 1500;
    private static final int DEFAULT_FIGURE_PADDING = 2;
    private static final String DEFAULT_COLOR = "#00897b";
    private static final TYPE DEFAULT_TYPE = TYPE.KITE;

    private int color;
    private int width, height;
    private int desiredWidth;
    private int desiredHeight;
    private int figurePadding;
    private int duration;
    private PointF center;
    private int numberOfAngles;
    private List<GeometricFigure> figures;
    private List<ValueAnimator> animators;
    private GeometricProgressView.TYPE type;

    @Nullable
    private GeometricFigureFactory mGeometricFigure = null;

    public GeometricProgressView(Context context) {
        this(context, null);
    }

    public GeometricProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeometricProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        desiredWidth = dpToPx(DEFAULT_SIZE);
        desiredHeight = dpToPx(DEFAULT_SIZE);
        center = new PointF(0, 0);
        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GeometricProgressView);
            figurePadding = array.getDimensionPixelSize(R.styleable.GeometricProgressView_gp_figure_padding, DEFAULT_FIGURE_PADDING);
            numberOfAngles = array.getInteger(R.styleable.GeometricProgressView_gp_number_of_angles, DEFAULT_NUMBER_OF_ANGLES);
            setColor(array.getColor(R.styleable.GeometricProgressView_gp_color, Color.parseColor(DEFAULT_COLOR)));
            duration = array.getInteger(R.styleable.GeometricProgressView_gp_duration, DEFAULT_DURATION);
            final String customClassName = array.getString(R.styleable.GeometricProgressView_gp_custom_figure);
            if (customClassName != null) {
                try {
                    mGeometricFigure = (GeometricFigureFactory) Class.forName(customClassName).getConstructors()[0].newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            int typeInt = array.getInt(R.styleable.GeometricProgressView_gp_type, 0);
            switch (typeInt) {
                case 0:
                    type = TYPE.KITE;
                    break;
                case 1:
                    type = TYPE.TRIANGLE;
                    break;
            }
            array.recycle();
        } else {
            figurePadding = dpToPx(DEFAULT_FIGURE_PADDING);
            numberOfAngles = DEFAULT_NUMBER_OF_ANGLES;
            setColor(Color.parseColor(DEFAULT_COLOR));
            duration = DEFAULT_DURATION;
            type = DEFAULT_TYPE;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int measuredWidth = resolveSize(desiredWidth, widthMeasureSpec);
        final int measuredHeight = resolveSize(desiredHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.width = getWidth();
        this.height = getHeight();
        this.center.set(width / 2.0f, height / 2.0f);
        initializeFigures();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (GeometricFigure figure : figures) {
            figure.draw(canvas);
        }
    }

    public void setNumberOfAngles(int numberOfAngles) {
        this.numberOfAngles = numberOfAngles;
        initializeFigures();
    }

    public void setType(TYPE type) {
        this.type = type;
        initializeFigures();
    }

    public void setFigurePadding(int figurePadding) {
        this.figurePadding = figurePadding;
        initializeFigures();
    }

    public void setFigurePaddingInDp(int figurePadding) {
        setFigurePadding(dpToPx(figurePadding));
    }

    public void setColor(int color) {
        this.color = color;
        if (figures != null) {
            for (GeometricFigure figure : figures) {
                figure.withColor(color);
            }
        }
        invalidate();
    }

    public void setDuration(int duration) {
        this.duration = duration;
        setupAnimation();
    }

    private void initializeFigures() {
        if (!isInEditMode()) cancelAnimation();
        int size = Math.min(width, height);

        double circumference = numberOfAngles * figurePadding;
        double distanceFromCenter = circumference / (Math.PI * 2);
        int radius = size / 2 - (int) (distanceFromCenter);
        figures = new ArrayList<>();
        if (mGeometricFigure != null) {
            buildFigures(mGeometricFigure, radius, (float) distanceFromCenter);
        } else {
            final GeometricFigureFactory figure;
            switch (type) {
                default:
                case KITE:
                    figure = new GeometricKiteFigureFactory();
                    break;
                case TRIANGLE:
                    figure = new GeometricTriangleFigureFactory();
                    break;
            }
            buildFigures(figure, radius, (float) distanceFromCenter);
        }
        setupAnimation();
    }

    private void buildFigures(@NonNull final GeometricFigureFactory figureFactory, float radius, float distanceFromCenter) {
        for (int i = 0; i < numberOfAngles; i++) {
            final float deltaAngle = 360f / numberOfAngles;
            final float p = (float) i / (numberOfAngles - 1);
            final int alpha = (int) (230f * p + 25f);
            figures.add(figureFactory.create()
                    .withCenter(center.x, center.y)
                    .withAngles(deltaAngle, i * deltaAngle)
                    .withRadius(radius)
                    .withDistanceFrom(distanceFromCenter)
                    .withColor(color)
                    .withAlpha(isInEditMode() ? alpha : 0)
                    .build()
            );
        }
    }

    private void setupAnimation() {
        if (isInEditMode()) return;
        cancelAnimation();
        animators = new ArrayList<>();
        for (int i = 0; i < figures.size(); i++) {
            final GeometricFigure figure = figures.get(i);
            if (i != 0) {
                ValueAnimator startingFadeAnimator = ValueAnimator.ofInt((int) (i * (255.0 / numberOfAngles)), 0);
                startingFadeAnimator.setRepeatCount(1);
                startingFadeAnimator.setDuration((int) (i * (((double) duration) / numberOfAngles)));
                startingFadeAnimator.setInterpolator(new LinearInterpolator());
                startingFadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        figure.withAlpha((int) animation.getAnimatedValue());
                        invalidate();
                    }
                });
                startingFadeAnimator.start();
                animators.add(startingFadeAnimator);
            }

            ValueAnimator fadeAnimator = ValueAnimator.ofInt(255, 0);
            fadeAnimator.setRepeatCount(ValueAnimator.INFINITE);
            fadeAnimator.setDuration(duration);
            fadeAnimator.setInterpolator(new LinearInterpolator());
            fadeAnimator.setStartDelay((int) (i * (((double) duration) / numberOfAngles)));
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    figure.withAlpha((int) animation.getAnimatedValue());
                    invalidate();
                }
            });
            fadeAnimator.start();

            animators.add(fadeAnimator);
        }
    }

    private void cancelAnimation() {
        if (isInEditMode()) return;
        if (animators != null) {
            for (ValueAnimator valueAnimator : animators) {
                valueAnimator.cancel();
            }
            animators.clear();
            animators = null;
        }
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public enum TYPE {
        TRIANGLE, KITE
    }

}
