package com.ineedyourcode.calculator.view;

import android.widget.TextView;

import java.util.HashMap;

public interface CalculatorView {
    void showResults(HashMap<String, String> mapTextValues, HashMap<String, Integer> mapVisibilityValues);
    void showResults(String message);
}
