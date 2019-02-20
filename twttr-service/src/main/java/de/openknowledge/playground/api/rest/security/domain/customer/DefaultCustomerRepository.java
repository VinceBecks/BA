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
package de.openknowledge.playground.api.rest.security.domain.customer;

import static org.apache.commons.lang3.Validate.notNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Implementation of the repository {@link CustomerRepository}.
 */
@ApplicationScoped
public class DefaultCustomerRepository implements Serializable, CustomerRepository {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultCustomerRepository.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Customer create(final Customer customer) {
    notNull(customer, "customer must not be null");

    LOG.debug("Create customer");
    entityManager.persist(customer);

    return customer;
  }

  @Override
  public void delete(final Customer customer) {
    notNull(customer, "customer must not be null");

    LOG.debug("Delete customer with id {}", customer);
    Customer reference = entityManager.getReference(Customer.class, customer.getId());
    entityManager.remove(reference);
  }

  @Override
  public Customer find(final Long id) throws CustomerNotFoundException {
    notNull(id, "id must not be null");

    LOG.debug("Locating customer {} with id {}", id);
    Customer customer = entityManager.find(Customer.class, id);

    if (customer == null) {
      throw new CustomerNotFoundException(id);
    }

    return customer;
  }

  @Override
  public List<Customer> findAll() {
    LOG.debug("Searching for customers");

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);

    Root<Customer> root = cq.from(Customer.class);

    cq.select(root);

    TypedQuery<Customer> query = entityManager.createQuery(cq);
    List<Customer> results = query.getResultList();

    LOG.debug("Located {} customer", results.size());

    return results;
  }

  @Override
  public Customer update(final Customer customer) {
    notNull(customer, "customer must not be null");

    LOG.debug("Update customer with id {}", customer.getId());

    return entityManager.merge(customer);
  }
}
