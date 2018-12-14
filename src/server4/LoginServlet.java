package server4;

public class LoginServlet extends Servlet {

    @Override
    public void doGet(Request req, Response rep) throws Exception {
        String name=req.getParameter("uname");
        String pwd = req.getParameter("pwd");
        if(login(name,pwd,rep)){
            //rep.println("login success");
            rep.println("login:\nwelcome: ").println(req.getParameter("uname")).println("go back");
        }else {
            rep.println("login failed");
        }


//        rep.println("<html lang=\"en\">\n" +
//                "<head>\n" +
//                "    <title>welcome back</title>\n" +
//                "</head>\n" +
//                "<body>");
//        rep.println("login:\nwelcome: ").println(req.getParameter("uname")).println("go back");
//        rep.println("</body></html>");
    }

    public boolean login(String name,String pwd,Response rep){
        if(name==null||pwd==null) {
            rep.println("please input full information");
            return false;
        }
        return name.equals("duwenli")&&pwd.equals("123");

    }
    @Override
    public void doPost(Request req, Response rep) throws Exception {
        String name=req.getParameter("uname");
        String pwd = req.getParameter("pwd");
        if(login(name,pwd,rep)){
            //rep.println("login success");
            //rep.println("login:\nwelcome: ").println(req.getParameter("uname")).println("go back");
            rep.println("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <title>welcome duwenli back</title>\n" +
                "</head>\n" +
                "<body>");
            rep.println("login:\nwelcome: ").println(req.getParameter("uname")).println("go back");
            rep.println("</body></html>");
        }else {
            rep.println("pwd not match login failed");
        }
    }
}
