package com.tensquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

/**
 * @program: tensquare_parent
 * @description:
 * @author: 好人
 * @date: 2019-12-26 22:17
 **/
public class ParseJwtTest {
    public static void main(String[] args) {
        Claims itcast = Jwts.parser().setSigningKey("itcast")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_pqawiLCJpYXQiOjE1NzczNjk2NDR9.q2gzuppgR2fhl6x3audTLqg-DUAepNdtXlTSUpFptOY")
                .getBody();
        System.out.println("用户id: "+itcast.getId());
        System.out.println("用户名: "+itcast.getSubject());
        System.out.println("登录时间: "+new SimpleDateFormat("yyyy-MM-dd").format(itcast.getIssuedAt()) );
        System.out.println("过期时间: "+new SimpleDateFormat("yyyy-MM-dd").format(itcast.getExpiration()) );
        System.out.println("用户角色: "+itcast.get("role"));
    }
}
