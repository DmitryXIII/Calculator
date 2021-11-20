package com.ineedyourcode.calculator.view;

import com.ineedyourcode.calculator.presenter.ArithmeticOperation;

public interface CalculatorView {
    void showResult(String value);
    void showHistory(Double argOne, Double argTwo, ArithmeticOperation operation);
}
