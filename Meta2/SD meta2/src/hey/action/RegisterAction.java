package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class RegisterAction extends ActionSupport implements SessionAware {
    String nome= null;
    String func= null;
    String password= null;
    String departamento= null;
    String telefone = null;
    String morada= null;
    String numCC = null;
    String valCC = null;
    private Map<String, Object> session;


    @Override
    public String execute() throws RemoteException {
        String r = null;
        String id=null;
        //se os dados estiverem preenchidos
        if(!nome.equals("") && !password.equals("") && !func.equals("") && !departamento.equals("") && !telefone.equals("") && !morada.equals("") && !numCC.equals("") && !valCC.equals("")) {

            //se tiver o id vai buscar
            if(this.session.containsKey("id")){
                id = this.session.get("id").toString();
                System.out.println("rA id "+id);
            }
            //se tiver ID e nao tiver nCC
            if(id!=null && !session.containsKey("numCC")){
                //adiciona a nova informacao ao user previamente criado
                r = this.getHeyBean().addInfoFB(id,func,password,departamento,Integer.parseInt(telefone),morada,Integer.parseInt(numCC),valCC);
                System.out.println(r);
                if(r.equals("Pessoa nao encontrada")){

                    return ERROR;
                }
                else{
                    this.session.put("numCC",numCC);
                    return SUCCESS;
                }
            }
            //regista um novo utilizador
            String type = this.getHeyBean().registaEleitor(nome, func, password, departamento.toUpperCase(), Integer.parseInt(telefone), morada, Integer.parseInt(numCC), valCC);
            System.out.println(type);
            if(type.equals("SUCCESS"))
                return SUCCESS;
            else
                return ERROR;
        }
        return ERROR;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome(String nome){
        return this.nome;
    }

    public void setFunc(String func) {
        this.func = func;
    }


    public String getFunc() {
        return func;
    }

    public String getPassword() {
        return password;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getMorada() {
        return morada;
    }

    public String getNumCC() {
        return numCC;
    }

    public String getValCC() {
        return valCC;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setNumCC(String numCC) {
        this.numCC = numCC;
    }

    public void setValCC(String valCC) {
        this.valCC = valCC;
    }

    public String getNome() {
        return nome;
    }

    public Map<String, Object> getSession() {
        return session;
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