package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class AddElementoAction  extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private int elem = -1;
    private int num = -1;
    private String nome = null;
    private String func = null;
    private String nomeE = null;
    private String funcE = null;
    private int numero = -1;
    private ArrayList<Integer> listaElementos;
    private int myindex = -1;
    private String frase = "";

    @Override
    public String execute() throws RemoteException {
        //verifica se está logged in
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }

        if (num != -1 && !nomeE.equals("") && !funcE.equals("")){
            frase = " ";
            //verifica se a lista existe
            if(listaElementos==null || !session.containsKey("lista")){
                listaElementos = new ArrayList<>();
            }

            //vai buscar a lista
            if(session.containsKey("lista")){
                listaElementos = (ArrayList<Integer>) session.get("lista");
            }

            //adiciona novos candidatos
            if(elem!=-1 && !listaElementos.contains(elem)){
                listaElementos.add(elem);
                frase = frase + " " + elem + " ";
                System.out.println("elemento adicionado");
                myindex = num;
                session.put("lista", listaElementos);
                return SUCCESS;
            }else
                myindex = num;
            return ERROR;
        }else
            myindex = num;
        return ERROR;
    }

    private void printLista(ArrayList<Integer> lista){
        for(Integer el : lista){
            System.out.println(el);
        }
    }

    public int getElem() {
        return elem;
    }

    public void setElem(int elem) {
        this.elem = elem;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNomeE() {
        return nomeE;
    }

    public void setNomeE(String nomeE) {
        this.nomeE = nomeE;
    }

    public String getFuncE() {
        return funcE;
    }

    public void setFuncE(String funcE) {
        this.funcE = funcE;
    }

    public ArrayList<Integer> getListaElementos() {
        return listaElementos;
    }

    public void setListaElementos(ArrayList<Integer> listaElementos) {
        this.listaElementos = listaElementos;
    }

    public int getMyindex() {
        return myindex;
    }

    public void setMyindex(int myindex) {
        this.myindex = myindex;
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
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

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }



}