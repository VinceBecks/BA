package de.openknowledge.twttrService.api.rest.application.user;

import de.openknowledge.twttrService.api.rest.application.tweet.TweetDTO;
import de.openknowledge.twttrService.api.rest.domain.account.Account;
import de.openknowledge.twttrService.api.rest.domain.account.AccountType;
import de.openknowledge.twttrService.api.rest.domain.account.User;
import de.openknowledge.twttrService.api.rest.domain.tweet.Tweet;
import de.openknowledge.twttrService.api.rest.infrastructure.persistence.repository.TwttrRepository;
import de.openknowledge.twttrService.api.rest.infrastructure.rest.validation.ValidationErrorDTO;
import de.openknowledge.twttrService.api.rest.infrastructure.security.Authenticated;
import de.openknowledge.twttrService.api.rest.infrastructure.security.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("users")
public class UserResource {

    private static final Logger LOG = LoggerFactory.getLogger("UserResource.class");


    @Inject @Authenticated
    private Account authenticatedAccount;

    @Inject
    private TwttrRepository repository;


    @GET
    @RolesAllowed({Roles.USER, Roles.MODERATOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@DefaultValue("") @QueryParam("searchString") final String searchString,
                            @DefaultValue("3")@QueryParam("numUsers") final Integer numUsers,
                            @DefaultValue("0") @QueryParam("index") final Integer index) {
        LOG.info("Request to get users with \"{}\"", searchString);

        List<User> foundUsers = repository.findUsersBySearchString(searchString);
        List<UserDTO> users = new LinkedList<>();
        foundUsers.forEach(user -> users.add(new UserDTO(user)));

        int startIndex = users.size() > index ? index : users.size();
        int endIndex = users.size() > index+numUsers ? index+numUsers : users.size();

        List<UserDTO> usersToResponse = new LinkedList<>();
        users.subList(startIndex, endIndex).forEach(user -> usersToResponse.add(user));

        return Response.ok().entity(usersToResponse).build();
    }

    @Path("{userId}/tweets")
    @GET
    @RolesAllowed({Roles.USER, Roles.MODERATOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTweets(@PathParam("userId") final Integer userId,
                              @DefaultValue("0") @QueryParam("index") final Integer index,
                              @DefaultValue("3") @QueryParam("numTweets") final Integer numTweets) {
        LOG.info("Request to get {} tweets from user with id {}", numTweets, userId);

        List<Tweet> persistedTweets = repository.findTweetsInStatePublishFromUser(userId);
        LOG.info("Found {} tweets", persistedTweets.size());

        int startIndex = persistedTweets.size() > index ? index : persistedTweets.size();
        int endIndex = persistedTweets.size() > index+numTweets ? index+numTweets : persistedTweets.size();

        List<TweetDTO> tweetsToResponse  = new LinkedList<>();
        persistedTweets.subList(startIndex, endIndex).forEach(tweet -> tweetsToResponse.add(new TweetDTO(tweet)));

        return Response.ok().entity(tweetsToResponse).build();
    }



    @Path("{userId}/follower")
    @POST
    @RolesAllowed({Roles.USER})
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response followUser (@PathParam("userId") final Integer userId) {
        LOG.info("Request to follow user with id {}", userId);
        //User requester = (User) authenticatedAccount;
        User requester = repository.findUserById(authenticatedAccount.getAccountId());

        Account accountToFollow = repository.findAccountById(userId);
        if (accountToFollow.getRole() == AccountType.MODERATOR) {
            LOG.info("Specified account to follow belongs to a moderator");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Specified account to follow belongs to a moderator")).build();
        }

        User userToFollow = repository.findUserById(userId);

        for (User user : userToFollow.getFollower()) {
            if (user.getAccountId() == requester.getAccountId()){
                LOG.info("Requesting user is already a follower of the user with id {}", userId);
                return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("User is already a follower of the specified user")).build();
            }
        }

        userToFollow.getFollower().add(requester);
        requester.getFollows().add(userToFollow);

        LOG.info("Set user with id {} as an follower of user with id {}", requester.getAccountId(), userToFollow.getAccountId());
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @Path("{userId}/follower")
    @GET
    @RolesAllowed({Roles.USER})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollower (@PathParam("userId") final Integer userId) {
        LOG.info("Request to get follower of user with id {}", userId);
        Account account = repository.findAccountById(userId);

        if (account.getRole() == AccountType.MODERATOR ) {
            LOG.info("Account with id {} belongs to a moderator", userId);
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Account to get the list of follower from belongs to a moderator")).build();
        }

        User user = repository.findUserById(userId);
        List<UserDTO> users = new LinkedList<>();
        LOG.info("Found follower from user with id {}", userId);
        user.getFollower().forEach(follower -> users.add(new UserDTO(follower)));
        users.sort((f1, f2) -> f1.getUserId()>f2.getUserId() ? 1 : -1);

        return Response.ok().entity(users).build();
    }

    @Path("{userId}/follower")
    @DELETE
    @RolesAllowed({Roles.USER})
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response unfollowUser (@PathParam("userId") final Integer userId) {
        User requester = repository.findUserById(authenticatedAccount.getAccountId());

        Account accountToUnfollow = repository.findAccountById(userId);
        if (accountToUnfollow.getRole() == AccountType.MODERATOR ) {
            LOG.warn ("Specified account to unfollow belongs to a moderator");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Specified account to unfollow belongs to a moderator")).build();
        }

        User userToUnfollow = repository.findUserById(userId);

        if (userToUnfollow.getFollower().contains(requester)){
            userToUnfollow.getFollower().removeIf(element -> element.getAccountId().equals(requester.getAccountId()));
            requester.getFollows().removeIf(followsElement -> followsElement.getAccountId().equals(userToUnfollow.getAccountId()));

            LOG.info("Unfollowed user");
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Requesting user isn´t a follower of the specified user")).build();
    }
}