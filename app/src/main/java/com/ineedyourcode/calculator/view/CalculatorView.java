package com.ineedyourcode.calculator.view;

import android.widget.TextView;

import java.util.HashMap;

public interface CalculatorView {
    void showResult(HashMap<String, String> mapTextValues, HashMap<String, Integer> mapVisibilityValues);
}
