package server4;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 封装响应信息
 */
public class Response {
    //存储头信息
    private StringBuilder headInfo;
    //存储两个常量
    public static final String CRLF="\r\n";
    public static final String BLANK=" ";
    //存储正文长度
    private int len=0;
    //正文
    private StringBuilder content;
    //流
    private BufferedWriter bw;
    /**
     * 构建响应头
     */
    public Response(){
        headInfo=new StringBuilder();
        content=new StringBuilder();
        len=0;
    }

    public Response(OutputStream os){
        this();
        bw = new BufferedWriter(new OutputStreamWriter(os));
    }

    public Response(Socket client){
        this();
        try {
            bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            headInfo=null;
            e.printStackTrace();
        }
    }

    /**
     * 构建正文和回车
     * @param info
     * @return
     */
    public Response println(String info){
        content.append(info).append(CRLF);
        len+=(info+CRLF).getBytes().length;
        return this;
    }

    private void createHeadInfo(int code){
        //1） Http协议版本，状态代码，描述
        headInfo.append("HTTP/1.1").append(BLANK).append("200").append(BLANK).append("OK").append(CRLF);
        switch (code){
            case 200:
                headInfo.append("OK");
                break;
            case 404:
                headInfo.append("NOT FOUND");
                break;
            case 505:
                headInfo.append("SERVER ERROR");
                break;
        }
        headInfo.append(CRLF);
        //2) 响应头（Response Head）
        headInfo.append("Server:bjsxt Server/0.0.1").append(CRLF);
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Content-type:text/html;charset=GBK").append(CRLF);
        //正文长度
        headInfo.append("Content-Length:").append(len).append(CRLF);
        headInfo.append(CRLF);

    }
    //推送到客户端
    void pushToClient(int code) throws IOException {
        if(headInfo==null){
            code=500;
        }
        createHeadInfo(code);
        //头信息加分隔符
        bw.append(headInfo.toString());

        //正文
        bw.append(content.toString());
        bw.flush();
    }

    public void close(){
        CloseUtil.closeIO(bw);
    }
}
