package rmiserver;

import classes.Dados;
import classes.Eleicao;
import classes.Lista;
import classes.Pessoa;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class RMIServer extends UnicastRemoteObject implements RMI_S_I,Runnable  {

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    public static Dados dados = new Dados();
    public static Scanner scan = new Scanner(System.in);
    private static final int PORT_NUMBER = 1099;
    private String appKey = "511066006682977";
    private String appSecret = "7e25ea19c0426bff1247d7570d257dad";


    public RMIServer() throws RemoteException {
        super();
        this.carregaDados();
    }
    final OAuth20Service service = new ServiceBuilder(appKey)
            .apiSecret(appSecret)
            .callback("https://localhost:8443/War/conectaFacebook.action")
            .build(FacebookApi.instance());

    //----------------------------------REST FACEBOOK------------------------------------

    //pede a coneccao do fb
    public String authorizationURLLogin(){
        // Obtain the Authorization URL
        String authorizationUrl = service.getAuthorizationUrl();
        return authorizationUrl;
    }

    //pede a coneccao do fb
    public String connectionURLLogin(){
        // Obtain the Authorization URL
        String authorizationUrl = service.getAuthorizationUrl();
        return authorizationUrl;
    }


    //adiciona a nova informacao ao user ligado pelo fb
    public String addInfoFB(String id, String func, String pass, String dep, int nTel, String morada, int nCC, String valCC ){

        String r = dados.addInfoPessoa(id,func,pass,dep,nTel,morada,nCC,valCC);

        System.out.println(r);
        printPessoas();
        guardaDados();
        return r;
    }

    //conecta ao facebook
    public ArrayList<String> conectaFb(String code,String username) throws RemoteException{
        ArrayList<String> array = new ArrayList<>();
        String r = null;
        String id = null;
        int nCC = -1;

        final OAuth2AccessToken accessToken;

        try {
            accessToken = service.getAccessToken(code);

            //pede ao fb os dados
            OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
            service.signRequest(accessToken, request);

            //recebe dados fb
            Response response = service.execute(request);
            System.out.println(response.getBody());

            JSONObject obj = (JSONObject) JSONValue.parse(response.getBody());

            String nome = obj.get("name").toString();
            id = obj.get("id").toString();
            System.out.printf("nome %s  id %s\n",nome,id);
            r = dados.encontraFB(id, nome);
            try{
                nCC = dados.getnCCfromId(id);
            }catch (Exception e){
                nCC=0;
            }
            System.out.println(nCC);


        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        array.add(r);
        array.add(id);
        array.add(String.valueOf(nCC));
        printPessoas();


        return array;
    }

    //vai buscar o nCC atraves do id
    public int getnCCFromID(String id) throws RemoteException{
        return dados.getnCCfromId(id);
    }


    //----------------------------------VERIFICACOES-------------------------------------

    //Verifica se o eleitor já existe
    public Pessoa checkaPessoa(int numCC) throws RemoteException{
        return dados.encontraPessoa(numCC);
    }

    //verifica se o utlizador ja esta na base de dados e se nao cria um novo
    public String encontraFB(String code, String nome){
        String r = dados.encontraFB(code,nome);
        //printPessoas();
        guardaDados();
        return r;
    }


    //Verifica se o server esta ativo
    public static int checkPing() throws RemoteException, NotBoundException {

        try {
            Registry r = LocateRegistry.getRegistry(PORT_NUMBER);
            //se 0 server estiver ativo da return 0
            if (r.lookup("project") != null) {
                return 0;
            }
        }catch (RemoteException re){
            //caso nao esteja ativo return 1
            System.out.println("Falha no ping");
            return 1;
        }
        return 1;
    }

    //-------------------------------------PRINTS----------------------------------------

    //imprime a lista de pessoas
    private void printPessoas(){
        for(Pessoa p : dados.getPessoas()){
            System.out.printf("Nome: %s, nCC: %d, idFB: %s\n",p.getNome(),p.getNumCC(),p.getFbID());
        }
    }


    //Imprime a informacao de uma dada eleicao
    public String eleicaoInfo(int nEleicao){
        String res = dados.printEleicaoInfo(nEleicao);
        guardaDados();
        return res;
    }

    //Imprime as listas candidatas a uma dada eleicao caso o eleitor ainda nao tenha votado
    public String printListas(int nEleicao,int nCC){
        String res = dados.printListasCandidatas(nEleicao,nCC);
        guardaDados();
        return res;
    }

    //Imprime as listas de determinada eleicao no formato do protocolo
    public String printListasCandidatasProtocolo(int nEleicao,int nCC, int id){
        String res = dados.printListasCandidatasProtocolo(nEleicao,nCC, id);
        guardaDados();
        return res;
    }

    //Imprime o numero de votos atual em dada mesa e eleicao
    public String votosAtuaisMesa(int nEleicao, int nMesa){
        return dados.eleitoresATM(nEleicao,nMesa);
    }

    //Imprime as eleicoes a decorrer numa determinada mesa
    public String eleicoesAtuaisNaMesaoString(int nMesa){
        return dados.eleicoesAtuaisNaMesaToString(nMesa);
    }

    //Imprime todas as eleicoes
    public String listaEleicoes(){
        String res = dados.eleicoesToString();
        guardaDados();
        return res;
    }

    public ArrayList<Eleicao> getListaEleicoes(){
        return dados.getEleicoes();
    }

    public ArrayList<Eleicao> getListaEleicoesConcluidas(){
        return dados.getEleicoesConcluidas();
    }

    public ArrayList<Eleicao> getListaEleicoesAtuais(){
        return dados.getEleicoesAtuais();
    }

    public ArrayList<Eleicao> getListaEleicoesAtuaisNCC(int nCC){
        ArrayList<Eleicao> atuais = dados.getEleicoesAtuais();
        ArrayList<Eleicao> nova = atuais;
        Pessoa p = null;
        ArrayList<Eleicao> elPessoas;
        for(Pessoa ps : dados.getPessoas()){
            if(ps.getNumCC()==nCC){
                p = ps;
            }
        }
        if(p==null){
            return atuais;
        }
        elPessoas = p.getEleicoesVotadas();
        for(Eleicao e:atuais){
            for(Eleicao ep : elPessoas){
                if(e.equals(ep)){
                    nova.remove(e);
                }
            }

        }
        return nova;
    }

    public ArrayList<Eleicao> getListaEleicoesNaoComecadas(){
        return dados.getEleicoesNaoComecadas();
    }


        //Imprime as eleicoes a decorrer no formato do protocolo
    public String listaEleicoesProtocolo(int id, int nMesa){
        return dados.eleicoesProtocolo(id,nMesa);
    }

    //Imprime as eleicoes concluidas
    public String printEleicoesConcluidas(){
        return dados.eleicoesConcluidasToString();
    }

    //Imprime as eleicoes que ainda nao comecaram
    public String printEleicoesNComecadas(){
        return dados.eleicoesNComecadasToString();
    }

    //Imprime as eleicoes que estao a decorrer
    public String eleicoesAtuaisToString(){
        return dados.eleicoesAtuaisToString();
    }

    //Imprime as informacoes das eleicoes concluidas
    public String printInformacoesEleicoesConc(int nEleicao){
        return dados.printInformacoes(nEleicao);
    }

    //Imprime as mesas de voto existentes
    public String printMesasDeVoto(){
        return dados.printMesasDeVoto();
    }

    //Imprime mesas de voto protocolo
    public String printMesasVotoProtocolo(){
        return dados.printMesasDeVotoProtocolo();
    }

    //Imprime o numero de mesas de voto
    public int numeroMesasDeVoto() {
        return dados.numeroMesasDeVoto();
    }

    //Imprime a informacao sobre o local e hora a que os eleitores
    // votaram numa dada eleicao
    public String saberLocal(int nEleicao){
        return dados.saberLocal(nEleicao);
    }

    //Imprime as mesas ativas e inativas
    public String printMesasAtivas(){
        return dados.printMesasAtivas();
    }


    //----------------------------ADICIONAR/REMOVER DADOS--------------------------------

    //Regista novo eleitor
    public String registaPessoa(String nome, String func, String password, String departamento, int nTelefone, String morada, int numCC, String valCC) throws RemoteException{
        String r = dados.inserePessoa(nome, func, password, departamento, nTelefone, morada, numCC, valCC);
        guardaDados();
        return r;
    }

    //Cria nova eleicao
    public String criaEleicao(GregorianCalendar iniData, GregorianCalendar fimData, String titulo, String desc, String type, String dep) throws RemoteException{
        dados.insereEleicao(iniData, fimData,titulo,desc, type, dep);
        guardaDados();
        return "Eleicao criada com sucesso.";
    }

    //Cria nova lista
    public String criaLista(String nome, String tipo,int[] candidatosCC, int nEleicao) throws RemoteException{
        String res = dados.insereLista(nome, tipo, candidatosCC, nEleicao);
        guardaDados();
        return res;
    }

    //print mesas de voto

    //Adiciona nova mesa de voto
    public String addMesa(int nEleicao, String dep){
        String res = dados.addMesa(nEleicao, dep);
        guardaDados();
        return res;
    }

    //Apaga mesa de voto
    public String deleteMesa(int nEleicao, String dep){
        String res = dados.delMesa(nEleicao, dep);
        guardaDados();
        return res;
    }

    //Remove mesas que não tenham eleicoes
    public void removeMesa(){
        String res = dados.removeMesa();
        guardaDados();
    }


    //--------------------------------ALTERAR DADOS------------------------------------

    //Adiciona um voto
    public String vota(int nEleicao, int nlista, int nCC, int mesa){
        GregorianCalendar time = new GregorianCalendar();
        String res = dados.addVoto(nEleicao,nlista,nCC, mesa, time);
        guardaDados();
        return res;
    }


    //Edita as informacoes de uma dada eleicao
    public String editaEleicao(int nEleicao, String oQue, String alteracao){
        String res = dados.editaEleicao(nEleicao,oQue,alteracao);
        guardaDados();
        return res;
    }

    //Carrega os dados do ficheiro de objetos
    public static void carregaDados(){
        File inputObjFile = new File("objectfile.obj");
        try {
            FileInputStream fis = new FileInputStream(inputObjFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            dados = (Dados) ois.readObject();
            ois.close();
            fis.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro não encontrado");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Dados carregados com sucesso");
    }

    //Guarda os dados no ficheiro de objetos
    public static void guardaDados(){
        File f = new File("objectfile.obj");

        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dados);
            oos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro a criar ficheiro." + ex);
        } catch (IOException ex) {
            System.out.println("Erro a escrever para o ficheiro." + ex);
        }
    }

    //Ativa uma determinada mesa
    public String ativarMesa(int nMesa){
        String res = dados.ativarMesa(nMesa);
        guardaDados();
        return res;
    }

    //Desativa uma determinada mesa
    public String desativarMesa(int nMesa){
        String res = dados.desativarMesa(nMesa);
        guardaDados();
        return res;
    }

    //Ativa um terminal
    public String ativaTerminal(int nMesa, int id){
        String res = dados.ativaTerminal(nMesa,id);
        guardaDados();
        return res;
    }

    //Desativa um terminal
    public String desativaTerminal(int nMesa, int id){
        String res = dados.desativaTerminal(nMesa,id);
        guardaDados();
        return res;
    }

    public ArrayList<Lista> getListasCandidatas(int nEleicao, int nCC){
        return dados.getListasCandidatas(nEleicao, nCC);
    }

    public ArrayList<Lista> getListasCandidatasAtuais(int nEleicao, int nCC){
        return dados.getListasCandidatasAtuais(nEleicao, nCC);
    }


    //Funcao da thread que vai colocando as eleicoes na lista certa:
    // eleicoes concluidas, eleicoes atuais, eleicoes que ainda nao comecaram
    @Override
    public void run() {
        while(true) {
            ArrayList<Eleicao> listaEleicoes = dados.getEleicoes();
            ArrayList<Eleicao> listaEleicoesConc = dados.getEleicoesConcluidas();
            ArrayList<Eleicao> listaEleicoesAtuais = dados.getEleicoesAtuais();
            ArrayList<Eleicao> listaEleicoesNComecadas = dados.getEleicoesNaoComecadas();
            GregorianCalendar agora = new GregorianCalendar();

            //ver se a eleicao ja acabou e remover das atuais e remover das mesas
            for (Iterator<Eleicao> iterator = listaEleicoesAtuais.iterator(); iterator.hasNext(); ) {
                Eleicao eleicao = iterator.next();
                if (eleicao.getFimData().before(agora)) {
                    System.out.println("arquivando eleicao " + eleicao.getTitulo());
                    //remover eleicao das mesas
                    dados.delEleicaoMesa(eleicao);
                    iterator.remove();
                    //atualizar a lista de eleicoes atuais
                    dados.setEleicoesAtuais(listaEleicoesAtuais);
                    //adicionar a eleicao as listas de eleicoes concluidas
                    listaEleicoesConc.add(eleicao);
                    dados.setEleicoesConcluidas(listaEleicoesConc);
                    //verificar se a mesa ficou sem eleicoes
                    removeMesa();

                    guardaDados();
                }
            }
            //ver se a eleicao ja comecou e adicionar as atuais e remover as que ainda nao comecaram
            for (Eleicao eleicao : listaEleicoes) {
                if (eleicao.getIniData().before(agora) && !listaEleicoesAtuais.contains(eleicao) && !listaEleicoesConc.contains(eleicao)){
                    if(eleicao.getListas().size()==0){
                        System.out.printf("eleicao %s não tem listas\n", eleicao.getTitulo());
                        //adicionar a eleicao as listas de eleicoes concluidas
                        listaEleicoesConc.add(eleicao);
                        dados.setEleicoesConcluidas(listaEleicoesConc);
                        //remover a eleicao da lista de eleicoes que ainda nao comecaram
                        listaEleicoesNComecadas.remove(eleicao);
                        dados.setEleicoesNaoComecadas(listaEleicoesNComecadas);
                        //verificar se a mesa ficou sem eleicoes
                        removeMesa();
                    }
                    else{
                        System.out.printf("eleicao %s a começar\n", eleicao.getTitulo());
                        //adicionar a eleicao a lista de eleicoes atuais
                        listaEleicoesAtuais.add(eleicao);
                        dados.setEleicoesAtuais(listaEleicoesAtuais);
                        //remover a eleicao da lista de eleicoes que ainda nao comecaram
                        listaEleicoesNComecadas.remove(eleicao);
                        dados.setEleicoesNaoComecadas(listaEleicoesNComecadas);
                        //atualizar as listas de eleicoes a decorrer dentro de cada mesa
                        dados.atualizaMesas(eleicao);
                    }
                    guardaDados();
                }
            }
            try {
                //esperar 15 segundos
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        RMIServer obj = new RMIServer();
        Thread thread = new Thread(obj);
        thread.start();
        RMI_S_I server = new RMIServer();
        Registry r;

        try {
            System.setProperty("java.rmi.server.hostname", "localhost");
            LocateRegistry.createRegistry(1099).rebind("project",server);

            System.out.println("RMI Server primário ready.");
        } catch (RemoteException re) {
            System.out.println("RMI Server secundario a espera...");

            try {
                int conta_pings = 0;
                //se falharem 5 pings seguidos, o secundario torna-se o primario
                while (conta_pings < 5) {
                    conta_pings += checkPing();
                }
                System.setProperty("java.rmi.server.hostname", "localhost");
                LocateRegistry.createRegistry(1099).rebind("project",server);
                carregaDados();
                System.out.println("RMI Server secundario vira primário.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}