package com.jxitc.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.jxitc.nlp.ChineseStanford;
import com.jxitc.nlp.Token;
import org.apache.log4j.PropertyConfigurator;

import com.alibaba.fastjson.JSONObject;

/**
 * Nlp Servlet
 *
 * @author Xiao Jiang <jxitc@hotmail.com>
 * @date 24 May
 */
public class NlpServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  public static final String UTF8 = "UTF8";

  public NlpServlet() {
    super();
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    PropertyConfigurator.configure("/Users/xiao/.log4j.properties");
    System.out.println("Loading model...");
    ChineseStanford.getInstance().init();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String query = req.getParameter("query");
    req.setCharacterEncoding(UTF8);
    resp.setCharacterEncoding(UTF8);
    resp.setContentType("application/json");
    if (query == null || query.trim().isEmpty()) {
      resp.getWriter().write("{\"status\": \"error\"}");
    }
    JSONObject ret = new JSONObject();

    List<Token> rtnTokens = ChineseStanford.getInstance().parseSentence(query);

    ret.put("status", "success");
    ret.put("tokens", JSON.toJSON(rtnTokens));
    resp.getWriter().write(ret.toString());
  }

}
