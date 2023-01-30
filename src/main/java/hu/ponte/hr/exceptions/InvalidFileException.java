package hu.ponte.hr.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidFileException extends Exception {
    public InvalidFileException(String message) {
        super(message);
    }
}
