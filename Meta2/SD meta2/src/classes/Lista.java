package classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Lista implements Serializable {

    private String nomeLista;
    private String type;
    private ArrayList<Pessoa> candidatos;
    private int nVotos;

    public Lista(String nomeLista, String type,ArrayList<Pessoa> candidatos) {
        this.nomeLista = nomeLista;
        this.type = type;
        this.candidatos= candidatos;
        this.nVotos=0;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public int getnVotos() {
        return nVotos;
    }

    public void setnVotos(int nVotos) {
        this.nVotos = nVotos;
    }
}
