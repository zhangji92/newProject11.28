package com.appanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class ActivityAnimator extends AppCompatActivity implements View.OnClickListener {
    Button bnt_scale;
    ImageView imageView;
    Button bnt_vertical;
    Button bnt_move;
    Button bnt_parabola;
    Button bnt_delete;
    Button bnt_meanwhile;
    Button bnt_successively;
    Button bnt_meanwhile_successively;
    Button bnt_delete2;
    Button bnt_parabola2;
    Button bnt_move_library;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        imageView = (ImageView) findViewById(R.id.img_animator);
        //缩小并淡出
        bnt_scale = (Button) findViewById(R.id.scale_animator);
        bnt_scale.setOnClickListener(this);
        //垂直
        bnt_vertical = (Button) findViewById(R.id.vertical);
        bnt_vertical.setOnClickListener(this);
        //平移
        bnt_move = (Button) findViewById(R.id.move);
        bnt_move.setOnClickListener(this);
        //抛物线
        bnt_parabola = (Button) findViewById(R.id.parabola);
        bnt_parabola.setOnClickListener(this);
        //删除元素
        bnt_delete = (Button) findViewById(R.id.delete);
        bnt_delete.setOnClickListener(this);
        //同时
        bnt_meanwhile = (Button) findViewById(R.id.meanwhile);
        bnt_meanwhile.setOnClickListener(this);
        //依次
        bnt_successively = (Button) findViewById(R.id.successively);
        bnt_successively.setOnClickListener(this);
        //同时依次
        bnt_meanwhile_successively = (Button) findViewById(R.id.meanwhile_successively);
        bnt_meanwhile_successively.setOnClickListener(this);
        //删除元素2
        bnt_delete2 = (Button) findViewById(R.id.delete2);
        bnt_delete2.setOnClickListener(this);
        //抛物线2
        bnt_parabola2 = (Button) findViewById(R.id.parabola2);
        bnt_parabola2.setOnClickListener(this);
        //调用库--移动动画
        bnt_move_library = (Button) findViewById(R.id.move_library);
        bnt_move_library.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.scale_animator) {//缩小并淡出
            scaleAnimatorRun();
        } else if (id == R.id.vertical) {//垂直
            verticalAnimator();
        } else if (id == R.id.move) {//平移缩放
            moveAnimatorRun();
        } else if (id == R.id.parabola) {//抛物线
            parabolaAnimator();
        }else if (id == R.id.parabola2) {//抛物线2
            parabolaAnimator2();
        } else if (id == R.id.delete) {//删除
            deleteAnimator();
        }else if (id == R.id.delete2) {//删除2
            deleteAnimator2();
        } else if (id == R.id.meanwhile) {//同时执行
            meanwhileAnimator();
        } else if (id == R.id.successively) {//依次执行
            successivelyAnimator();
        } else if (id == R.id.meanwhile_successively) {//同时依次执行
            mSAnimator();
        }else if (id == R.id.move_library) {//调用库--移动动画
            moveLibraryMode();
        }
    }
    //调用库--移动动画
    private void moveLibraryMode() {
        com.nineoldandroids.animation.ObjectAnimator objectAnimator= com.nineoldandroids.animation.ObjectAnimator.ofFloat(imageView,"translationX",0,200);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }

    //同时依次执行
    private void mSAnimator() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1, 1.5f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1, 1.5f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView, "translationX", 0,200);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(imageView, "translationX", 200,0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.play(animator1).with(animator2);
        animatorSet.play(animator2).with(animator3);
        animatorSet.play(animator4).after(animator3);
        animatorSet.start();
    }

    //依次执行
    private void successivelyAnimator() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1, 1.5f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1, 1.5f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.play(animator2).after(animator1);
        animatorSet.start();
    }

    //同时执行
    private void meanwhileAnimator() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView, "scaleX", 1, 1.5f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, "scaleY", 1, 1.5f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.playTogether(animator1, animator2);
        animatorSet.start();
    }

    //动画执行完，最终要删掉该元素
    private void deleteAnimator() {
        //透明动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", 0.5f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //获取imageView控件
                ViewGroup parent = (ViewGroup) imageView.getParent();
                //判断控件是否为空
                if (parent != null) {
                    //删除控件
                    parent.removeView(imageView);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
    //动画执行完，最终要删掉该元素
    private void deleteAnimator2() {
        //透明动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", 0.5f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup parent= (ViewGroup) imageView.getParent();
                if (parent!=null){
                    parent.removeView(imageView);
                }
            }
        });
        animator.start();

    }
    //抛物线
    private void parabolaAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {

            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                imageView.setX(point.x);
                imageView.setY(point.y);
            }
        });
    }
    //抛物线
    private void parabolaAnimator2() {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setDuration(3000);
        objectAnimator.setObjectValues(new PointF(0, 0));
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setTarget(imageView);
        objectAnimator.setEvaluator(new TypeEvaluator<PointF>() {

            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });
        objectAnimator.start();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                imageView.setX(point.x);
                imageView.setY(point.y);
            }
        });
    }
    //平移、缩放
    private void moveAnimatorRun() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", 0, imageView.getWidth());
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(imageView, pvhX, pvhY, pvhZ).setDuration(2000).start();
    }

    //自由落体
    private void verticalAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, imageView.getHeight());
        valueAnimator.setTarget(imageView);
        valueAnimator.setDuration(2000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                imageView.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }

    //缩小并淡出
    private void scaleAnimatorRun() {
        //设置透明动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "zhy", 1.0f, 0.4f);
        //设置时间
        animator.setDuration(2000);
        //开始动画
        animator.start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //根据时间的流逝获取百分比
                float value = (float) animation.getAnimatedValue();
                //透明
                imageView.setAlpha(value);
                //缩放
                imageView.setScaleX(value);
                imageView.setScaleY(value);
            }
        });
    }
}
