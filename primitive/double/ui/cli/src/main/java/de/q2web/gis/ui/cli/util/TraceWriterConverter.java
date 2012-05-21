package de.q2web.gis.ui.cli.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.beust.jcommander.IStringConverter;

/**
 * 
 * @author Oliver Schrenk <oliver.schrenk@q2web.de>
 */
public class TraceWriterConverter implements IStringConverter<Writer> {

	/*
	 * @see com.beust.jcommander.IStringConverter#convert(java.lang.String)
	 */
	@Override
	public Writer convert(final String argument) {

		try {
			return new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(argument), "UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			System.err.println("Error creating output file.\n" + e);
		} catch (final FileNotFoundException e) {
			System.err.println("Error creating output file.\n" + e);
		}

		return null;
	}
}