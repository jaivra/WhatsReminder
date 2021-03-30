package com.jaivra.whatsreminder.gui;


import android.content.Context;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewChat extends androidx.appcompat.widget.AppCompatEditText implements TextWatcher {
    public TextViewChat(Context context) {
        super(context);
        addTextChangedListener(this);
    }


    public TextViewChat(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public TextViewChat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int specModeW = View.MeasureSpec.getMode(widthMeasureSpec);
        if (specModeW != View.MeasureSpec.EXACTLY) {
            Layout layout = getLayout();
            int linesCount = layout.getLineCount();
            if (linesCount > 1) {
                float textRealMaxWidth = 0;
                for (int n = 0; n < linesCount; ++n) {
                    textRealMaxWidth = Math.max(textRealMaxWidth, layout.getLineWidth(n));
                }
                int w = (int) Math.ceil(textRealMaxWidth);
                if (w < getMeasuredWidth()) {
                    super.onMeasure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.AT_MOST),
                            heightMeasureSpec);
                }
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().equals(""))
            setGravity(Gravity.CENTER_HORIZONTAL);
        else
            setGravity(Gravity.TOP | Gravity.START);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
