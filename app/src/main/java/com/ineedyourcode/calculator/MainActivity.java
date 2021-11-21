package com.ineedyourcode.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ineedyourcode.calculator.presenter.ArithmeticOperation;
import com.ineedyourcode.calculator.presenter.Calculator;
import com.ineedyourcode.calculator.presenter.CalculatorPresenter;
import com.ineedyourcode.calculator.view.CalculatorView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CalculatorView {

    ViewGroup historyContainer;
    TextView txtDisplay;
    TextView txt_argOne;
    TextView txt_argTwo;
    TextView txt_operand;
    private CalculatorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDisplay = findViewById(R.id.txt_display);
        presenter = new CalculatorPresenter(this, new Calculator());

        HashMap<Integer, Integer> digits = new HashMap<>();
        digits.put(R.id.btn_0, 0);
        digits.put(R.id.btn_1, 1);
        digits.put(R.id.btn_2, 2);
        digits.put(R.id.btn_3, 3);
        digits.put(R.id.btn_4, 4);
        digits.put(R.id.btn_5, 5);
        digits.put(R.id.btn_6, 6);
        digits.put(R.id.btn_7, 7);
        digits.put(R.id.btn_8, 8);
        digits.put(R.id.btn_9, 9);

        View.OnClickListener digitsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDigitsPressed(digits.get(v.getId()));
            }
        };

        findViewById(R.id.btn_0).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_1).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_2).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_3).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_4).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_5).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_6).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_7).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_8).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_9).setOnClickListener(digitsClickListener);

        HashMap<Integer, ArithmeticOperation> operands = new HashMap<>();
        operands.put(R.id.btn_plus, ArithmeticOperation.PLUS);
        operands.put(R.id.btn_minus, ArithmeticOperation.MINUS);
        operands.put(R.id.btn_multiply, ArithmeticOperation.MULTIPLY);
        operands.put(R.id.btn_division, ArithmeticOperation.DIVISION);

        View.OnClickListener onArithmeticOperandsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onArithmeticOperandsPressed(operands.get(v.getId()));
            }
        };

        findViewById(R.id.btn_plus).setOnClickListener(onArithmeticOperandsListener);
        findViewById(R.id.btn_minus).setOnClickListener(onArithmeticOperandsListener);
        findViewById(R.id.btn_multiply).setOnClickListener(onArithmeticOperandsListener);
        findViewById(R.id.btn_division).setOnClickListener(onArithmeticOperandsListener);

        findViewById(R.id.btn_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onEnterPressed();
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClearPressed();
            }
        });

        findViewById(R.id.btn_clear).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                presenter.onClearLongPressed();
                return true;
            }
        });

        historyContainer = findViewById(R.id.layout_animated);
        txt_argOne = findViewById(R.id.txt_argOne);
        txt_argTwo = findViewById(R.id.txt_argTwo);
        txt_operand = findViewById(R.id.txt_operand);
        txt_argTwo.setVisibility(View.GONE);
        txt_operand.setVisibility(View.GONE);

        findViewById(R.id.btn_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDotPressed();
            }
        });
    }

    @Override
    public void showResult(String value) {
        if (value == null) {
            value = String.valueOf(0.0);
        }
        txtDisplay.setText(value);
    }

    @Override
    public void showHistory(Double argOne, Double argTwo, ArithmeticOperation operation) {
        if (argOne == 0.0 && argTwo == null && operation == null) {
            txt_argOne.setText(R.string.cleared);
        } else {
            txt_argOne.setText(String.valueOf(argOne));
        }

        if (operation != null) {
            TransitionManager.beginDelayedTransition(historyContainer);
            switch (operation) {
                case PLUS:
                    txt_operand.setText("+");
                    break;
                case MINUS:
                    txt_operand.setText("-");
                    break;
                case MULTIPLY:
                    txt_operand.setText("*");
                    break;
                case DIVISION:
                    txt_operand.setText("/");
                    break;
            }
            txt_operand.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(historyContainer);
            txt_operand.setVisibility(View.GONE);
        }

        if (argTwo != null && argTwo != 0.0) {
            TransitionManager.beginDelayedTransition(historyContainer);
            txt_argTwo.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(historyContainer);
            txt_argTwo.setVisibility(View.GONE);
        }

        txt_argTwo.setText(argTwo == null ? ("") : (String.valueOf(argTwo)));
    }
}