package hudson.plugins.jswidgets;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.util.RunList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the JS widgets pages for a job.
 *
 * @author mfriedenhagen
 */
public class JsJobAction extends JsBaseAction {

    /** The Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(JsJobAction.class);

    /** The project. */
    private final transient AbstractProject<?, ?> project;

    /**
     * @param project
     *            the job for which the health report will be generated.
     */
    public JsJobAction(AbstractProject<?, ?> project) {
        this.project = project;
        // add the JsBuildAction to all run builds, JsRunListener will append this to the others.
        final RunList<?> builds = project.getBuilds();
        for (Object object : builds) {
            final AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) object;
            final List<JsBuildAction> jsBuildActions = build.getActions(JsBuildAction.class);
            if (jsBuildActions.isEmpty()) {
                final JsBuildAction jsBuildAction = new JsBuildAction(build);
                build.addAction(jsBuildAction);
                LOG.debug("Adding {} to {}", jsBuildAction, build);
            } else {
                LOG.debug("{} already has {}", build, jsBuildActions);
            }
            LOG.trace("{}:{}", build, build.getActions());
        }

    }

    /**
     * Returns the job for which the health report will be generated.
     *
     * @return job
     */
    public AbstractProject<?, ?> getProject() {
        return project;
    }

    /**
     * Returns the description of the job without line feeds and ' as this will break the Javascript output.
     *
     * @param escapeApostroph escape apostroph (used by javascript-rendering).
     * @return the description in one line.
     */
    public String getJobDescription(boolean escapeApostroph) {
        final String description = project.getDescription().replace("\n", "").replace("\r", "");
        return escapeApostroph ? description.replace("'", "\\'") : description;
    }

}
