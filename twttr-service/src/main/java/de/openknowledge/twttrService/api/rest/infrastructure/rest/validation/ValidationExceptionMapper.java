package de.openknowledge.twttrService.api.rest.infrastructure.rest.validation;


import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedList;
import java.util.List;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<ValidationErrorDTO> errors = new LinkedList<>();
        e.getConstraintViolations().forEach(constraintViolation -> errors.add(new ValidationErrorDTO(constraintViolation.getMessage())));
        return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
    }
}
