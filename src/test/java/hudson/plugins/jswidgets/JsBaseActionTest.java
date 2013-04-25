/*
 * Copyright 2013 mifr.
 */

package hudson.plugins.jswidgets;

import org.junit.Test;
import org.kohsuke.stapler.StaplerRequest;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author mifr
 */
public class JsBaseActionTest {
    
    final JsBaseActionImpl sut = new JsBaseActionImpl();
    final StaplerRequest mockStaplerRequest = mock(StaplerRequest.class);
    /**
     * Test of wantHtml method, of class JsBaseAction.
     */
    @Test
    public void testWantHtml() {
        when(mockStaplerRequest.getParameter("html")).thenReturn("false");
        boolean expResult = false;
        boolean result = sut.wantHtml(mockStaplerRequest);
        assertEquals(expResult, result);
    }

    /**
     * Test of skipDescription method, of class JsBaseAction.
     */
    @Test
    public void testSkipDescription() {
        when(mockStaplerRequest.getParameter("skipDescription")).thenReturn("false");
        boolean expResult = false;
        boolean result = sut.skipDescription(mockStaplerRequest);
        assertEquals(expResult, result);
    }

    /**
     * Test of getBaseUrl method, of class JsBaseAction.
     */
    @Test
    public void testGetBaseUrl() {
        setupBaseUrlRequest();
        String expResult = "http://localhost:8080/jenkins";
        String result = sut.getBaseUrl(mockStaplerRequest);
        assertEquals(expResult, result);
    }

    /**
     * Test of getImagesUrl method, of class JsBaseAction.
     */
    @Test
    public void testGetImagesUrl() {
        setupBaseUrlRequest();
        String expResult = "http://localhost:8080/jenkins/images/16x16";
        String result = sut.getImagesUrl(mockStaplerRequest);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDisplayName method, of class JsBaseAction.
     */
    @Test
    public void testGetDisplayName() {
        String expResult = JsConsts.DISPLAYNAME;
        String result = sut.getDisplayName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIconFileName method, of class JsBaseAction.
     */
    @Test
    public void testGetIconFileName() {
        String expResult = JsConsts.ICONFILENAME;
        String result = sut.getIconFileName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUrlName method, of class JsBaseAction.
     */
    @Test
    public void testGetUrlName() {
        String expResult = JsConsts.URLNAME;
        String result = sut.getUrlName();
        assertEquals(expResult, result);
    }

    void setupBaseUrlRequest() {
        when(mockStaplerRequest.getRequestURI()).thenReturn("/jenkins/foo");
        when(mockStaplerRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/jenkins/foo"));
        when(mockStaplerRequest.getContextPath()).thenReturn("/jenkins");
    }

    public static class JsBaseActionImpl extends JsBaseAction {
    }
    
}
