import android.support.annotation.NonNull;

import com.bylh.yyb.follow.net.tcp.SocketClient;

/**
 * SocketClientDelegate
 * Feature:
 */
public interface SocketClientDelegate {
    void onConnected(SocketClient client);
    void onDisconnected(SocketClient client);
    void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket);

    class SimpleSocketClientDelegate implements SocketClientDelegate {
        @Override
        public void onConnected(SocketClient client) {

        }

        @Override
        public void onDisconnected(SocketClient client) {

        }

        @Override
        public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {

        }
    }
}
