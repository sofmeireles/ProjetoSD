package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;


public class CriarEleicaoAction extends ActionSupport implements SessionAware {
    String dataInicio = null;
    String dataFim = null;
    String titulo = null;
    String descricao = null;
    String tipo = null;
    String departamento = null;
    private Map<String, Object> session;
    private int numero = -1;

    @Override

    public String execute() throws RemoteException {
        //verifica se esta logged in
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }

        //cria uma nova eleicao
        if (!dataInicio.equals("") && !dataFim.equals("") && !titulo.equals("") && !descricao.equals("") && !tipo.equals("") && !departamento.equals("")){
            String type = this.getHeyBean().criarEleicao(dataInicio, dataFim, titulo, descricao, tipo, departamento);
            if (type.equals("CREATED")){
                return SUCCESS;
            }
            else
                return "ERROR";
        }
        else
            return "ERROR";
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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