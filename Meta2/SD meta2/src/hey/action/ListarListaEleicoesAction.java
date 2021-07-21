package hey.action;

import classes.Lista;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class ListarListaEleicoesAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private int nEleicao = -1;
    private int numCC = -1;
    private int numE = -1;
    private ArrayList<Lista> listas;
    private int tam = -1;
    private int numero = -1;


    @Override
    public String execute() throws RemoteException {
        //verifca se esta logged in
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }

        if (session.containsKey("numCC")) {
            numCC = (Integer) session.get("numCC");

            //vai buscar as listas candidatas pertencentes a eleicao escolhida
            if (numE != -1) {
                listas = this.getHeyBean().listarListasCandidatas(numE+1, numCC);

                tam=listas.size();
                return SUCCESS;
            } else
                return ERROR;
        } else
            return ERROR;
    }

    public int getTam() {
        return tam;
    }

    public void setTam(int tam) {
        this.tam = tam;
    }

    public int getnEleicao() {
        return nEleicao;
    }

    public void setnEleicao(int nEleicao) {
        this.nEleicao = nEleicao;
    }

    public ArrayList<Lista> getListas() {
        return listas;
    }

    public void setListas(ArrayList<Lista> listas) {
        this.listas = listas;
    }

    public int getNumCC() {
        return numCC;
    }

    public void setNumCC(int numCC) {
        this.numCC = numCC;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public int getNumE() {
        return numE;
    }

    public void setNumE(int numE) {
        this.numE = numE;
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