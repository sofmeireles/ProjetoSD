package classes;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;


public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS_DISCOVER = "224.0.224.";
    private String MULTICAST_ADDRESS_VOTER = "224.0.224.";
    private int id;
    // ports do address de descobrir terminais
    private final int PORT = 4322;
    private final int PORTR = 4321; //recebe neste
    // ports do address do voto
    private final int PORTVOTES = 4444;
    private final int PORTVOTER = 4445;
    private boolean disponibilidade = true;
    private int tamE = 0;
    private int tamLista = 0;

    public MulticastClient(int mesa,int id){
        this.id = id;
        MULTICAST_ADDRESS_DISCOVER +=  mesa;
        MULTICAST_ADDRESS_VOTER += (mesa+100);
        MulticastUserReceiver userReceiver = new MulticastUserReceiver(id);
        userReceiver.start();
    }

    public static void main(String[] args) {
        Random rand = new Random();
        Scanner sc1 = new Scanner(System.in);
        //pede a mesa a q pretende que o terminal esteja conectado
        System.out.println("ID Mesa: ");
        int mesa = Integer.parseInt(sc1.nextLine());
        int id = rand.nextInt(1000);
        System.out.println("Terminal " + id +" bloqueado");
        MulticastClient client = new MulticastClient(mesa,id);
        client.start();
    }

    public void run(){
        MulticastSocket socket = null;
        MulticastSocket socketR = null;
        /*
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {

            }
        });
        */
        try{
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            socketR = new MulticastSocket(PORTR);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS_DISCOVER);
            socket.joinGroup(group);
            socketR.joinGroup(group);
            while(true){

                //recebe os pacotes
                byte[] bufferR = new byte[254];
                DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                socketR.receive(packetR);
                String message = new String(packetR.getData(), 0, packetR.getLength());

                //manda os ids e respetiva disponibilidade
                String[] split = message.split(";");
                if (split[0].equals("type|status") && split[1].equals("terminal|id")) {
                    String[] current_time = split[2].split("\\|");
                    String msg = "type|terminal;status|on;id|"+id+";available|"+disponibilidade+";time|"+current_time[1]+";";
                    byte[] buffer = msg.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);

                }
                //conecta a um terminal disponivel e desbloqueia-o
                if(split[0].equals("type|status") && split[1].equals("terminal|connect") && split[2].equals("id|"+id+"")){
                    disponibilidade = false;
                    System.out.println("Terminal " + id + " desbloqueado");
                    //inicia a thread que permite votar
                    MulticastUserSender sendLogin = new MulticastUserSender(id,0);
                    sendLogin.start();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class MulticastUserSender extends Thread{
        private final int id;
        private final int type;

        public MulticastUserSender(int id, int type) {
            this.id = id;
            this.type = type;
        }

    public Long contaTempo(){
        long startTime = System.currentTimeMillis();
        return startTime;
    }

    private int verificaInteiro(String input){
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

    private int verificaNValores(String input, int min, int max){
        Scanner scan = new Scanner(System.in);
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


    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS_VOTER);
            socket.joinGroup(group);
            String message = "";
            Long inicioTempo = (long)0;
            //tempo para o bloqueio do terminal
            int s = 120;
            //login
            if(type == 0){
                int numCC;
                String password = "";
                System.out.println("Numero do cartao de cidadao:");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                inicioTempo = contaTempo();
                //conta o tempo de inatividade do terminal
                while((System.currentTimeMillis() - inicioTempo < s*1000)  && !input.ready()){

                }
                //caso a pessoa insira dados guarda-os
                if (input.ready()){
                    String nCC = input.readLine();
                    numCC = verificaInteiro(nCC);
                }else{
                    //terminal inativo por 120segundos portanto é bloqueado
                    System.out.println("Terminal " + id +" bloqueado");
                    disponibilidade = true;
                    String msgEleicoes = "type|shutdown;server|" + id + ";";
                    byte[] buffer = msgEleicoes.getBytes();
                    DatagramPacket packetEleicoes = new DatagramPacket(buffer, buffer.length, group, PORTVOTES);
                    socket.send(packetEleicoes);
                    return;
                }

                System.out.println("Password:");
                inicioTempo = contaTempo();
                //conta o tempo de inatividade do terminal
                while((System.currentTimeMillis() - inicioTempo < s*1000)  && !input.ready()){

                }
                if (input.ready()){
                    password = input.readLine();
                }else{
                    //120 segundos inativo, terminal é bloqueado
                    System.out.println("Terminal " + id +" bloqueado");
                    disponibilidade = true;
                    String msgEleicoes = "type|shutdown;server|" + id + ";";
                    byte[] buffer = msgEleicoes.getBytes();
                    DatagramPacket packetEleicoes = new DatagramPacket(buffer, buffer.length, group, PORTVOTES);
                    socket.send(packetEleicoes);
                    return;
                }
                message = "type|login;server|"+id+";username|" + numCC + ";password|" + password;
            }
            //pede eleicoes
            else if(type == 1){
                int escolha;
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                inicioTempo = contaTempo();
                while((System.currentTimeMillis() - inicioTempo < s*1000)  && !input.ready()){

                }
                if (input.ready()){
                    String e = input.readLine();
                    escolha = verificaNValores(e,1,tamE);
                }else{
                    System.out.println("Terminal " + id +" bloqueado");
                    disponibilidade = true;
                    String msgEleicoes = "type|shutdown;server|" + id + ";";
                    byte[] buffer = msgEleicoes.getBytes();
                    DatagramPacket packetEleicoes = new DatagramPacket(buffer, buffer.length, group, PORTVOTES);
                    socket.send(packetEleicoes);
                    return;
                }

                message = "type|eleicoes;server|" + id + ";eleicao|" + escolha + ";";
            }
            //realiza o voto
            else if(type == 2){
                int voto;
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                inicioTempo = contaTempo();
                while((System.currentTimeMillis() - inicioTempo < s*1000)  && !input.ready()){

                }
                if (input.ready()){
                    String v = input.readLine();
                    voto = verificaNValores(v, 1, tamLista);
                    disponibilidade = true;

                }else{
                    System.out.println("Terminal " + id +" bloqueado");
                    disponibilidade = true;
                    String msgEleicoes = "type|shutdown;server|" + id + ";";
                    byte[] buffer = msgEleicoes.getBytes();
                    DatagramPacket packetEleicoes = new DatagramPacket(buffer, buffer.length, group, PORTVOTES);
                    socket.send(packetEleicoes);
                    return;
                }
                message = "type|eleicoes;server|" + id + ";voto|" + voto + ";";
                System.out.println("Voto realizado com sucesso.\n\n");
                System.out.println("Terminal " + id +" bloqueado");
            }
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORTVOTES);
            socket.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    private class MulticastUserReceiver extends Thread {

        private final int id;

        public MulticastUserReceiver(int id) {
            this.id = id;
        }

        public void run() {
            MulticastSocket socket = null;
            MulticastSocket socketR = null;
            try {
                socket = new MulticastSocket();  // create socket without binding it (only for sending)
                socketR = new MulticastSocket(PORTVOTER);  // create socket and bind it
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS_VOTER);
                socket.joinGroup(group);
                socketR.joinGroup(group);
                while(true) {
                    //recebe os dados
                    byte[] bufferR = new byte[254];
                    DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                    socketR.receive(packetR);
                    String message = new String(packetR.getData(), 0, packetR.getLength());
                    String[] idResponse = message.split(";");

                    //se o login for concluido com sucesso inicia o processo do voto
                    if (message.equals("type | status; server | " + id + "; logged | on; msg | Welcome to eVoting")) {
                        System.out.println("Welcome to eVoting");
                        String msgEleicoes = "type|eleicoes;server|" + id + ";status|print";
                        byte[] buffer = msgEleicoes.getBytes();
                        DatagramPacket packetEleicoes = new DatagramPacket(buffer, buffer.length, group, PORTVOTES);
                        socket.send(packetEleicoes);
                    }
                    //dados de login invalidos
                    else if (message.equals("type | status; server | " + id + "; logged | off; msg | Falha ao fazer login. Tente de novo.")) {
                        System.out.println("Dados inválidos. Tente de novo.");
                        MulticastUserSender send = new MulticastUserSender(id, 0);
                        send.start();
                    //se a msg nao for para este id ignora-a
                    } else if (!idResponse[1].equals("server|" + id)) {
                        continue;
                    }

                    //caso nao haja eleicoes disponiveis de momento termina
                    else if (idResponse[0].equals("type|eleicoes") && idResponse[2].equals("item_count|0")) {
                        System.out.println("Não existem eleicoes disponiveis de momento.");
                        System.out.println("Terminal Bloqueado");
                        String msgEleicoes = "type|shutdown;server|" + id + ";";
                        byte[] buffer = msgEleicoes.getBytes();
                        DatagramPacket packetEleicoes = new DatagramPacket(buffer, buffer.length, group, PORTVOTES);
                        socket.send(packetEleicoes);
                        disponibilidade = true;

                    //caso nao existam listas disponiveis de momento termina
                    } else if (message.equals("type|listas;server|" + id + ";item_count|0;")) {
                        System.out.println("Não existem listas disponiveis de momento.");
                        System.out.println("Terminal Bloqueado");
                        String msgEleicoes = "type|shutdown;server|" + id + ";";
                        byte[] buffer = msgEleicoes.getBytes();
                        DatagramPacket packetEleicoes = new DatagramPacket(buffer, buffer.length, group, PORTVOTES);
                        socket.send(packetEleicoes);
                        disponibilidade = true;

                        //eleitor ja votou naquela eleicao
                    } else if (message.equals("type|eleicoes;server|" + id + ";eleicao|-1;")) {
                        System.out.println("O eleitor ja votou nesta eleicao.\n\n");
                        System.out.println("Terminal Bloqueado");
                        String msgEleicoes = "type|shutdown;server|" + id + ";";
                        byte[] buffer = msgEleicoes.getBytes();
                        DatagramPacket packetEleicoes = new DatagramPacket(buffer, buffer.length, group, PORTVOTES);
                        socket.send(packetEleicoes);
                        disponibilidade = true;

                        //escolhe a lista em que quer votar
                    } else if (idResponse[0].equals("type|listas") && idResponse[1].equals("server|"+id)) {
                        String[] listL = message.split(";");
                        String[] tamL = listL[2].split("\\|");
                        int numListas = Integer.parseInt(tamL[1]);
                        for (int i = 3; i < numListas + 3; i++) {
                            String[] nome = listL[i].split("\\|");
                            System.out.println((i - 2) + " - " + nome[1]);
                        }
                        System.out.println((numListas+1)+" - Voto Nulo");
                        System.out.println((numListas+2)+" - Voto Branco");
                        tamLista = numListas+2;
                        MulticastUserSender send = new MulticastUserSender(id, 2);
                        send.start();
                    } else {
                        //escolhe a eleicao
                        System.out.println("Escolha a eleicao:");
                        String[] listE = message.split(";");
                        String[] tam = listE[2].split("\\|");
                        int numEleicoes = Integer.parseInt(tam[1]);
                        for (int i = 3; i < numEleicoes + 3; i++) {
                            String[] nome = listE[i].split("\\|");
                            System.out.println((i - 2) + " - " + nome[1]);
                        }
                        tamE = numEleicoes;
                        MulticastUserSender send = new MulticastUserSender(id, 1);
                        send.start();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
                socketR.close();
            }
        }
    }
}
