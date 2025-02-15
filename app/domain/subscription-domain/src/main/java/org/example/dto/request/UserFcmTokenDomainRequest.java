package org.example.dto.request;

public record UserFcmTokenDomainRequest(
    String previousFcmToken,
    String updatedFcmToken
) {
    public static UserFcmTokenDomainRequest of(String previousFcmToken, String updatedFcmToken) {
        return new UserFcmTokenDomainRequest(previousFcmToken, updatedFcmToken);
    }
}
