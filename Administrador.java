package conexao;

// Certifique-se de que Usuario e Funcionario estão no mesmo pacote 'conexao' ou ajuste os imports.

public class Administrador extends Funcionario {

    // Construtor
    // Os parâmetros passados para o super() devem corresponder ao construtor da classe pai (Funcionario)
    public Administrador(int id, String nome, String tipoUsuario, String cargo, String departamento) {
        super(id, nome, tipoUsuario, cargo, departamento);
    }

    // Método para exibir as informações do administrador
    // Ele chama o método da classe pai (Funcionario) e adiciona informações específicas
    @Override
    public void exibirInformacoes() {
        super.exibirInformacoes();
        System.out.println("Tipo de Usuário: Administrador");
        // Se houver atributos específicos de Administrador além dos já herdados, eles seriam exibidos aqui.
        // No seu script SQL, a tabela 'admin' tem 'Permissoes'. Você pode adicionar um atributo 'permissoes'
        // aqui e passá-lo no construtor se quiser mapear isso diretamente para a classe Java.
    }

    // Os métodos 'adicionarLivro', 'removerLivro', 'listarLivros' foram removidos daqui.
    // Essas são operações que um Administrador pode *solicitar* no sistema,
    // mas a lógica de como essas operações interagem com o banco de dados pertence a classes DAO
    // ou a uma camada de serviço.
}