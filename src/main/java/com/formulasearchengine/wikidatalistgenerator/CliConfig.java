package com.formulasearchengine.wikidatalistgenerator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import org.apache.commons.io.FilenameUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

/**
 * Created by Moritz on 16.12.2015.
 */
public final class CliConfig {
  @Parameter(names = {"-i", "--input"}, description = "path to the file with the wikidata json dump")
  protected String inFile;

  @Parameter(names = {"-o", "--output"}, description = "path to output file")
  protected String outFile;

  @Parameter(names = {"-l", "--language"}, description = "language to extract")
  protected String lang = "en";

  @Parameter(names = {"-a", "--aliases"})
  private boolean aliases = false;

  @Parameter(names = {"-d", "--descriptions"}, description = "Add a third column with the descriptions.")
  private boolean descriptions = false;

  @Parameter(names = {"-n", "--numericItems"}, description = "Omit the Q in the Wikidata ID.")
  private boolean numericItems = false;

  @Parameter(names = "--help", help = true)
  private boolean help = false;

  public static CliConfig from(String[] args) {
    CliConfig config = new CliConfig();
    JCommander commander = new JCommander();
    commander.addObject(config);
    commander.parse(args);
    if (config.help) {
      commander.usage();
      System.exit(0);
    }
    return config;
  }

  public boolean isAliases() {
    return aliases;
  }

  public String getLang() {
    return lang;
  }

  public OutputStream getOut() throws FileNotFoundException {
    if (outFile != null) {
      return new FileOutputStream(outFile);
    }
    return System.out;
  }

  public InputStream getIn() throws IOException {
    if (inFile != null) {
      FileInputStream fis = new FileInputStream(inFile);
      if ( FilenameUtils.getExtension(inFile).equals("gz")) {
        return new GZIPInputStream(fis);
      }
      return fis;
    }
    return System.in;
  }

  public boolean isDescriptions() {
    return descriptions;
  }

  public boolean isNumericItems() {
    return numericItems;
  }

  public CliConfig setAliases(boolean aliases) {
    this.aliases = aliases;
    return this;
  }

  public CliConfig setDescriptions(boolean descriptions) {
    this.descriptions = descriptions;
    return this;
  }

  public CliConfig setNumericItems(boolean numericItems) {
    this.numericItems = numericItems;
    return this;
  }
}
