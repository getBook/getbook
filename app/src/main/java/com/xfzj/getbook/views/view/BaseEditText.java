package com.xfzj.getbook.views.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xfzj.getbook.R;
import com.xfzj.getbook.utils.MyUtils;

import java.util.List;

/**
 * Created by zj on 2016/4/16.
 */
public class BaseEditText extends FrameLayout implements TextWatcher, View.OnClickListener {
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
    private boolean isEmojiShow;
    private ImageView ivEmoji;
    /**
     * emoji表情文件数据
     */
    private List<String> datas;
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
        ivEmoji = (ImageView) view.findViewById(R.id.ivEmoji);
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
            isEmojiShow = a.getBoolean(R.styleable.BaseEditText_isEmojiShow, false);
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
            }else{
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) til.getLayoutParams();
                layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                til.setLayoutParams(layoutParams);
            }
            if (maxLines != -1) {
                editText.setMaxLines(maxLines);
            }
            if (isEmojiShow) {
                ivEmoji.setVisibility(VISIBLE);
                ivEmoji.setOnClickListener(this);
            } else {
                ivEmoji.setVisibility(GONE);
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

    public void setError(String error) {
        til.setError(error);
    }

    public void setErrorEnable(boolean b) {
        til.setErrorEnabled(b);
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

    public void setEditTextInputType(int tyepe) {
        editText.setInputType(tyepe);
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

    public void setText(String text) {
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
//        if(null==datas||datas.size()==0) {
//           datas = FileUtils.getEmojiFile(context);
//            FaceConversionUtil.getInstace().ParseData(datas, context);
//        }
//        String[] str = s.toString().split("[em]+\\S+[/em]");
//        for (String ss : str) {
//            for (ChatEmoji chatEmoji : FaceConversionUtil.emojis) {
//                if (chatEmoji.getCharacter().equals(ss)) {
//                    SpannableString spannableString = FaceConversionUtil.getInstace()
//                            .addFace(getContext(), chatEmoji.getId(), chatEmoji.getCharacter());
//                    int position = s.toString().indexOf(ss);
//                    
//                }
//
//            }
//        }
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

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        editText.setOnFocusChangeListener(l);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivEmoji) {
            if (null != onEmojiClickListener) {
                onEmojiClickListener.onEmojiClick();
            }
        }
    }

    private OnEmojiClickListener onEmojiClickListener;

    public void setOnEmojiClickListener(OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
    }

    public void append(SpannableString spannableString) {
        editText.append(spannableString);
    }

    
    public void deleteSpannaleString(String pre, String after) {
        Editable editable = editText.getEditableText();
        int start = editText.getSelectionStart();
        String str = editText.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            if (start - after.length() <= 0) {
                int end = editText.getSelectionEnd();
                editable.delete(start - 1, end);
                editText.setSelection(start - 1);
            } else {
                String sub = str.substring(start - after.length(), start);
                if (after.equals(sub)) {
                    int position = str.lastIndexOf(pre);
                    if (position != -1) {
                        editable.delete(position, start);
                        editText.setSelection(editText.getSelectionStart());
                    } else {
                        int end = editText.getSelectionEnd();
                        editable.delete(start - 1, end);
                        editText.setSelection(start - 1);
                    }
                } else {
                    int end = editText.getSelectionEnd();
                    editable.delete(start - 1, end);
                    editText.setSelection(start - 1);
                }
            }
        }
        etrequestFocus();
    }

    public void etrequestFocus() {
        editText.setFocusable(true);
        editText.requestFocus();
    }

    public ImageView getImageView() {
        return ivEmoji;
    }
    public interface OnEmojiClickListener {
        void onEmojiClick();
    }
}
