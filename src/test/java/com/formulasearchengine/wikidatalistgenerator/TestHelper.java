package com.formulasearchengine.wikidatalistgenerator;

import java.io.File;
import java.io.IOException;

/**
 * Created by Moritz on 16.12.2015.
 */
public class TestHelper {
  public static String getSampleResourcePath() {
    return (new TestHelper()).getClass().getResource("sample-q1.json").getFile();
  }

  public static String getGzSampleResourcePath() {
    return (new TestHelper()).getClass().getResource("sample-q1.json.gz").getFile();
  }

  public static File getTempFile() throws IOException {
    return File.createTempFile("temp", Long.toString(System.nanoTime()));
  }
}
