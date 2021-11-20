package com.ineedyourcode.calculator.presenter;

import com.ineedyourcode.calculator.view.CalculatorView;

public class CalculatorPresenter {
    private CalculatorView view;
    private Calculator calculator;

    private Double argOne = 0.0;
    private Double argTwo = null;
    private Double result = null;
    private ArithmeticOperation previousOperation = null;
    private boolean isOperandLast = false; // режим, когда последней нажат операнд
    private boolean isEnterLast = false; // режим, когда последней нажата клавиша "="

    public CalculatorPresenter(CalculatorView view, Calculator calculator) {
        this.view = view;
        this.calculator = calculator;
    }

    public void onDigitsPressed(int digit) {
        if (isEnterLast) {
            isOperandLast = false;
            clearAll();
            argOne = argOne * 10 + digit;
            view.showHistory(argOne, argTwo, previousOperation);
            view.showResult(String.valueOf(argOne));
        } else if (previousOperation != null) {
            isOperandLast = false;
            argTwo = argTwo * 10 + digit;
            view.showResult(String.valueOf(argTwo));
            view.showHistory(argOne, argTwo, previousOperation);
        } else {
            isOperandLast = false;
            argOne = argOne * 10 + digit;
            view.showResult(String.valueOf(argOne));
            view.showHistory(argOne, argTwo, previousOperation);
        }
    }

    public void onArithmeticOperandsPressed(ArithmeticOperation operation) {
        if (isEnterLast) {
            isOperandLast = true;
            isEnterLast = false;
            view.showHistory(argOne, argTwo, operation);
        }

        if (isOperandLast) {
            view.showHistory(argOne, argTwo, operation);
        }

        if (argTwo != null && !isOperandLast) {
            isOperandLast = true;
            result = calculator.arithmeticOperation(argOne, argTwo, previousOperation);
            view.showResult(String.valueOf(result));
            argOne = result;
            argTwo = 0.0;
            view.showHistory(argOne, argTwo, operation);
        } else if (!isOperandLast) {
            isOperandLast = true;
            argTwo = 0.0;
            view.showHistory(argOne, argTwo, operation);
        }
        previousOperation = operation;
    }

    public void onEnterPressed() {
        if (isEnterLast) {
            isOperandLast = false;
            view.showHistory(result, argTwo, previousOperation);
            result = calculator.arithmeticOperation(result, argTwo, previousOperation);
            view.showResult(String.valueOf(result));
        } else if (argTwo != null) {
            isOperandLast = false;
            result = calculator.arithmeticOperation(argOne, argTwo, previousOperation);
            view.showResult(String.valueOf(result));
            isEnterLast = true;
            view.showHistory(argOne, argTwo, previousOperation);
        }
    }

    public void onClearPressed() {
        if (argTwo != null && !isEnterLast && !isOperandLast) {
            argTwo = (argTwo - argTwo % 10) / 10;
            view.showResult(String.valueOf(argTwo));
            view.showHistory(argOne, argTwo, previousOperation);
        } else if (!isEnterLast && !isOperandLast) {
            argOne = (argOne - argOne % 10) / 10;
            view.showResult(String.valueOf(argOne));
            view.showHistory(argOne, argTwo, previousOperation);
        }
    }

    public void onClearLongPressed() {
        clearAll();
    }

    public void clearAll() {
        view.showHistory(0.0, null, null);
        isEnterLast = false;
        isOperandLast = false;
        previousOperation = null;
        argOne = 0.0;
        argTwo = null;
        result = null;
        view.showResult(String.valueOf(0.0));
    }
}
