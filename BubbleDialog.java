package com.guoguoquan.myapplication;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 作者： duanyikang on 2017/2/25.
 * 邮箱： duanyikang@yixia.com
 * 描述： 气泡弹窗
 * 目标： 动态的在代码布局中使用该弹框，三角方向分别对准上下左右，根据传入的X，Y动态移动
 */

public class BubbleDialog extends RelativeLayout {

    //方向控制 0：下  1：左  2：上  3：右
    private int direction;
    private float targetX, targetY, targetWidth, targetHeight;
    private String text;

    private TextView tv_center_text;
    private ImageView iv_triangle;
    private LayoutParams tv_params;
    private LayoutParams iv_params;
    private LayoutParams parent_params;
    private int animetime = 0;
    private AnimatorSet animSet;

    public BubbleDialog(Context context, int direction, float targetX, float targetY, float targetWidth, float targetHeight, String text) {
        super(context);
        this.direction = direction;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.text = text;
        initView(context);
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    public void initView(Context context) {
        //文字设置
        tv_center_text = new TextView(context);
        tv_center_text.setId(R.id.tv_center_text);
        tv_center_text.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tv_center_text.setBackground(getResources().getDrawable(R.drawable.shape_long_press_pic_hite));
        //tv_center_text.setPadding(UIUtil.dip2px(context,10),UIUtil.dip2px(context,10),UIUtil.dip2px(context,10),UIUtil.dip2px(context,10));
        //图片设置
        iv_triangle = new ImageView(context);
        iv_triangle.setId(R.id.iv_triangle);
        tv_center_text.setText(text);
        iv_triangle.setImageDrawable(getResources().getDrawable(R.drawable.pic_hite_triangle));
        setDirection(context);
    }


    /**
     * 设置布局结构
     *
     * @param context
     */
    private void setDirection(Context context) {
        if (direction == 0) {
            tv_params = new LayoutParams(UIUtil.dip2px(context, 120), UIUtil.dip2px(context, 30));
            iv_params = new LayoutParams(UIUtil.dip2px(context, 10), UIUtil.dip2px(context, 5));
            iv_params.addRule(RelativeLayout.BELOW, R.id.tv_center_text);
            iv_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            tv_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (direction == 1) {
            iv_triangle.setRotation(90);
            tv_params = new LayoutParams(UIUtil.dip2px(context, 120), UIUtil.dip2px(context, 30));
            iv_params = new LayoutParams(UIUtil.dip2px(context, 10), UIUtil.dip2px(context, 10));
            tv_params.addRule(RelativeLayout.RIGHT_OF, R.id.iv_triangle);
            iv_params.addRule(CENTER_VERTICAL);
            tv_params.addRule(CENTER_VERTICAL);
        } else if (direction == 2) {
            iv_triangle.setRotation(180);
            tv_params = new LayoutParams(UIUtil.dip2px(context, 120), UIUtil.dip2px(context, 30));
            iv_params = new LayoutParams(UIUtil.dip2px(context, 10), UIUtil.dip2px(context, 5));
            tv_params.addRule(RelativeLayout.BELOW, R.id.iv_triangle);
            iv_params.addRule(CENTER_HORIZONTAL);
            tv_params.addRule(CENTER_HORIZONTAL);
        } else if (direction == 3) {
            iv_triangle.setRotation(-90);
            tv_params = new LayoutParams(UIUtil.dip2px(context, 120), UIUtil.dip2px(context, 30));
            iv_params = new LayoutParams(UIUtil.dip2px(context, 10), UIUtil.dip2px(context, 10));
            iv_params.addRule(RelativeLayout.RIGHT_OF, R.id.tv_center_text);
            iv_params.addRule(CENTER_VERTICAL);
            tv_params.addRule(CENTER_VERTICAL);
        }

        tv_center_text.setLayoutParams(tv_params);
        iv_triangle.setLayoutParams(iv_params);


        addView(tv_center_text);
        addView(iv_triangle);
        parent_params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(parent_params);

        setPosition(context);
    }


    /**
     * 设置布局移动的位置
     */
    private void setPosition(Context context) {
        if (direction == 0) {
            this.setX(targetX - UIUtil.dip2px(context, 120) / 2 + targetWidth / 2);
            this.setY(targetY - UIUtil.dip2px(context, 30));
        } else if (direction == 1) {
            this.setX(targetX + targetWidth);
            this.setY(targetY + UIUtil.dip2px(context, 30) / 2);
        } else if (direction == 2) {
            this.setX(targetX - UIUtil.dip2px(context, 120) / 2 + targetWidth / 2);
            this.setY(targetY + targetHeight);
        } else if (direction == 3) {
            this.setX(targetX - UIUtil.dip2px(context, 130));
            this.setY(targetY + UIUtil.dip2px(context, 30) / 2);
        }
        startAnim();
    }

    /**
     * 设置动画开始
     */
    private void startAnim() {
        this.setVisibility(View.VISIBLE);
        int height = -UIUtil.dip2px(getContext(), 15);
        animSet = new AnimatorSet();
        if (direction == 0) {
            ObjectAnimator moveUP = ObjectAnimator.ofFloat(this, "translationY", this.getY(), this.getY() + height);
            moveUP.setDuration(600);
            ObjectAnimator moveDown = ObjectAnimator.ofFloat(this, "translationY", this.getY() + height, this.getY());
            moveDown.setDuration(600);
            animSet.play(moveDown).after(moveUP);
        } else if (direction == 1) {
            ObjectAnimator moveLeft = ObjectAnimator.ofFloat(this, "translationX", this.getX(), this.getX() - height);
            moveLeft.setDuration(600);
            ObjectAnimator moveRight = ObjectAnimator.ofFloat(this, "translationX", this.getX() - height, this.getX());
            moveRight.setDuration(600);
            animSet.play(moveLeft).after(moveRight);
        } else if (direction == 2) {
            ObjectAnimator moveUP = ObjectAnimator.ofFloat(this, "translationY", this.getY(), this.getY() -height);
            moveUP.setDuration(600);
            ObjectAnimator moveDown = ObjectAnimator.ofFloat(this, "translationY", this.getY() - height, this.getY());
            moveDown.setDuration(600);
            animSet.play(moveUP).after(moveDown);
        } else if (direction == 3) {
            ObjectAnimator moveLeft = ObjectAnimator.ofFloat(this, "translationX", this.getX(), this.getX() + height);
            moveLeft.setDuration(600);
            ObjectAnimator moveRight = ObjectAnimator.ofFloat(this, "translationX", this.getX() + height, this.getX());
            moveRight.setDuration(600);
            animSet.play(moveLeft).after(moveRight);
        }
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animetime > 5) {
                    setVisibility(GONE);
                } else {
                    animetime++;
                    animation.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
