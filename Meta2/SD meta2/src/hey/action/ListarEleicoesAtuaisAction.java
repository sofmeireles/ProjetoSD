package hey.action;

import classes.Eleicao;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class ListarEleicoesAtuaisAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;
    private ArrayList<Eleicao> info;
    private int numCC = -1;

    @Override
    public String execute() throws RemoteException {
        //verifica se esta logged in
        if(!session.containsKey("numCC") && !session.containsKey("id")){
            return LOGIN;
        }

        if(session.containsKey("numCC")) {
            numCC = (Integer) session.get("numCC");

        }else{
            return INPUT;
        }

        //vai buscar a lista de eleicoes atuais disponiveis para o utilizador votar
        info = this.getHeyBean().listarEleicoesAtuaisNCC(numCC);
        if (info.size() != 0){
            return SUCCESS;
        }
        else {
            System.out.println("Nao existem eleicoes atuais");
            return ERROR;
        }
    }

    public ArrayList<Eleicao> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<Eleicao> info) {
        this.info = info;
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