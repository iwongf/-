package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class BaseServlet extends HttpServlet {
    @Override
   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       System.out.println("baseservlet的service方法被执行了");

       //完成方法分发

       //1.获取请求路径
       String uri = request.getRequestURI();
       System.out.println("请求uri:"+uri);//   /travel/user/add
       //2.获取方法名称
       String methodName = uri.substring(uri.lastIndexOf('/') + 1);
       System.out.println("方法名称:" + methodName);
       //3.获取方法对象Method
        //谁调用我?我代表谁
       System.out.println(this);//UserServlet的对象
           //Declare 忽略权限修饰符，获取方法
       try {
           //获取方法
           Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
           //4.执行方法
           //暴力反射
           //method.setAccessible(true);
           method.invoke(this,request,response);
       } catch (NoSuchMethodException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (InvocationTargetException e) {
           e.printStackTrace();
       }


   }

    /**
     * 直接将传入的对象序列化为Json，并且写回客户端
     *
     * @param obj
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), obj);
    }

    /**
     * 将传入的对象序列化为Json，返回给调用者
     *
     * @param obj
     * @return
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

}
