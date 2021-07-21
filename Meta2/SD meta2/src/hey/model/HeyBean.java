/**
 * Raul Barbosa 2014-11-07
 */
package hey.model;

import classes.Eleicao;
import classes.Lista;
import rmiserver.RMI_S_I;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class HeyBean {
	private RMI_S_I server;
	private ArrayList<Integer> listaElementos;


	public HeyBean() {
		try {
			server = (RMI_S_I) Naming.lookup("rmi://localhost:1099/project");
		}
		catch(NotBoundException | RemoteException | MalformedURLException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}

	//regista novo eleitor
	public String registaEleitor(String nome, String func, String password, String departamento, int nTelefone, String morada, int numCC, String valCC) throws  RemoteException{
		if (!nome.equals("") && !password.equals("") && !func.equals("") && !departamento.equals("") && nTelefone != 0 && !morada.equals("") && numCC != 0 && !valCC.equals("")) {
			if (this.server.registaPessoa(nome, func, password, departamento, nTelefone, morada, numCC, valCC).equals("Eleitor registado com sucesso")) {
				System.out.println("Utilizador registado com sucesso.");
				return "SUCCESS";
			}
		}
		return "ERROR";
	}

	//login de utilizador
	public String loggaUser(int numCC, String password) throws RemoteException {
		if (numCC!=0 && !password.equals("")) {
			if (this.server.checkaPessoa(numCC) != null && this.server.checkaPessoa(numCC).getPassword().equals(password)) {
				System.out.println("Login efetuado com sucesso.");
				return "LOGGED";
			}
		return "FAILED";
		}
		return "FAILED";
	}

	//cria nova eleicao
	public String criarEleicao(String iniData, String fimData, String titulo, String desc, String type, String dep) throws RemoteException {
		GregorianCalendar dataInicio;
		GregorianCalendar dataFim;
		dataInicio = verificaStringData(iniData);
		if(dataInicio == null){
			System.out.println("FAILED. Data no formato errado");
			return "FAILED";
		}
		dataFim = verificaStringData(fimData);
		if(dataFim == null){
			System.out.println("FAILED. Data no formato errado");
			return "FAILED";
		}
		if (!iniData.equals("") && !fimData.equals("") && !titulo.equals("") && !desc.equals("") && !type.equals("") && !dep.equals("")){
			if ( this.server.criaEleicao(dataInicio, dataFim, titulo, desc, type,dep).equals("Eleicao criada com sucesso.")){
				System.out.println("Eleicao criada com sucesso.");
				return "CREATED";
			}else
				return "FAILED";
		}
	return "FAILED";
	}

	public String propriedadesEleicoes(){
		String prop = "";
		prop = "1 - Data de inicio(AAAA-MM-DD)\n2 - Data de fim(AAAA-MM-DD)\n3 - Titulo\n4 - Titulo\n5 - Tipo(1-Estudamte	2-Docente	3-Funcionario	4-Todos-)\n6 - Departamento";
		return prop;
	}

	public ArrayList<Eleicao> listarEleicoes() throws RemoteException {
		System.out.println(this.server.getListaEleicoesAtuais());
		return this.server.getListaEleicoesAtuais();
	}

	public ArrayList<Eleicao> listarEleicoesConcluidas() throws RemoteException {
		System.out.println(this.server.getListaEleicoesConcluidas());
		return this.server.getListaEleicoesConcluidas();
	}

	public ArrayList<Eleicao> listarEleicoesAtuais() throws RemoteException {
		System.out.println(this.server.getListaEleicoesAtuais());
		return this.server.getListaEleicoesAtuais();
	}

	public ArrayList<Eleicao> listarEleicoesNaoComecadas() throws RemoteException {
		System.out.println(this.server.getListaEleicoesNaoComecadas());
		return this.server.getListaEleicoesNaoComecadas();
	}

	public ArrayList<Lista> listarListasCandidatas(int nEleicao, int numCC) throws RemoteException{
		return this.server.getListasCandidatasAtuais(nEleicao, numCC);
	}

	public String detalhesEleicao(int nEleicao) throws RemoteException{
		return this.server.eleicaoInfo(nEleicao);
	}

	public String consultarDetalhesEleicoesPassadas(int nEleicao) throws RemoteException{
		return this.server.printInformacoesEleicoesConc(nEleicao);
	}

	public String editarEleicao(int nEleicao, String oq, String alteracao) throws RemoteException{
		return this.server.editaEleicao(nEleicao,oq, alteracao);
	}

	public String localVoto(int nEleicao) throws RemoteException{
		return this.server.saberLocal(nEleicao);
	}

	public String addMesaVoto(int nEleicao, String dep) throws RemoteException {
		return this.server.addMesa(nEleicao, dep);
	}

	public String votar(int nEleicao, int nlista, int nCC, int mesa) throws RemoteException {
		return this.server.vota(nEleicao,nlista,nCC,mesa);
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

	public String addMesaDeVoto(int nEleicao, String dep) throws RemoteException {
		String res = this.server.addMesa(nEleicao,dep);
		if(res.equals("Mesa adicionada com sucesso")){
			return "ADDED";
		}
		System.out.println(res);
		return "FAILED";
	}

	public String criaLista(String nomeLista, String type,int[] candidatosCC, int nEleicao) throws RemoteException {
		return this.server.criaLista(nomeLista,type,candidatosCC,nEleicao);
	}

	public ArrayList<Eleicao> listarEleicoesAtuaisNCC(int nCC) throws RemoteException {
		return this.server.getListaEleicoesAtuaisNCC(nCC);
	}

	public ArrayList<String> conectaFb(String code,String username) throws RemoteException {
		return this.server.conectaFb(code,username);
	}

	public String connectionURL() throws RemoteException{
		return this.server.connectionURLLogin();
	}

	public String authorizationURL() throws RemoteException{
		return this.server.authorizationURLLogin();
	}

	public String addInfoFB(String code, String func, String pass, String dep, int nTel, String morada, int nCC, String valCC ) throws RemoteException{
		return this.server.addInfoFB(code, func, pass, dep, nTel, morada, nCC, valCC);
	}

	public String encontraFB(String code, String nome) throws RemoteException{
		return this.server.encontraFB(code, nome);
	}

	public int getnCCFromID(String id) throws RemoteException{
		return this.server.getnCCFromID(id);
	}

	}
