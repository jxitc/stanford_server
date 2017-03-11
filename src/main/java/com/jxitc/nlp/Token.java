// Copyright 2017 Mobvoi Inc. All Rights Reserved.

package com.jxitc.nlp;

import com.alibaba.fastjson.annotation.JSONField;

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
}
