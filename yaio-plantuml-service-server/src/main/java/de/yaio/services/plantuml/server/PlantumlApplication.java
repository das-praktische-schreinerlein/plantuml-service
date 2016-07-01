/** 
 * software for diagram-converter
 * 
 * @FeatureDomain                Converter
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     diagram-services
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.services.plantuml.server;

import de.yaio.services.plantuml.server.configuration.PlantumlServerConfig;
import org.springframework.boot.SpringApplication;

/** 
 * the plantuml-service as spring boot application
 */
public class PlantumlApplication {

    /** 
     * Main-method to start the application
     * @param args                   the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(PlantumlServerConfig.class, args);
    }
}