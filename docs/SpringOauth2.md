# ReactiveOauth2Authorized ...

## ClientService

Implementations of this interface are responsible for the management of Authorized Client(s), which provide the purpose
of associating an Access Token credential to a Client and Resource Owner, who is the Principal that originally granted
the authorization.

    - InMemoryReactiveOAuth2AuthorizedClientService (default)
    - R2dbcReactiveOAuth2AuthorizedClientService

## ClientManager

Implementations of this interface are responsible for the overall management of Authorized Client(s).
The primary responsibilities include:
Authorizing (or re-authorizing) an OAuth 2.0 Client by leveraging a ReactiveOAuth2AuthorizedClientProvider(s).
Delegating the persistence of an OAuth2AuthorizedClient, typically using a ReactiveOAuth2AuthorizedClientService OR
ServerOAuth2AuthorizedClientRepository.

    - DefaultReactiveOAuth2AuthorizedClientManager
    - AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager

## ClientProvider

A strategy for authorizing (or re-authorizing) an OAuth 2.0 Client. Implementations will typically implement a specific
authorization grant type.

    - AuthorizationCodeReactiveOAuth2AuthorizedClientProvider
    - ClientCredentialsReactiveOAuth2AuthorizedClientProvider
    - DelegatingReactiveOAuth2AuthorizedClientProvider
    - JwtBearerReactiveOAuth2AuthorizedClientProvider
    - RefreshTokenReactiveOAuth2AuthorizedClientProvider

# OAuth2AuthorizedClient

A representation of an OAuth 2.0 "Authorized Client".
A client is considered "authorized" when the End-User (Resource Owner) has granted authorization to the client to access
it's protected resources.
This class associates the Client to the Access Token granted/authorized by the Resource Owner.

# ReactiveOAuth2UserService

Implementations of this interface are responsible for obtaining the user attributes of the End-User (Resource Owner)
from the UserInfo Endpoint using the Access Token granted to the Client and returning an AuthenticatedPrincipal in the
form of an OAuth2User.

    - DefaultReactiveOAuth2UserService
    - OidcReactiveOAuth2UserService