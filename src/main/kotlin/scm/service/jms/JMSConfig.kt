package scm.service.jms

import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.connection.CachingConnectionFactory
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType

@Configuration
@EnableJms
class JMSConfig {
    @Bean
    fun jmsContainerFactory(): DefaultJmsListenerContainerFactory {
        val containerFactory = DefaultJmsListenerContainerFactory()
        containerFactory.setPubSubDomain(true)
        containerFactory.setConnectionFactory(connectionFactory())
        containerFactory.setMessageConverter(jacksonJmsMessageConverter())
        return containerFactory
    }

    @Bean
    fun connectionFactory(): CachingConnectionFactory {
        val cachConnectionFactory = CachingConnectionFactory()
        val connectionFactory = ActiveMQConnectionFactory()
        connectionFactory.setBrokerURL("tcp://localhost:61616")
        cachConnectionFactory.setTargetConnectionFactory(connectionFactory)
        return cachConnectionFactory
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter {
        val converter = MappingJackson2MessageConverter()
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        return converter
    }
}