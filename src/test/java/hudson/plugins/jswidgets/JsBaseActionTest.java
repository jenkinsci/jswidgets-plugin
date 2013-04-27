/*
 * Copyright 2013 mifr.
 */

package hudson.plugins.jswidgets;

import jenkins.model.Jenkins;
import org.junit.Test;
import org.kohsuke.stapler.StaplerRequest;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author mifr
 */
public class JsBaseActionTest {

    final Jenkins mockJenkins = mock(Jenkins.class);
    final StaplerRequest mockStaplerRequest = mock(StaplerRequest.class);
    final JsBaseActionImpl sut = new JsBaseActionImpl(mockJenkins);

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
        final String expResult = "http://localhost:8080/jenkins";

        setRootUrlWithoutTrailingSlash();
        assertEquals(expResult, sut.getBaseUrl(mockStaplerRequest));

        setRootUrlWithTrailingSlash();
        assertEquals(expResult, sut.getBaseUrl(mockStaplerRequest));

    }

    /**
     * Test of getImagesUrl method, of class JsBaseAction.
     */
    @Test
    public void testGetImagesUrl() {
        final String expResult = "http://localhost:8080/jenkins/images/16x16";

        setRootUrlWithoutTrailingSlash();
        assertEquals(expResult, sut.getImagesUrl(mockStaplerRequest));

        setRootUrlWithTrailingSlash();
        assertEquals(expResult, sut.getImagesUrl(mockStaplerRequest));
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

    private void setRootUrlWithoutTrailingSlash() {
        when(mockJenkins.getRootUrl()).thenReturn("http://localhost:8080/jenkins");
    }

    private void setRootUrlWithTrailingSlash() {
        when(mockJenkins.getRootUrl()).thenReturn("http://localhost:8080/jenkins/");
    }

    public static class JsBaseActionImpl extends JsBaseAction {

        JsBaseActionImpl(Jenkins jenkins) {
            super(jenkins);
        }
    }

}
