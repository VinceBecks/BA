package de.openknowledge.playground.api.rest.security.application.tweet;


import de.openknowledge.playground.api.rest.security.application.user.UserDTO;
import de.openknowledge.playground.api.rest.security.domain.account.Account;
import de.openknowledge.playground.api.rest.security.domain.account.AccountType;
import de.openknowledge.playground.api.rest.security.domain.account.User;
import de.openknowledge.playground.api.rest.security.domain.tweet.Tweet;
import de.openknowledge.playground.api.rest.security.domain.tweet.TweetState;
import de.openknowledge.playground.api.rest.security.infrastructure.persistence.repository.TwttrRepository;
import de.openknowledge.playground.api.rest.security.infrastructure.rest.error.ErrorDTO;
import de.openknowledge.playground.api.rest.security.infrastructure.rest.validation.ValidationErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import static de.openknowledge.playground.api.rest.security.infrastructure.security.Roles.MODERATOR;
import static de.openknowledge.playground.api.rest.security.infrastructure.security.Roles.USER;

@Path("tweets")
public class TweetResource {

    Logger LOG = LoggerFactory.getLogger("TweetResource.class");

    @Inject
    private TwttrRepository repository;


    @POST
    @RolesAllowed({USER})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createNewTweet(@Valid @NotNull NewTweet newTweet, @Context SecurityContext securityContext) {
        //todo: DateFormat festlegen?

        LOG.info("Request to create new tweet");
        Principal principal = securityContext.getUserPrincipal();
        String userName = principal.getName();
        User requester = repository.findUserByUserName(userName);

        Tweet tweet = new Tweet(newTweet.getContent(), requester);
        LOG.info("New tweet created");
        repository.persistTweet(tweet);
        requester.getTweets().add(tweet);

        return Response.status(Response.Status.CREATED).entity(new TweetDTO(tweet)).build();
    }


    @GET
    @RolesAllowed({USER, MODERATOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTweets(@Valid @DefaultValue("0") @QueryParam("index") final Integer index,
                              @DefaultValue("3") @QueryParam("numTweets") final Integer numTweets,
                              @Context SecurityContext securityContext) {
        LOG.info("Request to get {} tweets", numTweets);

        if (index < 0 || numTweets < 0) {
            LOG.warn("Invalid Query Param");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Invalid Query Param")).build();
        }

        Principal principal = securityContext.getUserPrincipal();
        String userName = principal.getName();
        Account requester = repository.findAccountByUserName(userName);

        List<Tweet> persistedTweets = new LinkedList<>();
        if (requester.getRole() == AccountType.MODERATOR) {
            LOG.info("Request from a moderator");
            persistedTweets = repository.findAllTweetsInStatePublish();
        }else {
            LOG.info("Request from a user");
            User requestingUser = repository.findUserByUserName(userName);
            List<Tweet> finalPersistedTweets = persistedTweets;
            requestingUser.getFollows().forEach(user -> finalPersistedTweets.addAll(repository.findTweetsInStatePublishFromUser(user.getAccountId())));
            LOG.info("Found {} tweets from users the user {} is following", persistedTweets.size(), userName);
        }

        List<TweetDTO> allPublishTweets  = new LinkedList<>();
        persistedTweets.forEach(tweet -> allPublishTweets.add(new TweetDTO(tweet)));
        allPublishTweets.sort((t1, t2) -> t1.getPubDate().getTime() >= t2.getPubDate().getTime() ? -1 : 1);
        LOG.info("Sorted tweets after pubDate");

        List<TweetDTO> tweetsToResponse  = new LinkedList<>();
        int startIndex = allPublishTweets.size() > index ? index : allPublishTweets.size();
        int endIndex = allPublishTweets.size() > index+numTweets ? index+numTweets : allPublishTweets.size();
        allPublishTweets.subList(startIndex, endIndex).forEach(tweet -> tweetsToResponse.add(tweet));

        return Response.ok().entity(tweetsToResponse).build();
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
    public Response cancelTweet (@PathParam("tweetId") final Integer tweetId, @Context SecurityContext securityContext) {
        LOG.info("Request to cancel tweet with id {}", tweetId);
        Principal principal = securityContext.getUserPrincipal();
        String userName = principal.getName();
        Account requester = repository.findAccountByUserName(userName);
        Tweet tweetToDelete = repository.findTweetById(tweetId);
        if (!(tweetToDelete.getState() == TweetState.PUBLISH)) {
            LOG.warn("Tweet with id {} isn´t in status PUBLISH", tweetId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (requester.getRole() == AccountType.MODERATOR || requester.getAccountId().equals(tweetToDelete.getAuthor().getAccountId())) {
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
    public Response likeTweet (@PathParam("tweetId") final Integer tweetId, @Context SecurityContext securityContext) {
        LOG.info("Request to like tweet with id {}", tweetId);
        Principal principal = securityContext.getUserPrincipal();
        String userName = principal.getName();
        User requester = repository.findUserByUserName(userName);
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
        LOG.info("Liked tweet with id {} for user {}", tweetId, userName);
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
    public Response unlikeTweet (@PathParam("tweetId") final Integer tweetId, @Context SecurityContext securityContext) {
        LOG.info("Request to unlike the tweet with id {}", tweetId);
        Principal principal = securityContext.getUserPrincipal();
        String userName = principal.getName();
        User requester = repository.findUserByUserName(userName);
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
    public Response retweetTweet (@PathParam("tweetId") final Integer tweetId, @Context SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        String userName = principal.getName();
        User requester = repository.findUserByUserName(userName);

        Tweet tweetToRetweet = repository.findTweetById(tweetId);
        if (tweetToRetweet.getState() == TweetState.CANCELED) { return Response.status(Response.Status.NOT_FOUND).build(); }
        tweetToRetweet = tweetToRetweet.getRootTweet() != null ? tweetToRetweet.getRootTweet() : tweetToRetweet;        //case Tweet is already a retweet
        Tweet retweet = new Tweet(tweetToRetweet.getContent(), requester);
        retweet.setRootTweet(tweetToRetweet);
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
