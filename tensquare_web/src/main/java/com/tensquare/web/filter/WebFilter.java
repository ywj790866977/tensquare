package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-29 10:31
 **/
@Component
public class WebFilter extends ZuulFilter {


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String header = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(header)){
            currentContext.addZuulRequestHeader("Authorization",header);
        }
        return null;
    }
}
