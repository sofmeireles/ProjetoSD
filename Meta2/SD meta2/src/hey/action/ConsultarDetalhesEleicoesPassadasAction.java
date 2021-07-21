package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class ConsultarDetalhesEleicoesPassadasAction extends ActionSupport implements SessionAware{

    private int num = -1;
    private String det = null;
    private Map<String, Object> session;
    private int numero = -1;

    @Override
    public String execute() throws RemoteException {
        //verifica se esta logged in
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }

        //vai buscar os detalhes da eleicao escolhida
        if (this.num != -1) {
            System.out.println(num);
            det = this.getHeyBean().consultarDetalhesEleicoesPassadas(this.num+1);

            if (!det.equals(""))
                return SUCCESS;
            else
                return ERROR;
        }else
            return ERROR;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDet() {
        return det;
    }

    public void setDet(String det) {
        this.det = det;
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
