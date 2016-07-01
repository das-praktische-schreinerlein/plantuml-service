Yaio - Plantuml-Service
=====================

# Desc
A webservice to generate plantuml-diagramms via url.

Inspired by [Plantuml-Server](https://github.com/plantuml/plantuml-server) but simplyfied and running with spring-boot as standalone service. 

# Build and run
- test it

        mvn install
        java -Xmx768m -Xms128m -Dspring.config.location=file:config/plantuml-application.properties -Dlog4j.configuration=file:config/log4j.properties -cp "dist/yaio-plantuml-service-full.jar" de.yaio.services.plantuml.server.PlantumlApplication --config config/plantuml-application.properties
        curl --user plantuml:secret -X GET http://localhost:8082/services/plantuml/img/SyfFKj2rKt3CoKnELR1Io4ZDoSa70000 > /cygdrive/d/tmp/plantumltest.png

# Thanks to
- **Build-Tools**
    - [Apache Maven](https://github.com/apache/maven)
    - [Eclipse](http://eclipse.org/)
- **Java-Core-Frameworks**
    - [Spring-Framework](https://github.com/spring-projects/spring-framework)
    - [Spring-boot](https://github.com/spring-projects/spring-boot)
    - [Spring Security](https://github.com/spring-projects/spring-security)
- **Plantuml**
    - [Plantuml-Core](https://github.com/plantuml/plantuml)

# License
    /**
     * @author Michael Schreiner <michael.schreiner@your-it-fellow.de>
     * @category collaboration
     * @copyright Copyright (c) 2010-2014, Michael Schreiner
     * @license http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
     *
     * This Source Code Form is subject to the terms of the Mozilla Public
     * License, v. 2.0. If a copy of the MPL was not distributed with this
     * file, You can obtain one at http://mozilla.org/MPL/2.0/.
     */
