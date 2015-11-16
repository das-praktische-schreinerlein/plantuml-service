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
package de.yaio.services.plantuml.controller;

import javax.imageio.IIOException;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.plantuml.FileFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/** 
 * controller with Download-Services to convert plantuml-src to diagrams
 *  
 * @FeatureDomain                Webservice
 * @package                      de.yaio.services.plantuml.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     diagram-services
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Controller
@RequestMapping("${yaio-plantuml-service.baseurl}")
public class PlantumlController {

    @Autowired
    protected PlantumlUtils converterUtils;
    
    /** 
     * Request to generate plantuml-diagram from src as png
     * @FeatureDomain                Webservice
     * @FeatureResult                send diagram-data via HttpServletResponse
     * @FeatureKeywords              Webservice
     * @param src                    compressed plantuml-src
     * @param response               the response-Obj to set contenttype and headers
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/png/{src}",
                    produces = "image/png")
    public @ResponseBody void convertToPng(@PathVariable(value="src") String src,
                                        HttpServletResponse response) {
        try {
            // build the UML source from the compressed request parameter
            
            String uml = converterUtils.getUmlSource(src);
            converterUtils.exportAsDiagramm(uml, response, FileFormat.PNG);
        } catch (IIOException iioe) {
                // Browser has closed the connection, so the HTTP OutputStream is closed
                // Silently catch the exception to avoid annoying log
        } catch (Exception e) {
            System.err.println("exception start for src:" + src + " ex:" + e);
            e.printStackTrace();
        }
    }

    /** 
     * Request to generate plantuml-diagram from src as png
     * @FeatureDomain                Webservice
     * @FeatureResult                send diagram-data via HttpServletResponse
     * @FeatureKeywords              Webservice
     * @param src                    compressed plantuml-src
     * @param response               the response-Obj to set contenttype and headers
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/img/{src}",
                    produces = "image/png")
    public @ResponseBody void convertToImg(@PathVariable(value="src") String src,
                                        HttpServletResponse response) {
        this.convertToPng(src, response);
    }

    /** 
     * Request to generate plantuml-diagram from src as svg
     * @FeatureDomain                Webservice
     * @FeatureResult                send diagram-data via HttpServletResponse
     * @FeatureKeywords              Webservice
     * @param src                    compressed plantuml-src
     * @param response               the response-Obj to set contenttype and headers
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/svg/{src}",
                    produces = "image/png")
    public @ResponseBody void convertToSvg(@PathVariable(value="src") String src,
                                        HttpServletResponse response) {
        try {
            // build the UML source from the compressed request parameter
            
            String uml = converterUtils.getUmlSource(src);
            converterUtils.exportAsDiagramm(uml, response, FileFormat.SVG);
        } catch (IIOException iioe) {
                // Browser has closed the connection, so the HTTP OutputStream is closed
                // Silently catch the exception to avoid annoying log
        } catch (Exception e) {
            System.err.println("exception start for src:" + src + " ex:" + e);
            e.printStackTrace();
        }
    }

    /** 
    * Request to generate plantuml-diagram from src as ascii
    * @FeatureDomain                Webservice
    * @FeatureResult                send diagram-data via HttpServletResponse
    * @FeatureKeywords              Webservice
    * @param src                    compressed plantuml-src
    * @param response               the response-Obj to set contenttype and headers
    */
   @RequestMapping(method = RequestMethod.GET, 
                   value = "/txt/{src}",
                   produces = "text/plain")
   public @ResponseBody void convertToAscii(@PathVariable(value="src") String src,
                                       HttpServletResponse response) {
       try {
           // build the UML source from the compressed request parameter
           
           String uml = converterUtils.getUmlSource(src);
           converterUtils.exportAsDiagramm(uml, response, FileFormat.UTXT);
       } catch (IIOException iioe) {
               // Browser has closed the connection, so the HTTP OutputStream is closed
               // Silently catch the exception to avoid annoying log
       } catch (Exception e) {
           System.err.println("exception start for src:" + src + " ex:" + e);
           e.printStackTrace();
       }
   }
}