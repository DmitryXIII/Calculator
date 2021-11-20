package com.ineedyourcode.calculator.presenter;

import com.ineedyourcode.calculator.view.CalculatorView;

public class CalculatorPresenter {
    private CalculatorView view;
    private Calculator calculator;

    private Double argOne = 0.0;
    private Double argTwo = null;
    private Double result = null;
    private ArithmeticOperation previousOperation = null;
    private boolean isEnterLast = false; // режим, когда последней нажата клавиша "="
    private boolean isResultEditing = false; // режим редактировния результата (когда после нажатия "=" пользователь стирает символы клавишей "C"

    public CalculatorPresenter(CalculatorView view, Calculator calculator) {
        this.view = view;
        this.calculator = calculator;
    }

    public void onDigitsPressed(int digit) {
        if (isEnterLast && !isResultEditing) {
            clearAll();
            argOne = argOne * 10 + digit;
            view.showResult(String.valueOf(argOne));
        } else if (isEnterLast && isResultEditing) {
            argTwo = result * 10 + digit;
            view.showResult(String.valueOf(argTwo));
            isResultEditing = false;
        } else if (previousOperation != null) {
            argTwo = argTwo * 10 + digit;
            view.showResult(String.valueOf(argTwo));
        } else {
            argOne = argOne * 10 + digit;
            view.showResult(String.valueOf(argOne));
        }
    }

    public void onArithmeticOperandsPressed(ArithmeticOperation operation) {
        if (isEnterLast && !isResultEditing) {
            isEnterLast = false;
        } else if (isEnterLast && isResultEditing) {
            isEnterLast = false;
            isResultEditing = false;
            argOne = result;
            argTwo = 0.0;
        }
        if (argTwo != null) {
            result = calculator.arithmeticOperation(argOne, argTwo, previousOperation);
            view.showResult(String.valueOf(result));
            argOne = result;
            argTwo = 0.0;
        } else {
            argTwo = 0.0;
        }
        previousOperation = operation;
    }

    public void onEnterPressed() {
        if (isEnterLast) {
            result = calculator.arithmeticOperation(result, argTwo, previousOperation);
            view.showResult(String.valueOf(result));
        } else if (argTwo != null) {
            result = calculator.arithmeticOperation(argOne, argTwo, previousOperation);
            view.showResult(String.valueOf(result));
            isEnterLast = true;
        }
    }

    public void onClearPressed() {
        if (isEnterLast) {
            isResultEditing = true;
            result = (result - result % 10) / 10;
            view.showResult(String.valueOf(result));
        } else if (argTwo != null) {
            argTwo = (argTwo - argTwo % 10) / 10;
            view.showResult(String.valueOf(argTwo));
        } else {
            argOne = (argOne - argOne % 10) / 10;
            view.showResult(String.valueOf(argOne));
        }
    }

    public void onClearLongPressed() {
        clearAll();
    }

    public void clearAll() {
        isEnterLast = false;
        isResultEditing = false;
        previousOperation = null;
        argOne = 0.0;
        argTwo = null;
        result = null;
        view.showResult("Cleared");
    }
}
