package com.guli.video.test;


	import java.util.HashMap;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;


	public class Test2 {
	    public static void main(String[] args) {
	        Integer AppId = 1302227452;
	        String FileId = "5285890802664433271";
	        Integer CurrentTime = 1592493018;
	        Integer PsignExpire = 1593529818;
	        String UrlTimeExpire = "5ebe9423‬";
	        String Key = "YXI4LRSoQCziLlGfYgvd";
	        HashMap<String, String> urlAccessInfo = new HashMap<String, String>();
	        urlAccessInfo.put("t", UrlTimeExpire);

	        try {
	            Algorithm algorithm = Algorithm.HMAC256(Key);
	            String token = JWT.create().withClaim("appId", AppId).withClaim("fileId", FileId)
	                    .withClaim("currentTimeStamp", CurrentTime).withClaim("expireTimeStamp", PsignExpire)
	                    .withClaim("urlAccessInfo", urlAccessInfo).sign(algorithm);
	            System.out.println("token:" + token);
	        } catch (JWTCreationException exception) {
	            // Invalid Signing configuration / Couldn't convert Claims.
	        }
	    }
	}


