package com.formulasearchengine.wikidatalistgenerator;

/**
 * Created by Moritz on 16.12.2015.
 */
public class Main {
  public static void main(String[] args) throws Exception {
    CliConfig config = CliConfig.from(args);
    final ItemStreamExtractor extractor = new ItemStreamExtractor(config.getLang(), config.isAliases());
    extractor.extract(config.getIn(), config.getOut());
  }

}
