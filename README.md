#### Preparation stuff
`docker run --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=quattro -d postgres`

`mvn clean verify -Pclean-main-schema,migrate-main-schema`

#
#### Migrate testdata
`cd db && mvn install -Pmigrate-main-schema-testdata`

#
#### Run application
`mvn spring-boot:run`
