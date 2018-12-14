package server4;

import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WebApp {
    private static ServletContext context;
    static{
        try {
            System.out.println("webapp");
            //获取解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //获取解析器
            SAXParser sax = factory.newSAXParser();
            //指定xml+处理器
            WebHandler web = new WebHandler();
            sax.parse(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("server4/web.xml"), web);


            //将list 转成Map
            context =new ServletContext();
            Map<String,String> servlet =context.getServlet();

            //servlet-name  servlet-class
            for(Entity entity:web.getEntityList()){
                servlet.put(entity.getName(), entity.getClz());
            }

            //url-pattern servlet-name
            Map<String,String> mapping =context.getMapping();
            for(Mapping mapp:web.getMappingList()){
                List<String> urls =mapp.getUrlPattern();
                for(String url:urls ){
                    mapping.put(url, mapp.getName());
                }
            }
            System.out.println("webapp-end");
        } catch (Exception e) {

        }


    }

//        context=new ServletContext();
//        Map<String,String> mapping=context.getMapping();
//        mapping.put("/login","login");
//        mapping.put("/log","login");
//        mapping.put("/reg","register");
//        Map<String, String>servlet=context.getServlet();
//        servlet.put("login","server3.LoginServlet");
//        servlet.put("register","server3.RegisterServlet");

    public static Servlet getServlet(String url) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(null==url||(url=url.trim()).equals("")){
            return null;
        }
        //根据字符串创建对象
        String name=context.getServlet().get(context.getMapping().get(url));
        System.out.println(name);
        return (Servlet)Class.forName(name).newInstance();//确保空构造存在
        //return context.getServlet().get(context.getMapping().get(url));
    }
}
