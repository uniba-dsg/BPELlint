package bpellint.core.model;

import bpellint.core.imports.LocationAwareNodeFactory;
import nu.xom.Builder;
import nu.xom.Document;
import org.junit.Test;

import javax.xml.soap.Node;
import java.io.File;

import static org.junit.Assert.*;

public class Node2XpathTest {

    @Test
    public void testGetXpath() throws Exception {
        Document document = new Builder(new LocationAwareNodeFactory()).build(new File("Testcases/rules/SA00005/InvokeWithNonExistentPortType.bpel"));


        for(nu.xom.Node node : document.query("//*")) {
            System.out.println(Node2Xpath.getXpath(node));
        }
    }
}