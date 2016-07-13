/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus;

import java.io.IOException;

import org.eclipse.jdt.annotation.Nullable;
import org.pageseeder.berlioz.plus.exceptions.OutputException;
import org.pageseeder.xmlwriter.XMLWritable;
import org.pageseeder.xmlwriter.XMLWriter;

/**
* XML String Appender's methods
*/
public final class XMLPrinter implements XMLWriter {

  private final XMLWriter _xml;

  public XMLPrinter(XMLWriter xml) {
    this._xml = xml;
  }

  @Override
  public void writeXML(char[] text, int off, int len) throws IOException {
    try {
      this._xml.writeXML(text, off, len);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeText(char c) {
    try{
      this._xml.writeText(c);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeText(@Nullable String text) {
    try{
      this._xml.writeText(text);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeText(char[] text, int off, int len) {
    try{
      this._xml.writeText(text, off, len);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeCDATA(@Nullable String data) {
    try{
      this._xml.writeCDATA(data);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeXML(@Nullable String text) {
    try{
      this._xml.writeXML(text);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writeComment(@Nullable String comment) {
    try{
      this._xml.writeComment(comment);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void writePI(String target, String data) {
    try{
      this._xml.writePI(target, data);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void openElement(String name) {
    try{
      this._xml.openElement(name);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void openElement(String name, boolean hasChildren) {
    try{
      this._xml.openElement(name, hasChildren);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void openElement(String uri, String name, boolean hasChildren) {
    try{
      this._xml.openElement(uri, name, hasChildren);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void closeElement() {
    try{
      this._xml.closeElement();
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void element(String name, @Nullable String text) {
    try{
      this._xml.element(name, text);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void emptyElement(String element) {
    try{
      this._xml.emptyElement(element);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void emptyElement(String uri, String element) {
    try{
      this._xml.emptyElement(uri, element);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String name, String value) {
    try{
      this._xml.attribute(name, value);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String name, int value) {
    try {
      this._xml.attribute(name, value);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String uri, String name, String value) {
    try {
      this._xml.attribute(uri, name, value);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void attribute(String uri, String name, int value) {
    try{
      this._xml.attribute(uri, name, value);
    } catch (IOException | IllegalStateException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void setPrefixMapping(String uri, String prefix) {
    this._xml.setPrefixMapping(uri, prefix);
  }

  @Override
  public void xmlDecl() throws IOException {
    try {
      this._xml.xmlDecl();
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void setIndentChars(@Nullable String spaces) {
    this._xml.setIndentChars(spaces);
  }

  @Override
  public void flush() throws IOException {
    try {
      this._xml.flush();
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  @Override
  public void close() {
    try {
      this._xml.close();
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  // Additional methods
  // --------------------------------------------------------------------------

  public void writeXML(XMLWritable o) {
    try{
      o.toXML(this._xml);
    } catch (IOException ex) {
      throw new OutputException(ex);
    }
  }

  public void attribute(String name, Object value) {
    if (value != null) {
      try {
        this._xml.attribute(name, value.toString());
      } catch (IOException ex) {
        throw new OutputException(ex);
      }
    }
  }

  public void attribute(String uri, String name, Object value) {
    if (value != null) {
      try {
        this._xml.attribute(uri, name, value.toString());
      } catch (IOException | IllegalStateException ex) {
        throw new OutputException(ex);
      }
    }
  }

}
