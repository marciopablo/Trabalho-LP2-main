package biblioteca.model;

public class Professor extends Usuario{
    
    private String siape;
    public Professor(){}

    //Construtor
    public Professor(int id, String nome, String email, String telefone, String siape){
        super(id, nome, email, telefone);
        this.siape = siape;
    }

    @Override
    public String toString(){
        return "Professor{" +
            "siape='" + siape + '\'' +
            ", " + super.toString() +
            '}';
    }

    //getters e setters
    public String getSiape(){ 
        return siape; 
    }
    public void setSiape(String siape){ 
        this.siape = siape; 
    }
}
