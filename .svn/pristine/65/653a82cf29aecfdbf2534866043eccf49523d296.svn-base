package com.yeahmobi.yscheduler.agentframework.agent.daemon;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.yeahmobi.yscheduler.agentframework.agent.task.AgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskTransaction;
import com.yeahmobi.yscheduler.common.log.AgentLogUtils;
import com.yeahmobi.yscheduler.common.log.impl.HdfsLogServiceImpl;

public class LogTransfer {

    private Logger                   LOGGER                 = LoggerFactory.getLogger(LogTransfer.class);

    /** Base of nanosecond timings, to avoid wrapping (参考：javax.swing.TimerQueue。) */
    private static final long        NANO_ORIGIN            = System.nanoTime();
    private static final int         CHECK_INTERVAL_SECONDS = 1;

    private DelayQueue<TransferTask> logQueue               = new DelayQueue<TransferTask>();

    private ExecutorService          executorService        = Executors.newCachedThreadPool(new CustomizableThreadFactory(
                                                                                                                          "LogTransfer-worker-"));

    private Thread                   controllerThread;

    private AtomicBoolean            closed                 = new AtomicBoolean(false);

    private HdfsLogServiceImpl       logService;

    public void init() {
        this.controllerThread = new Thread() {

            @Override
            public void run() {

                while (!LogTransfer.this.closed.get()) {
                    /*
                     * 启动controller线程，负责扫描queue.
                     * 查看pair.inputstream是否可读（未构建时，先构建inputstream和outputstream），可读则交给executor，否则放回去
                     */
                    TransferTask transferTask;
                    try {
                        transferTask = LogTransfer.this.logQueue.take();
                        try {
                            if ((!transferTask.tx.getMeta().getStatus().isCompleted())
                                && (transferTask.inputStream != null) && (transferTask.inputStream.available() <= 0)) {
                                LogTransfer.this.logQueue.put(transferTask.delay(CHECK_INTERVAL_SECONDS));// 每隔多久check一次
                            } else {
                                LogTransfer.this.executorService.submit(transferTask);
                            }
                        } catch (IOException e) {
                            LogTransfer.this.logQueue.put(transferTask.delay());// 失败过的task，延迟N秒后再尝试
                            LogTransfer.this.LOGGER.error(String.format("IO error when transfer log, will try again later: txId=%s, task=%s",
                                                                        transferTask.tx.getId(),
                                                                        transferTask.tx.getTask()), e);
                        }
                    } catch (InterruptedException e) {
                        // may be closed
                    }
                }
            }
        };
        this.controllerThread.setName("LogTransfer-controller");
        this.controllerThread.start();

    }

    public void submit(TaskTransaction<AgentTask> tx) {
        // 放进queue
        TransferTask transferTask = new TransferTask(tx, null, null);
        this.logQueue.put(transferTask);
    }

    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            this.controllerThread.interrupt();
            this.executorService.shutdown();
            for (TransferTask task : this.logQueue) {
                IOUtils.closeQuietly(task.inputStream);
                IOUtils.closeQuietly(task.outputStream);
            }
        }
    }

    class TransferTask implements Runnable, Delayed {

        private static final int MAX_DELAY_SECONDS = 64;

        public TransferTask(TaskTransaction<AgentTask> tx, InputStream inputStream, FSDataOutputStream outputStream) {
            super();
            this.tx = tx;
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        TaskTransaction<AgentTask> tx;
        InputStream                inputStream;
        FSDataOutputStream         outputStream;
        private long               time         = 0;
        private long               delaySeconds = 1;

        long getDelaySeconds() {
            long delaySeconds = this.delaySeconds;

            if (this.delaySeconds < MAX_DELAY_SECONDS) {
                this.delaySeconds = Math.min(this.delaySeconds << 1, MAX_DELAY_SECONDS);
            }
            return delaySeconds;
        }

        void resetDelaySeconds() {
            this.delaySeconds = 1;
        }

        public TransferTask delay() {
            this.time = TimeUnit.SECONDS.toNanos(getDelaySeconds()) + now();
            return this;
        }

        public TransferTask delay(int seconds) {
            // 执行到这里，必然是顺利执行(无IO异常)，故重置delay时间
            resetDelaySeconds();

            this.time = TimeUnit.SECONDS.toNanos(seconds) + now();
            return this;
        }

        public void run() {
            try {
                setup();

                /**
                 * <pre>
                 * 如果tx状态已经结束，则读到-1（输出给outputstream），就结束
                 * 否则，读到-1（输出给outputstream），就放回queue
                 * </pre>
                 */
                IOUtils.copy(this.inputStream, this.outputStream);
                this.outputStream.hflush();

                if (this.tx.getMeta().getStatus().isCompleted()) {
                    IOUtils.copy(this.inputStream, this.outputStream);
                    // 写结尾标识符号
                    String endline = AgentLogUtils.getEndline(this.tx.getTask().getAttemptId());
                    IOUtils.write(AgentLogUtils.ENDLINE_SPLIT + endline, this.outputStream);
                    // 同步并关闭
                    this.outputStream.hflush();
                    IOUtils.closeQuietly(this.inputStream);
                    IOUtils.closeQuietly(this.outputStream);
                    // 等web不访问log和status之后，就可删除tx目录
                    this.tx.destroy();
                } else {
                    LogTransfer.this.logQueue.put(this.delay(CHECK_INTERVAL_SECONDS));
                }

            } catch (IOException e) {
                LogTransfer.this.logQueue.put(this.delay());// 失败过的task，延迟N秒后再尝试
                LogTransfer.this.LOGGER.error(String.format("IO Error when transfer log, will try again later: txId=%s, task=%s",
                                                            this.tx.getId(), this.tx.getTask()), e);
            } catch (Exception e) {
                LogTransfer.this.LOGGER.error(String.format("Error when transfer log, this log is ignored: txId=%s, task=%s",
                                                            this.tx.getId(), this.tx.getTask()), e);
            }

        }

        private void setup() throws IOException {
            if (this.outputStream == null) {
                long attemptId = this.tx.getTask().getAttemptId();
                // LogTransfer.this.LOGGER.info(String.format("Open output stream for attemptId(id:%s)", attemptId));
                this.outputStream = LogTransfer.this.logService.getOutputStream(attemptId);
            }
            if (this.inputStream == null) {
                // 从本地读取tx的log
                // LogTransfer.this.LOGGER.info(String.format("Open input stream from local tx(txId:%s)",
                // this.tx.getId()));
                this.inputStream = this.tx.getLogInputStream();
            }
        }

        // 在DeplyedQueue里，没有使用compator，故随意实现
        public int compareTo(Delayed o) {
            return 0;
        }

        public long getDelay(TimeUnit unit) {
            return unit.convert(this.time - now(), TimeUnit.NANOSECONDS);
        }
    }

    /**
     * Returns nanosecond time offset by origin
     */
    private static long now() {
        return System.nanoTime() - NANO_ORIGIN;
    }

    public void setLogService(HdfsLogServiceImpl logService) {
        this.logService = logService;
    }

}
