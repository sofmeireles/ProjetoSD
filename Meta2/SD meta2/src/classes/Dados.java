package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class Dados implements Serializable {

    private ArrayList<Pessoa> pessoas;
    private ArrayList<Eleicao> eleicoes;
    private ArrayList<Eleicao> eleicoesConcluidas;
    private ArrayList<Eleicao> eleicoesAtuais;
    private ArrayList<Eleicao> eleicoesNaoComecadas;
    private ArrayList<MesaDeVoto> mesasDeVoto;


    public Dados() {
        this.pessoas = new ArrayList<>();
        this.eleicoes = new ArrayList<>();
        this.eleicoesConcluidas = new ArrayList<>();
        this.eleicoesAtuais = new ArrayList<>();
        this.eleicoesNaoComecadas = new ArrayList<>();
        this.mesasDeVoto = new ArrayList<>();

    }

    //----------------------------------VERIFICACOES-------------------------------------

    //Procura um eleitor na lista de pessoas pelo numero do CC
    public synchronized Pessoa encontraPessoa(int numCC) {
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getNumCC() == numCC) {
                return pessoa;
            }
        }
        return null;
    }

    //-------------------------------------PRINTS----------------------------------------

    //imprime as eleicoes
    public synchronized String eleicoesToString() {
        StringBuilder aux = new StringBuilder(100);
        //verifica se existem eleicoes
        if(eleicoes.size()==0){
            return "Não existem eleicoes";
        }
        //adiciona a string com formatacao
        for (int i = 0; i < eleicoes.size();i++){
            aux.append(String.format("%d - %s \n", i + 1, eleicoes.get(i).getTitulo()));
        }
        return aux.toString();
    }

    //imprime as eleicoes atuais
    public synchronized String eleicoesAtuaisToString() {
        StringBuilder aux = new StringBuilder(100);
        //verifica se existem eleicoes atuais
        if(eleicoesAtuais.size()==0){
            return "Não existem eleicoes atuais";
        }
        //adiciona a string com formatacao
        for (int i = 0; i < eleicoesAtuais.size();i++){
            aux.append(String.format("%d - %s \n", i + 1, eleicoesAtuais.get(i).getTitulo()));
        }
        return aux.toString();
    }

    //imprime as eleicoes atuais numa determinada mesa
    public synchronized String eleicoesAtuaisNaMesaToString(int nMesa){
        StringBuilder aux = new StringBuilder(100);
        MesaDeVoto mesa = mesasDeVoto.get(nMesa-1);

        //verifica se existem mesas de voto
        if(mesasDeVoto.size()==0){
            return "Não existem mesas";
        }
        //verifica se existem eleicoes atuais na mesa
        if(mesa.getEleicoesAtuais().size()==0){
            return "Não existem eleicoes atuais";
        }
        int i = 0;
        //adiciona as eleicoes a string com formatacao
        aux.append("Eleicoes:\n");
        for(Eleicao eleicao : mesa.getEleicoesAtuais()){
            i++;
            aux.append(String.format("%d - %s",i,eleicao.getTitulo()));
        }
        return aux.toString();
    }

    //imprime o local/hora em que os eleitores votaram numa dada eleicao
    public synchronized String saberLocal(int nEleicao){
        Eleicao eleicao = eleicoesConcluidas.get(nEleicao-1);
        StringBuilder aux = new StringBuilder(100);

        //verifica se existem pessoas
        if(pessoas.size()==0){
            System.out.println("RIP");
            return "Nao existem eleitores";
        }

        for(Pessoa pessoa : pessoas){
            ArrayList<Voto> votos = pessoa.getListaDeVotos();
            for(Voto voto : votos){
                //se o voto for na eleicao indicada adiciona a string
                if(voto.getEleicao().equals(eleicao)){
                    GregorianCalendar horas = voto.getData();
                    String data = String.format("%d-%d-%d %d:%d",horas.get(Calendar.DATE),horas.get(Calendar.MONTH)+1,horas.get(Calendar.YEAR),horas.get(Calendar.HOUR_OF_DAY),horas.get(Calendar.MINUTE));
                    aux.append(String.format("nCC: %d - mesa: %s - %s\n",pessoa.getNumCC(),voto.getMesa().getDepartamento(),data));
                }
            }
        }
        if(aux.equals("")){
            System.out.println("FALHOU");
            return "Nao existem votos";
        }
        System.out.println(" WOW " + aux.toString());
        return aux.toString();
    }

    //imprime todas as mesas de voto
    public synchronized String printMesasDeVoto(){
        //verifica se existem mesas de voto
        if(mesasDeVoto.size()==0){
            return "Nao existem mesas de voto";
        }
        StringBuilder aux = new StringBuilder(100);
        aux.append("Mesas de voto:\n");
        int i = 0;
        for(MesaDeVoto mesaDeVoto : mesasDeVoto){
            i++;
            aux.append(String.format("%d - %s\n",i,mesaDeVoto.getDepartamento()));
        }
        return aux.toString();
    }

    public synchronized String printMesasDeVotoProtocolo(){
        String msg = "type|item_list;item_count|" + mesasDeVoto.size()+ ";" ;
        //verifica se existem mesas de voto
        if(mesasDeVoto.size()==0){
            return "type|listaMesas;item_count|0";
        }
        for (int i = 0; i < mesasDeVoto.size(); i++){
            msg = msg + "item"+i+"_name|"+mesasDeVoto.get(i).getDepartamento()+";";
        }
        return msg;
    }

    //vai buscar o numero de mesas de voto
    public synchronized int numeroMesasDeVoto(){
        return mesasDeVoto.size();

    }

    //imprime as eleicoes concluidas
    public synchronized String eleicoesConcluidasToString(){
        StringBuilder aux = new StringBuilder(100);
        //verifica se existem eleicoes concluidas
        if(eleicoesConcluidas.size()==0){
            return "Não existem eleicoes concluidas";
        }
        for (int i = 0; i < eleicoesConcluidas.size();i++){
            aux.append(String.format("%d - %s \n", i + 1, eleicoesConcluidas.get(i).getTitulo()));
        }
        return aux.toString();
    }

    //imprime as eleicoes que ainda nao comecaram
    public synchronized String eleicoesNComecadasToString(){
        StringBuilder aux = new StringBuilder(100);
        //verifica se existem eleicoes que ainda nao comecaram
        if(eleicoesNaoComecadas.size()==0){
            return "Nao existem eleicoes nao comecadas";
        }
        for (int i = 0; i < eleicoesNaoComecadas.size();i++){
            aux.append(String.format("%d - %s \n", i + 1, eleicoesNaoComecadas.get(i).getTitulo()));
        }
        return aux.toString();
    }

    //imprime informacoes de uma eleicao
    public synchronized String printInformacoes(int nEleicao){
        Eleicao eleicao = eleicoesConcluidas.get(nEleicao-1);

        StringBuilder aux = new StringBuilder(100);
        aux.append(String.format("Titulo: %s\n",eleicao.getTitulo()));
        aux.append(String.format("Descricao: %s\n",eleicao.getDesc()));
        aux.append(String.format("Data de inicio: %s\n",eleicao.dataInicioString()));
        aux.append(String.format("Data de fim: %s\n",eleicao.dataFimString()));
        aux.append(String.format("Tipo: %s\n",eleicao.getType()));
        aux.append(String.format("Departamento: %s\n\n",eleicao.getDep()));

        aux.append(eleicao.resultados());

        return aux.toString();
    }

    //Imprime as eleicoes atuais no formato do protocolo
    public synchronized String eleicoesProtocolo(int id,int nMesa){
        MesaDeVoto mesa = mesasDeVoto.get(nMesa-1);
        ArrayList<Eleicao> eleicoes = mesa.getEleicoesAtuais();
        String msg = "type|item_list;server|" + id + ";item_count|" + eleicoes.size()+ ";" ;
        if(eleicoes.size()==0){
            return "type|eleicoes;server|" + id + ";item_count|0;";
        }
        for (int i = 0; i < eleicoes.size(); i++) {
            msg = msg + "item"+i+"_name|"+eleicoes.get(i).getTitulo()+";";
        }
        return msg;
    }

    //imprime o numero de votos atual em cada mesa numa dada eleicao
    public synchronized String eleitoresATM(int nEleicao, int nMesa){
        MesaDeVoto mesa = mesasDeVoto.get(nMesa-1);
        Eleicao eleicao = mesa.getEleicoesAtuais().get(nEleicao-1);
        return String.format("No votos na mesa %s, eleicao %s: %d",mesa.getDepartamento(),eleicao.getTitulo(),contaVotosMesa(nMesa));
    }

    //imprime a informacao de uma determinada eleicao
    public synchronized String printEleicaoInfo(int nEleicao){
        Eleicao eleicao = eleicoesNaoComecadas.get(nEleicao-1);
        GregorianCalendar agora = new GregorianCalendar();
        if(eleicao.getIniData().before(agora)){
            return "A eleicao ja comecou, impossivel alterar os dados";
        }
        StringBuilder aux = new StringBuilder(100);
        aux.append(String.format("Titulo: %s\n",eleicao.getTitulo()));
        aux.append(String.format("Descricao: %s\n",eleicao.getDesc()));
        aux.append(String.format("Data de inicio: %s\n",eleicao.dataInicioString()));
        aux.append(String.format("Data de fim: %s\n",eleicao.dataFimString()));
        aux.append(String.format("Tipo: %s\n",eleicao.getType()));
        aux.append(String.format("Departamento: %s\n",eleicao.getDep()));
        return aux.toString();
    }

    //imprime as mesas que ja estao ativas e as que estao inativas
    public synchronized String printMesasAtivas(){
        StringBuilder aux = new StringBuilder(100);
        aux.append("Mesas ativas:\n");
        for(MesaDeVoto mesa: mesasDeVoto){
            if(mesa.isActive()){
                aux.append(mesa.getDepartamento()+"\n");
                for(TerminalDeVoto terminal : mesa.getTerminais()){
                    //imprime os terminais ativos e inativos de uma mesa
                    if(terminal.isActive()){
                        aux.append(String.format("  terminal %d - ativo\n",terminal.getId()));
                    }
                    else{
                        aux.append(String.format("  terminal %d - inativo\n",terminal.getId()));
                    }
                }
            }
        }
        aux.append("\nMesas inativas:\n");
        for(MesaDeVoto mesa: mesasDeVoto){
            if(!mesa.isActive()){
                aux.append(mesa.getDepartamento()+"\n");
            }
        }
        return aux.toString();
    }

    //imprime as listas candidatas a uma dada eleicao no formato do protocolo
    public synchronized String printListasCandidatasProtocolo(int nEleicao, int nCC, int id){
        Eleicao eleicao = eleicoesAtuais.get(nEleicao-1);
        Pessoa pessoa= encontraPessoa(nCC);

        if(pessoa==null){
            return "Eleitor nao registado";
        }
        ArrayList<Eleicao> eleicoesVotadas = pessoa.getEleicoesVotadas();
        if(eleicoesVotadas.contains(eleicao)){
            return "type|eleicoes;server|" + id + ";eleicao|-1;";
        }
        StringBuilder aux = new StringBuilder(100);
        ArrayList<Lista> listas = eleicao.getListas();
        String msg = "type|listas;server|" + id + ";item_count|" + listas.size()+ ";" ;
        int i = 0;
        for(Lista lista: listas){
            i++;
            msg = msg + "item_"+i+"_name|"+lista.getNomeLista()+";";
        }
        System.out.println(msg);
        return msg;
    }

    //imprime das listas candidatas
    public synchronized String printListasCandidatas(int nEleicao, int nCC){
        Eleicao eleicao = eleicoes.get(nEleicao-1);
        Pessoa pessoa= encontraPessoa(nCC);
        //verifica se o eleitor existe
        if(pessoa==null){
            return "Eleitor nao registado";
        }
        ArrayList<Eleicao> eleicoesVotadas = pessoa.getEleicoesVotadas();
        if(eleicoesVotadas.contains(eleicao)){
            return "type|eleicoes;eleicao|-1;";
        }
        StringBuilder aux = new StringBuilder(100);
        ArrayList<Lista> listas = eleicao.getListas();
        String msg = "type|listas;item_count|" + listas.size()+ ";" ;
        int i = 0;
        for(Lista lista: listas){
            i++;
            msg = msg + "item_"+i+"_name|"+lista.getNomeLista()+";";
        }
        System.out.println(msg);
        return msg;
    }

    public synchronized ArrayList<Lista> getListasCandidatas(int nEleicao, int nCC){
        Eleicao eleicao = eleicoes.get(nEleicao-1);
        System.out.println(eleicao.getListas().size()+ " nlistas");
        return eleicao.getListas();
    }
    public synchronized ArrayList<Lista> getListasCandidatasAtuais(int nEleicao, int nCC){
        Eleicao eleicao = eleicoesAtuais.get(nEleicao-1);
        System.out.println(eleicao.getListas().size()+ " nlistas");
        return eleicao.getListas();
    }

    //----------------------------ADICIONAR/REMOVER DADOS--------------------------------

    //Adiciona um novo eleitor
    public synchronized String inserePessoa(String nome, String func, String password, String departamento, int nTelefone, String morada, int numCC, String valCC){
        //Verifica se o eleitor já existe atraves do numero do CC
        for(Pessoa pessoa : pessoas){
            if(pessoa.getNumCC()==numCC){
                return "Eleitor já registado";
            }
        }
        //Cria o novo eleitor e adiciona a lista
        Pessoa p = new Pessoa(nome, func, password, departamento, nTelefone,morada,numCC,valCC);
        pessoas.add(p);
        return "Eleitor registado com sucesso";
    }

    //Insere uma nova lista candidata
    public synchronized String insereLista(String nomeLista, String type,int[] candidatosCC, int nEleicao){
        ArrayList<Pessoa> candidatos = new ArrayList<>();

        //verifica se existem eleitores
        if(pessoas.size()==0){
            return "Não existem eleitores";
        }

        //Verifica se existem eleicoes que ainda nao comecaram
        if(eleicoesNaoComecadas.size()==0){
            return "Não existem eleicoes";
        }

        //vai buscar a eleicao escolhida
        Eleicao el=eleicoesNaoComecadas.get(nEleicao-1);


        for (int j : candidatosCC) {
            //verifica se os candidatos estao registados como eleitores
            if (encontraPessoa(j) == null) {
                return "Lista não aceite. Possui candidatos não registados.";
            }

            //verifica se os candidatos sao do mesmo tipo (estudante,docente ou funcionario)
            if(!encontraPessoa(j).getFunc().equals(type)) {
                return "Lista não aceite. Possui candidatos de diferentes tipos";
            }

            //verifica se todos os candidatos pertencem ao mesmo departamento
            if(!(encontraPessoa(j).getDepartmento().equals(el.getDep())) && !el.getDep().equals("TODOS")){
                return "Lista não aceite. Possui candidatos que não pertencem ao departamento.";
            }

        }

        //adiciona os cadidatos
        for (int j : candidatosCC){
            candidatos.add(encontraPessoa(j));
        }

        //Cria uma nova lista
        Lista lista = new Lista(nomeLista,type,candidatos);
        ArrayList<Lista> aux = el.getListas();
        aux.add(lista);
        el.setListas(aux);
        return "Lista inserida com sucesso";

    }

    public synchronized String encontraFB(String id, String nome){
        for(Pessoa p : pessoas){
            if(p.getFbID()!=null && p.getFbID().equals(id)){
                return "Pessoa ja existe";
            }
        }

        Pessoa p = new Pessoa(id,nome);
        pessoas.add(p);
        return "Pessoa adicionada";
    }

    public synchronized int getnCCfromId(String id){
        for(Pessoa p : pessoas){
            if(p.getFbID().equals(id)){
                return p.getNumCC();
            }
        }
        return -1;
    }

    public synchronized String addInfoPessoa(String id, String func, String password, String departamento, int nTelefone, String morada, int numCC, String valCC){
        for (Pessoa p : pessoas){
            if(p.getFbID()!=null && p.getFbID().equals(id)){
                p.setFunc(func);
                p.setDepartamento(departamento);
                p.setMorada(morada);
                p.setnTelefone(nTelefone);
                p.setNumCC(numCC);
                p.setPassword(password);
                p.setValCC(valCC);
                return "Dados inseridos com sucesso";
            }
        }
        return "Pessoa nao encontrada";
    }

    //Remove as mesas que ficaram sem eleicoes
    public  synchronized String removeMesa(){
        for(Iterator<MesaDeVoto> iterator = mesasDeVoto.iterator(); iterator.hasNext(); ){
            MesaDeVoto mesa = iterator.next();
            //se o numero de eleicoes for 0, remover a mesa
            if(mesa.getEleicoes().size()==0){
                iterator.remove();
            }
        }
        return "Mesas sem eleicoes removidas";
    }

    //Elimina uma mesa de voto de uma dada eleicao
    public synchronized String delMesa(int nEleicao, String dep){

        //verifica se existem eleicoes que ainda nao comecaram
        if(eleicoesNaoComecadas.size()==0){
            return "Não existem eleicoes";
        }

        Eleicao eleicao = eleicoesNaoComecadas.get(nEleicao-1);
        ArrayList<MesaDeVoto> mesas = eleicao.getMesas();

        //percorre as mesas de voto
        for (MesaDeVoto mesa : mesasDeVoto) {
            //percorre as mesas de voto da eleicao
            for(MesaDeVoto mesaE : mesas){
                //se a mesa de voto for a mesma que a mesa de voto da eleicao
                if(mesa.getDepartamento().equals(mesaE.getDepartamento())) {
                    //eliminar a mesa se a mesa da eleicao for a mesa do departamento que se
                    // quer eliminar e a mesa so tiver essa eleicao
                    if (mesaE.getDepartamento().equals(dep) && mesa.getEleicoes().size() == 1) {
                        eleicao.delMesa(dep); //eliminar a mesa da eleicao
                        ArrayList<Eleicao> aux = mesa.getEleicoes();
                        aux.remove(eleicao); //eliminar a eleicao da mesa
                        mesa.setEleicoes(aux);
                        mesasDeVoto.remove(mesa); //eliminar a mesa da lista de mesas
                        return "Mesa eliminada com sucesso";
                    }

                    //eliminar a mesa se a mesa da eleicao for a mesa do departamento que se
                    // quer eliminar e a mesa tem mais eleicoes
                    if (mesaE.getDepartamento().equals(dep) && mesa.getEleicoes().size() > 1) {
                        eleicao.delMesa(dep); //eliminar a mesa da eleicao
                        ArrayList<Eleicao> aux = mesa.getEleicoes();
                        aux.remove(eleicao); //eliminar a eleicao da mesa
                        mesa.setEleicoes(aux);
                        return "Eleicao eliminada da mesa com sucesso";
                    }
                }
            }

        }
        return "Mesa não encontrada";
    }

    //adiciona uma mesa de voto
    public synchronized String addMesa(int nEleicao, String dep){
        //verificar se existem eleicoes que ainda nao comecaram
        if(eleicoesNaoComecadas.size()==0){
            return "Não existem eleicoes";
        }

        Eleicao eleicao = eleicoesNaoComecadas.get(nEleicao-1);
        ArrayList<MesaDeVoto> mesasEleicao = eleicao.getMesas();

        //Se a eleicao ja tiver mesas
        if(mesasEleicao.size()>0) {
            for (MesaDeVoto mesaE : mesasEleicao) {
                if (mesaE.getDepartamento().equals(dep)) {
                    return String.format("Esta eleiçao já possiu uma mesa neste departamento %s", mesaE.getDepartamento());
                }
                for (MesaDeVoto mesa : mesasDeVoto) {
                    if (mesa.getDepartamento().equals(dep)) { //se a mesa já existe add na eleicao
                        mesa.addEleicao(eleicao);
                        eleicao.addMesa(mesa);
                        return "Eleicao adicionada à mesa com sucesso";
                    }
                }
            }
            //mesa não foi encontrada: add mesa a lista de mesasDeVoto, add mesa a eleicao, add eleicao a mesa
            MesaDeVoto novaMesa = new MesaDeVoto(dep, eleicao);
            eleicao.addMesa(novaMesa);
            mesasDeVoto.add(novaMesa);
        }
        else{
            for (MesaDeVoto mesa : mesasDeVoto) {
                if (mesa.getDepartamento().equals(dep)) { //se a mesa já existe add na eleicao
                    mesa.addEleicao(eleicao);
                    eleicao.addMesa(mesa);
                    return "Eleicao adicionada à mesa com sucesso";
                }
            }
            //mesa não foi encontrada: add mesa a lista de mesasDeVoto, add mesa a eleicao, add eleicao a mesa
            MesaDeVoto novaMesa = new MesaDeVoto(dep, eleicao);
            eleicao.addMesa(novaMesa);
            mesasDeVoto.add(novaMesa);
        }
        return "Mesa adicionada com sucesso";
    }

    //Elimina uma eleicao de uma mesa (usada quando a eleicao acaba)
    public synchronized String delEleicaoMesa(Eleicao eleicao){
        //percorre as mesas todas
        for(MesaDeVoto mesa : mesasDeVoto){
            ArrayList<Eleicao> eleicoesMesa = mesa.getEleicoes();
            //percorre as eleicoes dentro de cada mesa
            for(Iterator<Eleicao> iterator = eleicoesMesa.iterator(); iterator.hasNext(); ){
                Eleicao eleicaoMesa = iterator.next();
                if(eleicaoMesa.equals(eleicao)){
                    ArrayList<Eleicao> eleicoesAtuais = mesa.getEleicoesAtuais();
                    for(Iterator<Eleicao> iterator1 = eleicoesAtuais.iterator(); iterator1.hasNext(); ){
                        Eleicao eleicaoAtual = iterator1.next();
                        if(eleicaoAtual.equals(eleicao)){
                            iterator1.remove(); //remover a eleicao das eleicoes atuais da mesa
                        }
                    }
                    iterator.remove(); //remover a eleicao das eleicoes da mesa
                    return "Eleicao eliminada da mesa com sucesso";
                }
            }
        }
        return "Eleicao eliminada da mesa com sucesso";
    }

    //Adiciona uma eleicao a lista de eleicoes atuais das mesas
    public synchronized String atualizaMesas(Eleicao eleicao){
        for(MesaDeVoto mesa : mesasDeVoto){
            ArrayList<Eleicao> eleicoes = mesa.getEleicoes();
            for(Eleicao eleicaoM : eleicoes){
                //se encontrar a eleicao
                if(eleicaoM.equals(eleicao)){
                    mesa.addEleicaoAtual(eleicao);
                    return "Eleicao atual na mesa";
                }
            }
        }
        return "Erro a atualizar eleicoes mesa";
    }

    //insere uma nova eleicao
    public synchronized void insereEleicao(GregorianCalendar iniData, GregorianCalendar fimData,String titulo, String desc, String type, String dep){

        Eleicao e = new Eleicao(iniData, fimData,titulo,desc, type, dep);
        eleicoes.add(e);
        eleicoesNaoComecadas.add(e);

    }


    //--------------------------------ALTERAR DADOS------------------------------------

    //edita as informacoes de uma eleicao
    public synchronized String editaEleicao(int nEleicao, String oQue, String alteracao){
        //verifica se a eleicao ja comecou
        if(eleicoesNaoComecadas.size()==0){
            return "Nao existem eleicoes nao comecadas";
        }

        Eleicao eleicao = eleicoesNaoComecadas.get(nEleicao-1);
        GregorianCalendar agora = new GregorianCalendar();
        switch (oQue) {
            case "titulo":
                eleicao.setTitulo(alteracao);
                return "Titulo editado com sucesso";
            case "desc":
                eleicao.setDesc(alteracao);
                return "Descricao alterada com sucesso";
            case "dep" :
                eleicao.setDep(alteracao);
                return "Departamento alterado com sucesso";
            case "dataInicio":
                //separa a data para meter no calendar
                String[] aux = alteracao.split("-");
                int ano = Integer.parseInt(aux[0]);
                int mes = Integer.parseInt(aux[1]);
                int dia = Integer.parseInt(aux[2]);
                int hora = Integer.parseInt(aux[3]);
                int minuto = Integer.parseInt(aux[4]);
                GregorianCalendar dataEleicao = new GregorianCalendar(ano,mes-1,dia,hora,minuto);
                if(eleicao.getFimData().before(dataEleicao)){
                    return "Data final antes da data inicial";
                }
                if(agora.after(dataEleicao)){
                    return "Data inicial já passou";
                }
                eleicao.setIniData(dataEleicao);
                return "Data de inicio alterada com sucesso";

            case "dataFim" :
                //separa a data para meter no calendar
                String[] aux1 = alteracao.split("-");
                int ano1 = Integer.parseInt(aux1[0]);
                int mes1 = Integer.parseInt(aux1[1]);
                int dia1 = Integer.parseInt(aux1[2]);
                int hora1 = Integer.parseInt(aux1[3]);
                int minuto1 = Integer.parseInt(aux1[4]);
                GregorianCalendar dataEleicao1 = new GregorianCalendar(ano1,mes1-1,dia1,hora1,minuto1);
                if(dataEleicao1.before(eleicao.getIniData())){
                    return "Data final antes da data inicial";
                }
                if(agora.before(dataEleicao1)){
                    return "Data final já passou";
                }
                eleicao.setFimData(dataEleicao1);
                return "Data de fim alterada com sucesso";
            case "type":
                eleicao.setType(alteracao);
                return "Tipo alterado com sucesso";
            default:
                return "erro";
        }
    }

    public synchronized String propriedadesEleicao(){
        return "1 - Data de inicio\t2 - Data de fim\t3 - Titulo\t4 - Descricao\t5 - Tipo\t6 - Departamento";
    }

    //ativa um terminal
    public synchronized String ativaTerminal(int nMesa, int id){
        MesaDeVoto mesa = mesasDeVoto.get(nMesa-1);
        return mesa.ativaTerminal(id);
    }

    //desativa o terminal
    public synchronized String desativaTerminal(int nMesa, int id){
        MesaDeVoto mesa = mesasDeVoto.get(nMesa-1);
        return mesa.desativaTerminal(id);
    }

    //Ativa mesa
    public synchronized String ativarMesa(int nMesa){
        MesaDeVoto mesa = mesasDeVoto.get(nMesa-1);
        //verifica se a mesa ja esta ativada
        if(mesa.isActive()){
            return "Mesa já ativada";
        }
        else{
            mesa.setActive(true);
            return "Mesa ativada";
        }
    }

    //desativa a mesa
    public synchronized String desativarMesa(int nMesa){
        MesaDeVoto mesa = mesasDeVoto.get(nMesa-1);
        //verifica se a mesa ja esta desativada
        if(!mesa.isActive()){
            return "Mesa já desativada";
        }
        else{
            mesa.setActive(false);
            return "Mesa desativada";
        }
    }

    //adiciona um voto a uma lista numa dada eleicao
    public synchronized String addVoto(int nEleicao, int nlista, int nCC, int mesa, GregorianCalendar time){
        Eleicao eleicao = eleicoesAtuais.get(nEleicao-1);
        MesaDeVoto mesaDeVoto;
        if(mesa==999){
            mesaDeVoto = new MesaDeVoto("WEB");
        }
        else
            mesaDeVoto = mesasDeVoto.get(mesa-1);
        ArrayList<Lista> listas = eleicao.getListas();
        if(listas.size()==0){
            return "Nao existem listas candidatas";
        }

        Pessoa pessoa= encontraPessoa(nCC);
        ArrayList<Eleicao> listaEleicoes= pessoa.getEleicoesVotadas();
        ArrayList<Voto> votos= pessoa.getListaDeVotos();

        if(listaEleicoes.contains(eleicao)){
            return "O eleitor já votou nesta eleicao";
        }

        if(nlista==listas.size()+1){ //Voto Nulo
            int votosNulos = eleicao.getVotosNulos();
            votosNulos++;
            eleicao.setVotosNulos(votosNulos);
        }
        else if(nlista==listas.size()+2){ //Voto branco
            int votosBranco = eleicao.getVotosBranco();
            votosBranco++;
            eleicao.setVotosBranco(votosBranco);
        }
        else{
            Lista lista = listas.get(nlista-1);
            int nVotos = lista.getnVotos();
            nVotos++;
            lista.setnVotos(nVotos);
        }

        listaEleicoes.add(eleicao);
        pessoa.setEleicoesVotadas(listaEleicoes);
        Voto voto = new Voto(mesaDeVoto,time,eleicao);
        votos.add(voto);
        pessoa.setListaDeVotos(votos);
        return "Voto adicionado";
    }

    //conta os votos de uma determinada mesa
    private synchronized int contaVotosMesa(int nMesa){
        MesaDeVoto mesa = mesasDeVoto.get(nMesa-1);
        int conta = 0;
        for(Pessoa pessoa : pessoas){
            //percorre os sitios onde a pessoa votou
            for(Voto voto : pessoa.getListaDeVotos()){
                //se for o mesmo sitio incrementa o contador
                if(mesa.getDepartamento().equals(voto.getMesa().getDepartamento())){
                    conta++;
                }
            }
        }
        return conta;
    }


    //--------------------------GETTERS E SETTERS-----------------------------------

    public synchronized ArrayList<Eleicao> getEleicoes() { return eleicoes; }


    public synchronized ArrayList<Eleicao> getEleicoesConcluidas() {
        return eleicoesConcluidas;
    }

    public synchronized void setEleicoesConcluidas(ArrayList<Eleicao> eleicoesConcluidas) {
        this.eleicoesConcluidas = eleicoesConcluidas;
    }

    public synchronized ArrayList<Eleicao> getEleicoesAtuais() {
        return eleicoesAtuais;
    }

    public synchronized void setEleicoesAtuais(ArrayList<Eleicao> eleicoesAtuais) {
        this.eleicoesAtuais = eleicoesAtuais;
    }

    public ArrayList<Eleicao> getEleicoesNaoComecadas() {
        return eleicoesNaoComecadas;
    }


    public void setEleicoesNaoComecadas(ArrayList<Eleicao> eleicoesNaoComecadas) {
        this.eleicoesNaoComecadas = eleicoesNaoComecadas;
    }

    public ArrayList<Pessoa> getPessoas() {
        return pessoas;
    }
}
