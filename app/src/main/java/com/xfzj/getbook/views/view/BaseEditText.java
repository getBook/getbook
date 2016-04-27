package com.xfzj.getbook.views.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.MyUtils;

/**
 * Created by zj on 2016/4/16.
 */
public class BaseEditText extends FrameLayout implements TextWatcher {
    private Context context;
    private EditText editText;
    private TextInputLayout til;
    private RelativeLayout rlCount;
    private TextView tvTotal, tvNow;
    private int hint, text;
    private int hintColor, textColor;
    private int maxLength;
    private int editTextStyle;
    private boolean editTextNoLineStyle, showWordCount;
    //文字的开始位置和结束位置
    private int editStart;
    private int editEnd;

    private float editTextHeight;
    private int maxLines;

    public BaseEditText(Context context) {
        this(context, null);
    }

    public BaseEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.edittext_layout, null);
        editText = (EditText) view.findViewById(R.id.et);
        til = (TextInputLayout) view.findViewById(R.id.til);
        rlCount = (RelativeLayout) view.findViewById(R.id.rlCount);
        tvNow = (TextView) view.findViewById(R.id.tvNow);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseEditText);
        if (null != a) {
            hint = a.getResourceId(R.styleable.BaseEditText_hint, -1);
            text = a.getResourceId(R.styleable.BaseEditText_text, -1);
            hintColor = a.getColor(R.styleable.BaseEditText_hintColor, -1);
            textColor = a.getColor(R.styleable.BaseEditText_textColor, -1);
            editTextStyle = a.getResourceId(R.styleable.BaseEditText_edittextStyle, -1);
            editTextNoLineStyle = a.getBoolean(R.styleable.BaseEditText_editTextNoLineStyle, false);
            maxLength = a.getInt(R.styleable.BaseEditText_maxWordCount, -1);
            showWordCount = a.getBoolean(R.styleable.BaseEditText_showWordCount, false);
            editTextHeight = a.getDimension(R.styleable.BaseEditText_editTextHeight, -1f);
            maxLines = a.getInt(R.styleable.BaseEditText_editTextmaxLines, -1);
            if (hint != -1) {
                til.setHint(context.getString(hint));
            }
            if (text != -1) {
                editText.setText(text);
            }
            if (hintColor != -1) {
                editText.setHintTextColor(hintColor);
            }
            if (textColor != -1) {
                editText.setTextColor(textColor);
            }
            if (editTextStyle != -1) {
                editText.setBackgroundResource(editTextStyle);
            }
            if (editTextNoLineStyle) {
                editText.setBackgroundResource(0);
            }
            if (maxLength != -1) {
                editText.addTextChangedListener(this);
            }
            if (editTextHeight != -1f) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) til.getLayoutParams();
                layoutParams.height = (int) editTextHeight;
                til.setLayoutParams(layoutParams);
            }
            if (maxLines != -1) {
                editText.setMaxLines(maxLines);
            }
            isShowWordCount();
            a.recycle();
        }

        addView(view);
    }

    public boolean isLessMaxCount() {
        if (MyUtils.calculateLength(editText.getText().toString().trim()) > maxLength) {
            return false;
        }
        return true;
    }

    public void setShowWordCount(boolean showWordCount) {
        this.showWordCount = showWordCount;
        isShowWordCount();
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        editText.setMaxLines(maxLines);
    }


    private void isShowWordCount() {
        if (maxLength != -1 && showWordCount) {
            rlCount.setVisibility(VISIBLE);
            tvNow.setText(MyUtils.calculateLength(editText.getText().toString()) + "");
            tvTotal.setText("/" + maxLength);
            tvTotal.setTextColor(context.getResources().getColor(R.color.accent));
        } else {
            rlCount.setVisibility(GONE);
        }
    }

    public void setEditTextNoLineStyle(boolean editTextNoLineStyle) {
        this.editTextNoLineStyle = editTextNoLineStyle;
    }

    public void setEditTextStyle(int editTextStyle) {
        this.editTextStyle = editTextStyle;
        editText.setBackgroundResource(editTextStyle);
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        isShowWordCount();
        editText.addTextChangedListener(this);
    }

    public void setHint(int hint) {
        this.hint = hint;
        til.setHint(context.getString(hint));
    }

    public void setText(int text) {
        this.text = text;
        editText.setText(text);
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
        editText.setHintTextColor(hintColor);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        editText.setTextColor(textColor);
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        editStart = editText.getSelectionStart();
        editEnd = editText.getSelectionEnd();

        editText.removeTextChangedListener(this);

        while (MyUtils.calculateLength(s.toString()) > maxLength) {
            s.delete(editStart - 1, editEnd);
            editStart--;
            editEnd--;
        }
        editText.setSelection(editStart);
        if (rlCount.getVisibility() == VISIBLE) {
            long length = MyUtils.calculateLength(editText.getText().toString());
            tvNow.setText(length + "");
            if (length > maxLength) {
                tvNow.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                tvNow.setTextColor(context.getResources().getColor(R.color.accent));
            }
        }
        editText.addTextChangedListener(this);
    }
}
