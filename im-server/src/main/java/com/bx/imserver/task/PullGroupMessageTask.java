package com.bx.imserver.task;

import com.alibaba.fastjson.JSONObject;
import com.bx.imcommon.contant.IMRedisKey;
import com.bx.imcommon.enums.IMCmdType;
import com.bx.imcommon.model.IMRecvInfo;
import com.bx.imserver.netty.IMServerGroup;
import com.bx.imserver.netty.processor.AbstractMessageProcessor;
import com.bx.imserver.netty.processor.ProcessorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullGroupMessageTask extends AbstractPullMessageTask {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void pullMessage() {
        // 从redis拉取未读消息
        String key = String.join(":", IMRedisKey.IM_MESSAGE_GROUP_QUEUE, IMServerGroup.serverId + "");
        JSONObject jsonObject = (JSONObject) redisTemplate.opsForList().leftPop(key, 10, TimeUnit.SECONDS);
        if (jsonObject != null) {
            IMRecvInfo recvInfo = jsonObject.toJavaObject(IMRecvInfo.class);
            AbstractMessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.GROUP_MESSAGE);
            processor.process(recvInfo);
        }
    }

}
