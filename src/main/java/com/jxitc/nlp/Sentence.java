package com.jxitc.nlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for sentence
 */
public class Sentence {
  private CoreMap coreMap;  // stanford coremap structure

  public List<Token> getTokens() {
    List<CoreLabel> coreLbls = coreMap.get(CoreAnnotations.TokensAnnotation.class);
    List<Token> tokens = new ArrayList<>();
    for (CoreLabel coreLbl : coreLbls) {
      tokens.add(Token.parse(coreLbl));
    }
    return tokens;
  }

  public Sentence(CoreMap coreMap) {
    this.coreMap = coreMap;
  }

  public String getRawString() {
    return coreMap.get(CoreAnnotations.TextAnnotation.class);
  }

}
