package biblioteca.model;

public class Aluno extends Usuario{

    private String matricula;
    public Aluno(){}

    //Construtor
    public Aluno(int id, String nome, String email, String telefone, String matricula){
        super(id, nome, email, telefone);
        this.matricula = matricula;
    }

    @Override
    public String toString(){
        return "Aluno{" +
            "matricula='" + matricula + '\'' +
            ", " + super.toString() +
        '}';
    }

    //Getters e Setters
    public String getMatricula(){ 
        return matricula; 
    }
    public void setMatricula(String matricula){ 
        this.matricula = matricula; 
    }

}
