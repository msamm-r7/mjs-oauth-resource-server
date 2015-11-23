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

