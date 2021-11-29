package com.ineedyourcode.calculator.theme;

import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import com.ineedyourcode.calculator.R;

public enum Theme {

    ONE(R.style.Theme_Calculator, R.string.theme_one, "one"),
    TWO(R.style.Theme_Calculator_Light, R.string.theme_two, "two");

    @StyleRes
    private final int theme;

    @StringRes
    private final int name;

    private String key;

    Theme(int theme, int name, String key) {
        this.theme = theme;
        this.name = name;
        this.key = key;
    }

    public int getTheme() {
        return theme;
    }

    public int getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
