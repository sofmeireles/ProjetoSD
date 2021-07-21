package classes;
import java.io.Serializable;
import java.util.ArrayList;

public class Pessoa implements Serializable {

    private String nome;
    private String func;
    private String password;
    private String departamento;
    private int nTelefone;
    private String morada;
    private int numCC;
    private String fbID;
    private String valCC;
    private ArrayList<Eleicao> eleicoesVotadas;
    private ArrayList<Voto> listaDeVotos;


    public Pessoa(String nome, String func, String password, String departamento, int nTelefone, String morada, int numCC, String valCC){

        this.nome = nome;
        this.func = func;
        this.password = password;
        this.departamento = departamento;
        this.nTelefone = nTelefone;
        this.morada = morada;
        this.numCC = numCC;
        this.valCC = valCC;
        this.fbID = null;
        this.eleicoesVotadas=new ArrayList<>();
        this.listaDeVotos = new ArrayList<>();
    }

    public Pessoa(String fbID, String nome){
        this.fbID=fbID;
        this.nome=nome;
        this.password = null;
        this.departamento = null;
        this.nTelefone = -1;
        this.morada = null;
        this.numCC = 0;
        this.valCC = null;
        this.eleicoesVotadas=new ArrayList<>();
        this.listaDeVotos = new ArrayList<>();

    }

    public String getFunc() {
        return func;
    }

    public String getPassword() {
        return password;
    }

    public String getDepartmento() {
        return departamento;
    }

    public int getNumCC() {
        return numCC;
    }

    public ArrayList<Eleicao> getEleicoesVotadas() {
        return eleicoesVotadas;
    }

    public void setEleicoesVotadas(ArrayList<Eleicao> eleicoesVotadas) {
        this.eleicoesVotadas = eleicoesVotadas;
    }

    public ArrayList<Voto> getListaDeVotos() {
        return listaDeVotos;
    }

    public void setListaDeVotos(ArrayList<Voto> listaDeVotos) {
        this.listaDeVotos = listaDeVotos;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getDepartamento() {
        return departamento;
    }

    public int getnTelefone() {
        return nTelefone;
    }

    public String getMorada() {
        return morada;
    }

    public String getValCC() {
        return valCC;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setnTelefone(int nTelefone) {
        this.nTelefone = nTelefone;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setNumCC(int numCC) {
        this.numCC = numCC;
    }

    public void setValCC(String valCC) {
        this.valCC = valCC;
    }
}
