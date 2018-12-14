package server4;


import java.util.HashMap;
import java.util.Map;

/**
 * 上下文
 */
public class ServletContext {
    //为每一个servlet取一个别名
    //login --> loginServlet,一个servlet针对多个mapping

    private Map<String, String> servlet;
    private Map<String,String> mapping;

    ServletContext(){
        servlet=new HashMap<String, String>();
        mapping=new HashMap<String,String>();
    }

    public Map<String, String> getServlet(){
        return servlet;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    public void setServlet(Map<String, String> servlet) {
        this.servlet = servlet;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }
}
