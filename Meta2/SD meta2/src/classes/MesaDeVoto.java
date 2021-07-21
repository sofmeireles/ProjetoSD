package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class MesaDeVoto implements Serializable {

    private String departamento;

    private ArrayList<Eleicao> eleicoes;

    private ArrayList<Eleicao> eleicoesAtuais;

    private ArrayList<TerminalDeVoto> terminais;

    private boolean active;

    public MesaDeVoto(String departamento) {
        this.departamento = departamento;
        this.eleicoes = new ArrayList<>();
        this.terminais = new ArrayList<>();
        this.eleicoesAtuais = new ArrayList<>();
        this.active = false;
    }

    public MesaDeVoto(String dep, Eleicao eleicao){
        this.departamento = dep;
        this.terminais = new ArrayList<>();
        this.eleicoes = new ArrayList<>();
        this.eleicoesAtuais = new ArrayList<>();
        eleicoes.add(eleicao);
        this.active = false;
    }


    //ativar um dado terminal
    public String ativaTerminal(int id){
        for(TerminalDeVoto terminal : terminais){
            if(id==terminal.getId()){
                terminal.setActive(true);
                return "Terminal já existente ativado";
            }
        }
        TerminalDeVoto terminal = new TerminalDeVoto(id);
        terminais.add(terminal);
        return "Terminal adicionado com sucesso";
    }

    //desativar um dado terminal
    public String desativaTerminal(int id){
        for(TerminalDeVoto terminal : terminais){
            if(id==terminal.getId()){
                terminal.setActive(false);
                return "Terminal desativado";
            }
        }
        return "Não foi possível desativar o terminal";
    }

    public void addEleicao(Eleicao eleicao){
        eleicoes.add(eleicao);
    }

    public void addEleicaoAtual(Eleicao eleicao){
        eleicoesAtuais.add(eleicao);
    }

    public String getDepartamento() {
        return departamento;
    }

    public ArrayList<Eleicao> getEleicoes() {
        return eleicoes;
    }

    public void setEleicoes(ArrayList<Eleicao> eleicoes) {
        this.eleicoes = eleicoes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<TerminalDeVoto> getTerminais() {
        return terminais;
    }

    public ArrayList<Eleicao> getEleicoesAtuais() {
        return eleicoesAtuais;
    }

}
