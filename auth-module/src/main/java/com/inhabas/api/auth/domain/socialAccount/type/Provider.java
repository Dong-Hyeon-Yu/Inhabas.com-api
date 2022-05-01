package com.inhabas.api.auth.domain.socialAccount.type;

public enum Provider {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String name;

    Provider(String name) {
        this.name = name;
    }
}