package classes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class Eleicao implements Serializable {

    private GregorianCalendar iniData;
    private GregorianCalendar fimData;
    private String titulo;
    private String desc;
    private String type;
    private String dep;
    private ArrayList<Lista> listas;
    private ArrayList<MesaDeVoto> mesas;
    private int votosNulos;
    private int votosBranco;


    public Eleicao(GregorianCalendar iniData, GregorianCalendar fimData, String titulo, String desc, String type, String dep){
        this.iniData = iniData;
        this.fimData = fimData;
        this.titulo = titulo;
        this.desc = desc;
        this.type = type;
        this.dep=dep;
        this.listas= new ArrayList<>();
        this.mesas= new ArrayList<>();
        this.votosNulos=0;
        this.votosBranco=0;
    }

    public String dataInicioString(){
        return String.format("%d-%d-%d %d:%d",iniData.get(Calendar.DATE),iniData.get(Calendar.MONTH)+1,iniData.get(Calendar.YEAR),iniData.get(Calendar.HOUR_OF_DAY),iniData.get(Calendar.MINUTE));
    }

    public String dataFimString(){
        return String.format("%d-%d-%d %d:%d",fimData.get(Calendar.DATE),fimData.get(Calendar.MONTH)+1,fimData.get(Calendar.YEAR),fimData.get(Calendar.HOUR_OF_DAY),fimData.get(Calendar.MINUTE));
    }

    //conta os votos
    private int totalVotos(){
        int total= votosBranco+votosNulos;
        for(Lista lista : listas){
            total=total + lista.getnVotos();
        }
        return total;
    }

    //imprime os resultados
    public String resultados(){
        int total=totalVotos();
        if(total==0){
            return "Nao existem votos nesta eleicao";
        }
        StringBuilder aux = new StringBuilder(100);
        aux.append(String.format("Total de Votos: %d\n",total));

        for(Lista lista : listas){
            float percentagem = (float) (((float)lista.getnVotos()/(float)total)*100.0);
            aux.append(String.format("Lista %s: %d votos (%.2f%%)\n",lista.getNomeLista(),lista.getnVotos(),percentagem));
        }
        float percentagemVB = (float) (((float)votosBranco/(float)total)*100.0);
        float percentagemVN = (float) (((float)votosNulos/(float)total)*100.0);
        aux.append(String.format("Votos em branco: %d votos (%.2f%%)\n",votosBranco,percentagemVB));
        aux.append(String.format("Votos nulos: %d votos (%.2f%%)\n",votosNulos,percentagemVN));

        return aux.toString();
    }

    //adiciona a mesa
    public void addMesa(MesaDeVoto mesa){
        mesas.add(mesa);
    }

    //elimina a mesa
    public void delMesa(String dep){
        for(Iterator<MesaDeVoto> iterator = mesas.iterator(); iterator.hasNext();){
            MesaDeVoto mesa = iterator.next();
            if(mesa.getDepartamento().equals(dep)){
                iterator.remove();
            }
        }
    }

    public GregorianCalendar getIniData() {
        return iniData;
    }

    public void setIniData(GregorianCalendar iniData) {
        this.iniData = iniData;
    }

    public GregorianCalendar getFimData() {
        return fimData;
    }

    public void setFimData(GregorianCalendar fimData) {
        this.fimData = fimData;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDep(){
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public ArrayList<Lista> getListas() {
        return listas;
    }

    public void setListas(ArrayList<Lista> listas) {
        this.listas = listas;
    }

    public ArrayList<MesaDeVoto> getMesas() {
        return mesas;
    }

    public int getVotosNulos() {
        return votosNulos;
    }

    public void setVotosNulos(int votosNulos) {
        this.votosNulos = votosNulos;
    }

    public int getVotosBranco() {
        return votosBranco;
    }

    public void setVotosBranco(int votosBranco) {
        this.votosBranco = votosBranco;
    }
}
