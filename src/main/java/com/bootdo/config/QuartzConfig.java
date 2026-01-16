package com.bootdo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author L
 */
@AutoConfiguration
@EnableScheduling // 开启 Spring 自带的定时任务
@Slf4j
public class QuartzConfig {

}
