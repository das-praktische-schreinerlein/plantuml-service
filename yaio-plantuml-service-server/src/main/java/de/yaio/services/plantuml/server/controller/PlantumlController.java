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
package de.yaio.services.plantuml.server.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.IIOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.plantuml.FileFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;


/** 
 * controller with Download-Services to convert plantuml-src to diagrams
 */
@Controller
@RequestMapping("${yaio-plantuml-service.baseurl}")
public class PlantumlController {

    private static final Logger LOGGER = Logger.getLogger(PlantumlController.class);

    @Autowired
    protected PlantumProvider converterUtils;

    /** 
     * Request to generate plantuml-diagram from src as png
     * @param src                    compressed plantuml-src
     * @param response               the response-Obj to set contenttype and headers
     * @throws IOException           possible
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/png/{src}",
                    produces = "image/png")
    public @ResponseBody void convertToPng(@PathVariable(value="src") String src,
                                           HttpServletResponse response) throws IOException {
        // build the UML source from the compressed request parameter
        String uml = converterUtils.getUmlSource(src);
        converterUtils.exportAsDiagramm(uml, response, FileFormat.PNG);
    }

    /** 
     * Request to generate plantuml-diagram from src as png
     * @param src                    compressed plantuml-src
     * @param response               the response-Obj to set contenttype and headers
     * @throws IOException           possible
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/img/{src}",
                    produces = "image/png")
    public @ResponseBody void convertToImg(@PathVariable(value="src") String src,
                                           HttpServletResponse response) throws IOException {
        this.convertToPng(src, response);
    }

    /** 
     * Request to generate plantuml-diagram from src as svg
     * @param src                    compressed plantuml-src
     * @param response               the response-Obj to set contenttype and headers
     * @throws IOException           possible
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/svg/{src}",
                    produces = "image/svg")
    public @ResponseBody void convertToSvg(@PathVariable(value="src") String src,
                                           HttpServletResponse response) throws IOException {
        // build the UML source from the compressed request parameter
        String uml = converterUtils.getUmlSource(src);
        converterUtils.exportAsDiagramm(uml, response, FileFormat.SVG);
    }

    /** 
     * Request to generate plantuml-diagram from src as ascii
     * @param src                    compressed plantuml-src
     * @param response               the response-Obj to set contenttype and headers
     * @throws IOException           possible
     */
    @RequestMapping(method = RequestMethod.GET, 
                    value = "/txt/{src}",
                    produces = "text/plain")
    public @ResponseBody void convertToAscii(@PathVariable(value="src") String src,
                                             HttpServletResponse response) throws IOException {
        // build the UML source from the compressed request parameter
        String uml = converterUtils.getUmlSource(src);
        converterUtils.exportAsDiagramm(uml, response, FileFormat.UTXT);
    }

    @ExceptionHandler(value = {IIOException.class})
    public void handleCustomException(final HttpServletRequest request, final IIOException e,
                                      final HttpServletResponse response) {
        // Browser has closed the connection, so the HTTP OutputStream is closed
        // Silently catch the exception to avoid annoying log
        LOGGER.info("IIOException (Browser has closed the connection, so the HTTP OutputStream is closed) " +
                "while running request:" + createRequestLogMessage(request), e);
    }

    @ExceptionHandler(value = {IOException.class})
    public void handleCustomException(final HttpServletRequest request, final Exception e,
                                   final HttpServletResponse response) {
        LOGGER.info("IOException while running request:" + createRequestLogMessage(request), e);
        response.setStatus(SC_BAD_REQUEST);
        try {
            response.getWriter().append("exception while converting plantuml");
        } catch (IOException ex) {
            LOGGER.warn("exception while exceptionhandling", ex);
        }
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public void handleAllException(final HttpServletRequest request, final Exception e,
                                   final HttpServletResponse response) {
        LOGGER.info("Exception while running request:" + createRequestLogMessage(request), e);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        try {
            response.getWriter().append("exception while converting plantuml");
        } catch (IOException ex) {
            LOGGER.warn("exception while exceptionhandling", ex);
        }
    }

    protected String createRequestLogMessage(HttpServletRequest request) {
        return new StringBuilder("REST Request - ")
                .append("[HTTP METHOD:")
                .append(request.getMethod())
                .append("] [URL:")
                .append(request.getRequestURL())
                .append("] [REQUEST PARAMETERS:")
                .append(getRequestMap(request))
                .append("] [REMOTE ADDRESS:")
                .append(request.getRemoteAddr())
                .append("]").toString();
    }

    private Map<String, String> getRequestMap(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap<>();
        Enumeration<?> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String)requestParamNames.nextElement();
            String requestParamValue = request.getParameter(requestParamName);
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }
        return typesafeRequestMap;
    }
}