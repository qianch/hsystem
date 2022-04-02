package com.bluebirdme.mes.core.messageconvertor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class UTF8StringHttpMessageConvertor extends AbstractHttpMessageConverter<String> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private final List<Charset> availableCharsets = new ArrayList(Charset.availableCharsets().values());
    private boolean writeAcceptCharset = true;

    public UTF8StringHttpMessageConvertor() {
        super(new MediaType[]{new MediaType("text", "plain", DEFAULT_CHARSET), MediaType.ALL});
    }

    public void setWriteAcceptCharset(boolean writeAcceptCharset) {
        this.writeAcceptCharset = writeAcceptCharset;
    }

    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    protected String readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException {
        Charset charset = this.getContentTypeCharset(inputMessage.getHeaders().getContentType());
        return FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), charset));
    }

    protected Long getContentLength(String s, MediaType contentType) {
        Charset charset = this.getContentTypeCharset(contentType);
        try {
            return (long) s.getBytes(charset.name()).length;
        } catch (UnsupportedEncodingException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

    protected void writeInternal(String s, HttpOutputMessage outputMessage) throws IOException {
        if (this.writeAcceptCharset) {
            outputMessage.getHeaders().setAcceptCharset(this.getAcceptedCharsets());
        }
        Charset charset = this.getContentTypeCharset(outputMessage.getHeaders().getContentType());
        FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(), charset));
    }

    protected List<Charset> getAcceptedCharsets() {
        return this.availableCharsets;
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        return contentType != null && contentType.getCharset() != null ? contentType.getCharset() : DEFAULT_CHARSET;
    }
}
