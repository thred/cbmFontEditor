package org.cbm.editor.font.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser implements Iterator<XMLParser>, Iterable<XMLParser>
{

    public static class XMLParserIterator implements Iterator<XMLParser>, Iterable<XMLParser>
    {

        private final List<Node> nodes;
        private int index = -1;

        public XMLParserIterator(final List<Node> nodes)
        {
            super();

            this.nodes = nodes;
        }

        /**
         * {@inheritDoc}
         *
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext()
        {
            return index < nodes.size() - 1;
        }

        /**
         * {@inheritDoc}
         *
         * @see java.util.Iterator#next()
         */
        @Override
        public XMLParser next()
        {
            index += 1;

            if (index >= nodes.size())
            {
                throw new NoSuchElementException();
            }

            return new XMLParser(nodes.get(index));
        }

        /**
         * {@inheritDoc}
         *
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Remove not supported");
        }

        /**
         * {@inheritDoc}
         *
         * @see java.lang.Iterable#iterator()
         */
        @Override
        public Iterator<XMLParser> iterator()
        {
            return this;
        }
    }

    private final Node root;

    public XMLParser(final File file) throws IOException
    {
        this(parse(file));
    }

    public XMLParser(final InputStream in) throws IOException
    {
        this(parse(in));
    }

    public XMLParser(final Reader reader) throws IOException
    {
        this(parse(reader));
    }

    public XMLParser(final Node root)
    {
        super();

        this.root = root;
    }

    public String getName()
    {
        return root.getNodeName();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<XMLParser> iterator()
    {
        List<Node> nodes = new ArrayList<>();
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i += 1)
        {
            Node node = childNodes.item(i);

            if (node.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            nodes.add(node);
        }

        return new XMLParserIterator(nodes);
    }

    public XMLParserIterator iterator(final String nodeName)
    {
        List<Node> nodes = new ArrayList<>();
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i += 1)
        {
            Node node = childNodes.item(i);

            if (node.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if (nodeName.equals(node.getNodeName()))
            {
                nodes.add(node);
            }
        }

        return new XMLParserIterator(nodes);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext()
    {
        Node node = root.getNextSibling();

        while (node != null)
        {
            if (Node.ELEMENT_NODE == node.getNodeType())
            {
                return true;
            }

            node = node.getNextSibling();
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Iterator#next()
     */
    @Override
    public XMLParser next()
    {
        Node node = root.getNextSibling();

        while (node != null)
        {
            if (Node.ELEMENT_NODE == node.getNodeType())
            {
                return new XMLParser(node);
            }

            node = node.getNextSibling();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove()
    {
        throw new UnsupportedOperationException("Remove not supported");
    }

    public XMLParser into(final String nodeName)
    {
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i += 1)
        {
            Node node = childNodes.item(i);

            if (node.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if (nodeName.equals(node.getNodeName()))
            {
                return new XMLParser(node);
            }
        }

        return null;
    }

    private static Node parse(final File file) throws IOException
    {
        final FileInputStream in = new FileInputStream(file);

        try
        {
            return parse(in);
        }
        finally
        {
            in.close();
        }
    }

    private static Node parse(final InputStream in) throws IOException
    {
        return parse(new InputSource(in));
    }

    private static Node parse(final Reader reader) throws IOException
    {
        return parse(new InputSource(reader));
    }

    private static Node parse(final InputSource source) throws IOException
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try
        {
            final DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(source);
        }
        catch (final SAXException e)
        {
            throw new IOException("Could not parse xml", e);
        }
        catch (final ParserConfigurationException e)
        {
            throw new IOException("Could not configure parser", e);
        }
    }

    public String attribute(final String name, final String defaultValue)
    {
        final NamedNodeMap map = root.getAttributes();
        String value = null;

        if (map != null)
        {
            final Node node = map.getNamedItem(name);

            if (node != null)
            {
                value = node.getNodeValue();
            }
        }

        return value != null ? value : defaultValue;
    }

    public int attribute(final String name, final int defaultValue)
    {
        final String value = attribute(name, null);

        if (value == null)
        {
            return defaultValue;
        }

        return Integer.decode(value);
    }

    public boolean attribute(final String name, final boolean defaultValue)
    {
        final String value = attribute(name, null);

        if (value == null)
        {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }

    public String text()
    {
        StringBuilder result = null;
        final NodeList list = root.getChildNodes();

        for (int i = 0; i < list.getLength(); i += 1)
        {
            final Node node = list.item(i);

            if (Node.TEXT_NODE == node.getNodeType() || Node.CDATA_SECTION_NODE == node.getNodeType())
            {
                if (result == null)
                {
                    result = new StringBuilder();
                }

                result.append(node.getNodeValue());
            }
        }

        return result != null ? result.toString().trim() : null;
    }

    public String textOf(final String nodeName)
    {
        XMLParser parser;

        final StringBuilder result = new StringBuilder();

        if ((parser = into(nodeName)) != null)
        {
            final String text = parser.text();

            if (text != null)
            {
                result.append(text);
            }
        }

        return result.toString();
    }

}
