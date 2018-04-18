import android.support.annotation.NonNull;

import com.bylh.yyb.follow.net.tcp.SocketClient;

import java.io.InputStream;

/**
 * SocketClientReceiveDelegate
 * Feature:
 */
public interface SocketClientReceiveDelegate {
//    /**
//     * 接收普通消息
////     * 与{@link SocketClientDelegate#onResponse(SocketClient, SocketResponsePacket)}相同，此回调同步在先前回调后执行
//     * @param client
//     * @param responsePacket
//     */
    void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket);

    /**
     * 接收到心跳包
     * @param socketClient
     */
    void onHeartBeat(SocketClient socketClient);

    public void onHaveData(InputStream inputStream);

    class SimpleSocketClientReceiveDelegate implements SocketClientReceiveDelegate {

        @Override
        public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {

        }

        @Override
        public void onHeartBeat(SocketClient socketClient) {

        }

        @Override
        public void onHaveData(InputStream inputStream) {

        }

    }
}
