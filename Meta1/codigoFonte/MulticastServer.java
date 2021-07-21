
import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.*;

public class MulticastServer extends Thread {

    private String MULTICAST_ADDRESS_DISCOVER = "224.0.224.";
    private String MULTICAST_ADDRESS_VOTE = "224.0.224.";
    // ports do address de descoberta de terminais
    private int PORT = 4321;
    private int PORTR = 4322;
    // port do adress do voto
    private int PORTVOTES = 4445;
    private int PORTVOTER = 4444;
    public int mesa;
    public static RMI_S_I serverRMI;

    public MulticastServer(int mesa) {
        this.mesa = mesa;
        //cria duas addresses uma para descobrir/ser descoberto e outra para votar apenas
        MULTICAST_ADDRESS_DISCOVER = MULTICAST_ADDRESS_DISCOVER + mesa;
        MULTICAST_ADDRESS_VOTE += (mesa + 100);
    }


    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        //conecta so servidor RMI
        serverRMI = (RMI_S_I) Naming.lookup("rmi://192.168.1.82:1099/project");
        //print das mesas de voto existentes
        String mesas = serverRMI.printMesasVotoProtocolo();
        String[] m = mesas.split(";");
        String[] m1 = m[1].split("\\|");
        for (int i = 2; i < Integer.parseInt(m1[1])+2; i++){
            String[] nome = m[i].split("\\|");
            System.out.println((i -1) + " - " +nome[1]);
        }
        int mesa = escolheMesa();
        //caso o numero da mesa seja invalido corre ate se escolher uma que exista
        while (mesa == 0){
            mesa = escolheMesa();
        }
        //ativar mesa de voto para o print na admin console
        serverRMI.ativarMesa(mesa);
        //inicia a thread da mesa em questao
        MulticastServer server = new MulticastServer(mesa);
        server.start();
    }


    public static boolean verificaLigacao() throws RemoteException{
        //verifica se tem ligacao ao servidor rmi
        try{
            serverRMI = (RMI_S_I) Naming.lookup("rmi://192.168.1.82:1099/project");
            if (serverRMI == null){
                return false;
            }
            else
                return true;
        } catch (NotBoundException | MalformedURLException e) {
            return false;
        }
    }


    public static void reconecta() throws RemoteException{
        //caso tenha falhado ao conectar ao servidor, tenta conectar de novo
        try{
            serverRMI = (RMI_S_I) Naming.lookup("rmi://192.168.1.82:1099/project");
        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static Integer escolheMesa() throws RemoteException {
        //funcao q escolhe que permite ao utilizador escolher em que mesa pretende votar
        if(verificaLigacao()) {
            serverRMI.printMesasDeVoto();
            Scanner sc1 = new Scanner(System.in);
            System.out.println("Escolha uma mesa: ");
            int mesa = Integer.parseInt(sc1.nextLine());
            if (mesa > serverRMI.numeroMesasDeVoto()) {
                System.out.println("Mesa nao existente, tente de novo.");
                return 0;
            }
            return mesa;
        }else {
            reconecta();
            return -3;
        }
    }

    private static int verificaInteiro(String input){

        Scanner scan = new Scanner(System.in);
        boolean inteiro = false;
        int n=0;
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

    public void run() {
        //caso ctrl c mete a mesa como inativa
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    serverRMI.desativarMesa(mesa);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            while (true) {
                MulticastSocket socket = null;
                MulticastSocket sendSocket = null;
                try {
                    socket = new MulticastSocket(PORTR);
                    sendSocket = new MulticastSocket();
                    //endereca-os para o grupo multicast
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS_DISCOVER);
                    socket.joinGroup(group);
                    sendSocket.joinGroup(group);

                    //System.out.println("Mesa: " + mesa);
                    //System.out.println(this.getName() + " running...");
                    while (true) {
                        if (verificaLigacao()) {
                            //pede ao utilizador o cc
                            System.out.println("Insira o numero do CC: ");
                            Scanner scan = new Scanner(System.in);
                            String numCC = scan.nextLine();
                            int nCC = verificaInteiro(numCC);


                            //System.out.println(idd[1]);

                            Pessoa utilizador = null;

                            utilizador = serverRMI.checkaPessoa(nCC);
                            //verifica se a pessoa existe para poder votar e inicia a thread voto
                            if (utilizador != null) {
                                try {
                                    //pede um terminal livre no grupo
                                    String id = "";
                                    int flag = 0;
                                    while(flag == 0) {
                                        //verifica o tempo para poder eliminar msgs da queue que ja sejam antigas
                                        long time = Instant.now().getEpochSecond();
                                        byte[] msgT = ("type|status;terminal|id;time|" + time + ";").getBytes();
                                        DatagramPacket packetT = new DatagramPacket(msgT, msgT.length, group, PORT);
                                        sendSocket.send(packetT);
                                        //recebe os terminais
                                        while (true) {
                                            String listaIds = "";
                                            try {
                                                byte[] bufferR = new byte[254];
                                                DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                                                //espero 5 segundos, se nao receber mais pacotes sobre o estado das maquinas ele manda novo pedido a perguntar a disponibilidade
                                                socket.setSoTimeout(5000);
                                                socket.receive(packetR);
                                                listaIds = new String(packetR.getData(), 0, packetR.getLength());
                                            } catch (SocketTimeoutException st) {
                                                System.out.println("Aguardando por terminais livres...");
                                                break;
                                            }

                                            //
                                            String[] aux = listaIds.split(";");
                                            String[] idd = aux[2].split("\\|");
                                            String[] dispAux = aux[3].split("\\|");
                                            String[] timerAux = aux[4].split("\\|");
                                            //escolhe um terminal livre naquele momento
                                            if (dispAux[1].equals("true") && timerAux[1].equals("" + time)) {
                                                id = idd[1];
                                                flag = 1;
                                                //avisa a admin console que o terminal está ativo
                                                serverRMI.ativaTerminal(mesa,Integer.parseInt(id));
                                                break;
                                            }
                                        }
                                    }
                                    //conecta ao terminal livre e inicia a thread voto
                                    byte[] msgI = ("type|status;terminal|connect;id|" + id + ";").getBytes();
                                    DatagramPacket packetI = new DatagramPacket(msgI, msgI.length, group, PORT);
                                    sendSocket.send(packetI);
                                    System.out.println("Dirija-se ao terminal de voto nº "+id);
                                    VoteListener vote = new VoteListener(utilizador, Integer.parseInt(id));
                                    vote.start();
                                } catch (NumberFormatException e) {
                                    reconecta();
                                    e.printStackTrace();
                                }
                            }else
                                System.out.println("Eleitor não reconhecido.");
                        }
                    }
                } catch (IOException e) {
                    reconecta();
                    System.out.println("Algo deu errado. Tente de novo.");
                }
            }
        } catch (RemoteException e) {
            try {
                reconecta();
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
           // e.printStackTrace();
        }
    }

    private class VoteListener extends Thread {

        private final Pessoa pessoa;
        private final int id;

        public VoteListener(Pessoa pessoa, int id) {
            this.pessoa = pessoa;
            this.id = id;
        }


        public void run() {
            MulticastSocket socket = null;
            MulticastSocket socketR = null;
            try {
                    //utiliza o multicast address dedicado ao voto
                    socket = new MulticastSocket();  // create socket without binding it (only for sending)
                    socketR = new MulticastSocket(PORTVOTER);  // create socket and bind it
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS_VOTE);
                    socket.joinGroup(group);
                    socketR.joinGroup(group);
                    int nEleicao = 0;
                    try {
                        while (true) {
                            if (verificaLigacao()) {

                                //recebe os pacotes
                                byte[] buffer = new byte[256];
                                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                                socketR.receive(packet);
                                String message = new String(packet.getData(), 0, packet.getLength());

                                String[] tipo = message.split(";");

                                //verifica os dados do login
                                if (tipo[0].equals("type|login") && tipo[1].equals("server|" + this.id)) {
                                    String[] username = tipo[2].split("\\|");
                                    String[] pass = tipo[3].split("\\|");

                                    //verifica se o cc introduzido no terminal corresponde ao inserido na mesa de voto
                                    if ((pessoa.getPassword().equals(pass[1])) && (pessoa.getNumCC() == Integer.parseInt(username[1]))) {
                                        byte[] msg = ("type | status; server | " + this.id + "; logged | on; msg | Welcome to eVoting").getBytes();
                                        DatagramPacket packet1 = new DatagramPacket(msg, msg.length, group, PORTVOTES);
                                        socket.send(packet1);
                                    } else {
                                        //caso login seja invalido
                                        byte[] msg = ("type | status; server | " + this.id + "; logged | off; msg | Falha ao fazer login. Tente de novo.").getBytes();
                                        DatagramPacket packet1 = new DatagramPacket(msg, msg.length, group, PORTVOTES);
                                        socket.send(packet1);
                                    }
                                }

                                //bloqueia o terminal
                                if (tipo[0].equals("type|shutdown") && tipo[1].equals("server|" + this.id)) {
                                    return;
                                }

                                //comando eleicoes
                                if (tipo[0].equals("type|eleicoes") && tipo[1].equals("server|" + this.id)) {
                                    String[] num = tipo[2].split("\\|");

                                    //da print das eleicoes disponiveis
                                    if (tipo[2].equals("status|print")) {
                                        byte[] eleicoes = serverRMI.listaEleicoesProtocolo(this.id,mesa).getBytes();
                                        DatagramPacket packet1 = new DatagramPacket(eleicoes, eleicoes.length, group, PORTVOTES);
                                        socket.send(packet1);
                                    }
                                    //da print das listas daquela eleicao
                                    if (num[0].equals("eleicao")) {
                                        nEleicao = Integer.parseInt(num[1]);
                                        byte[] listas = serverRMI.printListasCandidatasProtocolo(Integer.parseInt(num[1]), pessoa.getNumCC(), this.id).getBytes();
                                        DatagramPacket packet1 = new DatagramPacket(listas, listas.length, group, PORTVOTES);
                                        socket.send(packet1);
                                    }
                                    //realiza o voto
                                    if (num[0].equals("voto")) {
                                        serverRMI.vota(nEleicao, Integer.parseInt(num[1]), pessoa.getNumCC(), mesa);
                                        //System.out.println(serverRMI.vota(nEleicao, Integer.parseInt(num[1]), pessoa.getNumCC(), mesa));
                                        return;
                                    }
                                }
                            }
                        }
                    }catch(IOException | NumberFormatException e){
                        reconecta();
                        e.printStackTrace();
                    }
            } catch (IOException e) {
                try {
                    reconecta();
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                e.printStackTrace();
            }
        }

    }
}