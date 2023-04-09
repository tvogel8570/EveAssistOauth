# Actors

1. EveAssist - Custom OOG application written in Java with Spring Boot, i.e. this code
2. User - Eve Player that wants to use capabilities of EveAssist program
3. CCP - External Oauth2 site with ability to authenticate a pilot without granting any non-public scopes. Used as an
   authentication source for this application
4. ESI - External Oauth2 site with ability to authenticate and grant non-public scopes. Used with pilots for registered
   users
5. Authentication Source - One of many sources of authentication e.g. Local Userid/password, CCP, Google

---

# Flows

## Key User flows

1. Register as a User
    1. Display register page
    2. User enters information
        1. **required:** email, screen name
    3. Create / Save User as un-validated to prevent login
    4. Send Validation email
2. Validate User
    1. User opens validation email and clicks on link
    2. Application validates token
    3. Display sign-in form
    4. User validates with at least one of the selected Authentication Sources
3. Authenticate for any / each selected Authentication Source
    1. User selects Authentication Source
    2. User enters Authentication Source information
    3. Submit form to Authentication Source
    4. Authentication Source uses provided information to authenticate User
    5. Store successful authentication results returned by Authentication Source
4. ***Add pilot validated by ESI*** (Does NOT change spring's authenticated user)
    1. Redirect User to ESI login with appropriate scopes request
    2. User selects, authenticates and grants scope for a pilot
    3. CCP redirects to predefined EveAssist uri
    4. EveAssist stores pilot information and associates with current User
        1. PilotId, PilotName, ESI issued oauth2 JWT including scopes and oauth2 refreshToken
5. ***Refresh pilot with ESI*** (Does NOT change spring's authenticated user)
    1. EveAssist notifies User that pilot token has expired
    2. User selects pilot
    3. EveAssist redirects to ESI login with appropriate scopes request
    4. User selects, authenticates and grants scope for a pilot
    5. CCP redirects to predefined EveAssist uri
    6. EveAssist stores pilot information and associates with current User
    7. PilotId, PilotName, ESI issued oauth2 JWT including scopes and oauth2 refreshToken

## Key business flows

- Create notification sink
    1. Display possible notification sinks to User
    2. User selects one or more notification sinks
    3. User enters required information for each notification sink
    4. Store required information for each selected notification sink with User
- Select notification for a specific pilot
    1. Display potential notifications for a pilot to user
    2. Allow user to select notifications for a pilot
    3. Store notifications with pilot/user

## Scheduled flows

- Get new notification(s) from ESI
    1. For each pilot with any notification request
        1. Load ESI token for this pilot and this scope
        2. Check expiration of JWT token and refresh if needed
        3. Request notifications from ESI
        4. Find new notifications that match any requested criteria
        5. For each matching notification sink for this pilot
            1. Format notification specific message
            2. Send to notification sink

---

# Authentication Sources

1. EveAssist internal - email and password
2. CCP - CCP account hybrid oauth2
3. Google - Google account OIDC compliant
