package com.rc.gmall2020.manage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GwareConst {

    @Value("${order.split.url:novalue}")
    public static String ORDER_SPLIT_URL;
}