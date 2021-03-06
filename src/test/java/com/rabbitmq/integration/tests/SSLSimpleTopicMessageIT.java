/* Copyright (c) 2013 Pivotal Software, Inc. All rights reserved. */
package com.rabbitmq.integration.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.jms.DeliveryMode;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

/**
 * Integration test
 */
@DisabledIfSystemProperty(named = "travis-ci", matches = "true")
public class SSLSimpleTopicMessageIT extends AbstractITTopicSSL {
    private static final String TOPIC_NAME = "test.topic." + SSLSimpleTopicMessageIT.class.getCanonicalName();

    @Test
    public void testSendAndReceiveTextMessage() throws Exception {
        final String MESSAGE2 = "Hello " + SSLSimpleTopicMessageIT.class.getName();
        topicConn.start();
        TopicSession topicSession = topicConn.createTopicSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        Topic topic = topicSession.createTopic(TOPIC_NAME);
        TopicPublisher sender = topicSession.createPublisher(topic);
        TopicSubscriber receiver1 = topicSession.createSubscriber(topic);
        TopicSubscriber receiver2 = topicSession.createSubscriber(topic);

        sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        TextMessage message = topicSession.createTextMessage(MESSAGE2);
        sender.send(message);

        assertEquals(MESSAGE2, ((TextMessage) receiver1.receive()).getText());
        assertEquals(MESSAGE2, ((TextMessage) receiver2.receive()).getText());
    }
}
