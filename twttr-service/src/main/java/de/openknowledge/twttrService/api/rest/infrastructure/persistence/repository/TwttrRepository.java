package de.openknowledge.twttrService.api.rest.infrastructure.persistence.repository;


import de.openknowledge.twttrService.api.rest.domain.account.Account;
import de.openknowledge.twttrService.api.rest.domain.account.User;
import de.openknowledge.twttrService.api.rest.domain.tweet.Tweet;
import de.openknowledge.twttrService.api.rest.domain.tweet.TweetState;
import de.openknowledge.twttrService.api.rest.infrastructure.rest.error.TweetNotFoundException;
import de.openknowledge.twttrService.api.rest.infrastructure.rest.error.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


@ApplicationScoped
public class TwttrRepository {

    Logger LOG = LoggerFactory.getLogger("TwttrRepository.class");

    @PersistenceContext(name = "TwttrService")
    private EntityManager em;

    public void persistTweet (final Tweet tweet) {
        em.persist(tweet);
        LOG.info("Persisted tweet");
    }

    public List<User> findUsersBySearchString (final String searchString) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<User> root = cq.from(User.class);

        cq.select(root)
                .distinct(true)
                .where(cb.or(
                        cb.like(root.get("name").get("userName").get("userName"), "%" + searchString + "%"),
                        cb.like(root.get("name").get("firstName").get("firstName"), "%" + searchString + "%"),
                        cb.like(root.get("name").get("lastName").get("lastName"), "%" + searchString + "%")
                ))
                .orderBy(
                        cb.asc(root.get("name").get("userName").get("userName")),
                        cb.asc(root.get("name").get("firstName").get("firstName")),
                        cb.asc((root.get("name").get("lastName").get("lastName")))
                );

        TypedQuery<User> query = em.createQuery(cq);
        List<User> users = query.getResultList();
        if (users == null || users.size() == 0) {
            LOG.info("Found no user with searchString {}", searchString);
            //todo: hier keine NotFoundException werfen, damit leere Liste zurückgegeben wird, wenn SearchString Inhalt von User hat, den es nicht gibt
        }
        LOG.info("Found {} users with containing searchString {}", users.size(), searchString);
        return users;
    }

    public List<Tweet> findTweetsInStatePublishFromUser(final Integer userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);

        Root<Tweet> root = cq.from(Tweet.class);

        cq.select(root)
                .distinct(true)
                .where(cb.and(
                    cb.equal(root.get("author"), userId),
                    cb.equal(root.get("state"), TweetState.PUBLISH)))
                .orderBy(cb.desc(root.get("pubDate")));

        TypedQuery<Tweet> query = em.createQuery(cq);
        List<Tweet> tweets = query.getResultList();
        if (tweets == null || tweets.size() == 0) {
            LOG.info("Found no tweets in state PUBLISH from user with id {}", userId);
            //todo: hier keine NotFoundException werfen, damit leere Liste zurückgegeben wird, wenn SearchString Inhalt von User hat, den es nicht gibt
        }
        LOG.info("Found {} tweets in state PUBLISH from user with id {}", tweets.size(), userId);
        return tweets;
    }


    public Tweet findTweetById (final Integer tweetId) {
        Tweet tweet = em.find(Tweet.class, tweetId);
        if (tweet == null) {
            LOG.info("Found no tweet with id {}", tweetId);
            throw new TweetNotFoundException(tweetId);
        }
        LOG.info("Found tweet with id {}", tweetId);
        return tweet;
    }

    public List<Tweet> findAllTweetsInStatePublish() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tweet> cq = cb.createQuery(Tweet.class);

        Root<Tweet> root = cq.from(Tweet.class);

        cq.select(root)
                .distinct(true)
                .where(cb.equal(root.get("state"), TweetState.PUBLISH))
                .orderBy(cb.desc(root.get("pubDate")));

        TypedQuery<Tweet> query = em.createQuery(cq);
        List<Tweet> tweets = query.getResultList();
        LOG.info("Found {} tweets", tweets.size());
        return tweets;
    }

    public Account findAccountById (final Integer accountId) {
        Account account = em.find(Account.class, accountId);
        if (account == null) {
            LOG.info("Found no account with id {}", accountId);
            //todo: muss AccountNotFoundException() sein
            throw new UserNotFoundException(accountId);
        }
        LOG.info("Found account with id {}", accountId);
        return account;
    }

    public User findUserById (final Integer userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            LOG.info("Found no user with id {}", userId);
            throw new UserNotFoundException(userId);
        }
        LOG.info("Found user with id {}", userId);
        return user;
    }

    public User findUserByUserName (final String userName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<User> root = cq.from(User.class);

        cq.select(root).where(cb.equal(root.get("name").get("userName").get("userName"), userName));

        TypedQuery<User> query = em.createQuery(cq);
        try {
            User user = query.getSingleResult();
            LOG.info("Found user with userName {}", userName);
            return user;
        }catch (NoResultException e) {
            LOG.warn("Found no user with userName {}", userName);
            e.printStackTrace();
            throw new UserNotFoundException(userName);
        }
    }

    public Account findAccountByUserName (final String userName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);

        Root<Account> root = cq.from(Account.class);

        cq.select(root).where(cb.equal(root.get("name").get("userName").get("userName"), userName));

        TypedQuery<Account> query = em.createQuery(cq);
        Account account= query.getSingleResult();
        LOG.info("Found account from {}", userName);
        return account;
    }

}
