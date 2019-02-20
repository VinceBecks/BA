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
package de.openknowledge.playground.api.rest.security.application.version;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
 * A resource that provides access to version information
 */
@Path("version")
@PermitAll
public class VersionResource {

  private static final Logger LOG = LoggerFactory.getLogger(VersionResource.class);

  @Inject
  @ConfigProperty(name = "build.version")
  private String version;

  @Inject
  @ConfigProperty(name = "build.timestamp")
  private String timestamp;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response getVersion() {
    LOG.info("Get version information");
    return Response.ok(version + " build " + timestamp).build();
  }
}