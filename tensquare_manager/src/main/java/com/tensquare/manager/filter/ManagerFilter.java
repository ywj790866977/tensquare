package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-29 10:21
 **/
@Component
public class ManagerFilter extends ZuulFilter {


    @Autowired
    private JwtUtil jwtUtil;

    /**
     * @Description: 执行方式
     * @Return: java.lang.String
     * @Date: 2019-12-29 10:22
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * @Description: 优先级别
     * @Return: int
     * @Date: 2019-12-29 10:23
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * @Description: 是否启用
     * @Return: boolean
     * @Date: 2019-12-29 10:23
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * @Description: 具体执行内容
     * @Return: java.lang.Object
     * @Date: 2019-12-29 10:23
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("过滤器..");
        //获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        //第一次请求,获取请求类型是否可以用通过的
        //直接放行
        if(request.getMethod().equals("OPTIONS")){
            return null;
        }
        //放行登录请求
        if(request.getRequestURI().indexOf("login") > 0){
            return null;
        }

        //判断身份,并转发header
        String header = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(header) && header.startsWith("Bearer ")){
            String token = header.substring(7);
            try {
                Claims claims = jwtUtil.parseJWT(token);
                String roles = (String) claims.get("roles");
                if("admin".equals(roles)){
                    currentContext.addZuulRequestHeader("Authorization",header);
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                currentContext.setSendZuulResponse(false);//终止运行

            }
        }

        //没有身份,返回错误
        currentContext.setSendZuulResponse(false);//终止运行
        currentContext.setResponseStatusCode(403);
        currentContext.setResponseBody("权限不足");
        currentContext.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
