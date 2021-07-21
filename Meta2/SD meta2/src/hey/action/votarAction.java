package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class votarAction extends ActionSupport implements SessionAware{
    private Map<String, Object> session;
    private int numE = -1;
    private int numCC = -1;
    private int tam = -1;
    private int numero = -1;

    @Override
    public String execute() throws RemoteException {
        //verifca se esta logged in
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }


        if(session.containsKey("numCC")) {
            numCC = (Integer) session.get("numCC");
        }
        //vota
        if (this.numE != -1 && numCC != -1 && tam!=-1) {
            this.getHeyBean().votar(numE+1, tam+1, numCC, 999);
            System.out.println("Voto guardado com sucesso");
            return SUCCESS;
        }else
            return ERROR;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public int getTam() {
        return tam;
    }

    public void setTam(int tam) {
        this.tam = tam;
    }

    public int getNumE() {
        return numE;
    }

    public void setNumE(int numE) {
        this.numE = numE;
    }

    public int getNumCC() {
        return numCC;
    }

    public void setNumCC(int numCC) {
        this.numCC = numCC;
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