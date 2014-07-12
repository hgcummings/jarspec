What?
=====

[RSpec](http://rspec.info/)/[Jasmine](http://jasmine.github.io/2.0/introduction.html) style tests for native Java 8, a bit like the following:

```java
@RunWith(JarSpecJUnitRunner.class)
public class MinimalSpec implements UnitSpec {
    @Override
    public Specification specification() {
        return describe("A specification", () ->
            it("contains a statement with a test", () -> assertTrue(true))
        );
    }
}
```

Why?
====

Allows for more expressive test names without very_long_underscore_ridden_test_method_names, and more flexible test structure.

How?
====

Currently have to build from source to make available to Maven...

```
git clone git@github.com:hgcummings/jarspec.git
cd jarspec
mvn clean install
```

Then add dependency to your project

```
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
        </dependency>
    </dependencies>
```

*Optionally* configure SureFire to pick up classes named ...Spec (but you can stick with the ...Test convention if you like)

```
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

Start writing specs. For now, see [the project's own tests](https://github.com/hgcummings/jarspec/tree/master/src/test/java) for more examples.

When?
=====

Might not ever be a real thing, as I still haven't decided yet if it's a good idea. However, you can see [issues and milestones on huboard](https://huboard.com/hgcummings/jarspec).