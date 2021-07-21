package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class AdicionarMesaDeVotoAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    String dep = null;
    String mesas = null;
    int num = -1;
    private int myindex = -1;
    private int numero = -1;

    @Override
    public String execute() throws RemoteException {

        //verifica se esta logged in
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }

        if(num != -1){
            //adiciona uma mesa de voto
            String resultado = this.getHeyBean().addMesaVoto(num+1,dep);

            if (resultado.equals("Mesa adicionada com sucesso")) {
                myindex = num;
                mesas = "";
                //adiciona a mesa
                for(int i = 0; i < this.getHeyBean().listarEleicoesNaoComecadas().get(num).getMesas().size(); i++){
                    mesas = mesas + this.getHeyBean().listarEleicoesNaoComecadas()
                            .get(num).getMesas().get(i)
                            .getDepartamento() + " \n";
                }
                System.out.println(mesas + " mesas");
                if(mesas.equals("")){
                    mesas = "Nao Existe mesas adicionadas";
                }
                return SUCCESS;
            }
            else if (resultado.equals("Eleicao adicionada à mesa com sucesso")){
                myindex = num;
                mesas = "";
                //adiciona a eleicao a mesa
                for(int i = 0; i < this.getHeyBean().listarEleicoesNaoComecadas().get(num).getMesas().size(); i++){
                    mesas = mesas + this.getHeyBean().listarEleicoesNaoComecadas()
                            .get(num).getMesas().get(i)
                            .getDepartamento() + " \n";
                }
                System.out.println(mesas + " mesas");
                if(mesas.equals("")){
                    mesas = "Nao Existe mesas adicionadas";
                }
                return SUCCESS;
            }else{
                myindex = num;
                mesas = "";
                for(int i = 0; i < this.getHeyBean().listarEleicoesNaoComecadas().get(num).getMesas().size(); i++){
                    mesas = mesas + this.getHeyBean().listarEleicoesNaoComecadas()
                            .get(num).getMesas().get(i)
                            .getDepartamento() + " \n";
                }
                System.out.println(mesas + " mesas");
                if(mesas.equals("")){
                    mesas = "Nao Existe mesas adicionadas";
                }
                return ERROR;
            }

        }else {
            System.out.println("Erro a adicionar mesa de voto.");
            myindex = num;
            return ERROR;
        }
    }

    public int getMyindex() {
        return myindex;
    }

    public void setMyindex(int myindex) {
        this.myindex = myindex;
    }


    public String getMesas() {
        return mesas;
    }

    public void setMesas(String mesas) {
        this.mesas = mesas;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public int getNum() {
        return num;
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