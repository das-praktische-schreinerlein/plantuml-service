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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;

import org.springframework.stereotype.Service;

/** 
 * services to convert plantuml-src to diagrams
 */
@Service
class PlantumProvider {
    private static final Map<FileFormat, String> CONTENT_TYPE;
    static {
        Map<FileFormat, String> map = new HashMap<FileFormat, String>();
        map.put(FileFormat.PNG, "image/png");
        map.put(FileFormat.SVG, "image/svg+xml");
        map.put(FileFormat.UTXT, "text/plain;charset=UTF-8");
        CONTENT_TYPE = Collections.unmodifiableMap(map);
    }

    /**
     * generate and export plantuml-diagram from uml
     * @param uml                    the src of the plantuml-diagram
     * @param response               the response to send the diagram
     * @param format                 format of the diagram
     * @throws IOException           possible Exception
     */
    public void exportAsDiagramm(String uml, HttpServletResponse response, FileFormat format) throws IOException {
        if (StringUtils.isDiagramCacheable(uml)) {
            addHeaderForCache(response);
        }
        response.setContentType(getContentType(format));
        SourceStringReader reader = new SourceStringReader(uml);
        reader.generateImage(response.getOutputStream(), new FileFormatOption(format, false));
    }

    protected void addHeaderForCache(HttpServletResponse response) {
        long today = System.currentTimeMillis();
        // Add http headers to force the browser to cache the image
        response.addDateHeader("Expires", today + 31536000000L);
        // today + 1 year
        response.addDateHeader("Last-Modified", 1261440000000L);
        // 2009 dec 22 constant date in the past
        response.addHeader("Cache-Control", "public");
    }

    protected String getContentType(FileFormat format) {
        return CONTENT_TYPE.get(format);
    }

    protected String getUmlSource(String source) {
        // build the UML source from the compressed part of the URL
        String text;
        try {
            text = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            text = "' invalid encoded string";
        }
        Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
        try {
            text = transcoder.decode(text);
        } catch (IOException ioe) {
            text = "' unable to decode string";
        }

        // encapsulate the UML syntax if necessary
        String uml;
        if (text.startsWith("@start")) {
            uml = text;
        } else {
            StringBuilder plantUmlSource = new StringBuilder();
            plantUmlSource.append("@startuml\n");
            plantUmlSource.append(text);
            if (text.endsWith("\n") == false) {
                plantUmlSource.append("\n");
            }
            plantUmlSource.append("@enduml");
            uml = plantUmlSource.toString();
        }
        return uml;
    }
}
