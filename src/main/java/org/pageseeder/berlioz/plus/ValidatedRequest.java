/*
 * (c) Copyright LPCC Pty Ltd (Australia) 2016
 */
package org.pageseeder.berlioz.plus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jdt.annotation.Nullable;
import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.content.Environment;
import org.pageseeder.berlioz.content.Location;
import org.pageseeder.berlioz.plus.exceptions.InvalidParameterException;
import org.pageseeder.berlioz.plus.exceptions.MissingParameterException;
import org.pageseeder.berlioz.servlet.HttpContentRequest;

/**
 * Defines a content request which has been filtered for this application.
 */
public class ValidatedRequest {

  /**
   * The original wrapped request.
   */
  private final ContentRequest _req;

  /**
   * Create a new validated request wrapping a Berlioz content request.
   *
   * @param req The content request to wrap.
   */
  public ValidatedRequest(ContentRequest req) {
    this._req = req;
  }

  /**
   * Returns the dynamic path of the Berlioz request.
   *
   * <p>The Berlioz path corresponds to:
   * <ul>
   *   <li>the <code>pathInfo</code> when the Berlioz Servlet is mapped using a prefix servlet
   *   (for example <code>/html/*</code>);</li>
   *   <li>the <code>servletPath</code> when the Berlioz Servlet is mapped using a suffix servlet
   *   (for example <code>*.html</code>);</li>
   * </ul>
   *
   * <p>Use this method in preference to the {@link #getPathInfo()} which only works if Berlioz is
   * mapped to prefixes.
   *
   * @return The path information of this request.
   */
  public final String getBerliozPath() {
    return this._req.getBerliozPath();
  }

  /**
   * Returns the specified attribute object or <code>null</code>.
   *
   * @param name The name of the attribute.
   *
   * @return the specified attribute object or <code>null</code>.
   */
  @Nullable
  public final Object getAttribute(String name) {
    return this._req.getAttribute(name);
  }

  /**
   * Sets the specified attribute object or <code>null</code>.
   *
   * @param name The name of the attribute.
   * @param o    The object for this attribute.
   */
  public final void setAttribute(String name, Object o) {
    this._req.setAttribute(name, o);
  }

  /**
   * Returns an array containing all of the Cookie objects the client sent with this request.
   *
   * This method returns <code>null</code> if no cookies were sent.
   *
   * @return An array of all the Cookies included with this request,
   *         or <code>null</code> if the request has no cookies
   */
  @Nullable
  public Cookie[] getCookies() {
    // TODO We should return a list instead
    return this._req.getCookies();
  }

  /**
   * Returns the session of the wrapped HTTP servlet request.
   *
   * @return The session of the HTTP servlet request.
   */
  public HttpSession getSession() {
    return this._req.getSession();
  }

  /**
   * Returns the environment of the request.
   *
   * @return The environment of the request.
   */
  public Environment getEnvironment() {
    return this._req.getEnvironment();
  }

  /**
   * Returns information about the location of the request.
   *
   * <p>This includes information about the request URI.
   *
   * @return information about the location of the request.
   */
  public Location getLocation() {
    return this._req.getLocation();
  }

  /**
   * Sets the redirection URL.
   *
   * @param code The status code to use (required).
   * @param url  The URL to redirect to.
   *
   * @throws NullPointerException if the URL is <code>null</code>.
   * @throws IllegalArgumentException if the status is not a redirect status.
   */
  public void setRedirectURL(String url) {
    this._req.setRedirect(url, ContentStatus.TEMPORARY_REDIRECT);
  }

  /**
   * Return the specified parameter value as a string.
   *
   * @param parameter The parameter.
   *
   * @return The corresponding value or <code>null</code>
   *
   * @throws MissingParameterException if the parameter was <code>null</code>
   */
  public final String getParameter(String name) {
    String value = this._req.getParameter(name);
    if (value == null) throw new MissingParameterException(name);
    return value;
  }

  public final String getParameter(String parameter, String fallback) {
    String value = getOptionalParameter(parameter);
    return (value != null && value.length() > 0)? value : fallback;
  }

  /**
   * Return the specified parameter value as a string.
   *
   * @param parameter The parameter.
   *
   * @return The corresponding value or <code>null</code>
   *
   * @throws MissingParameterException if the parameter was <code>null</code>
   */
  public final String getString(RequestParameter parameter) {
    String name = parameter.getName();
    return getParameter(name);
  }

  public final String getString(RequestParameter parameter, String fallback) {
    String value = getOptionalString(parameter);
    return (value != null && value.length() > 0)? value : fallback;
  }

  /**
   * Return the specified parameter value as a string.
   *
   * @param parameter The parameter.
   *
   * @return The corresponding value or <code>null</code>
   */
  @Nullable
  public final String getOptionalParameter(String name) {
    return this._req.getParameter(name);
  }

  /**
   * Return the specified parameter value as a string.
   *
   * @param parameter The parameter.
   *
   * @return The corresponding value or <code>null</code>
   */
  @Nullable
  public final String getOptionalString(RequestParameter parameter) {
    return this._req.getParameter(parameter.getName());
  }

