# ![JarSpec](http://hgc.io/jarspec/images/logo.svg)

## What?
[RSpec](http://rspec.info/) (or [Jasmine](http://jasmine.github.io/2.0/introduction.html)/[Mocha](http://visionmedia.github.io/mocha/)) style tests for native Java 8, a bit like the following:

```java
@RunWith(JarSpecJUnitRunner.class)
public class MinimalSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("This specification", () ->
            it("contains a statement with a test", () -> assertTrue(true))
        );
    }
}
```

## Why?

Allows for more expressive test names without very_long_underscore_ridden_test_method_names, and for more flexible test structure. Java 8 features such as lambda expressions make this kind of syntax possible without an insane amount of boilerplate. See [wiki/Motivation](https://github.com/hgcummings/jarspec/wiki/Motivation) for more.

## How?

### Using Maven

You'd currently have to build from source to add to your local Maven repository...

```
git clone git@github.com:hgcummings/jarspec.git
cd jarspec
mvn clean install
```

Then you can add the dependency to your project...

```xml
    <dependencies>
        ....
        <dependency>
            <artifactId>jarspec</artifactId>
            <groupId>io.hgc</groupId>
            <version>0.1.0-SNAPSHOT</version>
        </dependency>
        ....
    </dependencies>
```

Configure SureFire to pick up classes named *Spec (although you can stick with the default *Test convention if you prefer)

```xml
    <build>
        <plugins>
            ...
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.17</version>
                <configuration>
                    <includes>
                        <include>**/*Spec.java</include>
                    </includes>
                </configuration>
            </plugin>
            ...
        </plugins>
    </build>
```

Then you can start writing specs. For now, see [wiki/Implementing Specifications](https://github.com/hgcummings/jarspec/wiki/Implementing-Specifications)) for more details, or the [project's own tests](https://github.com/hgcummings/jarspec/tree/master/src/test/java/io/hgc/jarspec) for examples.

## When?

See [open milestones](https://github.com/hgcummings/jarspec/issues/milestones) for an indication of progress and roadmap.

---
###### Developer resources: [Test results](http://hgc.io/jarspec/surefire-report.html) [![Build status](https://travis-ci.org/hgcummings/jarspec.svg?branch=master)](https://travis-ci.org/hgcummings/jarspec) [Coverage report](http://hgc.io/jarspec/jacoco/index.html) [![Coverage Status](https://coveralls.io/repos/hgcummings/jarspec/badge.png?branch=master)](https://coveralls.io/r/hgcummings/jarspec?branch=master) [Task board](https://huboard.com/hgcummings/jarspec)
