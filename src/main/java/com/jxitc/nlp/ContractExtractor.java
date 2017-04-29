package com.jxitc.nlp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contract extractor
 */
public class ContractExtractor {
  public static final String JIAFANG = "jiafang";
  public static final String YIFANG = "yifang";
  public static final String ZULIN_DIZHI = "zulin_dizhi";
  public static final String JIANZHU_MIANJI = "jianzhu_mianji";
  public static final String ZULIN_MIANJI = "zulin_mianji";
  public static final String ZUQI = "zuqi";
  public static final String KAISHI_RIQI = "kaishi_riqi";
  public static final String JIESHU_RIQI = "jieshu_riqi";
  public static final String ZUJIN = "zujin";

  private static final Map<String, Pattern> RULES = new ConcurrentHashMap<>();
  static {
    RULES.put(JIAFANG, Pattern.compile("甲方\\(出租方\\)[:：](?<VAL>.+)($|\n)"));
    RULES.put(YIFANG, Pattern.compile("乙方\\(承租方\\)[:：](?<VAL>.+)($|\n)"));
    RULES.put(ZULIN_DIZHI, Pattern.compile("甲方合法拥有坐落于(?<VAL>.+房屋)，"));
    RULES.put(JIANZHU_MIANJI, Pattern.compile("建筑面积为[:：](?<VAL>[^平米]+)平米"));
    RULES.put(ZULIN_MIANJI, Pattern.compile("甲方同意将该承租单元(?<VAL>[^平米]+)平米出租给乙方"));
    RULES.put(ZUQI, Pattern.compile("本合同租赁期为[:：](?<VAL>[^年]+)年"));
    RULES.put(KAISHI_RIQI, Pattern.compile("年，即自(?<VAL>[^起]+)起至([^止]+)止。免租"));
    RULES.put(JIESHU_RIQI, Pattern.compile("年，即自([^起]+)起至(?<VAL>[^止]+)止。免租"));
    RULES.put(ZUJIN, Pattern.compile("税后租金\\)，共计(?<VAL>[^元]+)元"));
  }

  // singleton
  private ContractExtractor() {}
  private static ContractExtractor instance = new ContractExtractor();
  public static ContractExtractor getInstance() {return instance;}

  public Map<String, String> getVal(String line) {
    if (line == null || line.isEmpty()) {
      return null;
    }
    Map<String, String> rtn = new HashMap<>();
    for (Map.Entry<String, Pattern> entry : RULES.entrySet()) {
      String key = entry.getKey();
      Pattern rex = entry.getValue();
      Matcher m = rex.matcher(line);
      if (!m.find()) {
        continue;
      }
      rtn.put(key, m.group("VAL").trim().replace(" ", ""));
    }
    return rtn;
  }

  public static void main(String[] args) {
    List<String> lines = Arrays.asList(
       "甲方(出租方):世海地产有限公司\n",
            "乙方(承租方):九章科技有限公司\n",
            "甲方合法拥有坐落于北京市海淀区立方庭大厦 3 层 225 房屋，建筑面积为:65 平米(以下简称该房屋)。甲方同意将该承租单元 65 平米出租给乙方。\n",
            "3.1本合同租赁期为:3年，即自2012年3月31日起至 2015年3月31日止。免租装修期为1个月，即自2012年3月31日至2012年4月30日， 免租装修期间免收房屋租金，但乙方仍需按时向该房屋物业管理公司缴纳这期间的物业管理费、电费、水费等费用。\n"
    );

    for (String line : lines) {
      System.out.println("\n[processing] " + line);
      System.out.println(ContractExtractor.getInstance().getVal(line));
    }
  }

}
