package de.openknowledge.playground.api.rest.security.stepDefinition;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.openknowledge.playground.api.rest.security.supportCode.SharedDomain;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

public class AuthenticationSteps {
    private SharedDomain domain;
    private String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrVrCuTtArbgaZzL1hvh0xtL5mc7o0NqPVnYXkLvgcwiC3BjLGw1tGEGoJaXDuSaRllobm53JBhjx33UNv+5z/UMG4kytBWxheNVKnL6GgqlNabMaFfPLPCF8kAgKnsi79NMo+n6KnSY8YeUmec/p2vjO2NjsSAVcWEQMVhJ31LwIDAQAB";


    public AuthenticationSteps (SharedDomain domain) {
        this.domain = domain;
    }


    @Given("following moderator")
    public void following_moderator(DataTable dataTable) {
        Map<String, String> account = dataTable.transpose().asMap(String.class, String.class);
        domain.addAccountCredentials(account.get("userName"), account.get("password"));
    }

    @Given("following user")
    public void following_user(DataTable dataTable) {
        Map<String, String> account = dataTable.transpose().asMap(String.class, String.class);
        domain.addAccountCredentials(account.get("userName"), account.get("password"));
    }


    @When("a client sends a POST request to \\/auth\\/realms\\/twttr\\/protocol\\/openid-connect\\/token to get a valid token for {string}")
    public void a_client_sends_a_POST_request_to_auth_realms_twttr_protocol_openid_connect_token_to_get_a_valid_token_for(String userName) {
        Map<String, String> account = domain.getAccountCredentials();
        //Generates in create() the required Information from keycloak.json
        AuthorizationResponse response = AuthzClient.create().authorization(account.get(userName), account.get("password")).authorize();

        domain.addValidToken(userName, response.getToken());
    }

    @Then("the response body should contain a valid token for the account of {string}")
    public void the_response_body_should_contain_a_valid_token_for_the_account_of(String userName) {
        //todo: Prüfung reicht nicht .. KeyCloak gibt immer einen Token zurück nur ist dieser nicht immer gültig
        Assert.assertNotNull(domain.tokenFromAccount(userName));

    }


}
