/*
 * Copyright 2013 mifr.
 */

package hudson.plugins.jswidgets;

import org.junit.Test;
import org.kohsuke.stapler.StaplerRequest;
import static org.junit.Assert.*;

/**
 *
 * @author mifr
 */
public class JsBaseActionTest {
    
    public JsBaseActionTest() {
        new JsBaseActionImpl();
    }

    /**
     * Test of wantHtml method, of class JsBaseAction.
     */
    @Test
    public void testWantHtml() {
        System.out.println("wantHtml");
        StaplerRequest request = null;
        JsBaseAction instance = new JsBaseActionImpl();
        boolean expResult = false;
        boolean result = instance.wantHtml(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of skipDescription method, of class JsBaseAction.
     */
    @Test
    public void testSkipDescription() {
        System.out.println("skipDescription");
        StaplerRequest request = null;
        JsBaseAction instance = new JsBaseActionImpl();
        boolean expResult = false;
        boolean result = instance.skipDescription(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBaseUrl method, of class JsBaseAction.
     */
    @Test
    public void testGetBaseUrl() {
        System.out.println("getBaseUrl");
        StaplerRequest req = null;
        JsBaseAction instance = new JsBaseActionImpl();
        String expResult = "";
        String result = instance.getBaseUrl(req);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImagesUrl method, of class JsBaseAction.
     */
    @Test
    public void testGetImagesUrl() {
        System.out.println("getImagesUrl");
        StaplerRequest req = null;
        JsBaseAction instance = new JsBaseActionImpl();
        String expResult = "";
        String result = instance.getImagesUrl(req);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisplayName method, of class JsBaseAction.
     */
    @Test
    public void testGetDisplayName() {
        System.out.println("getDisplayName");
        JsBaseAction instance = new JsBaseActionImpl();
        String expResult = "";
        String result = instance.getDisplayName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIconFileName method, of class JsBaseAction.
     */
    @Test
    public void testGetIconFileName() {
        System.out.println("getIconFileName");
        JsBaseAction instance = new JsBaseActionImpl();
        String expResult = "";
        String result = instance.getIconFileName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUrlName method, of class JsBaseAction.
     */
    @Test
    public void testGetUrlName() {
        System.out.println("getUrlName");
        JsBaseAction instance = new JsBaseActionImpl();
        String expResult = "";
        String result = instance.getUrlName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class JsBaseActionImpl extends JsBaseAction {
    }
    
}
