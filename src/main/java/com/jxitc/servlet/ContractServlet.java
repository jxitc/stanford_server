package com.jxitc.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxitc.nlp.ContractAnalyzer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Contract servlet
 */
public class ContractServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(ContractServlet.class.getSimpleName());

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String postStr = getPostAsString(req);
    JSONObject jObj = JSON.parseObject(postStr);
    String text = jObj.getString("text");
    String[] lines = text.split("\n");
    ContractAnalyzer ca = new ContractAnalyzer();
    JSONObject rtn = new JSONObject();
    rtn.put("status", "ok");
    rtn.put("important", new JSONArray());
    for (String line : lines) {
      for (String importantSen : ca.extractImportantSentences(line)) {
        rtn.getJSONArray("important").add(importantSen);
      }
    }
    resp.setCharacterEncoding("utf8");
    resp.setContentType("application/json; charset=UTF-8");
    resp.getWriter().write(JSON.toJSONString(rtn, true));
  }

  private static String getPostAsString(HttpServletRequest request) throws IOException {
    request.setCharacterEncoding("UTF8");
    StringBuilder sb = new StringBuilder();
    String line = null;
    BufferedReader br = request.getReader();
    while ((line = br.readLine()) != null) {
      sb.append(line);
    }
    return sb.toString();
  }

}
