package com.jxitc.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jxitc.nlp.Bookkeeper;
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
public class BookkeeperServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(BookkeeperServlet.class.getSimpleName());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF8");
    String query = req.getParameter("query");
    logger.info("Receiving query: " + query);
    resp.setContentType("application/json; charset=UTF-8");
    resp.setCharacterEncoding("UTF8");

    JSONObject rtn = new JSONObject();
    rtn.put("status", "success");
    rtn.put("records", Bookkeeper.getInstance().parse(query));

    resp.getWriter().write(rtn.toJSONString());
  }
}
