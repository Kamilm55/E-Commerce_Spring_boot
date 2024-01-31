package com.example.kamil.user.exception.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//The @ResponseStatus(HttpStatus.BAD_REQUEST) annotation is used to indicate to Spring that when an exception of type UserIsNotActiveException is thrown, it should automatically set the HTTP response status to "Bad Request" (HTTP 400). This annotation is particularly useful in scenarios where you want to provide a standardized HTTP status code for a specific exception.
//In your case, even if you don't use @ResponseStatus(HttpStatus.BAD_REQUEST), your exception handling in the @ExceptionHandler method explicitly sets the HTTP status code to "Bad Request" using HttpStatus.BAD_REQUEST. Therefore, the end result is the same—your response will have a status code of 400.
//The choice of whether to use @ResponseStatus or explicitly set the status in the exception handler depends on your preference and how you want to structure your code. Using @ResponseStatus can be seen as a declarative way of specifying the status code, making it clear at the exception level. On the other hand, explicitly setting the status in the exception handler gives you more flexibility and control over the response.
//If you have a global exception handler (like the one you provided) that sets the status code for this exception, the @ResponseStatus annotation might be redundant in this specific case. It's a matter of coding style and readability. You can choose the approach that aligns better with your code structure and preferences.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsNotActiveException extends RuntimeException {
    private static final String message = "User is not active!";
    public UserIsNotActiveException() {
        super(message);
    }

    public UserIsNotActiveException(String message) {
        super(message);
    }
}
