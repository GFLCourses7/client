package com.geeksforless.client.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonConfigReaderTest {

    @Test
    void testReadFile_Success() {

        String json = "[{\"name\":\"John\"}]";

        List<Person> persons = new JsonConfigReader().readFile(json.getBytes(), Person.class);

        assertEquals("John", persons.get(0).getName());
    }

    @Test
    void testReadFileIgnoreUnknownProperties() {

        String json = "[{\"name\":\"John\",\"age\":30}]";

        List<Person> persons = new JsonConfigReader().readFile(json.getBytes(), Person.class);

        assertEquals("John", persons.get(0).getName());
    }

    private static class Person {

        private String name;

        public Person() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
