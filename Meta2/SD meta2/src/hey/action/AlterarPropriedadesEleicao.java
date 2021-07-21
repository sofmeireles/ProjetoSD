package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class AlterarPropriedadesEleicao extends ActionSupport implements SessionAware {

    private int opcao = -1;
    private int num = -1;
    private String novo = null;
    private String desc = null;
    private Map<String, Object> session;
    private int myindex = -1;
    private int numero = -1;


    @Override
    public String execute() throws RemoteException {
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }

        if (this.num != -1) {
            System.out.println("opcao" + opcao);

            //altera titulo
            if(opcao==1){
                this.getHeyBean().editarEleicao(num+1 ,"titulo",novo);
            }
            //altera descricao
            else if(opcao==2){
                this.getHeyBean().editarEleicao(num+1 ,"desc",novo);
            }
            //altera data de inicio
            else if(opcao==3){
                this.getHeyBean().editarEleicao(num+1 ,"dataInicio",novo);
            }
            //altera data de fim
            else if(opcao==4){
                this.getHeyBean().editarEleicao(num+1 ,"dataFim",novo);
            }
            //altera tipo
            else if(opcao==5){
                if(Integer.parseInt(novo)==1){
                    this.getHeyBean().editarEleicao(num+1 ,"type","EST");
                }
                else if(Integer.parseInt(novo)==2){
                    this.getHeyBean().editarEleicao(num+1 ,"type","DOC");
                }
                else if(Integer.parseInt(novo)==3){
                    this.getHeyBean().editarEleicao(num+1 ,"type","FUNC");
                }
                else{
                    this.getHeyBean().editarEleicao(num+1 ,"type","TODOS");
                }
            }
            //altera departemento
            else if(opcao==6){
                this.getHeyBean().editarEleicao(num+1 ,"dep",novo);
            }
            myindex = this.num;
            System.out.println("Alterado com sucesso");
            desc = this.getHeyBean().detalhesEleicao(num+1);

            return SUCCESS;
        }
        else
            return ERROR;
    }

    public int getOpcao() {
        return opcao;
    }

    public void setOpcao(int opcao) {
        this.opcao = opcao;
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

    public String getNovo() {
        return novo;
    }

    public void setNovo(String novo) {
        this.novo = novo;
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