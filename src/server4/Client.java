package server4;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    //创建客户端 指定服务阿咪器端+端口
    public static void main(String[] args) throws IOException {
        System.out.println("请输入昵称：");
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String name=br.readLine();
        if(name.equals(""))
            return;

        //创建客户端 指定服务器+端口 此时在连接
        Socket client = new Socket("localhost", 8080);
        new Thread(new Send(client,name)).start();
        new Thread(new Recieve(client)).start();
    }


}
