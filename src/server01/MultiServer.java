package server01;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiServer {
    private List<myChannel> all=new ArrayList<myChannel>();

    public static void main(String[] args) throws IOException {
       new MultiServer().start();
    }

    public void start() throws IOException {
        //创建服务器 指定端口
        ServerSocket server = new ServerSocket(8080);
        while (true){
            Socket client=server.accept();
            myChannel channel = new myChannel(client);
            all.add(channel);
            new Thread(channel).start();//一条道路
        }
    }
    //方法内部类：便于访问私有的信息
    class myChannel implements Runnable{
        private DataInputStream dis;
        private  DataOutputStream dos;
        private boolean isRunning = true;
        private String name;
        public myChannel(Socket client){
            try {
                dis=new DataInputStream(client.getInputStream());
                dos=new DataOutputStream(client.getOutputStream());
                this.name=dis.readUTF();
                this.send("（系统消息）欢迎进入聊天室");
                sendOthers("进入了聊天室",true);
            } catch (IOException e) {
                //e.printStackTrace();
                CloseUtil.closeAll(dis,dos);
                isRunning=false;
            }
        }
        private String receive(){
            String msg="";
            try {
                msg=dis.readUTF();
            } catch (IOException e) {
                //e.printStackTrace();
                CloseUtil.closeAll(dis,dos);
                isRunning=false;
                all.remove(this);//移除自身
            }
            return msg;
        }

        public void send(String msg){
            if(null==msg||msg.equals("")){
                return;
            }
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
                // e.printStackTrace();
                CloseUtil.closeAll(dos);
                isRunning=false;
            }

        }


        //发送给其他客户端
        private void sendOthers(String msg,boolean isSystem){

            if(msg.startsWith("@")&&msg.indexOf(":")>=0){
                //获取name
                String name=msg.substring(1,msg.indexOf(":"));
                String content=msg.substring(msg.indexOf(":")+1);
                for (myChannel other:all
                        ) {
                    if(other.name.equals(name))
                    {
                        other.send("（私聊消息）"+this.name+":"+content);
                        return;
                    }
                }

                return;
            }
            //遍历容器
            for (myChannel other:all
                 ) {
                if(other==this)
                    continue;
                //发送给他人
                if(isSystem){
                    other.send("（系统消息）"+this.name+msg);
                }else
                other.send(this.name+":"+msg);
            }
        }

        @Override
        public void run() {
            while (isRunning){
                sendOthers(receive(),false);
            }
        }
    }



}
