package edu.gatech.cs6301.backend3;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TSFSBackendTests {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TSFSBackendTestSuite.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());

    }
}
