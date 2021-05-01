# :microscope: microservice-transformer

Simple dockerized microservice which consumes and transforms the output of another microservice.

## :whale2: How to launch?

The microservice-transformer server is available as a docker image
on [DockerHub](https://hub.docker.com/repository/docker/lukashavemann/microservice-transformer) and can be started with
the following command. The microservice-transformer service starts with
the [provided defaults](https://github.com/aacml/Recruiting/wiki/Cloud-Software-Engineer-Code-Challenge).

```
docker run -d -p 8080:8080 lukashavemann/microservice-transformer:latest
```

The base path, the remote microservice url and the api-key ca be changed like that:

```
docker run -d -p 8080:8080 lukashavemann/microservice-transformer:latest \
    --spring.webflux.base-path:/my-new/api/base/path \
    --de.havemann.lukas.transformer.url="http://new-url.de/" \
    --de.havemann.lukas.transformer.apitoken="new-api-token"
```

## :house: Architecture

The architecture follows the hexagonal architecture pattern
from [Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/). The microservice-transformer utelizes a
non-blocking execution model with spring webflux and spring webclient.

## :hammer: Development, Build & Pipeline

The execution of unit and integration tests is automated
with [GitHub Actions](https://github.com/LukasHavemann/microservice-transformer/actions). The docker image build is
automated with [DockerHub](https://hub.docker.com/repository/docker/lukashavemann/microservice-transformer). As soon as
a new merge to master happens, a new docker image is built by DockerHub cloud and provided with latest tag in
the [DockerHub registry](https://hub.docker.com/repository/docker/lukashavemann/microservice-transformer).

The docker image is built in a multi-stage docker build. The maven build process produces
a [layered jar](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#repackage-layers).
During the build process the layered jar gets unpacked and added as separate layers, to make use of the docker layer
deduplication feature to reduce server startup time.

