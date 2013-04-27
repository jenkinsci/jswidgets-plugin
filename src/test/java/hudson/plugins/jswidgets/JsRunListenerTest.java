package hudson.plugins.jswidgets;

import hudson.model.AbstractBuild;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author mirko
 */
public class JsRunListenerTest {

    public JsRunListenerTest() {
    }

    /**
     * Test of onFinalized method, of class JsRunListener.
     */
    @Test
    public void checkThatOnFinalizedWithoutActionAnActionIsAdded() {
        final AbstractBuild mockBuild = createBuildWithoutAction();
        JsRunListener sut = new JsRunListener();
        sut.onFinalized(mockBuild);
        verify(mockBuild, times(1)).addAction(any(JsBuildAction.class));
    }

    /**
     * Test of onFinalized method, of class JsRunListener.
     */
    @Test
    public void checkThatOnFinalizedWithActionNoActionIsAdded() {
        final AbstractBuild mockBuild = createBuildWithAction();
        JsRunListener sut = new JsRunListener();
        sut.onFinalized(mockBuild);
        verify(mockBuild, never()).addAction(any(JsBuildAction.class));
    }

    AbstractBuild createBuildWithAction() {
        AbstractBuild mockBuild = mock(AbstractBuild.class);
        when(mockBuild.getAction(JsBuildAction.class)).thenReturn(new JsBuildAction(mockBuild));
        return mockBuild;
    }

    AbstractBuild createBuildWithoutAction() {
        AbstractBuild mockBuild = mock(AbstractBuild.class);
        when(mockBuild.getActions(JsBuildAction.class)).thenReturn(null);
        return mockBuild;
    }
}
