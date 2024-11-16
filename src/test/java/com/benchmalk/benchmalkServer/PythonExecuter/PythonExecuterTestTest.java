package com.benchmalk.benchmalkServer.PythonExecuter;

import com.benchmalk.benchmalkServer.util.PythonExecuter.PythonExecuterTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


class PythonExecuterTestTest {

    @Test
    void test() throws IOException, InterruptedException, TimeoutException {
        PythonExecuterTest test = new PythonExecuterTest();
        test.execute();
    }
}