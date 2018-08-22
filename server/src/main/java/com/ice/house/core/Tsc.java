package com.ice.house.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author:ice
 * @Date: 2018/8/18 0018
 */
@Component
public class Tsc {
    private static final Logger logger = LoggerFactory.getLogger(Tsc.class);

    public Map<String, Integer> stub = new HashMap<>();/*连接所在的线程.*/

    private int work_count = 4;//工作线程的数量

    public ModbusWorker[] wks;//处理事务线程

    public TscLog tscLog;

    private long clock = System.currentTimeMillis();

    /**
     * 用于轮询分配工作线程.
     */
    public AtomicInteger rb = new AtomicInteger(0);

    public Tsc() {
        wks = new ModbusWorker[4];
        for (int i = 0; i < work_count; i++) {
            wks[i] = new ModbusWorker();
        }
        tscLog = new DeviceLog();
    }

    /**
     * 定时器震荡
     */
    public void hold() {
        logger.info("定时器震荡");
        while (true) {
            this.clock = System.currentTimeMillis();
            for (int i = 0; i < work_count; i++) {
                ModbusWorker worker = this.wks[i];
                worker.future(v -> {
                    worker.quartz(clock);
                });
            }
        }
    }

    /**
     * 得到当前事务线程
     */
    public ModbusWorker getWorker(String id) {
        Integer wk = stub.get(id);
        if (wk == null) {
            wk = roundRobinWorkerIndex();
            stub.put(id, wk);
        }
        return wks[wk];
    }

    /**
     * 为Actor选择一个工作线程(round-robin).
     */
    public int roundRobinWorkerIndex() {
        return (this.rb.incrementAndGet() & 0x7FFFFFFF) % work_count;
    }
}
