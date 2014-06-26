package hudson.plugins.jswidgets;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.util.RunList;
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
            addActionWhenNotExisting(build);
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

    /**
     * Adds an {@link JsBuildAction} to all builds of the job exactly once.
     *
     * @param build to add the action to.
     */
    final void addActionWhenNotExisting(final AbstractBuild<?, ?> build) {
        final JsBuildAction jsBuildAction = build.getAction(JsBuildAction.class);
        if (jsBuildAction == null) {
            final JsBuildAction newJsBuildAction = new JsBuildAction(build);
            build.addAction(newJsBuildAction);
            LOG.debug("Adding {} to {}", newJsBuildAction, build);
        } else {
            LOG.debug("{} already has {}", build, jsBuildAction);
        }
        LOG.trace("{}:{}", build, build.getActions());
    }

}
