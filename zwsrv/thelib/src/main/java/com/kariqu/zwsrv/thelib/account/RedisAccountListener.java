package com.kariqu.zwsrv.thelib.account;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by simon on 29/05/2018.
 */
public class RedisAccountListener implements MessageListener {

    public static final String kChannelAccount = "_account";
    public static final String Channel_NewMsgIncoming = "newMsgIncoming";

    public static String channel(String appId) {
        return appId+":"+kChannelAccount;
    }

    private RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    private Jackson2JsonRedisSerializer jackson2JsonRedisSerializer;

    private RedisAccountListenerDelegate delegate;

    public RedisAccountListener(RedisAccountListenerDelegate delegate) {
        this.delegate = delegate;
        jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String channel = stringSerializer.deserialize(message.getChannel());
//        if(StringUtil.equalsFull(channel,Channel_Chat)) {
//            Object object = jackson2JsonRedisSerializer.deserialize(message.getBody());
//            if (object instanceof ChatMessage) {
//                handleChatMessage((ChatMessage)object);
//            }
//        }
//        else if (StringUtil.equalsFull(channel,Channel_Online)) {
//
//        }
    }

//    void handleChatMessage(ChatMessage message) {
//
//    }

    void sendMessage() {

    }
}
