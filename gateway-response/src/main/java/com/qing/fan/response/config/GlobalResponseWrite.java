package com.qing.fan.response.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

/**
 * <a href='https://blog.csdn.net/qq_37958845/article/details/119208909'>统一异常返回</a>
 *
 * @author QingFan
 * @version 1.0.0
 * @date 2022年06月19日 19:57
 */
@Slf4j
@Component
public class GlobalResponseWrite implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        // 必须在 NettyWriteResponseFilter 之前执行
        return WRITE_RESPONSE_FILTER_ORDER - 2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 这里可以增加一些业务判断条件，进行跳过处理
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        // 响应装饰
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
                MediaType contentType = response.getHeaders().getContentType();
                ServerHttpRequest request = exchange.getRequest();
                log.info("global filter HttpResponseBody, RequestPath: [{}],RequestMethod:[{}], Response status=[{}]",
                        url, request.getMethodValue(), getStatusCode());
                // 文件流 content-type 为空
                if (contentType != null && body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        // 如果响应过大，会进行截断，出现乱码，看api DefaultDataBufferFactory
                        // 有个join方法可以合并所有的流，乱码的问题解决
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer dataBuffer = dataBufferFactory.join(dataBuffers);
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        // 释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        List<String> encodingList = exchange.getResponse().getHeaders().get(HttpHeaders.CONTENT_ENCODING);
                        boolean zip = encodingList != null && encodingList.contains("gzip");
                        // responseData就是response的值，就可查看修改了
                        String responseData = getResponseData(zip, content);
                        // 重置返回参数
                        String result = responseConversion(responseData);
                        // 编码
                        byte[] uppedContent = getUppedContent(zip, result);
                        response.getHeaders().setContentLength(uppedContent.length);
                        response.setStatusCode(getStatusCode());
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private String responseConversion(String result) {
        try {
            log.info("响应结果为：{}", result);
            JSONObject object = JSON.parseObject(result);
            if (object.containsKey("code") && object.containsKey("msg")) {
                return result;
            }
            return errorResponse(object);
        } catch (Exception e) {
            log.error("响应包装转换失败，异常信息为：", e);
            return result;
        }
    }

    /**
     * 处理调用远端接口的异常，比如远端接口不存在或者服务异常，并将其header返回状态设置为远端服务返回的状态
     */
    private String errorResponse(JSONObject object) {
        Map<String, Object> map = new LinkedHashMap<>();
        Integer status = object.getInteger("status");
        map.put("status", status);
        map.put("msg", object.get("error"));
        map.put("error", object.get("error"));
        return JSON.toJSONString(map);
    }

    private String getResponseData(boolean zip, byte[] content) {
        String responseData;
        if (zip) {
            responseData = GZIPUtils.uncompressToString(content);
        } else {
            responseData = new String(content, StandardCharsets.UTF_8);
        }
        return responseData;
    }
    private byte[] getUppedContent(boolean zip, String result) {
        byte[] uppedContent;
        if (zip) {
            uppedContent = GZIPUtils.compress(result);
        } else {
            uppedContent = result.getBytes(StandardCharsets.UTF_8);
        }
        return uppedContent;
    }
}
