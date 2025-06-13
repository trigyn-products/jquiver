package com.trigyn.jws.usermanagement.security.config;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.itextpdf.io.exceptions.IOException;
import com.trigyn.jws.usermanagement.security.config.oauth.CustomSAMLUserService;

@Component
public class CustomOpenSamlAuthenticationProvider implements AuthenticationProvider {
	
	private final Log								logger						= LogFactory.getLog(getClass());
	
	@Autowired
	CustomSAMLUserService							customSAMLUserService		= null;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	if (supports(authentication.getClass())) {
        Saml2AuthenticationToken saml2AuthenticationToken = (Saml2AuthenticationToken) authentication;
        Saml2Authentication convertedAuthentication = null;
			try {
				convertedAuthentication = convertSaml2Authentication(saml2AuthenticationToken);
			} catch (IOException | ParserConfigurationException | SAXException | java.io.IOException exc) {
				logger.error("Failed : Error while authenticating " + exc.getMessage());
			}
        return convertedAuthentication;
    	} else {
    	    return null;
    	}

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return Saml2AuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Saml2Authentication convertSaml2Authentication(Saml2AuthenticationToken authentication) throws IOException, ParserConfigurationException, SAXException, java.io.IOException {
    	
    	Map<String, Object> attributeDetails = extractUsername(authentication.getSaml2Response());
		UserInformation	userInformation			= customSAMLUserService.samlUser(attributeDetails);
        Saml2Authentication saml2Authentication = new Saml2Authentication(
                userInformation, 
                authentication.getSaml2Response(),
                userInformation.getAuthorities()
        );
        return saml2Authentication;
    }
    
    public static Map<String, Object> extractUsername(String samlResponse) throws ParserConfigurationException, SAXException, IOException, java.io.IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(samlResponse));
        Document document = builder.parse(is);
        Map<String, Object> attributeMap = new HashMap<>();
        String firstName = extractAttribute(document, "First Name");
        String lastName = extractAttribute(document, "Last Name");
        String email = extractAttribute(document, "Email");
        attributeMap.put("firstName", firstName);
        attributeMap.put("lastName", lastName);
        attributeMap.put("email", email);
        // Get the NameID element
        NodeList nameIdNodes = document.getElementsByTagName("saml2:NameID");
        if (nameIdNodes.getLength() > 0) {
            Node nameIdNode = nameIdNodes.item(0);
            attributeMap.put("userName", nameIdNode.getTextContent());
            return attributeMap;
        } else {
            return null;
        }
    }
    
    private static String extractAttribute(Document document, String attributeName) {
        NodeList attributeNodes = document.getElementsByTagName("saml2:Attribute");

        for (int i = 0; i < attributeNodes.getLength(); i++) {
            Element attributeElement = (Element) attributeNodes.item(i);
            String name = attributeElement.getAttribute("Name");
            if (name.equals(attributeName)) {
                NodeList attributeValueNodes = attributeElement.getElementsByTagName("saml2:AttributeValue");
                if (attributeValueNodes.getLength() > 0) {
                    Element attributeValueElement = (Element) attributeValueNodes.item(0);
                    return attributeValueElement.getTextContent();
                }
            }
        }
        return null; 
    }

}
