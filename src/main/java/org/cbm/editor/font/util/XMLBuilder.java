/**********************************************************************
 * Created on 24.09.2007 Copyright (c) 2004 Porsche Informatik, GmbH. All Rights Reserved.
 **********************************************************************/
package org.cbm.editor.font.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/**
 * Utility class to quickly build DOMs
 * 
 * @author Manfred Hantschel
 */
public class XMLBuilder
{

	private static final String DEFAULT_ENCODING = "UTF-8";

	protected Document document = null;
	protected Node root = null;
	protected Node node = null;

	/**
	 * Initializes the builder and creates a new document
	 * 
	 * @throws IllegalStateException if the parser could not be configured
	 */
	public XMLBuilder() throws IllegalStateException
	{
		super();

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try
		{
			builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			throw new IllegalStateException("Failed to configure parser");
		}

		document = builder.newDocument();
		root = document;
		node = document;
	}

	/**
	 * Initialized the generator and uses the specified document
	 * 
	 * @param document the document
	 * @param rootNode the node to attach all other nodes to
	 */
	public XMLBuilder(final Node root)
	{
		super();

		document = root.getOwnerDocument();
		this.root = root;
		node = root;
	}

	/**
	 * Returns the document
	 * 
	 * @return the document
	 */
	public Document document()
	{
		return document;
	}

	/**
	 * Adds a processing instruction for the stylesheet
	 * 
	 * @param link the link to the stylesheet
	 */
	public XMLBuilder stylesheet(final String link)
	{
		final ProcessingInstruction pi = document.createProcessingInstruction("xml-stylesheet",
				"type=\"text/xml\" href=\"" + link + "\"");

		document.insertBefore(pi, document.getFirstChild());

		return this;
	}

	/**
	 * Returns the root node
	 * 
	 * @return the root node
	 */
	public Node root()
	{
		return root;
	}

	/**
	 * Returns the currently active node
	 * 
	 * @return the currently active node
	 */
	public Node node()
	{
		return node;
	}

	/**
	 * Begins a new element and adds it at the current position
	 * 
	 * @param name the name of the element
	 */
	public XMLBuilder begin(final String name)
	{
		final Element element = document.createElement(name);

		node.appendChild(element);
		node = element;

		return this;
	}

	/**
	 * Adds an element to the current element
	 * 
	 * @param name the name of the attribute
	 * @param value the value of the attribute
	 */
	public XMLBuilder attribute(final String name, final Object value)
	{
		if (value != null)
		{
			((Element) node).setAttribute(name, String.valueOf(value));
		}

		return this;
	}

	/**
	 * Adds text to the current element
	 * 
	 * @param text the text
	 */
	public XMLBuilder text(final Object text)
	{
		if (text != null)
		{
			node.appendChild(document.createTextNode(String.valueOf(text)));
		}

		return this;
	}

	public XMLBuilder comment(final Object comment)
	{
		if (comment != null)
		{
			node.appendChild(document.createComment(String.valueOf(comment)));
		}

		return this;
	}

	/**
	 * Adds an element with the specified name that contains the specified text
	 * 
	 * @param name the name
	 * @param text the text
	 */
	public XMLBuilder element(final String name, final Object text)
	{
		begin(name);
		text(text);
		end();

		return this;
	}

	/**
	 * Adds an element with the specified name and exactly one attribute. The element contains no text.
	 * 
	 * @param name the name
	 * @param attrName the name of the attribute
	 * @param attrValue the value of the attribute
	 */
	public XMLBuilder element(final String name, final String attrName, final Object attrValue)
	{
		begin(name);
		attribute(attrName, attrValue);
		end();

		return this;
	}

	/**
	 * Adds an element with the specified name and exactly one attribute. The element contains the specified text.
	 * 
	 * @param name the name
	 * @param attrName the name of the attribute
	 * @param attrValue the value of the attribute
	 * @param text the text
	 */
	public XMLBuilder element(final String name, final String attrName, final Object attrValue, final Object text)
	{
		begin(name);
		attribute(attrName, attrValue);
		text(text);
		end();

		return this;
	}

	/**
	 * Ends the current node and steps one node back
	 */
	public XMLBuilder end()
	{
		node = node.getParentNode();

		return this;
	}

	/**
	 * Closes the current node and removes it from its parent
	 */
	public XMLBuilder remove()
	{
		final Node currentNode = node;

		end();

		node.removeChild(currentNode);

		return this;
	}

	public void toFile(final File file) throws IOException
	{
		final FileOutputStream out = new FileOutputStream(file);

		try
		{
			toStream(out);
		}
		finally
		{
			out.close();
		}
	}

	public void toStream(final OutputStream out) throws IOException
	{
		try
		{
			toResult(new StreamResult(out), DEFAULT_ENCODING);
		}
		catch (final TransformerException e)
		{
			throw new IOException("Failed to write document");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return toString(DEFAULT_ENCODING);
	}

	public String toString(final String encoding)
	{
		String result = null;

		try
		{
			final StringWriter writer = new StringWriter();

			toWriter(writer, encoding);

			result = writer.toString();
		}
		catch (final TransformerException e)
		{
			throw new RuntimeException("Could not transform document", e);
		}

		return result;
	}

	public XMLBuilder toWriter(final Writer writer, final String encoding) throws TransformerException
	{
		return toResult(new StreamResult(writer), encoding);
	}

	public XMLBuilder toResult(final Result result, final String encoding) throws TransformerException
	{
		final Transformer transformer = TransformerFactory.newInstance().newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setParameter(OutputKeys.ENCODING, encoding);

		final DOMSource source = new DOMSource(document);

		transformer.transform(source, result);

		return this;
	}

}
