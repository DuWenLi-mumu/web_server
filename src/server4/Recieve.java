package server4;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Recieve implements Runnable{
    private DataInputStream dis;
    private boolean isRunning=true;

    public Recieve(){
    }
    public Recieve(Socket client){
        try {
            dis=new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            //e.printStackTrace();
            isRunning=false;
            CloseUtil.closeAll(dis);
        }
    }

    public String receive(){
        String msg="";
        try {
            msg=dis.readUTF();
        } catch (IOException e) {
            CloseUtil.closeAll(dis);
        }
        return msg;
    }
    @Override
    public void run() {
        String msg="";
        while (isRunning){
            msg=receive();
            if(!msg.equals(""))
                System.out.println(msg);
        }

    }
}
