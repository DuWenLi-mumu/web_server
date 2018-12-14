package server4;

import java.io.IOException;
import java.net.Socket;

/**
 * 一个请求与响应，就一个此对象
 */
public class Dispatcher implements Runnable {
    private Socket client;
    private Request req;
    private Response rep;
    private int code=200;
    Dispatcher(Socket client){
        this.client=client;
        try {
            req=new Request(client.getInputStream());
            rep=new Response(client.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
            code=500;
            return;
        }



    }

    @Override
    public void run() {
        try {
            System.out.println(req.getUrl()+"");
            Servlet serv = WebApp.getServlet(req.getUrl());
            System.out.println(serv+"");
            if(null==serv){
                this.code=404;//找不到对应的处理
            }else
            serv.service(req,rep);
            rep.pushToClient(code);//推送到客户端
        } catch (Exception e) {
            //e.printStackTrace();
            this.code=500;
        }
        try {
            rep.pushToClient(500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        req.close();
        rep.close();
        CloseUtil.closeSocket(client);
    }
}
