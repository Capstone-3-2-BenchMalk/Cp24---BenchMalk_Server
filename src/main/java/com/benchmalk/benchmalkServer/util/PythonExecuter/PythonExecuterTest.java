package com.benchmalk.benchmalkServer.util.PythonExecuter;

import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PythonExecuterTest {

    public void execute() throws IOException, InterruptedException, TimeoutException {
        String output = new ProcessExecutor().command("python", "src/python/test.py")
                .readOutput(true).execute()
                .outputUTF8();
        System.out.println(output);
    }
}
