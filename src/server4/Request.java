package server4;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Request {
    /**
     * 封装request
     */
    //请求方式
    private String method;
    //请求资源
    private String url;
    //请求参数
    private Map<String,List<String>> parameterMapValues;

    //内部
    public static final String CRLF="\r\n";
    public static final String BLANK=" ";
    private InputStream is;
    private String requestInfo;

    public Request(){
        method="";
        url="";
        parameterMapValues=new HashMap<String,List<String>>();
        requestInfo="";
    }

    public Request(InputStream is){
      this();
      this.is=is;

        int len = 0;
        try {
            byte[] data = new byte[20480];
            len = is.read(data);
            requestInfo=new String(data,0,len);
        } catch (IOException e) {
            return;
        }
        //分析请求信息
        parseRequestInfo();
    }
    private void parseRequestInfo(){
        /**
         * 分析请求信息
         */
        if((null == requestInfo)||(requestInfo=requestInfo.trim()).equals("")){
            return;
        }

        /**
         * 从信息的首行分解出：请求方式，请求路径，请求参数，get可能存在
         * 如果是post方式，请求参数可能在最后正文中
         *
         */
        String paramString ="";//接收请求参数
        //1.获取请求方式
        String firstLine = requestInfo.substring(0,requestInfo.indexOf(CRLF));
        int idx = requestInfo.indexOf("/");
        this.method=firstLine.substring(0,idx).trim();
        String urlStr = firstLine.substring(idx,firstLine.indexOf("HTTP/"));
        if(this.method.equalsIgnoreCase("post")){
            this.url= urlStr;
            paramString = requestInfo.substring(requestInfo.lastIndexOf(CRLF));
        }else if(this.method.equalsIgnoreCase("get")){
            if(urlStr.contains("?")){
                //查看是否存在参数
                String[] urlArray = urlStr.split("\\?");
                this.url=urlArray[0];
                paramString=urlArray[1];//接收请求参数
            }else {
                this.url=urlStr;
            }
        }

        //2.将请求参数封装到map中
        //不存在请求参数
        if(paramString.equals("")){
            return ;
        }
        parseParams(paramString);


    }
    private void parseParams(String paramString){
        //分割，将字符串转成数组
        StringTokenizer tokenizer=new StringTokenizer(paramString,"&");
        while(tokenizer.hasMoreTokens()){
            String keyValue = tokenizer.nextToken();
            String[] keyValues=keyValue.split("=");
            if(keyValues.length==2){

            }else if(keyValues.length==1){
                keyValues=Arrays.copyOf(keyValues,2);
                keyValues[1]=null;
            }

            String key = keyValues[0].trim();
            String value = (null==keyValues[1]?null:decode(keyValues[1].trim(),"gbk"));

            //转换成map 分拣
            if(!parameterMapValues.containsKey(key)){
                parameterMapValues.put(key,new ArrayList<String>());
            }

            List<String> values = parameterMapValues.get(key);
            values.add(value);


        }
    }

    private String decode(String value,String code){
        try {
            return java.net.URLDecoder.decode(value,code);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 根据页面的name获取对应的多个值
     * @param
     */

    public String[] getParameterValues(String name){
        List<String> values = null;
        values=parameterMapValues.get(name);
        if(values==null){
            return null;
        }else {
            return values.toArray(new String[0]);
        }

    }

    /**
     * 根据页面的name获取对应的值
     * @param
     */

    public String getParameter(String name){
        String[] values = getParameterValues(name);
        if(null==values){
            return null;
        }
        else {
            return values[0];
        }

    }

    public String getUrl(){
        return url;
    }
    public void close(){
        CloseUtil.closeIO(is);
    }
}

