/**
 * Raul Barbosa 2014-11-07
 */
package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private int numCC = 0;
	private String password = null;
	private String url = null;


	@Override
	public String execute() throws RemoteException {

		//se inserir dados
		if(this.numCC != 0 && !password.equals("")) {

			//faz o login do utilizador
			if (this.getHeyBean().loggaUser(this.numCC, this.password).equals("LOGGED")) {
				//define as permissoes desta sessao
				if(this.numCC == 1){
					session.put("admin", true);
				}else{
					session.put("admin", false);
				}
				//coloca os dados na sessao
				session.put("numCC", numCC);
				session.put("loggedin", true); // this marks the user as logged in
				return SUCCESS;
			}
			else
				return ERROR;
		}
		else {
			//fazer log in com o facebook
			if(this.session.containsKey("id")){
				System.out.println("Login com o fbID: " + session.get("id"));
				return SUCCESS;
			}
			return ERROR;
		}
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setNumCC(int numCC) {
		this.numCC = numCC; // will you sanitize this input? maybe use a prepared statement?
	}

	public int getNumCC() {
		return numCC;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password; // what about this input?
	}

	public HeyBean getHeyBean() {
		if(!session.containsKey("heyBean"))
			this.setHeyBean(new HeyBean());
		return (HeyBean) session.get("heyBean");
	}

	public void setHeyBean(HeyBean heyBean) {
		this.session.put("heyBean", heyBean);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}