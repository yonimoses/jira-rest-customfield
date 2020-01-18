package com.thejaxx.jira.rest.plugin.admin;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.thejaxx.jira.rest.plugin.config.ConfigEntity;
import com.thejaxx.jira.rest.plugin.config.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Scanned
public class ConfigurationServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationServlet.class);


    //    @ComponentImport
//    private ConfigService configService;
    @JiraImport
    private IssueService issueService;
    @JiraImport
    private ProjectService projectService;
    @JiraImport
    private SearchService searchService;
    @JiraImport
    private TemplateRenderer templateRenderer;
    @JiraImport
    private JiraAuthenticationContext authenticationContext;
    @JiraImport
    private ConstantsManager constantsManager;
    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    private static final String ADMIN = "/templates/admin/admin.vm";

    public ConfigurationServlet(IssueService issueService, ProjectService projectService,
                                SearchService searchService,
                                TemplateRenderer templateRenderer,
                                JiraAuthenticationContext authenticationContext,
                                ConstantsManager constantsManager,
                                PluginSettingsFactory pluginSettingsFactory) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.searchService = searchService;
        this.templateRenderer = templateRenderer;
        this.authenticationContext = authenticationContext;
        this.constantsManager = constantsManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        ConfigUtils.init(pluginSettingsFactory);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String action = Optional.ofNullable(req.getParameter("actionType")).orElse("");

        Map<String, Object> context = new HashMap<>();
        resp.setContentType("text/html;charset=utf-8");

        context.put("url", ConfigUtils.get().getUrl());
        context.put("jsonKey", ConfigUtils.get().getJsonKey());

//        // (1)
//        ao.executeInTransaction((TransactionCallback<Void>) () -> {
//            ConfigEntity[] entities = ao.find(ConfigEntity.class);
//            context.put("config", entities != null && entities.length > 0 ? entities[0] : _config_map);
//            return null;
//        });

        templateRenderer.render(ADMIN, context, resp.getWriter());

//        switch (action) {
//            case "new":
//                templateRenderer.render(NEW_ISSUE_TEMPLATE, context, resp.getWriter());
//                break;
//            case "edit":
//                IssueService.IssueResult issueResult = issueService.getIssue(authenticationContext.getLoggedInUser(),
//                        req.getParameter("key"));
//                context.put("issue", issueResult.getIssue());
//                templateRenderer.render(EDIT_ISSUE_TEMPLATE, context, resp.getWriter());
//                break;
//            default:
//                List<Issue> issues = getIssues();
//                context.put("issues", issues);
//                templateRenderer.render(LIST_ISSUES_TEMPLATE, context, resp.getWriter());
//        }

    }

    /**
     * Retrieve issues using simple JQL query project="TUTORIAL"
     * Pagination is set to unlimited
     *
     * @return List of issues
     */
//    private List getIssues() {
//
//        ApplicationUser user = authenticationContext.getLoggedInUser();
//        JqlClauseBuilder jqlClauseBuilder = JqlQueryBuilder.newClauseBuilder();
//        Query query = jqlClauseBuilder.project("TUTORIAL").buildQuery();
//        PagerFilter pagerFilter = PagerFilter.getUnlimitedFilter();
//
//        SearchResults searchResults = null;
//        try {
//            searchResults = searchService.search(user, query, pagerFilter);
//        } catch (SearchException e) {
//            e.printStackTrace();
//        }
//        return searchResults != null ? searchResults.getResults() : null;
//    }
//

//    private void sendError(HttpServletResponse resp, String msg) {
//        Map<String, Object> context = new HashMap<String, Object>();
//        try {
//
//        }
//        catch (RenderingException e) {
//            log.error("CreateIssueServlet.sendError: " + e.getMessage());
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            log.error("CreateIssueServlet.sendError: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ApplicationUser aUser = authenticationContext.getLoggedInUser();
        ConfigEntity entity = ConfigUtils.get();
        Map<String, Object> context = new HashMap<>();

        context.put("url", entity.getUrl());
        context.put("jsonKey", entity.getJsonKey());


        if (!ComponentAccessor.getGlobalPermissionManager().hasPermission(GlobalPermissionKey.ADMINISTER, aUser)) {
            context.put("response_text", "Permission denied. You can't save these configuration");
        } else {
            entity.setUrl(ServletRequestUtils.getStringParameter(req, "url", entity.getUrl()));
            entity.setJsonKey(ServletRequestUtils.getStringParameter(req, "jsonKey", entity.getJsonKey()));
            ConfigUtils.save(entity);
        }


        templateRenderer.render(ADMIN, context, resp.getWriter());


//        ao.executeInTransaction(() -> {
//            final ConfigEntity configEntity = ao.create(ConfigEntity.class, new HashMap<>()); // (2)
//            configEntity.setUrl(url);
//            configEntity.setKey(key);
//            configEntity.setValue(value);
//            configEntity.save(); // (4)
//            return configEntity;
//        });
    }

