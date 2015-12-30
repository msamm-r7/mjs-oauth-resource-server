# mjs-oauth-resource-server

# Usage: #

Given you've authorised using my [OAuth2 Authorisation Server](https://github.com/msamm-r7/mjs-oauth-authorization-server), you can take the token it gives you and access resources here:

Use the 'access_token' in your call:

    curl http://localhost:9199/greeting -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"

After the specified time period, the access_token will expire. Use the refresh_token that was returned in the original OAuth authorization to retrieve a new access_token:

    curl -X POST \
    -vu clientapp:123456 \
    http://localhost:9099/oauth/token \
    -H "Accept: application/json" \
    -d "grant_type=refresh_token&refresh_token=ca6b8936-8756-459e-9b4a-f3c2e6decac4&client_secret=123456&client_id=clientapp"

## Using UAA

Instead of using my own resource server, we can use [Spring's UAA](https://github.com/cloudfoundry/uaa) which can act as Auth and Token store for us.

To run UAA (with DB Mysql based config) run:

    ./gradlew -Dspring.profiles.active=mysql,default run

### Authorisation

We're assuming we have a client (our app) registered. For this example i'm taking config that's pre-canned (in the DB or in the config under 'uaa/src/main//webapp/WEB-INF/spring/oauth-clients.xml'.

This is the config:

    <entry key="app">
        <map>
            <entry key="id" value="app" />
            <entry key="secret" value="appclientsecret" />
            <entry key="authorized-grant-types" value="password,implicit,authorization_code,client_credentials" />
            <entry key="scope"
                value="cloud_controller.read,cloud_controller.write,openid,password.write,scim.userids,organizations.acme" />
            <entry key="authorities" value="uaa.resource" />
            <entry key="autoapprove">
                <list>
                    <value>openid</value>
                </list>
            </entry>
            <entry key="signup_redirect_url" value="http://localhost:8080/app/" />
            <entry key="change_email_redirect_url" value="http://localhost:8080/app/" />
            <entry key="name" value="The Ultimate Oauth App" />
        </map>
    </entry> 

This translates into a call to UAA for a token:
 
    curl -i -X POST \
    -vu app:appclientsecret \
    http://localhost:8080/uaa/oauth/token \
    -H "Accept: application/json" \
    -d "grant_type=client_credentials&scope=uaa.resource&client_secret=appclientsecret&client_id=app"


As above this yields a token. Which we can then use having set up this server to validate that token using UAA.

{"access_token":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3YmU0OTFhNy1kMjk3LTRkMTYtYWI5YS1kMzljZTZiYjM1YjkiLCJzdWIiOiJhcHAiLCJhdXRob3JpdGllcyI6WyJ1YWEucmVzb3VyY2UiXSwic2NvcGUiOlsidWFhLnJlc291cmNlIl0sImNsaWVudF9pZCI6ImFwcCIsImNpZCI6ImFwcCIsImF6cCI6ImFwcCIsImdyYW50X3R5cGUiOiJjbGllbnRfY3JlZGVudGlhbHMiLCJyZXZfc2lnIjoiN2M5MjU1OGMiLCJpYXQiOjE0NTA3MDgwMTQsImV4cCI6MTQ1MDc1MTIxNCwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3VhYS9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJhcHAiLCJ1YWEiXX0.3b2v3IApiHWTn8mRpKkf1nFDXXT4HRF31vS_yHDuHIg",
 "token_type":"bearer",
 "expires_in":43199,
 "scope":"uaa.resource",
 "jti":"7be491a7-d297-4d16-ab9a-d39ce6bb35b9"
}

curl http://localhost:9199/greeting -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiMmIwMDM2NS05MTUzLTQ0YzUtOTA0Yi1mMzlhNWMxZjk2NzUiLCJzdWIiOiJhcHAiLCJhdXRob3JpdGllcyI6WyJ1YWEucmVzb3VyY2UiLCJtanMtcG9jLXJlc291cmNlLXNlcnZlciJdLCJzY29wZSI6WyJ1YWEucmVzb3VyY2UiXSwiY2xpZW50X2lkIjoiYXBwIiwiY2lkIjoiYXBwIiwiYXpwIjoiYXBwIiwiZ3JhbnRfdHlwZSI6ImNsaWVudF9jcmVkZW50aWFscyIsInJldl9zaWciOiI3YzkyNTU4YyIsImlhdCI6MTQ1MDcxMDUxMywiZXhwIjoxNDUwNzUzNzEzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvdWFhL29hdXRoL3Rva2VuIiwiemlkIjoidWFhIiwiYXVkIjpbImFwcCIsInVhYSJdfQ.lJYOTrKKD1gzV8t_RA6psLMaRAG2_YVgvpuiFAA5olc"

This in turn, gives us an error:

{"error":"access_denied","error_description":"Invalid token does not contain resource id (mjs-poc-resource-server)"}

To enable us to use the client credentials (e.g. 'X') for an additional resource amend the DB (UAA schema, oauth_client_details.authorities and oauth_client_details.scope), with suitable elements such as 'X.read'.

### Using UAA REST API



### Additional Resources with UAA

[See here for UAA info.](https://github.com/cloudfoundry/uaa/blob/master/docs/UAA-Security.md#uaa-resources)



 