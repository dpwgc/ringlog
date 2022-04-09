package com.dpwgc.ringlog.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dpwgc.ringlog.util.Md5Util;
import com.dpwgc.ringlog.util.MongodbUtil;
import com.dpwgc.ringlog.util.ResultUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 接口访问拦截器
 */
@Component
public class ApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

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

        String user = request.getHeader("user");
        String pwd = Md5Util.getMd5(request.getHeader("pwd"));  //MD5加密

        //验证用户身份
        BasicDBObject queryUser = new BasicDBObject();
        queryUser.put("user", user);
        queryUser.put("pwd", pwd);

        System.out.println(user);
        System.out.println(pwd);

        //查询用户
        List<DBObject> list = MongodbUtil.getDoc("user_info",queryUser);
        if(list.size() > 0){
            //验证通过
            return true;
        }
        //验证失败
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
            out.write(JSONObject.toJSONString(resultUtil).getBytes("utf-8"));
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