//    private void handleIssueEdit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//
//        ApplicationUser user = authenticationContext.getLoggedInUser();
//
//        Map<String, Object> context = new HashMap<>();
//
//        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
//        issueInputParameters.setSummary(req.getParameter("summary"))
//                .setDescription(req.getParameter("description"));
//
//        MutableIssue issue = issueService.getIssue(user, req.getParameter("key")).getIssue();
//
//        IssueService.UpdateValidationResult result =
//                issueService.validateUpdate(user, issue.getId(), issueInputParameters);
//
//        if (result.getErrorCollection().hasAnyErrors()) {
//            context.put("issue", issue);
//            context.put("errors", result.getErrorCollection().getErrors());
//            resp.setContentType("text/html;charset=utf-8");
//            templateRenderer.render(EDIT_ISSUE_TEMPLATE, context, resp.getWriter());
//        } else {
//            issueService.update(user, result);
//            resp.sendRedirect("issuecrud");
//        }
//    }
//
//    private void handleIssueCreation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        ApplicationUser user = authenticationContext.getLoggedInUser();
//
//        Map<String, Object> context = new HashMap<>();
//
//        Project project = projectService.getProjectByKey(user, "TUTORIAL").getProject();
//
//        if (project == null) {
//            context.put("errors", Collections.singletonList("Project doesn't exist"));
//            templateRenderer.render(LIST_ISSUES_TEMPLATE, context, resp.getWriter());
//            return;
//        }
//
//        IssueType taskIssueType = constantsManager.getAllIssueTypeObjects().stream().filter(
//                issueType -> issueType.getName().equalsIgnoreCase("task")).findFirst().orElse(null);
//
//        if (taskIssueType == null) {
//            context.put("errors", Collections.singletonList("Can't find Task issue type"));
//            templateRenderer.render(LIST_ISSUES_TEMPLATE, context, resp.getWriter());
//            return;
//        }
//
//        IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
//        issueInputParameters.setSummary(req.getParameter("summary"))
//                .setDescription(req.getParameter("description"))
//                .setAssigneeId(user.getName())
//                .setReporterId(user.getName())
//                .setProjectId(project.getId())
//                .setIssueTypeId(taskIssueType.getId());
//
//        IssueService.CreateValidationResult result = issueService.validateCreate(user, issueInputParameters);
//
//        if (result.getErrorCollection().hasAnyErrors()) {
//            List<Issue> issues = getIssues();
//            context.put("issues", issues);
//            context.put("errors", result.getErrorCollection().getErrors());
//            resp.setContentType("text/html;charset=utf-8");
//            templateRenderer.render(LIST_ISSUES_TEMPLATE, context, resp.getWriter());
//        } else {
//            issueService.create(user, result);
//            resp.sendRedirect("issuecrud");
//        }
//    }
//
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        ApplicationUser user = authenticationContext.getLoggedInUser();
//        String respStr;
//        IssueService.IssueResult issueResult = issueService.getIssue(user, req.getParameter("key"));
//        if (issueResult.isValid()) {
//            IssueService.DeleteValidationResult result = issueService.validateDelete(user, issueResult.getIssue().getId());
//            if (result.getErrorCollection().hasAnyErrors()) {
//                respStr = "{ \"success\": \"false\", error: \"" + result.getErrorCollection().getErrors().get(0) + "\" }";
//            } else {
//                issueService.delete(user, result);
//                respStr = "{ \"success\" : \"true\" }";
//            }
//        } else {
//            respStr = "{ \"success\" : \"false\", error: \"Couldn't find issue\"}";
//        }
//        resp.setContentType("application/json;charset=utf-8");
//        resp.getWriter().write(respStr);
//    }
}