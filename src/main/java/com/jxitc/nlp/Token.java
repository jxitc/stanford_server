// Copyright 2017 Mobvoi Inc. All Rights Reserved.

package com.jxitc.nlp;

import com.alibaba.fastjson.annotation.JSONField;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * Data structure for token
 *
 * @author Xiao Jiang, <jxitc@hotmail.com>
 * @date 17/3/11
 */
public class Token {
  @JSONField(name="raw_str")
  public String rawStr;

  @JSONField(name="start_index")
  public int startIndex;

  @JSONField(name="end_index")
  public int endIndex;

  public String pos;

  public String ner;

  public static Token parse(CoreLabel coreLbl) {
    Token token = new Token();
    token.rawStr = coreLbl.getString(CoreAnnotations.TextAnnotation.class);
    token.pos = coreLbl.getString(CoreAnnotations.PartOfSpeechAnnotation.class);
    token.ner = coreLbl.getString(CoreAnnotations.NamedEntityTagAnnotation.class);
    token.startIndex = coreLbl.beginPosition();
    token.endIndex = coreLbl.endPosition();
    return token;
  }
}
