package br.dev.jcp.training.springboot6brewery.sandbox;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConversionTest {
    @Test
    void convertBoolean() {
        Boolean boolClass = null;
        boolean boolValue = Boolean.FALSE;
        System.out.println("Class: " + boolClass);
        System.out.println("Primitive: " + boolValue);
        boolClass = Boolean.FALSE;
        System.out.println("Class: " + boolClass);
        assertThat(boolClass).isFalse();
        boolValue = !boolClass;
        System.out.println("Primitive: " + boolValue);
        assertThat(boolValue).isTrue();
    }
}
