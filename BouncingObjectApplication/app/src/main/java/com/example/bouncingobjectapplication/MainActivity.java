package com.example.bouncingobjectapplication;

import android.animation.Animator;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final boolean DEBUG = false;
    private final boolean DEBUG_TOUCH_XY = false;
    private final boolean DEBUG_FLOOR = false;
    private final String TAG = "ME";

    private MainActivity mainActivity;
    private RelativeLayout mainRelativeLayout;
    private int[] lastTouchDownXY = new int[2];

    private EditText lastNameEditText;

    private int letterIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        mainRelativeLayout = (RelativeLayout)findViewById(R.id.mainlayout);
        mainRelativeLayout.setOnTouchListener(mainRelativeLayoutTouchListener);
        mainRelativeLayout.setOnClickListener(mainRelativeLayoutClickListener);

        lastNameEditText = (EditText)findViewById(R.id.lastNameEditText);
        lastNameEditText.addTextChangedListener(lastNameEditTextChangedListener);
    }

    View.OnTouchListener mainRelativeLayoutTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    // Начальная позиция появления объекта определяется нажатием пальца по экрану:
                    lastTouchDownXY[0] = (int)event.getX();
                    lastTouchDownXY[1] = (int)event.getY();

                    break;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    View.OnClickListener mainRelativeLayoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int x0 = lastTouchDownXY[0];
            final int y0 = lastTouchDownXY[1];

            if (DEBUG || DEBUG_TOUCH_XY)
                Log.i(TAG, "x0 = " + x0 + ", y0 = " + y0);

            String lastNameStr = lastNameEditText.getText().toString();

            // Для примера <объект> - цифра восемь:
            if (lastNameStr.isEmpty()) {
                lastNameStr = "8";
            }

            if (DEBUG)
                Log.i(TAG, "lastNameStr = " + lastNameStr);

            final TextView tv = new TextView(mainActivity);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = x0;
            params.topMargin = y0;
            tv.setLayoutParams(params);

            // <Объект> - буква фамилии:
            tv.setText(Character.toString(lastNameStr.charAt(letterIndex)));

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,64);
            tv.setTextColor(Color.GREEN);
            mainRelativeLayout.addView(tv);
            tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            // После каждого нажатия буква меняется на следующую,
            // по окончанию возвращаемся на первую букву:
            letterIndex = letterIndex >= lastNameStr.length() - 1
                    ? 0 : letterIndex + 1;

            // Объектов может быть несколько,
            // поэтому каждый обрабатывается в отдельном потоке приложения:
            Thread animator = new Thread(new Runnable() {

                /** «Пол». */
                private float floorY = mainRelativeLayout.getHeight() - tv.getMeasuredHeight();

                private float wallX = mainRelativeLayout.getWidth() - tv.getMeasuredWidth();

                private float velocityXStopThreshold = 1.f;
                private float velocityYStopThreshold = 10.f;

                /** Нач. значение горизонтальной составляющей скорости. */
                private float velocityX = 12f;

                private float velocityY = 0f;

                /** Понижающий коэффициент. */
                private float decreasingCoefficient = 0.85f;

                /** Ускорение. */
                private float acceleration = 2f;

                /** Шаговое значение времени движения. */
                private float timeStep = 0.8f;

                @Override
                public void run() {

                    int[] location = new int[2];
                    mainRelativeLayout.getLocationOnScreen(location);

                    if (DEBUG || DEBUG_FLOOR) {
                        Log.i(TAG, "mainRelativeLayoutYOnScreen = " + location[1]
                                + "\nmainRelativeLayoutHeight = " + mainRelativeLayout.getHeight()

                                + "\ntvHeigth = " + tv.getHeight()
                                + "\ntvMeasuredHeigth = " + tv.getMeasuredHeight()

                                + "\ndisplayHeight = " + getDisplayHeight()
                                + "\nstatusBarHeight = " + getStatusBarHeight()
                                + "\nactionBarHeight = " + getActionBarHeight()
                                + "\nnavigationBarHeight = " + getNavigationBarHeight()

                                + "\nfloorY = " + floorY);
                    }

                    final BouncingObject bo = new BouncingObject(x0, y0,
                            floorY, wallX,
                            velocityXStopThreshold, velocityYStopThreshold,
                            velocityX, velocityY,
                            decreasingCoefficient,
                            acceleration,
                            timeStep);

                    // Объект падает с ускорением вниз, отражаясь от нижней границы экрана,
                    // и с незначительным смешением вправо:
                    while (bo.next()) {

                        if (DEBUG)
                            Log.i(TAG, "x=" + bo.getX()
                                    + ", y=" + bo.getY()
                                    + ", velocityX=" + bo.getVelocityX()
                                    + ", velocityY=" + bo.getVelocityY());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setVisibility(View.INVISIBLE);

                                // С каждым разом высота отскока объекта уменьшается:
                                tv.setY(bo.getY());

                                tv.setX(bo.getX());
                                tv.setVisibility(View.VISIBLE);
                            }
                        });

                        try {
                            Thread.sleep(32);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Когда объект остановится, он пропадает с экрана:
                    tv.animate()
                      .alpha(0)
                      .setDuration(1000)
                      .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) { }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                mainRelativeLayout.removeView(tv);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) { }

                            @Override
                            public void onAnimationRepeat(Animator animator) { }
                    });
                }
            });
            animator.start();
        }
    };

    private int getDisplayHeight() {
        return this.getResources().getDisplayMetrics().heightPixels;
    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize }
        );
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarHeight;
    }

    private int getNavigationBarHeight() {
        int navigationBarHeight = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }

    TextWatcher lastNameEditTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            letterIndex = 0;
        }
    };

}
