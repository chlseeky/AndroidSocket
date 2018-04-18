# AndroidSocket

使用时定义类继承SocketClient，例如：

public class SubSocketClient extends SocketClient {

    public void connectToServer() {
        this.connect();
    }

    public SubSocketClient(SocketClientAddress address) {
        this.address = address;
        getHeartBeatHelper().setHeartBeatInterval(1000 * 10);
        getHeartBeatHelper().setRemoteNoReplyAliveTimeout(-1);
        getHeartBeatHelper().setSendData(CTPHeartBeat());
        getSocketPacketHelper().setSegmentLength(8 * 1024);//8196
        registerSocketClientDelegate(new SocketClientDelegate() {
            @Override
            public void onConnected(SocketClient client) {
                LogUtils.e("连接：", "SubSocketClient Local onConnected "+ client.getAddress().getRemotePort()+" "+client.getAddress().getRemoteIP());

                doReLogin();
            }

            @Override
            public void onDisconnected(SocketClient client) {
                LogUtils.e("断开", "SubSocketClient Local onDisconnected");

                if(!isHandleDisconnect){
                    connect();
                }
            }

            @Override
            public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {

            }
        });

        registerSocketClientSendingDelegate(new SocketClientSendingDelegate() {

            @Override
            public void onSendPacketBegin(SocketClient client, SocketPacket packet) {

            }

            @Override
            public void onSendPacketCancel(SocketClient client, SocketPacket packet) {

            }

            @Override
            public void onSendPacketEnd(SocketClient client, SocketPacket packet) {

            }

            @Override
            public void onSendPacketProgress(SocketClient client, SocketPacket packet, float progress) {

            }
        });

        registerSocketClientReceiveDelegate(new SocketClientReceiveDelegate() {
            @Override
            public void onResponse(SocketClient client, @NonNull SocketResponsePacket responsePacket) {
                OnSubClientMsg(responsePacket.getData());
            }

            @Override
            public void onHeartBeat(SocketClient socketClient) {

            }

            @Override
            public void onHaveData(InputStream inputStream) {

            }
        });
    }

    //处理服务器传给客户端的数据
    public void OnSubClientMsg(byte[] buf) {
        
    }
}
