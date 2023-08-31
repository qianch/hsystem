package com.bluebirdme.mes.core.messageconvertor;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class UTF8StringHttpMessageConvertor extends AbstractHttpMessageConverter<String> {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final List<Charset> availableCharsets = new ArrayList<>(Charset.availableCharsets().values());
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
        return (long) s.getBytes(charset).length;
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
