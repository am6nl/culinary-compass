package nl.abnamro.culinarycompass.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NotFoundException extends RuntimeException{

    private String message;

}
