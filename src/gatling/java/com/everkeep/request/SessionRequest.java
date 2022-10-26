package com.everkeep.request;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

public final class SessionRequest {

    private static final String BASE_PATH = "/api/sessions";

    private SessionRequest() {
    }

    public static HttpRequestActionBuilder create() {
        return http("Create session")
                .post(BASE_PATH)
                .body(ElFileBody("body/session/session-create.json"))
                .check(status().is(201))
                .check(
                        jsonPath("$.authToken")
                                .exists()
                                .saveAs("authToken")
                )
                .check(jsonPath("$.refreshToken").exists())
                .check(jsonPath("$.email").exists());
    }
}
