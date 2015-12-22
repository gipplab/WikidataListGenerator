package com.formulasearchengine.wikidatalistgenerator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moritz on 16.12.2015.
 */
public class ItemStreamExtractor {
  final JsonFactory factory = new JsonFactory();
  final String language;
  final boolean useAlias;
  final boolean useDescriptions;
  final boolean useNumeric;
  String ItemId;


  public ItemStreamExtractor(String language, boolean useAlias) {
    this.language = language;
    this.useAlias = useAlias;
    useDescriptions = false;
    useNumeric = false;
  }

  public ItemStreamExtractor(CliConfig config) {
    this.language = config.getLang();
    this.useAlias = config.isAliases();
    useDescriptions = config.isDescriptions();
    useNumeric = config.isNumericItems();
  }

  public void extract(InputStream in, OutputStream out) throws IOException {
    JsonParser jParser = factory.createParser(in);
    if (jParser.nextToken() != JsonToken.START_ARRAY) {
      emitError("Expected a JSON array. Unexpected token " + jParser.getText());
    }
    while (jParser.nextToken() != JsonToken.END_ARRAY) {
      if (jParser.getCurrentToken() == JsonToken.START_OBJECT) {
        processItem(jParser, out);
      } else {
        emitError("Unexpected token " + jParser.getText());
      }
    }
    out.flush();
  }

  private void processItem(JsonParser jParser, OutputStream out) throws IOException {
    String id = null;
    // We can not expect that the id attribute comes first, so we have to store all titles
    // until the whole entry is read
    List<String> titles = new ArrayList<>();
    Boolean item = false;
    while (jParser.nextToken() != JsonToken.END_OBJECT) {
      if (jParser.getCurrentToken() != JsonToken.FIELD_NAME) {
        emitError("expects field name on top level object");
      }
      switch (jParser.getCurrentName()) {
        case "id":
          jParser.nextToken();
          id = jParser.getValueAsString();
          if (useNumeric) {
            id = id.replace("Q", "").trim();
          }
          break;
        case "labels":
          jParser.nextToken();
          if (jParser.getCurrentToken() == JsonToken.START_OBJECT) {
            jParser.nextToken();
            extractLabel(jParser, titles);
          } else {
            emitError("expects object start after labels");
          }
          break;
        case "type":
          jParser.nextToken();
          if ("item".equals(jParser.getValueAsString())) {
            item = true;
          }
          break;
        case "aliases":
          if (useAlias) {
            jParser.nextToken();
            if (jParser.getCurrentToken() == JsonToken.START_OBJECT) {
              jParser.nextToken();
              extractAlias(jParser, titles);
            } else {
              emitError("expects object start after aliases");
            }
            break;
          }
        default:
          jParser.nextToken();
          if (jParser.getCurrentToken().isStructStart()) {
            skipBlock(jParser);
          }
          // Skip value
      }
    }
    if (item && id != null) {
      OutputStreamWriter w = new OutputStreamWriter(out);
      CSVPrinter printer = CSVFormat.DEFAULT.withRecordSeparator("\n").print(w);
      for (String t : titles) {
        String[] output = {t.toLowerCase().trim(), id};
        printer.printRecord(output);
      }
      w.flush();
    }
  }

  private void extractLabel(JsonParser jParser, List<String> titles) throws IOException {
    if (jParser.getCurrentToken() == JsonToken.END_OBJECT) {
      return;
    }
    if (jParser.getCurrentToken() != JsonToken.FIELD_NAME) {
      emitError("expects field name on top level object");
    }
    if (language.equals(jParser.getCurrentName())) {
      jParser.nextToken();
      if (jParser.getCurrentToken() == JsonToken.START_OBJECT) {
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
          if (jParser.getCurrentName().equals("value")) {
            jParser.nextToken();
            titles.add(jParser.getValueAsString());
          }
        }
      } else {
        emitError("language is expected to be an object");
      }
    } else {
      jParser.nextToken();
      skipBlock(jParser);
    }
    jParser.nextToken();
    extractLabel(jParser, titles);
  }

  private void extractAlias(JsonParser jParser, List<String> titles) throws IOException {
    if (jParser.getCurrentToken() == JsonToken.END_OBJECT) {
      return;
    }
    if (jParser.getCurrentToken() != JsonToken.FIELD_NAME) {
      emitError("expects field name on top level object");
    }
    if (language.equals(jParser.getCurrentName())) {
      jParser.nextToken();
      if (jParser.getCurrentToken() == JsonToken.START_ARRAY) {
        while (jParser.nextToken() != JsonToken.END_ARRAY) {
          while (jParser.nextToken() != JsonToken.END_OBJECT) {
            if (jParser.getCurrentName().equals("value")) {
              jParser.nextToken();
              titles.add(jParser.getValueAsString());
            }
          }
        }
      } else {
        emitError("aliases is expected to be an array");
      }
    } else {
      jParser.nextToken();
      skipBlock(jParser);
    }
    jParser.nextToken();
    extractAlias(jParser, titles);
  }

  private void skipBlock(JsonParser jParser) throws IOException {
    if (jParser.getCurrentToken() == JsonToken.START_OBJECT) {
      skipStruct(jParser, JsonToken.START_OBJECT);
    } else if (jParser.getCurrentToken() == JsonToken.START_ARRAY) {
      skipStruct(jParser, JsonToken.START_ARRAY);
    } else {
      emitError("Unexpected token " + jParser.getText());
    }
  }

  private void skipStruct(JsonParser jParser, JsonToken type) throws IOException {
    JsonToken endToken;
    if (jParser.getCurrentToken() != type) {
      emitError("skip struct does not match current struct");
    }
    if (type == JsonToken.START_ARRAY) {
      endToken = JsonToken.END_ARRAY;
    } else {
      endToken = JsonToken.END_OBJECT;
    }
    while (jParser.nextToken() != endToken) {
      if (jParser.getCurrentToken() == JsonToken.START_OBJECT) {
        skipStruct(jParser, JsonToken.START_OBJECT);
      }
      if (jParser.getCurrentToken() == JsonToken.START_ARRAY) {
        skipStruct(jParser, JsonToken.START_ARRAY);
      }
    }
  }

  private void emitError(String message) throws IOException {
    throw new IOException(message);
  }
}
