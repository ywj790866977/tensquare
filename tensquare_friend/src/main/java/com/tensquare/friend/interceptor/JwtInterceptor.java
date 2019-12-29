package com.tensquare.friend.interceptor;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-27 09:37
 **/
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器");
        //获取token进行解析
        String header = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(header) && header.startsWith("Bearer ")){
            String token = header.substring(7);
            try {
                Claims claims = jwtUtil.parseJWT(token);
                String id = claims.getId();
                System.out.println(id);
                String roles = (String) claims.get("roles");
                if(roles !=null){
                    if("admin".equals(roles)){
                        request.setAttribute("claims_admin",claims);
                    }
                    if("user".equals(roles)){
                        request.setAttribute("claims_user",claims);
                    }
                }

            } catch (Exception e) {
                throw  new RuntimeException("令牌不正确!");
            }
        }

        return true;
    }

}
