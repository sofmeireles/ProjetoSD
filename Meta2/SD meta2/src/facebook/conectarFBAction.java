package facebook;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
public class conectarFBAction extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    public String code = null;
    private String CC = null;

    public String execute() throws RemoteException {
        ArrayList<String> res = new ArrayList<>();

        this.session.put("code",code);
        this.session.put("admin",false);

        if(CC==null){
            res = this.getHeyBean().conectaFb(code,CC);
            System.out.println(res.get(0));
            if(res.get(0).equals("Pessoa adicionada")){
                this.session.put("id",res.get(1));
                return LOGIN; //é preciso registar com os novos dados
            }
            else{
                if(!session.containsKey("numCC")){
                    return LOGIN;
                }
                this.session.put("numCC",Integer.parseInt(res.get(2)));
                return SUCCESS;
            }
        }
        return ERROR;



    }

    public Map<String, Object> getSession() {
        return session;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCC() {
        return CC;
    }

    public void setCC(String CC) {
        this.CC = CC;
    }



    public HeyBean getHeyBean() {
        if(!session.containsKey("heyBean")) {
            this.setHeyBean(new HeyBean());
        }
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
