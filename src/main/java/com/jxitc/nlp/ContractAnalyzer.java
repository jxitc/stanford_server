package com.jxitc.nlp;

import com.jxitc.servlet.ContractServlet;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contract analyzer
 */
public class ContractAnalyzer {
  private static final Logger logger = Logger.getLogger(ContractAnalyzer.class.getSimpleName());
  private static List<String> TARGET_NERS = Arrays.asList("MONEY", "DATE", "NUMBER");

  private static final List<Pattern> REX_LIST = Arrays.asList(
          Pattern.compile("^[甲乙]方.*[：:]"),
          Pattern.compile("[0-9一二三四五六七八九十]+(个工作日|个?[周天日月年])"),
          Pattern.compile("\\d\\d%"),
          Pattern.compile("\\d{6,10}"),
          Pattern.compile("^(户 *名|开户行|账号)[：:]")
  );

  private static final List<String> REX_EXCLUSION = Arrays.asList(
          "(^\\d\\.|（[0-9一二三四五六七八九十]+）|[0-9一二三四五六七八九十]、)",
          "[第共]\\d\\d?页",
          "[0-9一二三四五六七八九十]+(个工作日|个?[周天日月年])"
  );

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
    int i = -1;
    Token prevTok = null;
    for (Token tok : s.getTokens()) {
      i += 1;
      if (TARGET_NERS.contains(tok.ner)) {
        if (i == 1 && prevTok != null && "（".equals(prevTok.rawStr)) {
          continue;
        }
        if (i == 0) {
          continue;
        }
        logger.info(String.format("[important] ner: " + tok.ner));
        return true;
      }
      prevTok = tok;
    }

    // sentence level regex rules
    String sMatch = s.getRawString();
    for (String rep : REX_EXCLUSION) {
      sMatch = sMatch.replaceAll(rep, "");
    }
    for (Pattern rex : REX_LIST) {
      Matcher m = rex.matcher(sMatch);
      if (m.find()) {
        return true;
      }
    }

    return false;
  }

  public static void main(String[] args) {
    ChineseStanford.getInstance().init();
    String doc = "2. \n 开户行：中国银行\n甲方（需方）:\t九章原本科技有限公司\n乙方（供方）:\t哥生股份有限公司\n甲方至少在交货日期前四周下达正式订单给乙方并加盖甲方公章\n" +
            "（二）首笔订单，甲方向乙方的采购量应不少于10000只（计量单位以下简称为“Pcs”）整机产品。\n 第5页/共9页 \n （二）执行本协议期间，双方的正式通知（包括联络人员的变更）应以书面形式提前函告对方。 \n" +
            "（三）甲方每个月定期应向乙方提供至少滚动12周的需求预测给乙方，并按双方确认的所列长交期物料的采购周期下发正式备料通知。由于滚动预测取消或变更造成的物料呆滞、半成品、成品等费用和损失全部由甲方承担。针对长交期物料在乙方已按甲方要求提前备料并能满足甲方出货需求的前提下，甲方至少在交货日期前四周下达正式订单给乙方并";
    ContractAnalyzer analyzer = new ContractAnalyzer();
    System.out.println(analyzer.extractImportantSentences(doc));
  }

}
