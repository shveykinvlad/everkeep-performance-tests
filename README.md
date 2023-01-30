
# Performance tests for [Everkeep API](https://github.com/shveykinvlad/everkeep-api)
[![Build](https://github.com/shveykinvlad/everkeep-api/actions/workflows/build.yml/badge.svg)](https://github.com/shveykinvlad/everkeep-performance-tests/actions/workflows/build.yml)

## How to run locally
### Prerequisites
* Java 17
### Steps
1. open project folder: `cd <path-to-project>`;
2. build project: `./gradlew clean build`;
3. import data from [users_table_data.csv](src/gatling/resources/database/users_table_data.csv) and [users_roles_table_data.csv](src/gatling/resources/database/users_roles_table_data.csv) files to the Everkeep API database;
4. run performance tests: `./gradlew gatlingRun-com.everkeep.simulation.NoteSimulation -DUSERS=100 -DRAMP_DURATION=10 -DDURATION=60`;


