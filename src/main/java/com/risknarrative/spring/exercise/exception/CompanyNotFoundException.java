package com.risknarrative.spring.exercise.exception;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(String search) {
        super("Company with the given details " + search + " doesn't exists.");
    }
}
