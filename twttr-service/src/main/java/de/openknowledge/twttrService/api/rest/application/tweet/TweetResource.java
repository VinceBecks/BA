package de.openknowledge.twttrService.api.rest.application.tweet;


import de.openknowledge.twttrService.api.rest.application.user.UserDTO;
import de.openknowledge.twttrService.api.rest.domain.account.Account;
import de.openknowledge.twttrService.api.rest.domain.account.AccountType;
import de.openknowledge.twttrService.api.rest.domain.account.User;
import de.openknowledge.twttrService.api.rest.domain.tweet.Content;
import de.openknowledge.twttrService.api.rest.domain.tweet.Tweet;
import de.openknowledge.twttrService.api.rest.domain.tweet.TweetState;
import de.openknowledge.twttrService.api.rest.infrastructure.persistence.repository.TwttrRepository;
import de.openknowledge.twttrService.api.rest.infrastructure.rest.validation.ValidationErrorDTO;
import de.openknowledge.twttrService.api.rest.infrastructure.security.Authenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

import static de.openknowledge.twttrService.api.rest.infrastructure.security.Roles.MODERATOR;
import static de.openknowledge.twttrService.api.rest.infrastructure.security.Roles.USER;

@Path("tweets")
public class TweetResource {

    private static final Logger LOG = LoggerFactory.getLogger("TweetResource.class");

    @Inject
    private TwttrRepository repository;

    @Inject @Authenticated
    private Account authenticatedAccount;


    @POST
    @RolesAllowed({USER})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createNewTweet(@Valid @NotNull NewTweet newTweet) {
        LOG.info("Request to create new tweet");
        User requester = repository.findUserById(authenticatedAccount.getAccountId());

        Tweet tweet = Tweet.newTweet()
                .withContent(new Content(newTweet.getContent()))
                .withAuthor(requester)
                .build();
        LOG.info("New tweet created");
        repository.persistTweet(tweet);
        requester.getTweets().add(tweet);

        return Response.status(Response.Status.CREATED).entity(new TweetDTO(tweet)).build();
    }


