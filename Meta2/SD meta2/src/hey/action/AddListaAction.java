package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class AddListaAction  extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private int elem = -1;
    private int num = -1;
    private String nome = null;
    private String func = null;
    private String nomeE = null;
    private String funcE = null;
    private int numero = -1;
    private ArrayList<Integer> lista;
    private int myindex = -1;

    @Override
    public String execute() throws RemoteException {

        //verifica se esta logged in
        if(!session.containsKey("numCC")) {
            numero = (Integer) session.get("numCC");
            return INPUT;
        }

        //vai buscar a lista de listas
        if(session.containsKey("lista")){
            lista = (ArrayList<Integer>) session.get("lista");
        }else{
            lista = new ArrayList<>();
        }

        System.out.println("LISTA " + lista.size());

        //adiciona os membros à lista
        if (num != -1 && !nomeE.equals("") && !funcE.equals("")){
            int[] array = new int[lista.size()];
            String membros = "";

            for(int i = 0; i < lista.size(); i++){
                array[i]= lista.get(i);
                membros+=array[i] + " ";
            }

            //cria a lista candidata
            String r = getHeyBean().criaLista(this.nomeE, this.funcE, array, this.num + 1);

            if (r.equals("Lista inserida com sucesso")) {
                myindex = num;
                session.remove("lista");
                return SUCCESS;
            }else
                return ERROR;
        }else
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

    public ArrayList<Integer> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Integer> listaElementos) {
        this.lista = lista;
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