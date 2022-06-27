package project.cyberproton.atom.inject;

import project.cyberproton.atom.LifeCycle;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Install {
    LifeCycle lifeCycle() default LifeCycle.LOADING;
}
