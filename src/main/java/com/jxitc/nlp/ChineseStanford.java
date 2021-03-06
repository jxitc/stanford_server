package com.jxitc.nlp;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Chinese Stanford Util
 *
 * @author Xiao Jiang, <jxitc@homtail.com>
 * @date 17/3/11
 */
public class ChineseStanford {
  private static final Logger logger = Logger.getLogger(ChineseStanford.class.getSimpleName());

  private StanfordCoreNLP corenlp = null;

  // singleton
  private ChineseStanford() {}
  private static ChineseStanford instance = new ChineseStanford();
  public static ChineseStanford getInstance() {return instance;}

  public void init() {
    logger.info("Start loading model...");
    this.corenlp = new StanfordCoreNLP("xjiang-stanford-chinese.properties");
    logger.info("Load model succeeded!");
  }

  /**
   * Parse single sentence
   */
  public List<Token> parseSentence(String sentence) {
    logger.info("[parse sentence] " + sentence);
    Annotation document = new Annotation(sentence);
    this.corenlp.annotate(document);
    List<CoreLabel> coreLbls = document.get(CoreAnnotations.TokensAnnotation.class);
    List<Token> rtn = new ArrayList<>();
    for (CoreLabel coreLbl : coreLbls) {
      rtn.add(Token.parse(coreLbl));
    }
    return rtn;
  }

  public List<Sentence> parseDocument(String docString){
    Annotation annotation = new Annotation(docString);
    this.corenlp.annotate(annotation);
    List<CoreMap> coreMaps = annotation.get(CoreAnnotations.SentencesAnnotation.class);
    List<Sentence> sentences = new ArrayList<>();
    for (CoreMap cm : coreMaps) {
      sentences.add(new Sentence(cm));
    }
    logger.info(String.format("Parsed to %d sentences", sentences.size()));
    return sentences;
  }

  public static void main(String[] args) {
    ChineseStanford.getInstance().init();
    String text = "克林顿说，华盛顿将逐步落实对韩国的经济援助。"
            + "金大中对克林顿的讲话报以掌声：克林顿总统在会谈中重申，他坚定地支持韩国摆脱经济危机。";
    ChineseStanford.getInstance().parseSentence(text);
  }
}
