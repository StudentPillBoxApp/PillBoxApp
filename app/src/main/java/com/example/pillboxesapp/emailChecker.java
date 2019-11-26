package com.example.pillboxesapp;

import java.util.regex.Pattern;

public class emailChecker {

    protected static final String EMAIL_FORMAT_ERROR = "Email not formatted correctly";
    protected static final String EMAIL_EMPTY_ERROR = "Email required";
    private String emailAddress;

    public emailChecker(String email) {
        emailAddress = email;
    }

    protected boolean isEmailAddressValid() {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        final boolean emailMatchesPattern = emailPattern.matcher(emailAddress).matches();
        return emailMatchesPattern;
    }
}
