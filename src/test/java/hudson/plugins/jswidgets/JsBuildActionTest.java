package hudson.plugins.jswidgets;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Node;
import hudson.scm.ChangeLogSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author mirko
 */
public class JsBuildActionTest {

    final AbstractBuild mockBuild = mock(AbstractBuild.class);
    final JsBuildAction sut = new JsBuildAction(mockBuild);
    /**
     * Test of getUrlName method, of class JsBuildAction.
     */
    @Test
    public void testGetUrlName() {
        String expResult = JsConsts.URLNAME;
        String result = sut.getUrlName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBuild method, of class JsBuildAction.
     */
    @Test
    public void testGetBuild() {
        AbstractBuild result = sut.getBuild();
        assertNotNull("build is null", result);
    }

    /**
     * Test of getProject method, of class JsBuildAction.
     */
    @Test
    public void testGetProject() {
        when(mockBuild.getProject()).thenReturn(mock(AbstractProject.class));
        AbstractProject result = sut.getProject();
        assertNotNull("project is null", result);
    }

    /**
     * Test of getBuiltOn method, of class JsBuildAction.
     */
    @Test
    public void testGetBuiltOnUnknownNode() {
        String expResult = "UNKNOWN";
        String result = sut.getBuiltOn();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBuiltOn method, of class JsBuildAction.
     */
    @Test
    public void testGetBuiltOnKnownNode() {
        String expResult = "MyNode";
        final Node mockNode = mock(hudson.model.Node.class);
        when(mockNode.getNodeDescription()).thenReturn(expResult);
        when(mockBuild.getBuiltOn()).thenReturn(mockNode);
        String result = sut.getBuiltOn();
        assertEquals(expResult, result);
    }

    /**
     * Test of getChangeSetEntries method, of class JsBuildAction.
     */
    @Test
    public void testGetChangeSetEntriesForScmWithoutGetAffectedFiles() {
        ChangeLogSet.Entry mockEntry = mock(ChangeLogSet.Entry.class);
        final List<String> expResult = Arrays.asList("a", "b");
        when(mockEntry.getAffectedPaths()).thenReturn(expResult);
        when(mockEntry.getAffectedFiles()).thenThrow(UnsupportedOperationException.class);
        Collection result = sut.getChangeSetEntries(mockEntry);
        assertEquals(expResult, result);
    }
}
