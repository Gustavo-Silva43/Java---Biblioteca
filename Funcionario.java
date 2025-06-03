package conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Adicionado para Statement.RETURN_GENERATED_KEYS
import java.util.ArrayList;
import java.util.List;

public class Funcionario extends Usuario {

    private String cargo;
    private String departamento;

    // Construtor
    // IMPORTANTE: O construtor da superclasse (Usuario) deve ser chamado com parâmetros compatíveis.
    // No seu código original, você chamava: super(id, nome, tipoUsuario, departamento);
    // Mas 'departamento' não é um parâmetro típico para um construtor de Usuario base.
    // Assumindo que Usuario tem um construtor (id, nome, tipoUsuario) e talvez login/senha.
    // Se o Usuario tiver login/senha, você precisará passá-los para este construtor de Funcionario
    // e então para o super(). Por simplicidade, vou manter a estrutura que você tentou usar,
    // assumindo que 'tipoUsuario' pode ser usado de forma flexível ou que o construtor de Usuario
    // foi ajustado para aceitar apenas 3 parâmetros.
    public Funcionario(int id, String nome, String tipoUsuario, String cargo, String departamento) {
        // Correção aqui: O construtor da superclasse Usuario deve ser compatível.
        // Se Usuario tem (id, nome, tipoUsuario), então é assim:
        super(id, nome, tipoUsuario);
        // Se Usuario tem (id, nome, tipoUsuario, login, senha), você precisaria de mais parâmetros aqui.

        this.cargo = cargo;
        this.departamento = departamento;
    }

    // Getters e Setters
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    // Método para exibir as informações do funcionário (inclui as informações do usuário)
    @Override // Agora este @Override deve funcionar se Usuario tiver exibirInformacoes()
    public void exibirInformacoes() {
        super.exibirInformacoes(); // Chama o método exibirInformacoes() da classe pai (Usuario)
        System.out.println("Cargo: " + cargo);
        System.out.println("Departamento: " + departamento);
    }

    // --- MÉTODOS DAO (PERSISTÊNCIA) ---

