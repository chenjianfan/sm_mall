package cn.woshicheng.common.netty;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.PlatformDependent;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadFactory;

/**
 * Netty帮助类
 * by phc
 */
public class NettyUtils {
    private final static Logger logger = Logger.getLogger(NettyUtils.class);
    /**
     * 建立线程工厂
     * @param threadPoolPrefix 线程名称
     * @return
     */
    public static ThreadFactory createThreadFactory(String threadPoolPrefix) {
        return new CustomizableThreadFactory(threadPoolPrefix);
    }

    /**
     * 根据模式建立netty  EventLoopGroup，mode为nio或者epoll，epoll只能在linux中使用
     * @param mode 模式只能为NIO或EPOLL
     * @param numThreads 线程数量
     * @param threadPrefix 线程名称
     * @return
     */
    public static EventLoopGroup createEventLoop(String mode, int numThreads, String threadPrefix) {
        ThreadFactory threadFactory = createThreadFactory(threadPrefix);
        mode = mode.toUpperCase();
        switch (mode) {
            case "NIO":
                logger.info("构建NioEventLoopGroup");
                return new NioEventLoopGroup(numThreads, threadFactory);
//                return new NioEventLoopGroup(numThreads);
            case "EPOLL":
                logger.info("构建EpollEventLoopGroup");
                return new EpollEventLoopGroup(numThreads, threadFactory);
            default:
                throw new IllegalArgumentException("Unknown io mode: " + mode);
        }
    }

    /**
     * 根据mode返回客户端channelClass，mode值为nio or epoll,epoll只能在linux中使用
     * @param mode nio or epoll
     * @return
     */
    public static Class<? extends Channel> getClientChannelClass(String mode) {
        mode = mode.toUpperCase();
        switch (mode) {
            case "NIO":
                logger.info("构建NioSocketChannel");
                return NioSocketChannel.class;
            case "EPOLL":
                logger.info("构建EpollSocketChannel");
                return EpollSocketChannel.class;
            default:
                throw new IllegalArgumentException("Unknown io mode: " + mode);
        }
    }

    /**
     * 根据mode返回服务端channel类型，mode值为nio or epoll,epoll只能在linux中使用
     * @param mode nio or epoll
     * @return
     */
    public static Class<? extends ServerChannel> getServerChannelClass(String mode) {
        mode = mode.toUpperCase();
        switch (mode) {
            case "NIO":
                logger.info("构建NioServerSocketChannel");
                return NioServerSocketChannel.class;
            case "EPOLL":
                logger.info("构建EpollServerSocketChannel");
                return EpollServerSocketChannel.class;
            default:
                throw new IllegalArgumentException("Unknown io mode: " + mode);
        }
    }

    /**
     * 获取远程地址
     * @param channel
     * @return
     */
    public static String getRemoteAddress(Channel channel) {
        if (channel != null && channel.remoteAddress() != null) {
            return channel.remoteAddress().toString();
        }
        return "<unknown remote>";
    }

    /**
     * 创建一个集合ByteBuf allocator，禁用线程本地缓存。线程本地缓存
     * 对于传输客户端是禁用的，因为ByteBufs是由事件循环线程分配的，
     * 但是由执行程序线程而不是事件循环线程释放。这些线程本地
     * 缓存实际上延迟了缓冲区的回收，导致了更大的内存使用。
     * @param allowDirectBufs 是否建立直接缓冲区
     * @param allowCache 建议服务端true，客户端false
     * @param numCores
     * @return
     */
    public static PooledByteBufAllocator createPooledByteBufAllocator(
            boolean allowDirectBufs,
            boolean allowCache,
            int numCores) {
        if (numCores == 0) {
            numCores = Runtime.getRuntime().availableProcessors();
        }
        return new PooledByteBufAllocator(
                allowDirectBufs && PlatformDependent.directBufferPreferred(),
                Math.min(getPrivateStaticField("DEFAULT_NUM_HEAP_ARENA"), numCores),
                Math.min(getPrivateStaticField("DEFAULT_NUM_DIRECT_ARENA"), allowDirectBufs ? numCores : 0),
                getPrivateStaticField("DEFAULT_PAGE_SIZE"),
                getPrivateStaticField("DEFAULT_MAX_ORDER"),
                allowCache ? getPrivateStaticField("DEFAULT_TINY_CACHE_SIZE") : 0,
                allowCache ? getPrivateStaticField("DEFAULT_SMALL_CACHE_SIZE") : 0,
                allowCache ? getPrivateStaticField("DEFAULT_NORMAL_CACHE_SIZE") : 0
        );
    }


    private static int getPrivateStaticField(String name) {
        try {
            Field f = PooledByteBufAllocator.DEFAULT.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return f.getInt(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
