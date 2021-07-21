package classes;

import rmiserver.RMI_S_I;

import java.net.MalformedURLException;
import java.rmi.*;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class AdminConsole {

    private static RMI_S_I server;
    public static Scanner scan = new Scanner(System.in);

    //----------------------------------VERIFICACOES-------------------------------------

    //verifica de o numero e inteiro
    private static int verificaInteiro(String input){
        boolean inteiro = false;
        int n=0;
        //enquanto nao for inteiro pede ao utilizador
        while(!inteiro){
            try{
                n = Integer.parseInt(input);
                inteiro = true;
            }
            catch(Exception e){
                System.out.println("Insira um numero inteiro");
                input = scan.nextLine();
            }
        }
        return n;
    }

    //verifica o numero do telemovel
    private static int verificaTelemovel(String input){
        int conta=0;
        int tel = 0;
        //enquanto o numero nao tiver 9 digitos pede ao utilizador
        while(conta!=9){
            conta = 0;
            tel = verificaInteiro(input);
            int num = tel;
            while (num != 0) {
                // num = num/10
                num /= 10;
                conta++;
            }
            if(conta!=9){
                System.out.println("O numero tem de ter 9 digitos");
                input = scan.nextLine();
            }
        }
        return tel;
    }

    //verifica se a data do CC esta correta e dentro da validade
    private static String verificaData(String input){
        boolean certo = false;
        GregorianCalendar agora = new GregorianCalendar();
        //enquanto estiver errada pede ao utilizador
        while(!certo){
            try{
                String[] aux = input.split("-");
                int ano = Integer.parseInt(aux[0]);
                int mes = Integer.parseInt(aux[1])-1;
                int dia = Integer.parseInt(aux[2])+1;
                GregorianCalendar data = new GregorianCalendar(ano,mes,dia);
                if(data.before(agora)){
                    System.out.println("A data já passou (se o CC caducou, por favor atualize o mesmo)");
                    input = scan.nextLine();
                }
                else{
                    certo = true;
                }
            } catch (Exception e){
                System.out.println("Data no formato invalido (AAAA-MM-DD)");
                input = scan.nextLine();
            }
        }
        return input;
    }

    private static boolean verificaLigacao() throws RemoteException{
        try{
            server = (RMI_S_I) Naming.lookup("rmi://localhost:1099/project");

            if (server == null){
                return false;
            }
            else
                return true;
        } catch (NotBoundException | MalformedURLException e) {
            return false;
        }
    }

    private static GregorianCalendar verificaStringData(String data){
        String[] aux = data.split("-");
        GregorianCalendar dataEleicao;
        int ano, mes, dia, hora, minuto;
        try{
            ano = Integer.parseInt(aux[0]);
            mes = Integer.parseInt(aux[1]);
            dia = Integer.parseInt(aux[2]);
            hora = Integer.parseInt(aux[3]);
            minuto = Integer.parseInt(aux[4]);
        }catch(Exception e){
            System.out.println("Data no formato errado");
            return null;
        }
        try{
            dataEleicao = new GregorianCalendar(ano,mes-1,dia,hora,minuto);
            GregorianCalendar dataAtual = new GregorianCalendar();
            if(dataEleicao.before(dataAtual)){
                System.out.println("Data já passou");
                return null;
            }
        }catch (Exception e){
            System.out.println("Data no formato errado");
            return null;
        }

        return dataEleicao;
    }

    private static boolean verificaDatas(GregorianCalendar dataInicio,GregorianCalendar dataFim){
        if(dataFim.before(dataInicio)){
            System.out.println("Data final é antes da inicial");
            return false;
        }
        else{
            return true;
        }
    }

    private static int verificaNValores(String input, int min, int max){
        boolean certo = false;
        int n = 0;
        while(!certo){
            n = verificaInteiro(input);
            if(n>=min && n<=max){
                certo = true;
            }
            else{
                System.out.printf("O numero inserido tem de estar entre %d e %d\n",min,max);
                input = scan.nextLine();
            }
        }
        return n;
    }


    //-------------------------------------PRINTS----------------------------------------

    //imprime as mesas de voto
    public static void printMesasDeVoto() throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }
        //comunica com o rmi
        String utilizador;
        try {
            utilizador = server.printMesasDeVoto();
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
    }

    //imprime a hora e o local de voto dos eleitores numa dada eleicao
    public static void saberLocal() throws RemoteException{
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }

        //pede ao RMI o print das eleicoes concluidas
        String utilizador = null;
        try {
            utilizador = server.printEleicoesConcluidas();
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
        assert utilizador != null;
        if (utilizador.equals("Não existem eleicoes concluidas") || utilizador.equals("Nao existem votos nesta eleicao")) {
            System.out.println(utilizador);
            return;
        }

        //Conta o numero de listas que existem
        String[] auxListas = utilizador.split("\n");
        int nListas = auxListas.length;

        System.out.println("Insira o número da eleição:");
        String opS = scan.nextLine();
        int op = verificaNValores(opS,1, nListas);

        //comunica com o rmi
        utilizador = null;
        try {
            utilizador = server.saberLocal(op);
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
    }

    //imprime numero de votos dos eleitores em tempo real
    public static  void eleitoresATM() throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }
        //pedir ao utilizador que escolha a mesa
        int nMesa = escolheMesa();
        if(nMesa == -1){
            return;
        }
        //pedir ao utilizador que escolha a eleicao
        int nEleicao = escolheEleicaoMesa(nMesa);
        if(nEleicao == -1){
            return;
        }

        //comunica com o rmi
        String utilizador = null;
        try {
            utilizador = server.votosAtuaisMesa(nEleicao,nMesa);
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
    }

    //imprime as mesas ativas e inativas
    public static void imprimeMesasAtivas() throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }
        //comunica com o rmi
        String utilizador = null;
        try {
            utilizador = server.printMesasAtivas();
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
    }


    //------------------------------------ESCOLHAS---------------------------------------


    //mostra ao utilizador os eleicoes a decorrer e pede para escolher uma
    private static int escolheEleicaoNComecada() throws RemoteException{
        if(!verificaLigacao()){
            System.out.println("Nao foi possivel ligar ao server");
            return -3;
        }
        String utilizador = null;
        try{
            utilizador = server.printEleicoesNComecadas();
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
        assert utilizador != null;
        if(utilizador.equals("Nao existem eleicoes nao comecadas")){
            return -1;
        }
        //conta quantas eleicoes e que existem
        String[] aux = utilizador.split("\n");
        int max = aux.length;

        System.out.println("Insira o número da eleição:");
        String numEleicao = scan.nextLine();

        return verificaNValores(numEleicao,1,max);
    }

    //mostra ao utilizador as eleicoes existentes numa mesa e pede que escolha uma
    public static int escolheEleicaoMesa(int nMesa) throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return -3;
        }
        String utilizador = null;
        try{
            utilizador = server.eleicoesAtuaisNaMesaoString(nMesa);
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
        assert utilizador != null;
        if(utilizador.equals("Não existem eleicoes atuais")){
            return -1;
        }
        //conta quantas eleicoes e que existem
        String[] aux = utilizador.split("\n");
        int max = aux.length;

        System.out.println("Insira o número da eleição:");
        String numEleicao = scan.nextLine();

        return verificaNValores(numEleicao,1,max);
    }

    //escolhe uma mesa de voto
    public static int escolheMesa() throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return -3;
        }
        //pede ao RMI as mesas de voto
        String utilizador = null;
        try{
            utilizador = server.printMesasDeVoto();
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
        assert utilizador != null;
        if(utilizador.equals("Nao existem mesas de voto")){
            return -1;
        }
        //pede ao utilizador uma opcao
        System.out.println("Insira o número da mesa:");
        String[] auxMesas = utilizador.split("\n");
        String numEleicaoS = scan.nextLine();

        return verificaNValores(numEleicaoS,1, auxMesas.length);
    }

    //pede ao utilizador que escolha uma eleicao concluida
    public static void eleicoesConcluidas() throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }
        //pede ao RMI print das eleicoes concluidas
        String utilizador = null;
        try {
            utilizador = server.printEleicoesConcluidas();
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
        assert utilizador != null;
        if (utilizador.equals("Não existem eleicoes concluidas") || utilizador.equals("Nao existem votos nesta eleicao")) {
            return;
        }
        //contar o numero de listas
        String[] auxListas = utilizador.split("\n");
        int nListas = auxListas.length;

        System.out.println("Insira o número da eleição:");
        String opS = scan.nextLine();
        int op = verificaNValores(opS,1,nListas);

        //comunica com o rmi
        try {
            utilizador = server.printInformacoesEleicoesConc(op);
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
    }


    //----------------------------ADICIONAR/REMOVER DADOS--------------------------------

    //regista um novo eleitor
    public static void registarPessoa() throws RemoteException{

        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }

        System.out.println(("Nome:"));
        String nome = scan.nextLine();
        System.out.println("Funcao:\n1-Estudante\n2-Docente\n3-Funcionario");
        String typeS = scan.nextLine();
        int type=verificaNValores(typeS,1,3);
        System.out.println("Password:");
        String password = scan.nextLine();
        System.out.println("Departamento (sigla):");
        String departamento = scan.nextLine();
        System.out.println("Numero de telefone:");
        String nTelefoneS = scan.nextLine();
        int nTel = verificaTelemovel(nTelefoneS);
        System.out.println("Morada:");
        String morada = scan.nextLine();
        System.out.println("Numero do cartao de cidadao:");
        String numCC = scan.nextLine();
        int nCC = verificaInteiro(numCC);
        System.out.println("Validade do cartao de cidadao [AAAA-MM-DD]:");
        String valCC = scan.nextLine();
        valCC = verificaData(valCC);

        if(type==1){
            typeS = "EST";
        }
        else if(type==2){
            typeS = "DOC";
        }
        else{
            typeS = "FUNC";
        }

        String utilizador;
        try{
            utilizador = server.registaPessoa(nome, typeS, password, departamento.toUpperCase(), nTel,morada,nCC,valCC);
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in registarPessoa: " + e);
        }

    }

    //cria uma nova eleicao
    public static void criarEleicao() throws RemoteException {
        String dataInicio, dataFim;
        GregorianCalendar dataVInicio, dataVFim;
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }

        do {
            System.out.println(("Data de inicio (AAAA-MM-DD-HH-MM):"));
            dataInicio = scan.nextLine();
            dataVInicio = verificaStringData(dataInicio);
            while (dataVInicio == null) {
                System.out.println(("Data de inicio (AAAA-MM-DD-HH-MM):"));
                dataInicio = scan.nextLine();
                dataVInicio = verificaStringData(dataInicio);
            }
            System.out.println("Data de fim (AAAA-MM-DD-HH-MM):");
            dataFim = scan.nextLine();
            dataVFim = verificaStringData(dataFim);
            while (dataVFim == null) {
                System.out.println(("Data de fim (AAAA-MM-DD-HH-MM):"));
                dataFim = scan.nextLine();
                dataVFim = verificaStringData(dataFim);
            }


        }while (!verificaDatas(dataVInicio,dataVFim));

        System.out.println("Titulo:");
        String titulo = scan.nextLine();
        System.out.println("Descricao:");
        String desc = scan.nextLine();
        System.out.println("Tipo:\n1-Estudante\n2-Docente\n3-Funcionario\n4-Todos");
        String typeS = scan.nextLine();
        int type=verificaNValores(typeS,1,4);
        System.out.println("Departamento (sigla):");
        String dep = scan.nextLine();

        if(type==1){
            typeS = "EST";
        }
        else if(type==2){
            typeS = "DOC";
        }
        else if(type==3){
            typeS = "FUNC";
        }
        else{
            typeS = "TODOS";
        }

        //comunica com o rmi
        String utilizador = null;
        try{
            utilizador = server.criaEleicao(dataVInicio, dataVFim, titulo.toUpperCase(), desc, typeS, dep.toUpperCase());
            System.out.println(utilizador );
        } catch (RemoteException e) {
            System.out.println("Exception in criaEleicao: " + e);
        }
    }

    //cria nova lista
    public static void criarLista()  throws RemoteException{
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }

        //pede ao utilizador uma eleicao
        int numEleicao = escolheEleicaoNComecada();
        if(numEleicao==-1){
            return;
        }

        System.out.println(("Nome da lista:"));
        String nomeLista = scan.nextLine();

        System.out.println("Funcao dos membros:\n1-Estudante\n2-Docente\n3-Funcionario");
        String typeS = scan.nextLine();
        int type = verificaNValores(typeS,1,3);

        System.out.println("Numero de membros da lista:");
        String numMembrosS = scan.nextLine();
        int numMembros = verificaInteiro(numMembrosS);

        //cria um array com os numeros de CC dos membros
        int[] array = new int[numMembros];
        for (int i = 0; i < numMembros; i++) {
            System.out.println("Numero de cc:");
            String nCCS = scan.nextLine();
            int nCC = verificaInteiro(nCCS);
            array[i] = nCC;
        }

        if(type==1){
            typeS = "EST";
        }
        else if(type==2){
            typeS = "DOC";
        }
        else{
            typeS = "FUNC";
        }

        //comunica com o RMI
        String utilizador;
        try{
            utilizador = server.criaLista(nomeLista, typeS, array, numEleicao);
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
    }

    //apaga uma mesa de voto de uma eleicao
    public static void deleteMesa() throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }
        //pede ao utilizador uma eleicao
        int numEleicao = escolheEleicaoNComecada();
        if(numEleicao==-1){
            System.out.println("Nao existem eleicoes nao comecadas");
            return;
        }

        System.out.println("Insira o departamento (sigla):");
        String dep = scan.nextLine();

        //comunica com o RMI
        String utilizador;
        try{
            utilizador = server.deleteMesa(numEleicao,dep);
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
    }

    //adiciona uma mesa de voto a uma eleicao
    public static void addMesa() throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }

        //pede ao utilizador uma eleicao
        int numEleicao = escolheEleicaoNComecada();
        if(numEleicao==-1){
            System.out.println("Não existem eleicoes");
            return;
        }

        System.out.println("Insira o departamento (sigla):");
        String dep = scan.nextLine();

        //comunica com o RMI
        String utilizador;
        try{
            utilizador = server.addMesa(numEleicao,dep);
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
    }


    //--------------------------------ALTERAR DADOS------------------------------------


    //editar um campo de uma dada eleicao
    public static String editaEleicao(int numEleicao,String oQue) throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return "";
        }

        int check = 0;
        String alteracao;
        //ver qual é o tipo de alteracao
        while(check==0){
            //se for da funcao, pede ao user a alteracao
            if(oQue.equals("type")){
                System.out.println("Tipo:\n1-Estudante\n2-Docente\n3-Funcionario\n4-Todos");
                String typeS = scan.nextLine();
                int type=verificaNValores(typeS,1,4);
                if(type==1){
                    alteracao = "EST";
                }
                else if(type==2){
                    alteracao = "DOC";
                }
                else if(type==3){
                    alteracao = "FUNC";
                }
                else{
                    alteracao = "TODOS";
                }
            }
            else {
                System.out.println(("Insira a alteracao:"));
                alteracao = scan.nextLine();
                System.out.println(alteracao);
            }

            //se for a data pede e verifica
            if(oQue.equals("dataInicio") || oQue.equals("dataFim")){
                GregorianCalendar data = verificaStringData(alteracao);
                while (data == null) {
                    System.out.println(("Data (AAAA-MM-DD-HH-MM):"));
                    alteracao = scan.nextLine();
                    data = verificaStringData(alteracao);
                }
            }
            //imprime as alteracoes e pergunta se confirma
            System.out.println("Confirma a alteracao?\n1-Sim\n2-Nao\n0-Sair");
            String opS = scan.nextLine();
            int op = verificaNValores(opS,0,2);
            if(op==1){
                //comunica com o rmi
                String utilizador;
                try{
                    utilizador = server.editaEleicao(numEleicao,oQue,alteracao);
                    System.out.println(utilizador);
                } catch (RemoteException e) {
                    System.out.println("Exception in criaLista: " + e);
                }
                check=1;
            }
            else if(op==2){
                System.out.println("\n");
            }
            else if(op==0){
                return "Cancelado";
            }
            else {
                System.out.println("Opcao invalida");
            }
        }
        return "Cancelado";
    }

    //alterar uma eleicao
    public static void alterarEleicao() throws RemoteException {
        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }

        //pedir a eleicao
        int numEleicao = escolheEleicaoNComecada();
        if(numEleicao==-1){
            return;
        }

        //comunica com o rmi
        String utilizador = null;
        try{
            utilizador = server.eleicaoInfo(numEleicao);
            System.out.println(utilizador);
        } catch (RemoteException e) {
            System.out.println("Exception in criaLista: " + e);
        }
        if(utilizador.equals("A eleicao ja comecou, impossivel alterar os dados")){
            return;
        }

        int op=-1;

        //menu
        while(op!=0) {
            System.out.println("O que pretende editar?");
            System.out.println("1 - Titulo");
            System.out.println("2 - Descricao");
            System.out.println("3 - Data de inicio");
            System.out.println("4 - Data de fim");
            System.out.println("5 - Tipo");
            System.out.println("6 - Departamento");
            System.out.println("0 - Sair");
            String opS = scan.nextLine();
            op=verificaInteiro(opS);

            if(op==1){
                editaEleicao(numEleicao,"titulo");
            }
            else if(op==2){
                editaEleicao(numEleicao,"desc");
            }
            else if(op==3){
                editaEleicao(numEleicao,"dataInicio");

            }
            else if(op==4){
                editaEleicao(numEleicao,"dataFim");
            }
            else if(op==5){
                editaEleicao(numEleicao,"type");
            }
            else if(op==6){
                editaEleicao(numEleicao,"dep");
            }
            else if(op==0){
                return;
            }
            else{
                System.out.println("Opcao invalida");
            }
        }
    }



    //menu
    public static int menu(){
        System.out.println("Opcoes:");
        System.out.println("1 - Registar eleitor");
        System.out.println("2 - Criar eleicao");
        System.out.println("3 - Registar lista candidata");
        System.out.println("4 - Adicionar mesa de voto");
        System.out.println("5 - Eliminar mesa de voto");
        System.out.println("6 - Alterar propriedades de uma eleição");
        System.out.println("7 - Saber em que local votou cada eleitor");
        System.out.println("8 - Consultar resultados detalhados de eleições passadas");
        System.out.println("9 - Número de eleitores que votaram em cada mesa em tempo real");
        System.out.println("10 - Imprimir mesas ativas/inativas");
        System.out.println("0 - Sair");

        String n = scan.nextLine();

        return verificaInteiro(n);

    }

    public static void main(String args[]) throws RemoteException {

        if(!verificaLigacao()){
            System.out.println("Não foi possivel ligar ao server");
            return;
        }

        try {
            server = (RMI_S_I) Naming.lookup("rmi://localhost:1099/project");
            int op=-1;

            while(op!=0){
                op=menu();
                if(op==1){
                    registarPessoa();
                }
                else if(op==2){
                    criarEleicao();
                }
                else if(op==3){
                    criarLista();
                }
                else if(op==4){
                    addMesa();
                }
                else if(op==5){
                    deleteMesa();
                }
                else if(op==6){
                    alterarEleicao();
                }
                else if(op==7){
                    saberLocal();
                }
                else if(op==8){
                    eleicoesConcluidas();
                }
                else if(op==9){
                    eleitoresATM();
                }
                else if(op==10){
                    imprimeMesasAtivas();
                }

                else if(op==0){
                    return;
                }
                else{
                    System.out.println("Opcao invalida");
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }

    }
}
