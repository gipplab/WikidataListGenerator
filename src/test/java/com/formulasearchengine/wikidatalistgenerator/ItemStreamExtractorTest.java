package com.formulasearchengine.wikidatalistgenerator;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;

/**
 * Created by Moritz on 16.12.2015.
 */
public class ItemStreamExtractorTest {

	@Test
	public void testExtract() throws Exception {
		InputStream is = getClass().getResourceAsStream( "sample-q1.json" );
		ItemStreamExtractor extractor = new ItemStreamExtractor( "en", false );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		extractor.extract( is, baos );
		assertEquals( "universe,Q1\n", baos.toString() );
	}
	@Test
	public void testExtractAliases() throws Exception {
		InputStream is = getClass().getResourceAsStream( "sample-q1.json" );
		ItemStreamExtractor extractor = new ItemStreamExtractor( "en", true );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		extractor.extract( is, baos );
		final String result = baos.toString();
		assertEquals( 6, result.split( "\n" ).length );
		assertThat(result, containsString("the universe"));
	}
}