    // Método para adicionar um novo funcionário ao banco de dados
    // Refatorado para usar Conexao.fechar() e lidar com transações explicitamente
    public static void adicionarFuncionario(String nome, String tipoUsuario, String cargo, String departamento) {
        Connection conn = null;
        PreparedStatement stmtUsuario = null;
        PreparedStatement stmtFuncionario = null;
        ResultSet rs = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível adicionar funcionário.");
                return;
            }
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Inserir na tabela 'usuario'
            // SQL assumindo colunas 'nome', 'tipo_usuario' na tabela 'usuario'.
            // Você mencionou 'login' e 'senha' em 'usuario' no script SQL. Se sim, adicione-os aqui.
            // Ex: "INSERT INTO usuario (nome, tipo_usuario, login, senha) VALUES (?, ?, ?, ?)";
            // E passe os valores no stmt.setString()
            String sqlUsuario = "INSERT INTO usuario (nome, tipo_usuario) VALUES (?, ?)";
            stmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS); // Para pegar o ID gerado

            stmtUsuario.setString(1, nome);
            stmtUsuario.setString(2, tipoUsuario);
            stmtUsuario.executeUpdate();

            rs = stmtUsuario.getGeneratedKeys();
            int idUsuario = -1;
            if (rs.next()) {
                idUsuario = rs.getInt(1); // Obtém o ID auto-gerado do usuário
            }
            // Não feche rs e stmtUsuario aqui, pois estamos dentro de uma transação e precisamos do idUsuario

            if (idUsuario != -1) {
                // 2. Inserir na tabela 'funcionario' usando o ID gerado
                // Assumindo que a tabela 'funcionario' tem colunas 'id', 'cargo', 'departamento'
                // e que 'id' é FK para 'usuario.id'
                String sqlFuncionario = "INSERT INTO funcionario (id, cargo, departamento) VALUES (?, ?, ?)";
                stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                stmtFuncionario.setInt(1, idUsuario); // Usa o ID do usuário
                stmtFuncionario.setString(2, cargo);
                stmtFuncionario.setString(3, departamento);

                int linhasAfetadasFuncionario = stmtFuncionario.executeUpdate();

                if (linhasAfetadasFuncionario > 0) {
                    conn.commit(); // Confirma a transação se ambos os inserts foram bem-sucedidos
                    System.out.println("Funcionário " + nome + " adicionado com sucesso com ID: " + idUsuario);
                } else {
                    conn.rollback(); // Desfaz se falhou no insert de funcionário
                    System.out.println("Falha ao adicionar detalhes do funcionário. Rollback realizado.");
                }
            } else {
                conn.rollback(); // Desfaz se falhou ao obter o ID do usuário
                System.out.println("Falha ao obter o ID do usuário para o novo funcionário. Rollback realizado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar funcionário: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Garante rollback em caso de exceção
            } catch (SQLException rb_e) {
                System.err.println("Erro ao realizar rollback de adição de funcionário: " + rb_e.getMessage());
            }
        } finally {
            // Garante que todos os recursos sejam fechados
            Conexao.fechar(null, stmtUsuario, rs); // Fecha o ResultSet e o stmtUsuario
            Conexao.fechar(conn, stmtFuncionario, null); // Fecha a conexão e o stmtFuncionario
            try {
                if (conn != null) conn.setAutoCommit(true); // Reseta o auto-commit da conexão
            } catch (SQLException e) {
                System.err.println("Erro ao resetar auto-commit da conexão: " + e.getMessage());
            }
        }
    }

    // Método para listar todos os funcionários do banco de dados
    // Refatorado para usar Conexao.fechar()
    public static List<Funcionario> listarFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível listar funcionários.");
                return funcionarios;
            }

            // SQL para fazer JOIN entre 'usuario' e 'funcionario' para obter todos os dados
            // Certifique-se de que as colunas 'id', 'nome', 'tipo_usuario' existem em 'usuario'
            // e 'cargo', 'departamento' em 'funcionario'.
            String sql = "SELECT u.ID, u.Nome, u.perfil AS tipo_usuario, f.cargo, f.departamento " +
                         "FROM usuario u JOIN funcionario f ON u.ID = f.ID";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            System.out.println("=== Listando Funcionários ===");
            if (!rs.isBeforeFirst()) { // Verifica se o ResultSet está vazio
                System.out.println("Nenhum funcionário encontrado.");
            }

            while (rs.next()) {
                int id = rs.getInt("ID");
                String nome = rs.getString("Nome");
                String tipoUsuario = rs.getString("tipo_usuario"); // Usando alias 'tipo_usuario' para 'perfil'
                String cargo = rs.getString("cargo");
                String departamento = rs.getString("departamento");

                Funcionario funcionario = new Funcionario(id, nome, tipoUsuario, cargo, departamento);
                funcionarios.add(funcionario);
                funcionario.exibirInformacoes(); // Exibe as informações imediatamente
                System.out.println("---");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexao.fechar(conn, stmt, rs); // Garante o fechamento dos recursos
        }
        return funcionarios; // Retorna a lista de funcionários
    }

    // Método para atualizar informações de um funcionário no banco de dados
    // Refatorado para usar Conexao.fechar() e lidar com transações
    public static void atualizarFuncionario(int id, String novoNome, String novoTipoUsuario, String novoCargo, String novoDepartamento) {
        Connection conn = null;
        PreparedStatement stmtUsuario = null;
        PreparedStatement stmtFuncionario = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível atualizar funcionário.");
                return;
            }
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Atualizar tabela 'usuario'
            // Assumindo que 'nome' e 'perfil' podem ser atualizados na tabela 'usuario'
            String sqlUsuario = "UPDATE usuario SET Nome = ?, perfil = ? WHERE ID = ?";
            stmtUsuario = conn.prepareStatement(sqlUsuario);
            stmtUsuario.setString(1, novoNome);
            stmtUsuario.setString(2, novoTipoUsuario); // 'perfil' corresponde a 'tipo_usuario' no seu modelo
            stmtUsuario.setInt(3, id);
            int linhasAfetadasUsuario = stmtUsuario.executeUpdate();

            // 2. Atualizar tabela 'funcionario'
            // Assumindo que 'cargo' e 'departamento' podem ser atualizados na tabela 'funcionario'
            String sqlFuncionario = "UPDATE funcionario SET cargo = ?, departamento = ? WHERE ID = ?";
            stmtFuncionario = conn.prepareStatement(sqlFuncionario);
            stmtFuncionario.setString(1, novoCargo);
            stmtFuncionario.setString(2, novoDepartamento);
            stmtFuncionario.setInt(3, id);
            int linhasAfetadasFuncionario = stmtFuncionario.executeUpdate();

            if (linhasAfetadasUsuario > 0 || linhasAfetadasFuncionario > 0) {
                conn.commit(); // Confirma a transação
                System.out.println("Funcionário com ID " + id + " atualizado com sucesso.");
            } else {
                conn.rollback(); // Desfaz se nada foi atualizado
                System.out.println("Nenhum funcionário encontrado com o ID " + id + " para atualizar ou nenhuma alteração aplicada.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar funcionário: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Garante rollback em caso de exceção
            } catch (SQLException rb_e) {
                System.err.println("Erro ao realizar rollback de atualização de funcionário: " + rb_e.getMessage());
            }
        } finally {
            // Garante o fechamento dos recursos
            Conexao.fechar(null, stmtUsuario, null);
            Conexao.fechar(null, stmtFuncionario, null);
            try {
                if (conn != null) conn.setAutoCommit(true); // Reseta o auto-commit
                Conexao.fechar(conn, null, null); // Fecha a conexão
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos após atualização: " + e.getMessage());
            }
        }
    }

    // Método para remover um funcionário pelo ID (também remove da tabela usuario)
    // Lógica de transação já estava boa, apenas refatorando para Conexao.fechar()
    public static void removerFuncionario(int idFuncionario) {
        Connection conn = null;
        PreparedStatement stmtFuncionario = null;
        PreparedStatement stmtUsuario = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível remover funcionário.");
                return;
            }
            conn.setAutoCommit(false); // Inicia a transação

            // 1. Remover da tabela 'funcionario' primeiro (para respeitar chaves estrangeiras)
            String sqlFuncionario = "DELETE FROM funcionario WHERE ID = ?";
            stmtFuncionario = conn.prepareStatement(sqlFuncionario);
            stmtFuncionario.setInt(1, idFuncionario);
            int linhasFuncionario = stmtFuncionario.executeUpdate();

            // 2. Em seguida, remover da tabela 'usuario'
            String sqlUsuario = "DELETE FROM usuario WHERE ID = ?";
            stmtUsuario = conn.prepareStatement(sqlUsuario);
            stmtUsuario.setInt(1, idFuncionario);
            int linhasUsuario = stmtUsuario.executeUpdate();

            if (linhasFuncionario > 0 && linhasUsuario > 0) {
                conn.commit(); // Confirma a transação se ambas as exclusões foram bem-sucedidas
                System.out.println("Funcionário com ID " + idFuncionario + " removido com sucesso.");
            } else {
                conn.rollback(); // Desfaz se nem todas as exclusões foram bem-sucedidas
                System.out.println("Nenhum funcionário encontrado com o ID " + idFuncionario + " para remover.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao remover funcionário: " + e.getMessage());
            try {
                if (conn != null) conn.rollback(); // Garante rollback em caso de exceção
            } catch (SQLException rb_e) {
                System.err.println("Erro ao realizar rollback de remoção de funcionário: " + rb_e.getMessage());
            }
            e.printStackTrace();
        } finally {
            // Garante o fechamento dos recursos e reseta o auto-commit
            Conexao.fechar(null, stmtFuncionario, null);
            Conexao.fechar(null, stmtUsuario, null);
            try {
                if (conn != null) conn.setAutoCommit(true); // Reseta o auto-commit
                Conexao.fechar(conn, null, null); // Fecha a conexão
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos após remoção: " + e.getMessage());
            }
        }
    }
}