package io.ahad.Service;
import io.ahad.Service.*;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;



@ControllerAdvice
public class GlobalExceptionHandler {

	//global exception
	//@ExceptionHandler(Exception.class)
	//public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,WebRequest webrequest)
	//{
//		ErrorDetails errorDetails=new ErrorDetails(new Date(),exception.getMessage(),webrequest.getDescription(false));
//		return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
//	@ExceptionHandler(value=NotFoundException.class)
//	public ResponseEntity<Object> handleUnauthorizedException(NotFoundException exception)
//	{
//		return ResponseHandler.generateresponse("User Not Found", HttpStatus.NOT_FOUND);
//	}

	 // handle specific exception: ResourceNotFound
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetail> handleResourceNotFoundException(NotFoundException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    // global exception: Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleGlobalException(Exception exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }


	

}