    @GET
    @RolesAllowed({USER, MODERATOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTweets(@Valid @DefaultValue("0") @QueryParam("index") final Integer index,
                              @DefaultValue("3") @QueryParam("numTweets") final Integer numTweets) {
        LOG.info("Request to get {} tweets", numTweets);

        if (index < 0 || numTweets < 0) {
            LOG.warn("Invalid Query Param");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Invalid Query Param")).build();
        }

        List<Tweet> persistedTweets = new LinkedList<>();
        if (authenticatedAccount.getRole() == AccountType.MODERATOR) {
            LOG.info("Request from a moderator");
            persistedTweets = repository.findAllTweetsInStatePublish();
        }else {
            LOG.info("Request from a user");
            User requestingUser = repository.findUserById(authenticatedAccount.getAccountId());
            List<Tweet> finalPersistedTweets = persistedTweets;
            requestingUser.getFollows().forEach(user -> finalPersistedTweets.addAll(repository.findTweetsInStatePublishFromUser(user.getAccountId())));
            LOG.info("Found {} tweets from users the user {} is following", persistedTweets.size(), requestingUser.getName().getUserName().getUserName());
        }

        persistedTweets.sort((t1, t2) -> t1.getPubDate().getPubDate().getTime() >= t2.getPubDate().getPubDate().getTime() ? -1 : 1);
        LOG.info("Sorted tweets after pubDate");

        int startIndex = persistedTweets.size() > index ? index : persistedTweets.size();
        int endIndex = persistedTweets.size() > index+numTweets ? index+numTweets : persistedTweets.size();
        persistedTweets = persistedTweets.subList(startIndex, endIndex);

        List<TweetDTO> tweetDTOS = new LinkedList<>();
        persistedTweets.forEach(tweet -> tweetDTOS.add(new TweetDTO(tweet)));

        return Response.ok().entity(tweetDTOS).build();
    }


    @Path("{tweetId}")
    @GET
    @RolesAllowed({USER, MODERATOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTweetDetails (@PathParam("tweetId") final Integer tweetId) {
        LOG.info("Request to get details of tweet with id {}", tweetId);
        Tweet tweet = repository.findTweetById(tweetId);
        if (tweet.getState() == TweetState.CANCELED) {
            LOG.info("Tweet with id {} is in state CANCELED", tweetId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        DetailedTweet tweetToResponse = new DetailedTweet(tweet);
        return Response.ok().entity(tweetToResponse).build();
    }


    @Path("{tweetId}")
    @DELETE
    @RolesAllowed({USER, MODERATOR})
    @Transactional
    public Response cancelTweet (@PathParam("tweetId") final Integer tweetId) {
        LOG.info("Request to cancel tweet with id {}", tweetId);
        Tweet tweetToDelete = repository.findTweetById(tweetId);
        if (!(tweetToDelete.getState() == TweetState.PUBLISH)) {
            LOG.warn("Tweet with id {} isn´t in status PUBLISH", tweetId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (authenticatedAccount.getRole() == AccountType.MODERATOR || authenticatedAccount.getAccountId().equals(tweetToDelete.getAuthor().getAccountId())) {
            tweetToDelete.setState(TweetState.CANCELED);
            LOG.info("Canceled tweet");
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            LOG.warn("User is not allowed to cancel tweet with id {}", tweetId);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }



    @Path("{tweetId}/liker")
    @POST
    @RolesAllowed({USER})
    @Transactional
    public Response likeTweet (@PathParam("tweetId") final Integer tweetId) {
        LOG.info("Request to like tweet with id {}", tweetId);
        User requester = repository.findUserById(authenticatedAccount.getAccountId());
        Tweet tweetToLike = repository.findTweetById(tweetId);
        if (!(tweetToLike.getState() == TweetState.PUBLISH)) {
            LOG.warn("Tweet with id {} isn´t in state PUBLISH", tweetId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        for (User user : tweetToLike.getLiker()) {
            if (user.getAccountId().equals(requester.getAccountId())){
                LOG.warn("Requesting user has already liked tweet with id {}", tweetId);
                return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Requesting user is already a liker of the specified tweet")).build();
            }
        }

        tweetToLike.getLiker().add(requester);
        LOG.info("Liked tweet with id {} for user {}", tweetId, requester.getName().getUserName().getUserName());
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @Path("{tweetId}/liker")
    @GET
    @RolesAllowed({USER})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLiker (@PathParam("tweetId") final Integer tweetId) {
        LOG.info("Request to get a list of liker of the tweet with id {}", tweetId);
        Tweet requestedTweet = repository.findTweetById(tweetId);
        if (requestedTweet.getState() == TweetState.CANCELED) {
            LOG.info("Tweet with id {} is in state CANCELED", tweetId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<UserDTO> liker = new LinkedList<>();
        requestedTweet.getLiker().forEach(user -> liker.add(new UserDTO(user)));
        return Response.ok().entity(liker).build();
    }


    @Path("{tweetId}/liker")
    @DELETE
    @RolesAllowed({USER})
    @Transactional
    public Response unlikeTweet (@PathParam("tweetId") final Integer tweetId) {
        LOG.info("Request to unlike the tweet with id {}", tweetId);
        User requester = repository.findUserById(authenticatedAccount.getAccountId());
        Tweet tweetToUnlike = repository.findTweetById(tweetId);

        if (tweetToUnlike.getState() == TweetState.CANCELED) {
            LOG.info("Tweet with id {} is in state CANCELED", tweetId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        for (int i=0; i<tweetToUnlike.getLiker().size(); i++) {
            if (tweetToUnlike.getLiker().get(i).getAccountId() == requester.getAccountId()) {
                tweetToUnlike.getLiker().remove(i);
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Requesting user isn´t a liker of the specified tweet")).build();

    }



    @Path("{tweetId}/retweets")
    @POST
    @RolesAllowed({USER})
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response retweetTweet (@PathParam("tweetId") final Integer tweetId) {
        User requester = repository.findUserById(authenticatedAccount.getAccountId());

        Tweet tweetToRetweet = repository.findTweetById(tweetId);
        if (tweetToRetweet.getState() == TweetState.CANCELED) { return Response.status(Response.Status.NOT_FOUND).build(); }
        tweetToRetweet = tweetToRetweet.getRootTweet() != null ? tweetToRetweet.getRootTweet() : tweetToRetweet;        //case Tweet is already a retweet
        Tweet retweet = Tweet.newTweet()
                .withContent(tweetToRetweet.getContent())
                .withAuthor(requester)
                .withRootTweet(tweetToRetweet)
                .build();
        repository.persistTweet(retweet);
        tweetToRetweet.getRetweets().add(retweet);

        return Response.status(Response.Status.CREATED).entity(new TweetDTO(retweet)).build();
    }


    @Path("{tweetId}/retweets/authors")
    @GET
    @RolesAllowed({USER})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRetweeter (@PathParam("tweetId") final Integer tweetId) {
        LOG.info("Request to get a list of retweeter of the tweet with id {}", tweetId);
        Tweet requestedTweet = repository.findTweetById(tweetId);
        if (requestedTweet.getState() == TweetState.CANCELED) {
            LOG.info("Tweet with id {} is in state CANCELED", tweetId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<UserDTO> retweeter = new LinkedList<>();
        requestedTweet.getRetweets().forEach(tweet -> retweeter.add(new UserDTO(tweet.getAuthor())));
        retweeter.sort((a1, a2) -> a1.getUserId() > a2.getUserId() ? 1 : -1);
        return Response.ok().entity(retweeter).build();
    }
}
