package com.jxitc.servlet;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class StatusServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(StatusServlet.class.getSimpleName());
  private static final long serialVersionUID = 6436188605853517673L;
  private String succStr;
  private String failStr;
  private String hostName;

  public void init() throws ServletException {
    try {
      hostName = InetAddress.getLocalHost().getHostName();
    } catch (Exception e) {
    }
    Map<String, Object> status = new HashMap<>(1);
    status.put("status", "ok");
    status.put("app", "stanford-nlp");
    status.put("hostname", hostName);
    succStr = JSONObject.toJSONString(status);
    status = new HashMap<>(2);
    status.put("status", "error");
    status.put("message", "unknown");
    failStr = JSONObject.toJSONString(status);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    try {
      response.getWriter().write(succStr);
    } catch (Exception e) {
      Map<String, Object> status = new HashMap<>(2);
      status.put("status", "error");
      status.put("message", e.getMessage());
      status.put("app", "stanford");
      status.put("hostname", hostName);
      response.getWriter().write(JSONObject.toJSONString(status));
    }

  }
}
