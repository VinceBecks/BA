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
package de.openknowledge.twttrService.api.rest.application.user;

import de.openknowledge.twttrService.api.rest.domain.account.Account;
import de.openknowledge.twttrService.api.rest.infrastructure.persistence.repository.TwttrRepository;
import de.openknowledge.twttrService.api.rest.infrastructure.security.Authenticated;
import org.keycloak.KeycloakSecurityContext;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Produces an authenticatedAccount user for the application.
 */

@RequestScoped
public class AuthenticatedAccountProducer {

  @Inject
  private TwttrRepository repository;

  @Produces
  @RequestScoped
  @Authenticated
  public Account getUser(final KeycloakSecurityContext context) {
    String userName = context.getToken().getPreferredUsername();
    Account account = repository.findAccountByUserName(userName);
    return account;
  }
}
