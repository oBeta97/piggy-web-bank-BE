package oBeta.PiggyWebBank.security;

import oBeta.PiggyWebBank.exceptions.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@Component
public class ValidationControl {

    public void checkErrors(BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new BadRequestException(message);
        }
    }

}
