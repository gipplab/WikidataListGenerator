package com.formulasearchengine.wikidatalistgenerator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static junit.framework.Assert.*;

/**
 * Created by Moritz on 16.12.2015.
 */
public class CliConfigTest {
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void testFromEmpty() throws Exception {
		String[] args = {};
		CliConfig cfg = CliConfig.from( args );
		assertFalse(cfg.isAliases());
		assertEquals( "en", cfg.getLang() );
		assertEquals( System.in, cfg.getIn() );
		assertEquals( System.out, cfg.getOut() );
	}

	@Test
	public void testAllParam() throws Exception {
		final String inFile = TestHelper.getSampleResourcePath();
		final File temp = TestHelper.getTempFile();
		final String outFile = temp.getAbsolutePath();
		String[] args = {"-i", inFile ,"-o", outFile, "-l", "de", "-a"};
		CliConfig cfg = CliConfig.from( args );
		assertTrue( cfg.isAliases() );
		assertEquals( "de", cfg.getLang() );
		// TODO: Update to org.hamcrest.core.IsInstanceOf
		assertTrue( cfg.getOut() instanceof FileOutputStream );
		assertTrue( cfg.getIn() instanceof FileInputStream );
	}

	@Test
	public void testHelp() {
		exit.expectSystemExitWithStatus(0);
		String[] args = { "--help" };
		CliConfig.from( args );
	}
}