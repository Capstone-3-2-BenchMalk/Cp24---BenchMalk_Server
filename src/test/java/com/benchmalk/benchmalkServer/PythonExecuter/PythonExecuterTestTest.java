package com.benchmalk.benchmalkServer.PythonExecuter;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;


class PythonExecuterTestTest {

    @Test
    void test() throws IOException, InterruptedException, TimeoutException {
        PythonExecuterTest test = new PythonExecuterTest();
        test.execute();
    }
}