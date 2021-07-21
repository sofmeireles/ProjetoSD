
import java.rmi.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public interface RMI_S_I extends Remote {


    public String registaPessoa(String nome,String func, String password, String departamento, int nTelefone, String morada, int numCC, String valCC) throws RemoteException;

    public String criaEleicao(GregorianCalendar iniData, GregorianCalendar fimData, String titulo, String desc, String type, String dep) throws RemoteException;

    public String criaLista(String nomeLista, String type,int[] candidatosCC, int nEleicao) throws RemoteException;

    public String listaEleicoes() throws RemoteException;

    public String addMesa(int nEleicao,String dep)  throws RemoteException;

    public String deleteMesa(int nEleicao, String dep)  throws RemoteException;

    public String eleicaoInfo(int nEleicao) throws RemoteException;

    public String editaEleicao(int nEleicao, String oQue, String alteracao) throws RemoteException;

    public String vota(int nEleicao, int nlista, int nCC, int mesa) throws RemoteException;

    public String listaEleicoesProtocolo(int id, int nMesa) throws RemoteException;

    public String printListas(int nEleicao,int nCC) throws RemoteException;

    public String printEleicoesConcluidas() throws RemoteException;

    public String printEleicoesNComecadas() throws RemoteException;

    public String printInformacoesEleicoesConc(int nEleicao) throws RemoteException;

    public String printMesasVotoProtocolo() throws RemoteException;

    public Pessoa checkaPessoa(int numCC) throws RemoteException;

    public String printMesasDeVoto() throws RemoteException;

    public int numeroMesasDeVoto() throws RemoteException;

    public String saberLocal(int nEleicao) throws RemoteException;

    public String eleicoesAtuaisToString() throws RemoteException;

    public String printListasCandidatasProtocolo(int nEleicao, int nCC, int id) throws RemoteException;

    public String votosAtuaisMesa(int nEleicao, int nMesa) throws RemoteException;

    public String eleicoesAtuaisNaMesaoString(int nMesa) throws RemoteException;

    public String printMesasAtivas() throws RemoteException;

    public String ativarMesa(int nMesa) throws RemoteException;

    public String desativarMesa(int nMesa) throws RemoteException;

    public String ativaTerminal(int nMesa, int id) throws RemoteException;

    public String desativaTerminal(int nMesa, int id) throws RemoteException;

    }