  /**
   * Return the specified Parameter as a long
   *
   * @param parameter The parameter name
   *
   * @return the corresponding long
   *
   * @throws MissingParameterException If the parameter was missing.
   * @throws InvalidParameterException If the value is not a long value
   */
  public final long getLong(RequestParameter parameter) {
    String value = getString(parameter);
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException ex) {
      throw new InvalidParameterException(parameter, ex);
    }
  }

  /**
   * Return the specified Parameter as a long
   *
   * @param parameter The parameter name
   *
   * @return the corresponding long
   *
   * @throws InvalidParameterException If the value is not a long value
   */
  public final long getLong(RequestParameter parameter, long fallback) {
    String value = getOptionalString(parameter);
    if (value == null || value.length() == 0) return fallback;
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException ex) {
      throw new InvalidParameterException(parameter, ex);
    }
  }

  /**
   * Return the specified Parameter as a positive long
   *
   * @param parameter The parameter name
   *
   * @return the corresponding positive long
   *
   * @throws MissingParameterException If the parameter was missing.
   * @throws InvalidParameterException If the value is not a long value greater than 0
   */
  public final long getPositiveLong(RequestParameter parameter) {
    String value = getString(parameter);
    try {
      long number = Long.parseLong(value);
      if (number > 0)
        return number;
      else
        throw new InvalidParameterException(parameter);
    } catch (NumberFormatException ex) {
      throw new InvalidParameterException(parameter, ex);
    }
  }

  /**
   * Returns the specified parameter as a local date.
   *
   * @param parameter The date parameter
   * @param fallback  the local date
   *
   * @return the local date parameter value.
   *
   * @throws InvalidParameterException If the value is not parsable as a local date
   */
  public final LocalDate getLocalDate(RequestParameter parameter, LocalDate fallback) {
    LocalDate date = fallback;
    try {
      String text = getOptionalString(parameter);
      if (text != null && text.length() > 0) {
        date = LocalDate.parse(text);
      }
    } catch (DateTimeParseException ex) {
      throw new InvalidParameterException(parameter, ex);
    }
    return date;
  }

  /**
   * Returns the specified parameter as a local date.
   *
   * @param parameter The date parameter
   *
   * @return the local date parameter value.
   *
   * @throws MissingParameterException If the parameter was missing.
   * @throws InvalidParameterException If the value is not parsable as a local date
   */
  public final LocalDate getLocalDate(RequestParameter parameter) {
    String text = getString(parameter);
    try {
      return LocalDate.parse(text);
    } catch (DateTimeParseException ex) {
      throw new InvalidParameterException(parameter, ex);
    }
  }

  /**
   * Returns the specified parameter as a local date.
   *
   * @param parameter The date parameter
   *
   * @return the local date parameter value.
   *
   * @throws InvalidParameterException If the value is not parsable as a local date
   */
  @Nullable
  public final LocalDate getOptionalLocalDate(RequestParameter parameter) {
    try {
      String date = getOptionalString(parameter);
      if (date != null && date.length() > 0)
       return LocalDate.parse(date);
    } catch (DateTimeParseException ex) {
      throw new InvalidParameterException(parameter, ex);
    }
    return null;
  }

  /**
   * Returns the specified parameter as a local datetime.
   *
   * @param parameter The date parameter
   *
   * @return the local datetime parameter value.
   *
   * @throws MissingParameterException If the parameter was missing.
   * @throws InvalidParameterException If the value is not parsable as a local datetime
   */
  public final LocalDateTime getLocalDateTime(RequestParameter parameter) {
    try {
      String datetime = getString(parameter);
      return LocalDateTime.parse(datetime);
    } catch (DateTimeParseException ex) {
      throw new InvalidParameterException(parameter, ex);
    }
  }

  /**
   * Returns the specified parameter as a local datetime.
   *
   * @param parameter The datetime parameter
   *
   * @return the local datetime parameter value.
   *
   * @throws InvalidParameterException If the value is not parsable as a local datetime
   */
  @Nullable
  public final LocalDateTime getOptionalLocalDateTime(RequestParameter parameter) {
    try {
      String datetime = getOptionalString(parameter);
      if (datetime != null && datetime.length() > 0)
       return LocalDateTime.parse(datetime);
    } catch (DateTimeParseException ex) {
      throw new InvalidParameterException(parameter, ex);
    }
    return null;
  }

  /**
   * Return a simple parameter map for this validated request.
   *
   * @return
   */
  public Map<String, String> getParameterMap() {
    List<String> names = Collections.list(this._req.getParameterNames());
    Map<String, String> parameters = new HashMap<>(names.size());
    for (String name : names) {
      String value = this._req.getParameter(name);
      if (value != null) {
        parameters.put(name, value);
      }
    }
    return parameters;
  }

  protected ContentRequest request() {
    return this._req;
  }

  // HTTP Objects
  // --------------------------------------------------------------------------

  /**
   * @return the original HTTP request
   */
  public final HttpServletRequest httpRequest() {
    return ((HttpContentRequest)this._req).getHttpRequest();
  }

  /**
   * @return the original HTTP response
   */
  public final HttpServletResponse httpResponse() {
    return ((HttpContentRequest)this._req).getHttpResponse();
  }

  /**
   * Adds a cookie to the response.
   *
   * @param cookie The cookie to add.
   */
  public final void addCookie(Cookie cookie) {
    HttpServletResponse res = ((HttpContentRequest)this._req).getHttpResponse();
    res.addCookie(cookie);
  }

}
