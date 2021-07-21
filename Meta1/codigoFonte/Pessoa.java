
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Pessoa implements Serializable {

    private String nome;
    private String func;
    private String password;
    private String departamento;
    private int nTelefone;
    private String morada;
    private int numCC;
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
}
