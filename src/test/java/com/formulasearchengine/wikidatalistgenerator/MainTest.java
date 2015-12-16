package com.formulasearchengine.wikidatalistgenerator;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
		Main.main( args );
		List<String> lines = Files.readAllLines( Paths.get( outFile ) );
		assertEquals( 5, lines.size() );
		assertEquals( "universum,Q1", lines.get( 0 ) );
	}


}