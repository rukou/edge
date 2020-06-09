package io.rukou.edge.routes;

import io.rukou.edge.Message;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.*;

import java.util.Map;

public class PulsarRoute extends Route {
    private int id;
    private String url;
    private PulsarClient client;

    public PulsarRoute(int id, String url) {
        this.id = id;
        this.url = url;
        try {
            client = PulsarClient.builder()
                    .serviceUrl(url)
                    .build();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getType() {
        return "apache-pulsar";
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getAlias() {
        return String.valueOf(id);
    }

    @Override
    public void shutdown() {
        if(client!=null) {
            try {
                client.shutdown();
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String invokeEdge2Local(Message msg) {
        try {
            Producer<String> stringProducer = client.newProducer(Schema.STRING)
                    .topic("edge2local")
                    .create();
            MessageId msgId = stringProducer.newMessage().properties(msg.header).value(msg.body).send();
            return msgId.toString();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void initLocal2EdgeSubscription() {
        try {
            Consumer<String> consumer = client.newConsumer(Schema.STRING)
                    .topic("local2edge")
                    .subscriptionName("subscription")
                    .messageListener(new MessageListener<String>() {
                        @Override
                        public void received(Consumer<String> consumer, org.apache.pulsar.client.api.Message<String> message) {
                            org.apache.pulsar.client.api.Message<String> msg = null;
                            try {
                                msg = consumer.receive();
                                System.out.println("Got message: " + msg.getValue());
                                Map<String,String> header = msg.getProperties();
                                consumer.acknowledge(msg);
                            } catch (PulsarClientException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .subscribe();
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }
}
