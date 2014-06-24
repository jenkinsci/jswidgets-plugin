/**
 *
 */
package hudson.plugins.jswidgets;

import hudson.model.Hudson;
import hudson.model.Project;
import hudson.model.User;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jvnet.hudson.test.Bug;
import org.jvnet.hudson.test.recipes.LocalData;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import static org.junit.Assert.*;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginIT {

    /** Our logger. */
    private static final Logger LOG = LoggerFactory.getLogger(PluginIT.class);

    @Rule
    public JenkinsRule j = new JenkinsRule();

    /**
     *
     */
    private static final int TOTAL_NUMBER_OF_RUNS = 7;

    private static final String JAVA_SCRIPT_NEEDLE = "document.write(";

    private JenkinsRule.WebClient webClient;

    @Before
    public void setUp() throws Exception {
        webClient = j.createWebClient();
    }

    /**
     * {@inheritDoc}. Deletes the hudson instance directory on teardown to avoid leakage of testdirectories.
     */
    @After
    public void tearDown() throws Exception {
        final File rootDir = j.jenkins.getRootDir();
        LOG.info("Deleting " + rootDir + " in tearDown");
        FileUtils.deleteDirectory(rootDir);
    }

    /**
     * Test method for an existing job without any builds.
     *
     * @throws IOException
     * @throws SAXException
     */
    @Test
    @LocalData
    public void testJsHealthWithoutBuilds() throws IOException, SAXException {
        final String emptyIcon = "16x16/empty.png";
        final String relative = "job/foo/" + JsConsts.URLNAME + "/health";
        final String jobDescription = "Just a small instance for testing";
        checkJavaScriptOutput(emptyIcon, relative);
        checkJavaScriptOutput(jobDescription, relative);
        checkHtmlOutput(emptyIcon, relative);
        checkHtmlOutput(jobDescription, relative);
    }

    @Test
    @LocalData
    public void testJsHealthWithBuilds() throws IOException, SAXException {
        final String blueIcon = "16x16/blue.png";
        final String relative = "job/bar/" + JsConsts.URLNAME + "/health";
        checkJavaScriptOutput(blueIcon, relative);
        checkHtmlOutput(blueIcon, relative);
        checkRowCount(webClient.goTo(relative + "?html=true"), 3);
        XmlPage gadget = webClient.goToXml("job/bar/" + JsConsts.URLNAME + "/health-gadget.xml");
        j.assertXPath(gadget, "/Module/Content[@type=\"html\"]");
    }

    @Test
    @LocalData
    public void testJsHealthWithoutDescription() throws IOException, SAXException {
        final String blueIcon = "16x16/blue.png";
        final String relative = "job/bar/" + JsConsts.URLNAME + "/health";
        final String htmlNeedle = "job with \\'3\\' builds";
        checkJavaScriptOutput(htmlNeedle, relative);
        final JavaScriptPage javaScriptPage = checkJavaScriptOutput(blueIcon, relative + "?skipDescription=true");
        final String javaScript = javaScriptPage.getContent().trim();
        assertFalse(htmlNeedle + " found in " + javaScript, javaScript.contains(htmlNeedle));
    }

    @Test
    @LocalData
    public void testBuildHistoryWhereCountIsNull() throws IOException, SAXException {
        final String htmlNeedle = "<div align=\"center\">";
        final String relative = "/" + JsConsts.URLNAME + "/runs";
        checkHtmlOutput(htmlNeedle, relative);
        checkJavaScriptOutput(htmlNeedle, relative);
        checkRowCount(webClient.goTo(relative + "?html=true"), TOTAL_NUMBER_OF_RUNS);
    }


    @Test
    @LocalData
    public void testBuildHistoryWhereCountIsGiven() throws IOException, SAXException {
        checkRowCount(webClient.goTo("/" + JsConsts.URLNAME + "/runs" + "?html=true&count=2"), 2);
    }

    @Test
    @LocalData
    public void testBuildHistoryWhereCountGreaterThanSizeOfRunlist() throws IOException, SAXException {
        checkRowCount(webClient.goTo("/" + JsConsts.URLNAME + "/runs" + "?html=true&count=100"), TOTAL_NUMBER_OF_RUNS);
    }

    @Test
    @LocalData
    public void testJsJobIndexAction() throws IOException, SAXException {
        webClient.setJavaScriptEnabled(false); // TODO webClient chokes on the jshealth javascript right now
        checkSubJelly("/job/foo", "health");
    }

    @Test
    @LocalData
    public void testJsBuildActionWithChanges() throws IOException, SAXException {
        final String buildPath = "/job/svntest/4";
        final String changesJelly = "changes";
        final String changeLogNeedle = "Now with more text";
        // Built on removed slave.
        final String nodeName = "UNKNOWN";
        testJsBuildAction(buildPath, changesJelly, changeLogNeedle, nodeName);
        // test JsBuildAction.getChangeSetEntries
        testJsBuildAction(buildPath, changesJelly, "#/trunk/foo", nodeName);
    }

    @Test
    @Bug(5106)
    @LocalData
    public void testJsBuildActionWithAnApostroph() throws IOException, SAXException {
        webClient.setJavaScriptEnabled(false); // TODO webClient chokes on the jshealth javascript right now
        final String relative = "job/bar/" + JsConsts.URLNAME + "/health";
        checkHtmlOutput("job with '3' builds", relative);
        checkJavaScriptOutput("job with \\'3\\' builds", relative);
    }

    @Test
    @LocalData
    public void testJsBuildActionWithOutChanges() throws IOException, SAXException {
        final String buildPath = "/job/svntest/2";
        final String changesJelly = "changes";
        final String changeLogNeedle = "No changes in this build";
        final String nodeName = "master Jenkins node";
        testJsBuildAction(buildPath, changesJelly, changeLogNeedle, nodeName);
    }

    @Test
    @LocalData
    public void testJsProjectActionFactory() {
        @SuppressWarnings("unchecked")
        final List<Project> projects = Hudson.getInstance().getProjects();
        assertTrue("Have not projects", projects.size() > 0);
        @SuppressWarnings("unchecked")
        final Project firstProject = projects.get(0);
        new JsProjectActionFactory().createFor(firstProject);
        new JsProjectActionFactory().createFor(firstProject);
        assertEquals(1, firstProject.getActions(JsJobAction.class).size());
    }

    @Test
    public void testSCMWithoutAffectedFilesImplementation() {
        final Entry entry = new ChangeLogSet.Entry() {

            @Override
            public String getMsg() {
                return "Jepp";
            }

            @Override
            public User getAuthor() {
                return null;
            }

            @Override
            public Collection<String> getAffectedPaths() {
                return Arrays.asList("/trunk/foo");
            }
        };
        final JsBuildAction buildAction = new JsBuildAction(null);
        assertArrayEquals(entry.getAffectedPaths().toArray(), buildAction.getChangeSetEntries(entry).toArray());
    }

    /**
     * @param buildPath
     * @param changesJelly
     * @param changeLogNeedle
     * @param nodeName
     * @throws IOException
     * @throws SAXException
     */
    private void testJsBuildAction(final String buildPath, final String changesJelly, final String changeLogNeedle,
            final String nodeName) throws IOException, SAXException {
        webClient.setJavaScriptEnabled(false); // TODO webClient chokes on the javascript right now
        checkSubJelly(buildPath, changesJelly);
        final String indexPath = buildPath + "/" + JsConsts.URLNAME;
        // Built on removed slave.
        checkHtmlOutput(nodeName, indexPath);
        final String changesPath = indexPath + "/changes";
        checkHtmlOutput(changeLogNeedle, changesPath);
        checkJavaScriptOutput(changeLogNeedle, changesPath);
    }

    /**
     * @param htmlPage
     * @param expectedRows
     */
    private void checkRowCount(final HtmlPage htmlPage, final int expectedRows) {
        final String xml = htmlPage.asXml();
        final Pattern pattern = Pattern.compile("<tr>");
        final Matcher matcher = pattern.matcher(xml);
        int foundRows = 0;
        while (matcher.find()) {
            foundRows++;
        }
        assertEquals(xml, expectedRows, foundRows);
    }

    /**
     * Checks the existence of the index-jelly entry and an additional specialized jelly referenced by jellyPath.
     *
     * @param objectPath
     * @param jellyPath
     * @throws IOException
     * @throws SAXException
     */
    private void checkSubJelly(final String objectPath, final String jellyPath) throws IOException, SAXException {
        final HtmlPage jobPage = webClient.goTo(objectPath);
        checkXpath(jobPage, "//img[contains(@src,\"" + JsConsts.ICONFILENAME + "\")]");
        checkXpath(jobPage, "//a[contains(@href, \"" + objectPath + "/" + JsConsts.URLNAME + "\")]");
        final String href = objectPath + "/" + JsConsts.URLNAME;
        final HtmlPage jsIndexPage = (HtmlPage) jobPage.getAnchorByHref(href).click();
        checkXpath(jsIndexPage, "//script[@type=\"text/javascript\" and contains(@src, \"" + href + "/" + jellyPath
                + "\")]");
    }

    /**
     * @param htmlPage
     * @param needleXPath
     */
    private void checkXpath(final HtmlPage htmlPage, final String needleXPath) {
        assertNotNull(needleXPath + " not found in " + htmlPage.asXml(), htmlPage.getFirstByXPath(needleXPath));
    }

    /**
     * @param htmlNeedle
     * @param htmlPage
     * @throws SAXException
     * @throws IOException
     */
    private void checkHtmlOutput(final String htmlNeedle, final String relative) throws IOException, SAXException {
        final HtmlPage htmlPage = webClient.goTo(relative + "?html=true");
        checkHtmlOutput(htmlNeedle, htmlPage);
    }

    /**
     * @param htmlNeedle
     * @param htmlPage
     */
    private void checkHtmlOutput(final String htmlNeedle, final HtmlPage htmlPage) {
        final String html = htmlPage.asXml();
        assertTrue(htmlNeedle + " not found in " + html, html.contains(htmlNeedle));
        assertFalse(JAVA_SCRIPT_NEEDLE + " found in " + html, html.contains(JAVA_SCRIPT_NEEDLE));
    }

    /**
     * @param htmlNeedle
     * @param relative
     * @return
     * @throws IOException
     * @throws SAXException
     */
    private JavaScriptPage checkJavaScriptOutput(final String htmlNeedle, final String relative) throws IOException,
            SAXException {
        final JavaScriptPage jsPage = (JavaScriptPage) webClient.goTo(relative, "text/javascript");
        checkJavaScriptOutput(htmlNeedle, jsPage);
        return jsPage;
    }

    /**
     * @param htmlNeedle
     * @param jsPage
     */
    private void checkJavaScriptOutput(final String htmlNeedle, final JavaScriptPage jsPage) {
        final String javaScript = jsPage.getContent().trim();
        assertTrue(htmlNeedle + " not found in " + javaScript, javaScript.contains(htmlNeedle));
        assertTrue(javaScript + " does not start with " + JAVA_SCRIPT_NEEDLE, javaScript.startsWith(JAVA_SCRIPT_NEEDLE));
    }

    /**
     * Checks that the jsindex.png is only referenced once on the page.
     *
     * @param build
     * @throws IOException
     * @throws SAXException
     */
    private void checkJsWidgetsOnlyOnce(final String build) throws IOException, SAXException {
        final HtmlPage buildPage = webClient.goTo(build);
        final String body = buildPage.asXml();
        final String needle = "/plugin/jswidgets/img/jsindex.png";
        assertEquals(body + " has more than one jsindex.png", body.indexOf(needle), body.lastIndexOf(needle));
    }

}
