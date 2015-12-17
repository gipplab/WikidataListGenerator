package com.formulasearchengine.wikidatalistgenerator;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Moritz on 16.12.2015.
 */
public class MainTest {

  @Test
  public void testMain() throws Exception {
    final String inFile = TestHelper.getSampleResourcePath();
    final File temp = TestHelper.getTempFile();
    final String outFile = temp.getAbsolutePath();
    String[] args = {"-i", inFile, "-o", outFile, "-l", "de", "-a"};
    Main.main(args);
    Scanner s = new Scanner(temp);
    List<String> lines = new ArrayList<>();
    while (s.hasNext()) {
      lines.add(s.next());
    }
    s.close();
    assertEquals(5, lines.size());
    assertEquals("universum,Q1", lines.get(0));
  }


  @Test
  public void testConstructor() throws Exception {
    // Only test default constructor
    new Main();
  }
}