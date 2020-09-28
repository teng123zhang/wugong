package com.guli.ucenter.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


import com.guli.ucenter.entity.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {
	public static final String SUBJECT="guli";
	
	//密钥
	public static final String APPSECRET="guli";
	
	public static final long EXPIRE= 1000*60*30; //过期时间，毫秒，30分钟
	
	
	/**
	 * 根据对象生成jwt token
	 */
	
	public static String genJsonWebToken(Member member) {
		
		if(member==null || StringUtils.isEmpty(member.getId())
				|| StringUtils.isEmpty(member.getNickname())
				|| StringUtils.isEmpty(member.getAvatar())) {
			
			return null;
			
			
		}
		
		String token = Jwts.builder().setSubject(SUBJECT)
				
				  .claim("id", member.getId()) //向jwt设置肢体部分数据
				  .claim("nickname", member.getNickname())
				  .claim("avatar", member.getAvatar())
				  
				  .setIssuedAt(new Date()) //设置jwt的过期时间
				  .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
				  .signWith(SignatureAlgorithm.HS256, APPSECRET).compact(); //根据密钥对字符串进行加密，加密方式使用的是hs256
		
		return token;
	}
	
	/**
	 * 根据token获取内容
	 * @param args
	 */
	
	public static Claims checkJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
		
		return claims;
		
	}
	
	
	/**
     * 根据token字符串获取会员id
     * @param request
     * @return
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if(StringUtils.isEmpty(jwtToken)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("id");
    }
    
    
    //生成token字符串的方法
    public static String getJwtToken(String id, String nickname){

        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                .setSubject("guli-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

                .claim("id", id)  //设置token主体部分 ，存储用户信息
                .claim("nickname", nickname)

                .signWith(SignatureAlgorithm.HS256, APPSECRET)
                .compact();

        return JwtToken;
    }

	
	
	

}
