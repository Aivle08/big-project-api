package com.aivle08.big_project_api.util;

import com.aivle08.big_project_api.exception.PasswordValidationException;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final int MIN_LENGTH = 8;

    // 영문(대소문자), 숫자, 특수문자 포함
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$");

    public static boolean isValid(String password) {

        if (password == null) {
            throw new PasswordValidationException("비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        if (password.length() < MIN_LENGTH) {
            throw new PasswordValidationException("비밀번호는 최소 8자 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new PasswordValidationException("비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다.", HttpStatus.BAD_REQUEST);
        }

        return true;
    }
}
