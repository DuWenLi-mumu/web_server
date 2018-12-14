package server4;

//import server2.Request;
//import server2.Response;

/**
 *抽象为一个父类
 */
public abstract class Servlet {
    public void service(Request req, Response rep) throws Exception {
        //this.doGet(req,rep);
        this.doPost(req,rep);
    }
    public abstract void doGet(Request req, Response rep) throws Exception;
    public abstract void doPost(Request req, Response rep)throws Exception;
}
