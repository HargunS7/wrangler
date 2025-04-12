/*
 * Copyright © 2024 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */



package io.cdap.wrangler.extension;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.ArrayList;
import java.util.List;

public class ParseByteSize implements Directive {

  private String column;
  private long byteValue;

  @Override
  public UsageDefinition define() {
    UsageDefinition.Builder builder = UsageDefinition.builder("parse-bytesize");
    builder.define("column", TokenType.COLUMN_NAME);
    builder.define("bytesize", TokenType.BYTE_SIZE);
    return builder.build();
  }

  @Override
  public void initialize(Arguments args) {
    this.column = ((ColumnName) args.value("column")).value();
    this.byteValue = ((ByteSize) args.value("bytesize")).getBytes();
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext ctx) {
    List<Row> results = new ArrayList<>();
    for (Row row : rows) {
      row.addOrSet(column + "_bytes", byteValue);
      results.add(row);
    }
    return results;
  }

  @Override
  public void destroy() {
    // No cleanup needed
  }
}
