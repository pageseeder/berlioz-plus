# berlioz-plus

Java 8 annotations and extensions for Berlioz.

## `Generator` base class

Berlioz Plus provides a `ContentGenerator` abstract implementation to use as 
a base class. It extends content request in multiple ways:
 - it validates of the request
 - it allows custom request implementations
 - supports annotation-based validation
 - supplies an `XMLPrinter` implement `XMLWriter` without the `IOException`
 - requires a `ContentStatus` to be returned

The generator signature is simplified so that it becomes:

```java
   public ContentStatus generate(CustomRequest request, XMLPrinter xml);
```

## `ValidatedRequest`

The content request has been replaced by a `ValidatedRequest` object which wraps
the content request and can be used as a base class for custom requests. 

The validated request is similar to the content request, but is designed to make 
it more predictable when values are expected to return `null`.

By default, getter methods are expected to return a non-null object or throw a
 `RequestException` with the `BAD_REQUEST` content status. 
To return a nullable values, use "optional" getters.

For example, for request parameters, the validated request defines:
```java
  @NonNull  String getParameter(String) throws RequestException;
  @Nullable String getOptionalParameter(String);
```

Combined with annotations, this simplifies the code and error handling when
request don't include the correct parameters, request or session attributes.

## Custom requests

Creating custom requests can also simplify the code in generators for common 
operations on requests, such as retrieving application-specific values from 
the session or attributes on the request, by centralizing application-specific
business logic in the request that every generator receives.

To create custom requests, simply extend the `ValidatedRequest` as below: 

```java
public final class CustomRequest extends ValidatedRequest {

  public CustomRequest(ContentRequest request) {
    super(request);
  }

  // your custom code goes here

}
```

You can then create your custom generator base class as below by 
supplying a request factory. 

```java
/**
 * Base class for all generators in this application.
 */
public abstract class CustomGenerator extends Generator<TimesheetRequest> {

  /** Singleton pattern using enum */
  private enum Factory implements RequestFactory<CustomRequest> {

    INSTANCE;

    @Override
    public CustomRequest getRequest(ContentRequest request) {
      return new CustomRequest(request);
    }
  };

  public TimesheetGenerator() {
    super(Factory.INSTANCE);
  }

} 
```

This usually has to be setup only once per application.


## `RequestException` runtime exception

The `RequestException` is a `RuntimeException` used by Berlioz plus to flag any
issue with the request.

Berlioz Plus defines a number of implementations for common use cases such as:

 - `InvalidParameterException` when a parameter failed validity constraints
 - `MissingParameterException` when a required parameter was not available
 - `OutputException` when the output could not be written out

Request exceptions are designed to be extended and should generally NOT be caught
as they are handled by Berlioz plus automatically to return an appropriate error 
code.

For example, using `getLongParameter("id")" when the specified "id" parameter 
cannot be parsed as long will automatically throw an `InvalidParameterException`
and cause the generator to return a 400 (bad request) content status.

## Annotations

To avoid exceptions being thrown, Berlioz Plus defines repeatable annotations
which are used to validate the request. 

Annotations are automatically used to validate the request prior before it is 
processed, and can be used to provide more information and prevent the entire
service from failing.

The predefined annotations are:
 - `@Parameter` for string with an optional regular expression to match
 - `@EmailParameter` for parameter values expected to be valid email addresses
 - `@LongParameter` for parameter values castable as long with optional range
 - `@TemporalParameter` for Java 8 date and time classes (e.g. `LocalDate`) 


## Custom annotations

Berlioz Plus provides a pluggable mechanism to define annotations to validate a request.

To create a custom annotation, ensure that the retention policy is `RUNTIME` and the 
target element is `TYPE`. It is also a good idea to make it a documented annotation.

For example to create an annotation, requiring a user to be logged in:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Authenticated {

}
```

Next, create a `Constraint` implementation that will receive a `ContentRequest`
and an `XMLPrinter` to customize the reponse.

For example:

```java
public enum AuthenticatedConstraint implements Constraint {

  INSTANCE;

  @Override
  public ContentStatus validate(ContentRequest req, XMLPrinter xml) {
    HttpSession session = req.getSession();
    Object user = session.getAttribute(Sessions.USER_ATTRIBUTE);
    if (user instanceof OAuthUser) return ContentStatus.OK;
    else return ContentStatus.FORBIDDEN;
  }

}
```

Finally, create a custom annotation processor to associate the annotation
to the constraint:

```java
public final class CustomAnnotationProcessor implements AnnotationProcessor {

  @Override
  public boolean accepts(Annotation annotation) {
    return (annotation instanceof Authenticated);
  }

  @Override
  public Constraint getConstraint(Annotation annotation) {
    if (annotation instanceof Authenticated) return AuthenticatedConstraint.INSTANCE;
    else throw new IllegalArgumentException();
  }

}
```

The `AnnotationProcessor` is a SPI, so you also need to specify it in a file
`META-INF/services/org.pageseeder.berlioz.plus.spi.AnnotationProcessor`, with
the value:
```
org.example.app.CustomAnnotationProcessor
```

Note that for convenience, you can define the annotation and annotation processor as
inner classes of your constraint:

```java
public enum CustomConstraint implements Constraint {

  public ContentStatus validate(ContentRequest req, XMLPrinter xml) { ... }

  public @interface Custom { ... }
  
  public class Processor implements AnnotationProcessor { ... }

}
```

## `RequestParameter` interface

Berlioz Plus defines a `RequestParameter` interface that can be used in
validated request. 

Usually, it is best implemented as an enum to ensure consistency of 
parameter usage.



