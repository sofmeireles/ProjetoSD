package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class PassaIndexAction extends ActionSupport implements SessionAware {

    private int opcao = -1;
    private int num = -1;
    private String mesas = null;
    private Map<String, Object> session;
    private int myindex = -1;
    private int numero = -1;

    @Override
    public String execute() throws RemoteException {
        //verifca se esta logged in
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }
        if (this.num != -1) {

            mesas = "";
            //vai buscar as mesas
            for(int i = 0; i < this.getHeyBean().listarEleicoesNaoComecadas().get(num).getMesas().size(); i++){
                mesas = mesas + this.getHeyBean().listarEleicoesNaoComecadas()
                        .get(num).getMesas().get(i)
                        .getDepartamento() + " \n";
            }

            System.out.println(this.getHeyBean().listarEleicoesNaoComecadas().get(num).getTitulo() +" "+ this.getHeyBean().listarEleicoesNaoComecadas().get(num).getMesas().size());
            System.out.println(mesas + " mesas");
            if(mesas.equals("")){
                mesas = "Nao Existe mesas adicionadas";
            }
            myindex = num;
            return SUCCESS;
        }else
            return ERROR;
    }


    public int getMyindex() {
        return myindex;
    }

    public void setMyindex(int myindex) {
        this.myindex = myindex;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMesas() {
        return mesas;
    }

    public void setMesas(String mesas) {
        this.mesas = mesas;
    }

    public int getOpcao() {
        return opcao;
    }

    public void setOpcao(int opcao) {
        this.opcao = opcao;
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