package server4;

/**
 *  <servlet>
 *     <servlet-name>login</servlet-name>
 *     <servlet-class>server4.LoginServlet</servlet-class>
 *     </servlet>
 */

public class Entity {
    private String name;
    private String clz;

    public void setClz(String clz) {
        this.clz = clz;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClz() {
        return clz;
    }

    public String getName() {
        return name;
    }
}
