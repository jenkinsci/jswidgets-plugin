package hudson.plugins.jswidgets;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.util.RunList;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author mirko
 */
public class JsJobActionTest {

    final AbstractProject mockProject = mock(AbstractProject.class);

    /**
     * Test of getProject method, of class JsJobAction.
     */
    @Test
    public void testGetProject() {
        setupRunListWithAJsBuildAction();
        final JsJobAction sut = new JsJobAction(mockProject);
        AbstractProject result = sut.getProject();
        assertNotNull(result);
    }

    /**
     * Test of getJobDescription method, of class JsJobAction.
     */
    @Test
    public void testGetJobDescriptionUnescaped() {
        JsJobAction sut = setupProjectWithDescription();
        boolean escapeApostroph = false;
        String expResult = "With ' ";
        String result = sut.getJobDescription(escapeApostroph);
        assertEquals(expResult, result);
    }

    /**
     * Test of getJobDescription method, of class JsJobAction.
     */
    @Test
    public void testGetJobDescriptionEscaped() {
        JsJobAction sut = setupProjectWithDescription();
        boolean escapeApostroph = true;
        String expResult = "With \\' ";
        String result = sut.getJobDescription(escapeApostroph);
        assertEquals(expResult, result);
    }

    void setupRunListWithAJsBuildAction() {
        final RunList runList = new RunList();
        AbstractBuild mockBuild = mock(AbstractBuild.class);
        mockBuild.addAction(new JsBuildAction(mockBuild));
        runList.add(mockBuild);
        when(mockProject.getBuilds()).thenReturn(runList);
    }

    JsJobAction setupProjectWithDescription() {
        setupRunListWithAJsBuildAction();
        when(mockProject.getDescription()).thenReturn("With ' ");
        JsJobAction sut = new JsJobAction(mockProject);
        return sut;
    }

}
