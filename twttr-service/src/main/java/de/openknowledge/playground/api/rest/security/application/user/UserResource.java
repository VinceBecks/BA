package de.openknowledge.playground.api.rest.security.application.user;

import de.openknowledge.playground.api.rest.security.application.tweet.TweetDTO;
import de.openknowledge.playground.api.rest.security.domain.account.Account;
import de.openknowledge.playground.api.rest.security.domain.account.AccountType;
import de.openknowledge.playground.api.rest.security.domain.account.User;
import de.openknowledge.playground.api.rest.security.domain.tweet.Tweet;
import de.openknowledge.playground.api.rest.security.infrastructure.repository.TwttrRepository;
import de.openknowledge.playground.api.rest.security.infrastructure.rest.validation.ValidationErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static de.openknowledge.playground.api.rest.security.infrastructure.security.Roles.MODERATOR;
import static de.openknowledge.playground.api.rest.security.infrastructure.security.Roles.USER;

@Path("users")
public class UserResource {

    Logger LOG = LoggerFactory.getLogger("UserResource.class");

    @Inject
    private TwttrRepository repository;


    @GET
    @RolesAllowed({USER, MODERATOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@DefaultValue("") @QueryParam("searchString") final String searchString,
                            @DefaultValue("3")@QueryParam("numUsers") final Integer numUsers,
                            @DefaultValue("0") @QueryParam("index") final Integer index) {

        List<User> foundUsers = repository.findUsersBySearchString(searchString);
        List<UserDTO> users = new LinkedList<>();
        foundUsers.forEach(user -> users.add(new UserDTO(user)));

        List<UserDTO> usersToResponse = new LinkedList<>();
        int startIndex = users.size() > index ? index : users.size();
        int endIndex = users.size() > index+numUsers ? index+numUsers : users.size();
        users.subList(startIndex, endIndex).forEach(user -> usersToResponse.add(user));

        return Response.ok().entity(usersToResponse).build();
    }

    @Path("{userId}/tweets")
    @GET
    @RolesAllowed({USER, MODERATOR})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTweets(@PathParam("userId") final Integer userId,
                              @DefaultValue("0") @QueryParam("index") final Integer index,
                              @DefaultValue("3") @QueryParam("numTweets") final Integer numTweets,
                              @Context SecurityContext securityContext) {
        LOG.info("Request to get {} tweets from user with id {}", numTweets, userId);
        Principal principal = securityContext.getUserPrincipal();
        String userName = principal.getName();
        User requester = repository.findUserByUserName(userName);

        List<Tweet> persistedTweets = repository.findTweetsFromUser(userId);
        LOG.info("Found {} tweets", persistedTweets.size());

        int startIndex = persistedTweets.size() > index ? index : persistedTweets.size();
        int endIndex = persistedTweets.size() > index+numTweets ? index+numTweets : persistedTweets.size();

        List<TweetDTO> tweetsToResponse  = new LinkedList<>();
        persistedTweets.subList(startIndex, endIndex).forEach(tweet -> tweetsToResponse.add(new TweetDTO(tweet)));

        return Response.ok().entity(tweetsToResponse).build();
    }



    @Path("{userId}/follower")
    @POST
    @RolesAllowed({USER})
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response followUser (@PathParam("userId") final Integer userId, @Context SecurityContext securityContext) {
        LOG.info("Request to follow user with id {}", userId);
        Principal principal = securityContext.getUserPrincipal();
        String userName = principal.getName();
        User requester = repository.findUserByUserName(userName);

        Account accountToFollow = repository.findAccountById(userId);
        if (accountToFollow.getRole() == AccountType.MODERATOR) {
            LOG.info("Specified account to follow belongs to a moderator");
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Specified account to follow belongs to a moderator")).build();
        }

        User userToFollow = repository.findUserById(userId);

        //prüfen, ob requester bereits follower ist
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
    @RolesAllowed({USER})
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
    @RolesAllowed({USER})
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response unfollowUser (@PathParam("userId") final Integer userId, @Context SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        String userName = principal.getName();
        User requester = repository.findUserByUserName(userName);

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

        LOG.info("User with id {} isn´t a follower of user with id {}", requester.getAccountId(), userToUnfollow.getAccountId());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDTO("Requesting user isn´t a follower of the specified user")).build();
    }
}
