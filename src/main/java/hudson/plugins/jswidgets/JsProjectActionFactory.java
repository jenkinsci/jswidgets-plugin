package hudson.plugins.jswidgets;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Extends project actions for all jobs.
 * 
 * @author mfriedenhagen
 */
@Extension
public class JsProjectActionFactory extends TransientProjectActionFactory {

    /** Our logger. */
    private static final Logger LOG = LoggerFactory.getLogger(JsProjectActionFactory.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends Action> createFor(@SuppressWarnings("rawtypes") AbstractProject target) {
        LOG.debug("{} adds JsJobAction for {}", this, target);
        final List<JsJobAction> jsJobActions = target.getActions(JsJobAction.class);
        final ArrayList<Action> actions = new ArrayList<Action>();
        if (jsJobActions.isEmpty()) {
            LOG.debug("{} already has {}", target, jsJobActions);
            final JsJobAction newAction = new JsJobAction(target);
            actions.add(newAction);
            return actions;    
        } else {
            return jsJobActions;
        }
        
    }

}
