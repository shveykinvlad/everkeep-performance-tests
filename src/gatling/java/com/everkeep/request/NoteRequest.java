package com.everkeep.request;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

public final class NoteRequest {

    public static final String BASE_PATH = "/api/notes";
    public static final String ID_PATH = "/#{noteId}";

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "#{authToken}";

    private NoteRequest() {
    }

    public static HttpRequestActionBuilder create() {
        return http("Create note")
                .post(BASE_PATH)
                .body(ElFileBody("body/note/note-create.json"))
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE)
                .check(status().is(201))
                .check(jsonPath("$.id")
                        .exists()
                        .saveAs("noteId")
                )
                .check(jsonPath("$.title")
                        .exists()
                        .saveAs("noteTitle")
                )
                .check(jsonPath("$.text").exists())
                .check(jsonPath("$.priority").exists());
    }

    public static HttpRequestActionBuilder get() {
        return http("Get note by id")
                .get(BASE_PATH + ID_PATH)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE)
                .check(status().is(200));
    }

    public static HttpRequestActionBuilder search() {
        return http("Get note by title")
                .get(BASE_PATH + "/search")
                .queryParam("title", "#{noteTitle}")
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE)
                .check(status().is(200));
    }

    public static HttpRequestActionBuilder getAll() {
        return http("Get all notes")
                .get(BASE_PATH)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE)
                .check(status().is(200));
    }

    public static HttpRequestActionBuilder update() {
        return http("Update note")
                .put(BASE_PATH + ID_PATH)
                .body(ElFileBody("body/note/note-update.json"))
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE)
                .check(status().is(200))
                .check(jsonPath("$.title")
                        .exists()
                        .saveAs("noteTitle")
                );
    }

    public static HttpRequestActionBuilder delete() {
        return http("Delete note")
                .delete(BASE_PATH + ID_PATH)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE)
                .check(status().is(204));
    }
}
