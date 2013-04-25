package hudson.plugins.jswidgets;

import hudson.Functions;
import hudson.model.Action;

import org.kohsuke.stapler.StaplerRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements some basic methods for returning baseUrl and image paths. This is the base class for javascript actions.
 * 
 * @author mfriedenhagen
 */
public abstract class JsBaseAction implements Action {

    /** Our logger. */
    private static final Logger LOG = LoggerFactory.getLogger(JsBaseAction.class);

    /**
     * Returns whether we want HTML instead of javascript by checking the request for {@literal html=true}.
     * 
     * @param request
     *            stapler request
     * @return true if html is true
     */
    public boolean wantHtml(final StaplerRequest request) {
        final boolean wantHtml = Boolean.parseBoolean(request.getParameter("html"));
        LOG.debug("wantHtml={}", wantHtml);
        return wantHtml;
    }

    /**
     * Returns whether we want to skip the description of the job.
     * 
     * @param request
     *            stapler request
     * @return true if skipDescription is true
     */
    public boolean skipDescription(final StaplerRequest request) {
        final boolean skipDescription = Boolean.parseBoolean(request.getParameter("skipDescription"));
        LOG.trace("skipDescription={}", skipDescription);
        return skipDescription;
    }

    /**
     * Calculates Jenkins' URL including protocol, host and port from the request.
     * 
     * @param req
     *            request from the jelly page.
     * @return the baseurl
     */
    public String getBaseUrl(final StaplerRequest req) {
        final String requestURL = String.valueOf(req.getRequestURL());
        final String requestURI = req.getRequestURI();
        final String baseUrl = requestURL.substring(0, requestURL.length() - requestURI.length())
                + req.getContextPath();
        LOG.trace("baseUrl={} from requestURL={}", baseUrl, requestURL);
        return baseUrl;
    }

    /**
     * Returns the static path for images.
     * 
     * TODO: Check how we may get this from injected h-Object.
     * 
     * @param req
     *            request from the jelly page.
     * @return static image path
     */
    public String getImagesUrl(final StaplerRequest req) {
        final String imagesPath = getBaseUrl(req) + Functions.getResourcePath() + "/images/16x16";
        LOG.trace("imagesPath={}", imagesPath);
        return imagesPath;
    }

    /**
     * {@inheritDoc}
     * 
     * Make method final, as we always want the same display name.
     */
    //@Override
    public final String getDisplayName() {
        return JsConsts.DISPLAYNAME;
    }

    /**
     * {@inheritDoc}
     * 
     * Make method final, as we always want the same icon file.
     */
    //@Override
    public final String getIconFileName() {
        return JsConsts.ICONFILENAME;
    }

    /** {@inheritDoc} */
    //@Override
    public String getUrlName() {
        return JsConsts.URLNAME;
    }
}
