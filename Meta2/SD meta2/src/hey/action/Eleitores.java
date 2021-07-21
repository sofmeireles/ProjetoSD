package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class Eleitores extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    String info = null;
    private int num = -1;
    private int numero = -1;

    @Override
    public String execute() throws RemoteException {
        //verifica se esta logged in
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }

        //mostra o local de voto dos eleitores numa determinada eleicao
        if (this.num != -1) {
            info = this.getHeyBean().localVoto(num+1);
            System.out.println(info);
            if (!info.equals("")){
                return SUCCESS;
            }
            else
                return ERROR;
        }else
            return ERROR;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getNum() {
        return num;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

    public HeyBean getHeyBean() {
        if(!session.containsKey("heyBean"))
            try{
                this.setHeyBean(new HeyBean());
            }catch (Exception e){
                e.printStackTrace();
            }
        return (HeyBean) session.get("heyBean");
    }


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}