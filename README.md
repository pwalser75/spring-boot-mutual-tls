# Spring Boot example with mutual TLS

Reference project for configuring Spring Boot for TLS:
- Enable HTTPS
- Enable HTTP/2
- Redirect HTTP to HTTPS
- Proper setup of keystore and truststore
- Client cert authentication
- Server and client certificates signed by common CA
- Limit cipher suites to secure ones

## Configure certificates

Hint: to create your own certificates, use the **Frostnova Certificate Generator** at https://frostnova.ch/cert

To install your own certificates:

### Server

Configure in `src/main/resources/application.yml`.

### (Test) Client

Configure in `src/test/resources/client.properties`.

### Browser

In your browser:

- add the `test-ca.cer` certificate (in `src/test/resources`) as **CA** (allow it to trust web sites when asked).
- add the `test-client-keystore.p12` client certificate (in `src/test/resources`) as **Client certificate** (password: `Client-5ECr3T!`).


## Build

To build this project with Gradle (default tasks: _clean build install_):

    gradle
    
## Start
    
To start the Spring Boot application with grade, use:
    
    gradle start
    
## Ports

- **HTTPS** port: **8443**
- **HTTP**  port: **8080**, redirects to HTTPS port.

Those ports are configured in `src/main/resources/application.yml` in case you want to change them.

## Endpoints

- Info page: https://localhost:8443/info
- Weather web service (REST): https://localhost:8443/api/weather/forecast

