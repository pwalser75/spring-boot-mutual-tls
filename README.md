# Spring Boot example with mutual TLS

Reference project for configuring Spring Boot for TLS:
- Enable HTTPS
- Redirect HTTP to HTTPS
- Proper setup of keystore and truststore
- Client cert authentication
- Server and client certificates signed by common CA
- Limit cipher suites to secure ones

## Configure certificates

- In your browser, add the `test-ca.cer` certificate (in `src/test/resources`) as **CA** (allow it to trust web sites when asked).
- In your browser, add the `test-client-keystore.p12` client certificate (in `src/test/resources`) as **Client certificate** (password: `Client-5ECr3T!`).

Hint: to create your own certificates, use the **Frostnova Certificate Generator** at https://frostnova.ch/cert

## Build

To build this project with Gradle (default tasks: _clean build install_):

    gradle
    
## Start
    
To start the Spring Boot application with grade, use:
    
    gradle start
    
## Ports

- **HTTPS** port: **443**
- **HTTP**  port: **80**, redirects to HTTPS port.

Those ports are configured in `src/main/resources/application.yml` in case you want to change them (e.g. to 8443/8080).

## Endpoints

- Info page: https://localhost/info
- Weather web service (REST): https://localhost/api/weather/forecast

