package com.mysitedemo.core.servlets;
import com.drew.lang.annotations.NotNull;
import com.mysitedemo.core.services.APIcallService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Slf4j
@Component(
        immediate = true,
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "= ChatGPT Integration",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/chat",
                "sling.servlet.extensions={\"json\"}"
        }
)
public class chatServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    static final String RESOURCE_PATH = "/bin/chat";
    private static final String TEASER_TITLE = "jcr:title";
    @Reference
    private APIcallService apiInvoker;

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws ServletException, IndexOutOfBoundsException, IOException {

        JSONObject jsonObject = new JSONObject();


        String prompt = request.getParameter("prompt");
        String maxtoken = request.getParameter("maxtoken");

                try {
                    if (StringUtils.isNoneEmpty(prompt) && StringUtils.isNotEmpty(maxtoken)) {
                        int tokenCount = Integer.parseInt(maxtoken);
                        jsonObject.put("Response : ", apiInvoker.callApi(prompt, tokenCount));
                    }
                } catch (JSONException e) {
                    log.error(e.getMessage());
                }
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().print(jsonObject);
        }
    }

