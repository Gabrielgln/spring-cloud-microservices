package br.com.alurafood.pagamentos.amqp;

import org.springframework.amqp.core.FanoutExchange;
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
//    @Bean
//    public Queue queue(){
//        // return  new Queue("pagamento.concluido", true);
//        return QueueBuilder
//                .durable("pagamento.concluido")
//                .build();
//    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> init(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    /**
     * Configura o conversor responsável por serializar e desserializar
     * mensagens no formato JSON utilizando a biblioteca Jackson.
     *
     * @return conversor de mensagens JSON
     */
    @Bean
    public Jackson2JsonMessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura o {@link RabbitTemplate} para utilizar o conversor
     * de mensagens JSON, permitindo a serialização e desserialização
     * automática dos objetos enviados e recebidos pelo RabbitMQ.
     *
     * @param factory conexão com o broker RabbitMQ
     * @param converter conversor de mensagens JSON
     * @return RabbitTemplate configurado com o conversor JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory factory,
            Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    /**
     * Cria e configura um FanoutExchange responsável por encaminhar as mensagens
     * recebidas para todas as filas vinculadas a ele.
     *
     * @return instância do FanoutExchange configurada com o nome "pagamentos.ex".
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("pagamentos.ex");
    }
}