package com.example.client.console;

import com.example.client.consts.Commands;
import com.example.client.consts.ConsoleMassages;
import com.example.client.produser.impl.Producer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class Console implements Runnable {

    private final Producer producer;

    @Autowired
    public Console(Producer producer) {
        this.producer = producer;
    }


    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            System.out.println("Input command ...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            final String command = reader.readLine();
            switch (command) {
                case (Commands.CREATE):
                case ("1"):
                    create(reader);
                    break;
                case (Commands.CLOSE):
                case ("2"):
                    close(reader);
                    break;
                case (Commands.DEPOSIT):
                case ("3"):
                    deposit(reader);
                    break;
                case (Commands.WITHDRAW):
                case ("4"):
                    withdraw(reader);
                    break;
                case (Commands.TRANSFER):
                case ("5"):
                    transfer(reader);
                    break;
                case (Commands.HELP):
                    System.out.println(ConsoleMassages.HELP);
                    break;
                default:
                    System.out.println("Command isn't valid");
                    break;
            }
        }
    }

    @SneakyThrows
    private void close(BufferedReader reader) {
        System.out.println("Input account id ...");
        try {
            Long id = Long.parseLong(reader.readLine());
            producer.closeAccount(id);
        } catch (NumberFormatException exception) {
            System.out.println("Illegal id format");
        }
    }

    @SneakyThrows
    private void create(BufferedReader reader) {
        try {
            System.out.println("Input account id ...");
            Long id = Long.parseLong(reader.readLine());
            producer.createAccount(id);
        } catch (NumberFormatException exception) {
            System.out.println("Illegal id format");
        }
    }

    @SneakyThrows
    private void transfer(BufferedReader reader) {
        try {
            System.out.println("Input withdraw account id ...");
            Long idFrom = Long.parseLong(reader.readLine());
            System.out.println("Input deposit account id ...");
            Long idTo = Long.parseLong(reader.readLine());
            System.out.println("Input amount ...");
            Double amount = Double.parseDouble(reader.readLine());
            producer.transferTo(idFrom, idTo, amount);
        } catch (NumberFormatException exception) {
            System.out.println("Illegal id or amount format");
        }
    }

    @SneakyThrows
    private void deposit(BufferedReader reader) {
        try {
            System.out.println("Input account id ...");
            Long id = Long.parseLong(reader.readLine());
            System.out.println("Input amount ...");
            Double amount = Double.parseDouble(reader.readLine());
            producer.deposit(id, amount);
        } catch (NumberFormatException exception) {
            System.out.println("Illegal id or amount format");
        }
    }

    @SneakyThrows
    private void withdraw(BufferedReader reader) {
        try {
            System.out.println("Input account id ...");
            Long id = Long.parseLong(reader.readLine());

            System.out.println("Input amount ...");
            Double amount = Double.parseDouble(reader.readLine());

            producer.withdraw(id, amount);
        } catch (NumberFormatException exception) {
            System.out.println("Illegal id or amount format");
        }
    }
}
