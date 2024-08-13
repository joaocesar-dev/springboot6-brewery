package br.dev.jcp.training.springboot6brewery.sandbox;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class FormatterTest {

    @Test
    void stringWithPercentageFormatTest() {
        String like = "%%%s%%";
        String formatted = String.format(like, "TESTANDO");
        System.out.println(formatted);
        assertThat(formatted).isEqualTo("%TESTANDO%");
    }

    @Test
    void stringFormatTest() {
        String result = String.format("%07d", 1);
        System.out.println(result);
        assertThat(result).isEqualTo("0000001");
        result = String.format("%07d", 12);
        System.out.println(result);
        assertThat(result).isEqualTo("0000012");
        result = String.format("%07d", 1011);
        System.out.println(result);
        assertThat(result).isEqualTo("0001011");
        result = String.format("%07d", 123456);
        System.out.println(result);
        assertThat(result).isEqualTo("0123456");
        result = String.format("%07d", 7543210);
        System.out.println(result);
        assertThat(result).isEqualTo("7543210");
        result = String.format("%07d", 12345678);
        System.out.println(result);
        assertThat(result).isEqualTo("12345678");
    }

    @Test
    void telephoneFormatterTest() {
        String phoneFormatted = "+55 (41) 3333-1234";
        String mobileFormatted = "+55 (41) 99988-5678";
        String regex = "(\\+\\d{1,3})\\s\\((\\d{2})\\)\\s(\\d{4,5})-(\\d{4})";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneFormatted);
        assertThat(matcher.groupCount()).isEqualTo(4);
        assertThat(matcher.matches()).isTrue();
        assertThat(matcher.group(1)).isEqualTo("+55");
        assertThat(matcher.group(2)).isEqualTo("41");
        assertThat(matcher.group(3)).isEqualTo("3333");
        assertThat(matcher.group(4)).isEqualTo("1234");
        System.out.println(matcher.groupCount());
        if (matcher.matches()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
            System.out.println(matcher.group(4));
        }

        matcher = pattern.matcher(mobileFormatted);
        assertThat(matcher.groupCount()).isEqualTo(4);
        assertThat(matcher.matches()).isTrue();
        assertThat(matcher.group(1)).isEqualTo("+55");
        assertThat(matcher.group(2)).isEqualTo("41");
        assertThat(matcher.group(3)).isEqualTo("99988");
        assertThat(matcher.group(4)).isEqualTo("5678");
        System.out.println(matcher.groupCount());
        if (matcher.matches()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
            System.out.println(matcher.group(4));
        }

    }
}
