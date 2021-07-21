package hey.action;


import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class PassaURLAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private String url;

    @Override
    public String execute() throws RemoteException {
        //vai buscar o url do facebook
        String url = this.getHeyBean().connectionURL();
        session.put("url", url);
        return SUCCESS;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HeyBean getHeyBean() {
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
