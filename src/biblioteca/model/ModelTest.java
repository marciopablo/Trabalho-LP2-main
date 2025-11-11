package biblioteca.model;

public class ModelTest{
    public static void main(String[] args){
        
        Livro l = new Livro(1, "Algoritmos", "Átila", "9780262033848", 2012, Livro.Status.DISPONIVEL);
        Aluno a = new Aluno(1, "Joao", "joao@ufrn.edu.br", "99999-9999", "20250001");
        Funcionario f = new Funcionario(2, "Marcio", "marcio@ufrn.edu.br", "98871-9999", "Almoxarifadista");

        System.out.println(l);
        System.out.println(a);
        System.out.println(f);
    }
}
