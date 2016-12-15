package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class CalculatorActivity extends Activity {
    private enum Operation {ADD, SUB, MUL, DIV}

    private String number, textError;
    TextView window;
    double p1, p2;
    private boolean AnsOk;
    Operation operation;

    static final String NUMBER = "number";
    static final String OPERATOR1 = "p1";
    static final String OPERATOR2 = "p2";
    static final String OPERATION = "operation";
    static final String ISANSWER = "AnsOk";
    static final String TEXTERROR = "textError";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        window = (TextView) findViewById(R.id.result);
        if (savedInstanceState != null) {
            number = savedInstanceState.getString(NUMBER);
            p1 = savedInstanceState.getDouble(OPERATOR1);
            p2 = savedInstanceState.getDouble(OPERATOR2);
            operation = (Operation) savedInstanceState.getSerializable(OPERATION);
            AnsOk = savedInstanceState.getBoolean(ISANSWER);
            textError = savedInstanceState.getString(TEXTERROR);
            if (textError.isEmpty()) {
                window.setText(number);
            } else {
                window.setText(textError);
            }
        } else {
            reset();
        }
    }

    public void onClickDigit(View view) {
        Button btn = (Button) view;
        if (number.equals("Error") && operation == null) {
            reset();
        }
        if (!number.equals("0")) {
            number += btn.getText().toString();
        } else if (btn.getId() != R.id.d0) {
            number = btn.getText().toString();
        }
        if (operation == null) {
            if (AnsOk) {
                reset();
                number = btn.getText().toString();
            }
            p1 = Double.parseDouble(number);
        } else {
            p2 = Double.parseDouble(number);
        }
        window.setText(number);
    }

    public void onClickEquivalence(View view) {
        if (!number.equals("Error") && operation != null) {
            ResultAction();
        } else if (number.equals("Error")) {
            textError = "Error: please, enter expression!";
            window.setText(textError);
        }
    }

    public void onClickSign(View view) {
        Button btn = (Button) view;
        if (p2 == 0 && operation == Operation.DIV && number.equals("0.0") || p2 != 0) {
            ResultAction();
        }
        if (number.equals("Error")) {
            textError = "Error: no number";
            window.setText(textError);
            return;
        }
        switch (btn.getId()) {
            case R.id.sub:
                operation = Operation.SUB;
                number = "0";
                break;
            case R.id.add:
                operation = Operation.ADD;
                number = "0";
                break;
            case R.id.mul:
                operation = Operation.MUL;
                number = "0";
                break;
            case R.id.div:
                operation = Operation.DIV;
                number = "0";
                break;
        }
    }


    public void onClickClear(View view) {
        reset();
    }

    public void ResultAction() {
        double answer = p1;
        boolean ERROR = false;
        switch (operation) {
            case ADD:
                answer = p1 + p2;
                break;
            case SUB:
                answer = p1 - p2;
                break;
            case MUL:
                answer = p1 * p2;
                break;
            case DIV:
                if (Math.abs(p2) < 0.0000000001) {
                    ERROR = true;
                    reset();
                    number = "Error";
                    textError = "Error: divide by zero";
                    window.setText(textError);
                } else {
                    answer = p1 / p2;
                }
                break;
        }
        if (!number.equals("Error")) {
            reset();
            number = String.valueOf(answer);
            window.setText(number);
            p1 = answer;
            AnsOk = true;
        }
    }

    private void reset() {
        p1 = 0;
        p2 = 0;
        operation = null;
        number = "0";
        AnsOk = false;
        textError = "";
        window.setText(number);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putString(NUMBER, number);
        saveInstanceState.putDouble(OPERATOR1, p1);
        saveInstanceState.putDouble(OPERATOR2, p2);
        saveInstanceState.putSerializable(OPERATION, operation);
        saveInstanceState.putBoolean(ISANSWER, AnsOk);
        saveInstanceState.putString(TEXTERROR, textError);
    }

}