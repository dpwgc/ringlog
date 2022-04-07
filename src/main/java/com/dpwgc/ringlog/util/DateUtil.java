package com.dpwgc.ringlog.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取当前时间
 */
@Component
public class DateUtil {

    public String getDateTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ");
        return sdf.format(date);
    }
}
