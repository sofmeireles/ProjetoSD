package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class ConsultarDetalhesEleicaoAction extends ActionSupport implements SessionAware {

    private int myindex = -1;
    private int num = -1;
    private String desc = null;
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
            desc = this.getHeyBean().detalhesEleicao(this.num+1);

            if (!desc.equals("")) {
                myindex = this.num;
                return SUCCESS;
            }
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

    public int getMyindex() {
        return myindex;
    }

    public void setMyindex(int myindex) {
        this.myindex = myindex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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