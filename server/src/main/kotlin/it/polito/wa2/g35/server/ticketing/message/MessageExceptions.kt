package it.polito.wa2.g35.server.ticketing.message

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class MessageExceptions : ResponseEntityExceptionHandler() {
    @ExceptionHandler(MessageNotFoundException::class)
    fun handleProductNotFound(e: MessageNotFoundException) = ProblemDetail
        .forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!!)
}

class MessageNotFoundException(message : String) : RuntimeException(message)

