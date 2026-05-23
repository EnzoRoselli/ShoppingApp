package com.shoppingapp.system;

import com.shoppingapp.ShoppingApp;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingAppMainTest {

    @Test
    void appClassCanBeInstantiated() {
        assertNotNull(new ShoppingApp());
    }

    @Test
    void mainRunsASessionThroughStandardStreams() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        try {
            String script = String.join("\n", "Enzo", "IL", "1", "7") + "\n";
            System.setIn(new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)));
            ByteArrayOutputStream captured = new ByteArrayOutputStream();
            System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));

            ShoppingApp.main(new String[]{});

            assertTrue(captured.toString(StandardCharsets.UTF_8).contains("Goodbye Enzo"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
