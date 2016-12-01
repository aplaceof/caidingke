package net.caidingke.rabbitmq.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.io.UnsupportedEncodingException;

/**
 * fastjson messageconver插件实现
 *
 * @author bowen
 * @create 2016-11-02 16:21
 */
public class FastJsonMessageConverter extends AbstractJsonMessageConverter {
    private static JSON jsonObject = new JSONObject();
    public static final String DEFAULT_CHARSET = "UTF-8";

    private volatile String defaultCharset = DEFAULT_CHARSET;

    public FastJsonMessageConverter() {
        super();
        //init();
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = (defaultCharset != null) ? defaultCharset
                : DEFAULT_CHARSET;
    }



    public <T> T fromMessage(Message message,T t) {
        String json = "";
        try {
            json = new String(message.getBody(),defaultCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (T) jsonObject.parseObject(json, t.getClass());
    }


    @Override
    protected Message createMessage(Object objectToConvert,
                                    MessageProperties messageProperties)
            throws MessageConversionException {
        byte[] bytes = null;
        try {
            String jsonString = jsonObject.toJSONString(objectToConvert);
            bytes = jsonString.getBytes(this.defaultCharset);
        } catch (UnsupportedEncodingException e) {
            throw new MessageConversionException(
                    "Failed to convert Message content", e);
        }
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(this.defaultCharset);
        if (bytes != null) {
            messageProperties.setContentLength(bytes.length);
        }
        return new Message(bytes, messageProperties);

    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return null;
    }


}
