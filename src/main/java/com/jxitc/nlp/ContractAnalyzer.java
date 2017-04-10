package com.jxitc.nlp;

import com.jxitc.servlet.ContractServlet;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contract analyzer
 */
public class ContractAnalyzer {
  private static final Logger logger = Logger.getLogger(ContractAnalyzer.class.getSimpleName());
  private static List<String> TARGET_NERS = Arrays.asList("MONEY", "DATE");

  public List<String> extractImportantSentences(String contractText) {
    List<Sentence> sentences = ChineseStanford.getInstance().parseDocument(contractText);
    List<String> rtn = new ArrayList<>();
    for (Sentence s : sentences) {
      if (isImportant(s)) {
        rtn.add(s.getRawString());
      }
    }
    return rtn;
  }

  private boolean isImportant(Sentence s) {
    for (Token tok : s.getTokens()) {
      if (TARGET_NERS.contains(tok.ner)) {
        logger.info(String.format("[important] ner: " + tok.ner));
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args) {
    ChineseStanford.getInstance().init();
    String doc = "100元如发生变更，应提前及时告知对方，否则责任方应承担因此所产生的全部责任。\n" +
            "（二）首笔订单，甲方向乙方的采购量应不少于10000只（计量单位以下简称为“Pcs”）整机产品。\n" +
            "（三）甲方每个月定期应向乙方提供至少滚动12周的需求预测给乙方，并按双方确认的所列长交期物料的采购周期下发正式备料通知。由于滚动预测取消或变更造成的物料呆滞、半成品、成品等费用和损失全部由甲方承担。针对长交期物料在乙方已按甲方要求提前备料并能满足甲方出货需求的前提下，甲方至少在交货日期前四周下达正式订单给乙方并";
    ContractAnalyzer analyzer = new ContractAnalyzer();
    System.out.println(analyzer.extractImportantSentences(doc));
  }

}
