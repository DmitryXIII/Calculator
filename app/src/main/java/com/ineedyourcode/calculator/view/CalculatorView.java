package com.ineedyourcode.calculator.view;

import android.widget.TextView;

import com.ineedyourcode.calculator.presenter.ArithmeticOperation;

public interface CalculatorView {
    void showResult(String value);

    void showHistory(Double argOne, Double argTwo, ArithmeticOperation operation, boolean isEnter);

    void showLongOrDouble(TextView view, String value);
}
