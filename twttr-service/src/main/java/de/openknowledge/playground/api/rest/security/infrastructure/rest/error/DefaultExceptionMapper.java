/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package de.openknowledge.playground.api.rest.security.infrastructure.rest.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Handles all uncaught Exceptions. Prevents leaking internal details to the client.
 *
 * Returns always status {@link Status#UNAUTHORIZED} except for @{link WebApplicationException}.
 */
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionMapper.class);

  @Override
  public Response toResponse(final Exception exception) {
    LOG.error(exception.getMessage(), exception);

    if (exception instanceof NotFoundException) {
      // do not leak information about non existing entity
      return Response.status(Status.UNAUTHORIZED).build();
    }

    if (exception instanceof WebApplicationException) {
      return ((WebApplicationException)exception).getResponse();
    }

    return Response.status(Status.UNAUTHORIZED).build();
  }
}
