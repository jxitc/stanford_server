package com.jxitc.nlp;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Jizhang Util
 */
public class Bookkeeper {
  private static final Logger logger = Logger.getLogger(Bookkeeper.class.getSimpleName());

  public static final String CAT_BAOXIAO = "baoxiao";
  public static final String CAT_XIAOSHOU = "xiaoshou";
  public static final String CAT_CAIGOU = "caigou";
  public static final String CAT_FUFEI = "fufei";

  // singleton
  private Bookkeeper() {}
  private static Bookkeeper instance = new Bookkeeper();
  public static Bookkeeper getInstance() {return instance;}

  public static class Record {
    public String category;
    public String detail;
    public double amount;
    public int count = 1;
    public String query;

    public String toString() {
      return JSON.toJSONString(this);
    }

  }

  public static final Map<String, Pattern> rules = new HashMap<>();

  static {
    rules.put(CAT_BAOXIAO, Pattern.compile("报销(?<DETAIL>.+费)用?(?<AMOUNT>\\d+)元"));
    rules.put(CAT_XIAOSHOU, Pattern.compile("销售(?<DETAIL>.+?)(?<AMOUNT>\\d+)元"));
    rules.put(CAT_CAIGOU, Pattern.compile("采购(?<DETAIL>.+?)(?<AMOUNT>\\d+)元"));
    rules.put(CAT_FUFEI, Pattern.compile("支付?(?<DETAIL>.+费)用?(?<AMOUNT>\\d+)元"));
  }

  public List<Record> parse(String query) {
    if (query == null || query.isEmpty()) {
      return null;
    }

    List<Record> rtn = new ArrayList<>();
    for (Map.Entry<String, Pattern> entry : rules.entrySet()) {
      String cat = entry.getKey();
      Pattern rex = entry.getValue();
      Matcher m = rex.matcher(query);
      if (!m.find()) {
        continue;
      }

      try {
        Record r = new Record();
        r.query = query;
        r.category = cat;
        r.detail = m.group("DETAIL");
        r.amount = Double.parseDouble(m.group("AMOUNT"));
        r.count = 1;  // TODO(xjiang): count

        rtn.add(r);
      } catch (Exception ex) {
        logger.error("Error to build record: " + ex);
      }

    }
    return rtn;
  }

  public static void main(String[] args) {
    System.out.println(Bookkeeper.getInstance().parse("蒋潇报销机票费用100元"));
    System.out.println(Bookkeeper.getInstance().parse("销售报纸300元"));
    System.out.println(Bookkeeper.getInstance().parse("采购西瓜、排骨200元"));
    System.out.println(Bookkeeper.getInstance().parse("支付手机费用1000元"));
  }
}
