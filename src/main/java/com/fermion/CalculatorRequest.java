package com.fermion;

/**
 * Created by @author frankegan on 9/28/18.
 */
public class CalculatorRequest {

    String arg1;
    String arg2;

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String a1) {
        this.arg1 = a1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String a2) {
        this.arg2 = a2;
    }

    public CalculatorRequest(String a1, String a2) {
        this.arg1 = a1;
        this.arg2 = a2;
    }

    public CalculatorRequest() {
        arg1="0";
        arg2="0";
    }
}
