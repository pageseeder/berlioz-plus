/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus;

import java.io.IOException;

import org.jspecify.annotations.Nullable;
import org.pageseeder.berlioz.plus.exceptions.OutputException;
import org.pageseeder.xmlwriter.XMLWritable;
import org.pageseeder.xmlwriter.XMLWriter;

/**
* XML String Appender's methods
*/
public final class XMLPrinter implements XMLWriter {

  private final XMLWriter xml;

  public XMLPrinter(XMLWriter xml) {
    this.xml = xml;
  }

  @Override
  public void writeXML(char[] text, int off, int len) {
    try {
      this.xml.writeXML(text, off, len);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeText(char c) {
    try{
      this.xml.writeText(c);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeText(@Nullable String text) {
    try{
      this.xml.writeText(text);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeText(char[] text, int off, int len) {
    try{
      this.xml.writeText(text, off, len);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeCDATA(String data) {
    try{
      this.xml.writeCDATA(data);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeXML(@Nullable String text) {
    try{
      this.xml.writeXML(text);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeComment(String comment) {
    try {
      this.xml.writeComment(comment);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writePI(String target, String data) {
    try{
      this.xml.writePI(target, data);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void openElement(String name) {
    try{
      this.xml.openElement(name);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void openElement(String name, boolean hasChildren) {
    try{
      this.xml.openElement(name, hasChildren);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void openElement(String uri, String name, boolean hasChildren) {
    try{
      this.xml.openElement(uri, name, hasChildren);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void closeElement() {
    try{
      this.xml.closeElement();
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void element(String name, String text) {
    try{
      this.xml.element(name, text);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void emptyElement(String element) {
    try{
      this.xml.emptyElement(element);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void emptyElement(String uri, String element) {
    try{
      this.xml.emptyElement(uri, element);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String name, String value) {
    try{
      this.xml.attribute(name, value);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String name, int value) {
    try {
      this.xml.attribute(name, value);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String name, long value) {
    try {
      this.xml.attribute(name, value);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String uri, String name, String value) {
    try {
      this.xml.attribute(uri, name, value);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String uri, String name, int value) {
    try{
      this.xml.attribute(uri, name, value);
    } catch (IOException | IllegalStateException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String uri, String name, long value) {
    try{
      this.xml.attribute(uri, name, value);
    } catch (IOException | IllegalStateException ex) {
      throw new OutputException(ex);
    }
  }


  @Override
  public void setPrefixMapping(String uri, String prefix) {
    this.xml.setPrefixMapping(uri, prefix);
  }

  @Override
  public void xmlDecl() {
    try {
      this.xml.xmlDecl();
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void setIndentChars(@Nullable String spaces) {
    this.xml.setIndentChars(spaces);
  }

  @Override
  public void flush() {
    try {
      this.xml.flush();
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void close() {
    try {
      this.xml.close();
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  // Additional methods
  // --------------------------------------------------------------------------

  /**
   * Writes the provided object as XML content to the underlying writer.
   *
   * <p>This method delegates the responsibility of generating the XML representation
   * of the object to the {@code toXML} method of the {@link XMLWritable} instance.
   *
   * @param o The object implementing {@link XMLWritable} to be written as XML.
   *          Must not be {@code null}.
   * @throws OutputException If an {@link IOException} occurs while writing the XML content.
   */
  public void writeXML(XMLWritable o) {
    try{
      o.toXML(this.xml);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  /**
   * Adds an attribute with the specified name and value to the XML element being constructed.
   * If the provided value is {@code null}, no attribute will be added.
   * In case of an {@link IOException}, an {@link OutputException} is thrown.
   *
   * @param name The name of the attribute to add. Must not be {@code null}.
   * @param value The value of the attribute. If {@code null}, no action is performed.
   *
   * @throws OutputException If an {@link IOException} occurs while adding the attribute.
   */
  public void attribute(String name, @Nullable Object value) {
    if (value != null) {
      try {
        this.xml.attribute(name, value.toString());
      } catch (IOException ex) {
        throw new OutputException(ex);
      }
    }
  }

  /**
   * Adds an attribute with the specified namespace URI, name, and value to the XML element being constructed.
   * If the provided value is {@code null}, no attribute will be added.
   * In case of an {@link IOException} or {@link IllegalStateException}, an {@link OutputException} is thrown.
   *
   * @param uri The namespace URI of the attribute to add. Must not be {@code null}.
   * @param name The name of the attribute to add. Must not be {@code null}.
   * @param value The value of the attribute. If {@code null}, no attribute will be added.
   *
   * @throws OutputException If an {@link IOException} or {@link IllegalStateException} occurs while adding the attribute.
   */
  public void attribute(String uri, String name, @Nullable Object value) {
    if (value != null) {
      try {
        this.xml.attribute(uri, name, value.toString());
      } catch (IOException | IllegalStateException ex) {
        throw new OutputException(ex);
      }
    }
  }

}
