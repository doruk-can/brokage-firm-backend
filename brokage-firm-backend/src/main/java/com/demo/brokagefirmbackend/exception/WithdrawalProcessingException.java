package com.demo.brokagefirmbackend.exception;

public class WithdrawalProcessingException extends RuntimeException {
    public WithdrawalProcessingException(String message) {
        super(message);
    }
}