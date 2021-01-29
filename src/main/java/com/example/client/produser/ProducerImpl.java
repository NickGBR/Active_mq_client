package com.example.client.produser;

import com.example.client.consts.Commands;
import com.example.client.consts.JmsMessagePropertyName;
import com.example.client.consts.JmsMessagePropertyValue;
import com.example.client.consts.Queue;
import com.example.client.produser.impl.Producer;
import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.*;

@Component
public class ProducerImpl implements Producer {

    private JmsTemplate jmsTemplate;
    private final static String CORRELATION_ID = JmsMessagePropertyValue.CORRELATION_ID;

    @Autowired
    public ProducerImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void withdraw(Long id, Double amount) {
        Message message = new ActiveMQMessage();
        try {
            message.setJMSCorrelationID(CORRELATION_ID);
            message.setLongProperty(JmsMessagePropertyName.ACCOUNT_ID, id);
            message.setDoubleProperty(JmsMessagePropertyName.AMOUNT, amount);
            message.setStringProperty(JmsMessagePropertyName.COMMAND, Commands.WITHDRAW);
            jmsTemplate.convertAndSend(Queue.REQUEST_QUEUE, message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deposit(Long id, Double amount) {
        Message message = new ActiveMQMessage();
        try {
            message.setJMSCorrelationID(CORRELATION_ID);
            message.setLongProperty(JmsMessagePropertyName.ACCOUNT_ID, id);
            message.setDoubleProperty(JmsMessagePropertyName.AMOUNT, amount);
            message.setStringProperty(JmsMessagePropertyName.COMMAND, Commands.DEPOSIT);
            jmsTemplate.convertAndSend(Queue.REQUEST_QUEUE, message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transferTo(Long idFrom, Long idTo, Double amount) {
        Message message = new ActiveMQMessage();
        try {
            message.setJMSCorrelationID(CORRELATION_ID);
            message.setLongProperty(JmsMessagePropertyName.TRANSFER_FROM, idFrom);
            message.setLongProperty(JmsMessagePropertyName.TRANSFER_TO, idTo);
            message.setDoubleProperty(JmsMessagePropertyName.AMOUNT, amount);
            message.setStringProperty(JmsMessagePropertyName.COMMAND, Commands.TRANSFER);
            jmsTemplate.convertAndSend(Queue.REQUEST_QUEUE, message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAccount(Long id) {
        Message message = new ActiveMQMessage();
        try {
            message.setJMSCorrelationID(CORRELATION_ID);
            message.setLongProperty(JmsMessagePropertyName.ACCOUNT_ID, id);
            message.setStringProperty(JmsMessagePropertyName.COMMAND, Commands.CREATE);
            jmsTemplate.convertAndSend(Queue.REQUEST_QUEUE, message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeAccount(Long id) {
        Message message = new ActiveMQMessage();
        try {
            message.setJMSCorrelationID(CORRELATION_ID);
            message.setLongProperty(JmsMessagePropertyName.ACCOUNT_ID, id);
            message.setStringProperty(JmsMessagePropertyName.COMMAND, Commands.CLOSE);
            jmsTemplate.convertAndSend(Queue.REQUEST_QUEUE, message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = Queue.RESPONSE_TOPIC)
    public void getResponse(Message message, @Header(JmsHeaders.CORRELATION_ID) String correlationId) throws JMSException {
        System.out.println(message.getJMSCorrelationID());
        System.out.println(CORRELATION_ID);
        if(CORRELATION_ID.equals(correlationId)) {
            handleResponse(message);
        }
    }

    private void handleResponse(Message message) throws JMSException {
        System.out.print(message.getStringProperty(JmsMessagePropertyName.OPERATION_STATUS));
        if (message.propertyExists(JmsMessagePropertyName.ACCOUNT_DEPOSIT_STATUS)) {
            System.out.println(" Your balance is " + message.getDoubleProperty(JmsMessagePropertyName.ACCOUNT_DEPOSIT_STATUS));
        }
        if (message.propertyExists(JmsMessagePropertyName.NOT_VALID_USER_IDs)) {
            System.out.println();
            List<Long> ids = (List<Long>) message.getObjectProperty(JmsMessagePropertyName.NOT_VALID_USER_IDs);
            for (Long id : ids) {
                System.out.println(" \"id = " + id + "\"");
            }
        } else System.out.println();
    }
}
