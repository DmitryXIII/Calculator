package com.ineedyourcode.calculator.presenter;

import com.ineedyourcode.calculator.view.CalculatorView;

public class CalculatorPresenter {
    private CalculatorView view;
    private Calculator calculator;

    private Double argOne = 0.0;
    private Double argTwo = null;
    private ArithmeticOperation previousOperation = null;

    public CalculatorPresenter(CalculatorView view, Calculator calculator) {
        this.view = view;
        this.calculator = calculator;
    }

    public void onDigitsPressed(int digit) {
        if (previousOperation != null) {
            argTwo = argTwo * 10 + digit;
            view.showResult(String.valueOf(argTwo));
        } else {
            argOne = argOne * 10 + digit;
            view.showResult(String.valueOf(argOne));
        }
    }

    public void onArithmeticOperandsPressed (ArithmeticOperation operation) {
        if (argTwo != null) {
            double result = calculator.arithmeticOperation(argOne, argTwo, previousOperation);
            view.showResult(String.valueOf(result));
            argOne = result;
            argTwo = 0.0;
        } else {
            argTwo = 0.0;
        }
        previousOperation = operation;
    }
}
