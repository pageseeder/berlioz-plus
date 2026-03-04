package org.pageseeder.berlioz.plus.test;

import org.jspecify.annotations.Nullable;
import org.pageseeder.berlioz.content.ContentRequest;
import org.pageseeder.berlioz.content.ContentStatus;
import org.pageseeder.berlioz.content.Environment;
import org.pageseeder.berlioz.content.Location;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.*;

public final class MapBackedContentRequest implements ContentRequest {

  private final Map<String, String> params;

  private final Map<String, Object> attributes = new HashMap<>();

  private ContentStatus status = ContentStatus.OK;

  public MapBackedContentRequest(Map<String, String> params) {
    this.params = new HashMap<>(params);
  }

  @Override
  public @Nullable String getParameter(String name) {
    return this.params.get(name);
  }

  public String getBerliozPath() {
    return "";
  }

  public String getParameter(String name, String def) {
    return this.params.getOrDefault(name, def);
  }

  public int getIntParameter(String name, int def) {
    return Integer.parseInt(getParameter(name, Integer.toString(def)));
  }

  public long getLongParameter(String name, long def) {
    return Long.parseLong(getParameter(name, Long.toString(def)));
  }

  public String @Nullable[] getParameterValues(String name) {
    return this.params.get(name) == null ? null : this.params.get(name).split(",");
  }

  public Enumeration<String> getParameterNames() {
    return Collections.enumeration(this.params.keySet());
  }

  public @Nullable Object getAttribute(String name) {
    return attributes.get(name);
  }

  public void setAttribute(String name, Object o) {
    this.attributes.put(name, o);
  }

  public Date getDateParameter(String name) {
    return null;
  }

  public Cookie @Nullable[] getCookies() {
    return null;
  }

  public @Nullable HttpSession getSession() {
    return null;
  }

  public Environment getEnvironment() {
    return null;
  }

  /**
   * Returns information about the location of the request.
   *
   * <p>This includes information about the request URI.
   *
   * @return information about the location of the request.
   */
  public Location getLocation() {
    return null;
  }

  public void setStatus(ContentStatus code) {
    this.status = code;
  }

  public void setRedirect(String url, ContentStatus code) {
    this.status = code;
  }

  public ContentStatus getStatus() {
    return status;
  }
}
