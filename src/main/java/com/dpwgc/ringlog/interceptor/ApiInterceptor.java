package com.dpwgc.ringlog.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dpwgc.ringlog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 接口访问拦截器
 */
@Component
public class ApiInterceptor implements HandlerInterceptor {

    @Value("${spring.data.elasticsearch.client.reactive.username}")
    String username;

    @Value("${spring.data.elasticsearch.client.reactive.password}")
    String password;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //前端页面放行
        if (request.getMethod().equals("GET")) {
            return true;
        }

        //在拦截器中设置允许跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers","*");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Max-Age","3600");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        String user = request.getHeader("username");
        String pwd = request.getHeader("password");

        //验证账户密码
        if (user.equals(username) && pwd.equals(password)) {
            return true;
        }
        returnErrorResponse(response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 身份验证出错
     * @param response 响应信息
     * @throws IOException
     */
    private void returnErrorResponse(HttpServletResponse response) throws IOException {
        ResultUtil<Object> resultUtil = new ResultUtil<>();
        resultUtil.setCode(440);
        resultUtil.setMsg("authentication error");
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/x-www-form-urlencoded");
            out = response.getOutputStream();
            out.write(JSONObject.toJSONString(resultUtil).getBytes(StandardCharsets.UTF_8));
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
