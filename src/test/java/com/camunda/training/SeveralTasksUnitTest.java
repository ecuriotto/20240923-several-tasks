package com.camunda.training;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.execute;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.findId;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.job;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.community.process_test_coverage.junit5.platform7.ProcessEngineCoverageExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Deployment(resources = { "several-tasks-process.bpmn" })
@ExtendWith({MockitoExtension.class, ProcessEngineCoverageExtension.class})
public class SeveralTasksUnitTest {

  public static final String PROCESS_ID = "SeveralTasksProcess";

  @Mock
  private MyCustomDelegate myCustomDelegate;

  @BeforeEach
  public void setup() {
    // Registering the specific mocked delegates
    Mocks.register("myCustomDelegate", myCustomDelegate);
  }

  @Test
  public void happy_path_test() {
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(PROCESS_ID);
    
    assertThat(processInstance).isWaitingAt("ExecuteScript");
    execute(job()); //ExecuteScript

    assertThat(processInstance).isWaitingAt("validateRequest");
    complete(task("validateRequest"));

    assertThat(processInstance).hasPassed(findId("Process completed"));
    try {
      verify(myCustomDelegate, times(1)).execute(any(DelegateExecution.class));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

}
