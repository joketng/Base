package com.jointem.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.jointem.base.util.DensityUtils;


public class DrawableCenterButton extends AppCompatRadioButton {
    private String TAG = "DrawableCenterButton";
    private int width, height;
    private int offx = 0;//20
    private Context ct;

    public DrawableCenterButton(Context context) {
        super(context);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        height = width = DensityUtils.dip2px(context, 10);
        ct = context;

    }

    public RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableRight = drawables[2];
            if (drawableRight != null) {
                setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawableRight.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                setPadding(0, 0, (int) (getWidth() - bodyWidth) - offx, 0);
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }
        String text = getText().toString().trim();
        int length = text.length();
        if (length >= 5) {
            String charSequence = text.subSequence(0, 4).toString();
            String charSe = charSequence.concat("...");
            setText(charSe);
        }
        super.onDraw(canvas);
    }
}
