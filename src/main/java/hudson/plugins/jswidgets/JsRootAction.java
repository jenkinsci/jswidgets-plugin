package hudson.plugins.jswidgets;

import hudson.Extension;
import hudson.model.RootAction;
import hudson.model.Run;
import hudson.util.RunList;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;

/**
 * Implements methods for javascript root widgets.
 *
 * @author mfriedenhagen
 */
@Extension
public class JsRootAction extends JsBaseAction implements RootAction {

    /**
     * {@inheritDoc}
     *
     * This actions always starts from the context directly, so prefix {@link JsConsts} with a slash.
     */
    @Override
    public String getUrlName() {
        return "/" + JsConsts.URLNAME;
    }

    /**
     * Returns some or all known runs of this hudson instance, depending on parameter count.
     *
     * @param request evalutes parameter <tt>count</tt>
     * @return runlist
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public RunList getRunList(StaplerRequest request) {
        final RunList<Run> allRuns = new RunList(Jenkins.getInstance().getPrimaryView());
        final String countParameter = request.getParameter("count");
        if (countParameter == null) {
            return allRuns;
        } else {
            final ArrayList<Run> result = new ArrayList<Run>();
            final int count = Integer.valueOf(countParameter);
            int current = 0;
            for (final Run run : allRuns) {
                if (current < count) {
                    result.add(run);
                } else {
                    break;
                }
                current++;
            }
            return RunList.fromRuns(result);
        }
    }

}
