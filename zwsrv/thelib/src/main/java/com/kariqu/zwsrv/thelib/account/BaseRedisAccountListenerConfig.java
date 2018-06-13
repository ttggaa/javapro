package com.kariqu.zwsrv.thelib.account;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * Created by simon on 29/05/2018.
 */
//@Configuration
//@ComponentScan
public abstract class BaseRedisAccountListenerConfig {

    protected abstract RedisAccountListenerDelegate theDelegate();

    @Bean
    public JedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        for (String appId:AppIds.appIds)
        {
            container.addMessageListener(listenerAdapter, new PatternTopic(RedisAccountListener.channel("")));
        }
        container.setConnectionFactory(connectionFactory);
        return container;
    }


    @Bean
    MessageListenerAdapter listenerAdapter(RedisAccountListener listener) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(listener);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
        return messageListenerAdapter;
    }

    @Bean
    RedisAccountListener receiver(/*CountDownLatch latch*/) {
        return new RedisAccountListener(theDelegate());
    }

}
