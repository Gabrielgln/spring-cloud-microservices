package br.com.alurafood.pagamentos.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoAMQPConfiguration {
    @Bean
    public Queue criaFila(){
        // return  new Queue("pagamento.concluido", true);
        return QueueBuilder
                .durable("pagamento.concluido")
                .build();
    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    /**
     * Configura o conversor responsável por serializar e desserializar
     * mensagens no formato JSON utilizando a biblioteca Jackson.
     *
     * @return conversor de mensagens JSON
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura o {@link RabbitTemplate} para utilizar o conversor
     * de mensagens JSON, permitindo a serialização e desserialização
     * automática dos objetos enviados e recebidos pelo RabbitMQ.
     *
     * @param conn conexão com o broker RabbitMQ
     * @param converter conversor de mensagens JSON
     * @return RabbitTemplate configurado com o conversor JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory conn,
            Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(conn);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

}