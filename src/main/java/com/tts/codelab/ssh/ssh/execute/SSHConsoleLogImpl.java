package com.tts.codelab.ssh.ssh.execute;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SSHConsoleLogImpl implements SSHConsoleLog {

    private Cache<String, Map<String, Queue<String>>> cacheLoader;

    @PostConstruct
    public void initialize(){
        // @format:off
        cacheLoader = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Map<String, Queue<String>>>() {
                    @Override
                    public Map<String, Queue<String>> load(String s) throws Exception {
                        return new HashMap<>();
                    }
                });
        // @format:on
    }

    public Map<String, Queue<String>> getConsoleLogByIpAddress() {
        try {
            return cacheLoader.get(SSHConsoleLogImpl.class.getSimpleName(), new Callable<Map<String, Queue<String>>>() {

                @Override
                public Map<String, Queue<String>> call() throws Exception {
                    return new HashMap<>();
                }
            });
        } catch (Exception e) {
            log.error("", e);
        }
        return new HashMap<>();
    }

    @Override
    public void log(String ipAddress, String outputConsole) {
        Queue<String> queue = getConsoleLogByIpAddress().get(ipAddress);
        if (queue == null) {
            synchronized (ipAddress) {
                queue = getConsoleLogByIpAddress().get(ipAddress);
                if (queue == null) {
                    queue = new LinkedList<>();
                    getConsoleLogByIpAddress().put(ipAddress, queue);
                }
            }
        }
        synchronized (queue) {
            queue.offer(outputConsole);
        }
    }

    @Override
    public String getLog(String ipAddress) {
        Queue<String> queue = getConsoleLogByIpAddress().get(ipAddress);
        if (queue == null) {
            return "";
        }
        synchronized (queue) {
            StringBuilder sb = new StringBuilder();
            for (String log : queue) {
                sb.append(log);
            }
            queue.clear();
            return sb.toString();
        }
    }

}
