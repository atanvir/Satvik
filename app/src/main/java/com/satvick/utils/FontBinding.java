package com.satvick.utils;

import androidx.databinding.BindingAdapter;
import android.widget.TextView;

public class FontBinding {
    @BindingAdapter({"bind:font"})
    public static void setFont(TextView textView, String fontName) {
        textView.setTypeface(CustomFontFamily.getInstance().getFont(fontName));
    }

}