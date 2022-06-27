package project.cyberproton.atom.inject;

import project.cyberproton.atom.LifeCycle;

import java.lang.annotation.*;

@Documented
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auto {
    LifeCycle lifecycle() default LifeCycle.ENABLING;
}
