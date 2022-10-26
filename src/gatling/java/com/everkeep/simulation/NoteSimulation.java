package com.everkeep.simulation;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.nothingFor;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import com.everkeep.request.NoteRequest;
import com.everkeep.request.SessionRequest;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;

public class NoteSimulation extends Simulation {

    private final int userCount = Integer.parseInt(System.getProperty("USERS"));
    private final int rampDuration = Integer.parseInt(System.getProperty("RAMP_DURATION"));
    private final int duration = Integer.parseInt(System.getProperty("DURATION"));

    private final FeederBuilder<String> usersFeeder = csv("users.csv").random();

    private final ChainBuilder login = exec(SessionRequest.create());
    private final ChainBuilder create = exec(NoteRequest.create());
    private final ChainBuilder get = exec(NoteRequest.get());
    private final ChainBuilder getAll = exec(NoteRequest.getAll());
    private final ChainBuilder search = exec(NoteRequest.search());
    private final ChainBuilder update = exec(NoteRequest.update());
    private final ChainBuilder delete = exec(NoteRequest.delete());

    private final ScenarioBuilder crud = scenario("Note CRUD")
            .feed(usersFeeder)
            .exec(login)
            .exec(create)
            .exec(get)
            .exec(getAll)
            .exec(search)
            .exec(update)
            .exec(delete);

    private final HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8080")
                    .contentTypeHeader("application/json")
                    .acceptHeader("application/json")
                    .connectionHeader("keep-alive");

    public NoteSimulation() {
        super();
        setUp(
                crud.injectOpen(
                                nothingFor(5),
                                atOnceUsers(5),
                                rampUsers(userCount).during(rampDuration)
                        )
                        .protocols(httpProtocol)
        )
                .maxDuration(Duration.ofSeconds(duration))
                .assertions(
                        global().responseTime().max().lt(20),
                        global().successfulRequests().percent().gt(99.0)
                );
    }
}
