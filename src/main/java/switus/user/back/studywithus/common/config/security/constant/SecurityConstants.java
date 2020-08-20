package switus.user.back.studywithus.common.config.security.constant;

public final class SecurityConstants {
    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "study-with-us-server"; //토큰 발급자
    public static final String TOKEN_AUDIENCE = "study-with-us-client"; //토큰 대상자

    public static final String TOKEN_SUBJECT_PREFIX = "USER_";
    public static final String TOKEN_CLAIM_KEY_USER_ID = "USER_ID";
    public static final String TOKEN_CLAIM_KEY_USER_TYPE = "USER_TYPE";

    public static final long TOKEN_VALID_MILISECOND = 12 * 60 * 60 * 1000; // milli-seconds

    public static final long MINIMUM_ACCESS_TOKEN_VALIDITY = 1 * 60 * 60; // seconds
    public static final long MINIMUM_REFRESH_TOKEN_VALIDITY = 1 * 24 * 60 * 60; // seconds

    public static final long DEFAULT_ACCESS_TOKEN_VALIDITY = 1 * 24 * 60 * 60 * 1000; // milli-seconds
    public static final long DEFAULT_REFRESH_TOKEN_VALIDITY = 30 * 24 * 60 * 60 * 1000; // milli-seconds

}
