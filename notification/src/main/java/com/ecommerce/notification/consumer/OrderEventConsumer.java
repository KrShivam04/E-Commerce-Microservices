package com.ecommerce.notification.consumer;
import com.ecommerce.notification.payload.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderevent)
    {
        System.out.println("Received Order Event: " + orderevent);

    }

}
