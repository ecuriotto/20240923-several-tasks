package com.camunda.training;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("myCustomDelegate") 
public class MyCustomDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyCustomDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Retrieve process variables
        String someText = (String) execution.getVariable("someText");

        // Log information
        LOGGER.info("Executing delegate for process instance id: " + execution.getProcessInstanceId());
        LOGGER.info("Processing variable: " + someText);

        // Business logic
        String result = processData(someText);

        // Set a new variable in the process
        execution.setVariable("processedVariable", result);
    }

    // Simulate some business logic
    private String processData(String input) {
        return "Processed: " + input;
    }
}
