package ch.ipt.see.playground;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class MathController {
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    @PostMapping(
            path = "/math/area/rectangle",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity calculateRectangleArea(@RequestBody String input) {
        Document document;
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            document = documentBuilder.parse(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        NodeList sideElements = document.getElementsByTagName("side");
        int numberOfSides = sideElements.getLength();

        if (numberOfSides < 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<error>Needs at least two sides</error>");
        }

        Long area = null;
        for (int i = 0; i < numberOfSides; i++) {
            Element sideElement = (Element) sideElements.item(i);
            Long length = Long.valueOf(sideElement.getAttribute("length"));
            area = area == null ? length : area * length;
        }
        return ResponseEntity.ok("<calculation><area>"+area+"</area></calculation>");
    }
}