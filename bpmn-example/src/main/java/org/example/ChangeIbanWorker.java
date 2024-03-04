package org.example;

import org.camunda.bpm.client.ExternalTaskClient;
import java.util.logging.Logger;

public class ChangeIbanWorker {

    private final static Logger LOGGER = Logger.getLogger(ChangeIbanWorker.class.getName());

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // Polling timeout
                .build();

        client.subscribe("change-iban") //Prozess ID
                .lockDuration(1000) // Lock Duration für Task
                .handler((externalTask, externalTaskService) -> {
                    // Task variablen abrufen
                    String newiban = externalTask.getVariable("newiban");
                    String user = externalTask.getVariable("user");
                    //Boolean approved = externalTask.getVariable("approved"); nicht mehr nötig

                    LOGGER.info("Aktualisiere für " + user + " IBAN auf: " + newiban);

                    // Camundaprozess weiterlaufen lassen
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}
