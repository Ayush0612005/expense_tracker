package com.ayush.expensetracker.exception;

public class InvalidCredentials extends RuntimeException {
	public InvalidCredentials(String message) {
		super(message);
	}
}
