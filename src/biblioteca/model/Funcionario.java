package biblioteca.model;

public class Funcionario extends Usuario{

    private String cargo;
    public Funcionario(){}

    //Construtor
    public Funcionario(int id, String nome, String email, String telefone, String cargo){
        super(id, nome, email, telefone);
        this.cargo = cargo;
    }

    @Override
    public String toString(){
        return "Funcionario{" +
            "cargo='" + cargo + '\'' +
            ", " + super.toString() +
        '}';
    }

    //Getters e Setters
    public String getCargo(){ 
        return cargo; 
    }
    public void setCargo(String cargo){ 
        this.cargo = cargo; 
    }
}
