package com.example.party.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.net.URI;

public class LogoutHeader extends HttpHeaders {
    public static String RF_TOKEN = "rfToken";

    public static LogoutHeader of() {
        LogoutHeader header = new LogoutHeader();
        ResponseCookie accessTokenCookie = ResponseCookie.from(AUTHORIZATION, "")
                .maxAge(0L)
                .path("/page")
                .build();
        header.add(SET_COOKIE, accessTokenCookie.toString());

        ResponseCookie rfTokenCookie = ResponseCookie.from(RF_TOKEN, "")
                .maxAge(0L)
                .path("/page")
                .httpOnly(true)
                .build();
        header.add(SET_COOKIE, rfTokenCookie.toString());

        header.setLocation(URI.create("/page/indexPage"));

        return header;
    }
}
