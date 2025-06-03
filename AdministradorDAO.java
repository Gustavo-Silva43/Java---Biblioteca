package conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO {

    public AdministradorDAO(int id, String nome, String string, Object object, Object object2, Object object3,
			Object object4, String permissoes) {
		// TODO Auto-generated constructor stub
	}

	/**
     * Adiciona um novo administrador na tabela 'admin' do banco de dados.
     * Assume que 'ID_Admin' é uma coluna IDENTITY (auto-incremento) no banco de dados.
     *
     * @param nomeUsuario O nome de usuário para o administrador.
     * @param senha A senha do administrador.
     * @param permissoes As permissões do administrador (ex: "Total", "Parcial").
     */
    public static void adicionarAdministrador(String nomeUsuario, String senha, String permissoes) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; // Para obter chaves geradas

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível adicionar o administrador.");
                return;
            }

            // SQL para inserir na tabela 'admin' com as colunas corretas
            // ID_Admin é gerado automaticamente pelo banco, então não o incluímos no INSERT
            String sql = "INSERT INTO admin (Nome_Usuario, Senha, Permissoes) VALUES (?, ?, ?)";

            // Statement.RETURN_GENERATED_KEYS é usado para obter o ID gerado automaticamente
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, nomeUsuario);
            stmt.setString(2, senha);
            stmt.setString(3, permissoes); // Mapeia para a coluna 'Permissoes'

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int idGerado = rs.getInt(1); // Obtém o ID gerado (da primeira coluna)
                    System.out.println("Administrador '" + nomeUsuario + "' adicionado com sucesso com ID: " + idGerado);
                } else {
                    System.out.println("Administrador '" + nomeUsuario + "' adicionado com sucesso, mas ID não foi retornado.");
                }
            } else {
                System.out.println("Falha ao adicionar administrador.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar administrador: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Garante que os recursos sejam fechados
            Conexao.fechar(conn, stmt, rs);
        }
    }

    /**
     * Edita as informações de um administrador existente na tabela 'admin'.
     * Permite atualizar nome de usuário, senha e permissões.
     *
     * @param idAdmin O ID do administrador a ser editado.
     * @param novoNomeUsuario Novo nome de usuário (pode ser nulo ou vazio para não alterar).
     * @param novaSenha Nova senha (pode ser nula ou vazia para não alterar).
     * @param novasPermissoes Novas permissões (pode ser nula ou vazia para não alterar).
     */
    public static void editarAdministrador(int idAdmin, String novoNomeUsuario, String novaSenha, String novasPermissoes) {
        String sql = "UPDATE admin SET "; // Tabela 'admin'
        boolean first = true;

        // Constrói a query UPDATE dinamicamente
        if (novoNomeUsuario != null && !novoNomeUsuario.isEmpty()) {
            sql += "Nome_Usuario = ?"; // Coluna 'Nome_Usuario'
            first = false;
        }
        if (novaSenha != null && !novaSenha.isEmpty()) {
            sql += (first ? "" : ", ") + "Senha = ?"; // Coluna 'Senha'
            first = false;
        }
        if (novasPermissoes != null && !novasPermissoes.isEmpty()) {
            sql += (first ? "" : ", ") + "Permissoes = ?"; // Coluna 'Permissoes'
            first = false;
        }

        if (first) {
            System.out.println("Nenhuma alteração foi inserida para o administrador com ID " + idAdmin + ".");
            return;
        }

        sql += " WHERE ID_Admin = ?"; // Condição WHERE usando a PK 'ID_Admin'

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível editar o administrador.");
                return;
            }

            stmt = conn.prepareStatement(sql);

            int paramIndex = 1; // Índice para os parâmetros da PreparedStatement
            if (novoNomeUsuario != null && !novoNomeUsuario.isEmpty()) {
                stmt.setString(paramIndex++, novoNomeUsuario);
            }
            if (novaSenha != null && !novaSenha.isEmpty()) {
                stmt.setString(paramIndex++, novaSenha);
            }
            if (novasPermissoes != null && !novasPermissoes.isEmpty()) {
                stmt.setString(paramIndex++, novasPermissoes);
            }

            stmt.setInt(paramIndex, idAdmin); // Define o ID para a condição WHERE

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Administrador com ID " + idAdmin + " atualizado com sucesso!");
            } else {
                System.out.println("Administrador com ID " + idAdmin + " não encontrado ou nenhuma alteração foi aplicada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao editar administrador: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexao.fechar(conn, stmt, null); // Fechar Connection e PreparedStatement
        }
    }

    /**
     * Remove um administrador da tabela 'admin' pelo seu ID.
     *
     * @param idAdmin O ID do administrador a ser removido.
     */
    public static void removerAdministrador(int idAdmin) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível remover o administrador.");
                return;
            }

            // SQL para deletar da tabela 'admin'
            String sql = "DELETE FROM admin WHERE ID_Admin = ?"; // Tabela 'admin' e coluna 'ID_Admin'
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, idAdmin); // Define o ID para a condição WHERE

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Administrador com ID " + idAdmin + " removido com sucesso!");
            } else {
                System.out.println("Administrador com ID " + idAdmin + " não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover administrador: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexao.fechar(conn, stmt, null); // Fechar Connection e PreparedStatement
        }
    }

    /**
     * Lista todos os administradores cadastrados na tabela 'admin'.
     *
     * @return Uma lista de objetos Administrador.
     */
    public static List<Administrador> listarAdministradores() {
        List<Administrador> administradores = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível listar administradores.");
                return administradores;
            }

            String sql = "SELECT ID_Admin, Nome_Usuario, Senha, Permissoes FROM admin";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            System.out.println("--- Lista de Administradores ---");
            if (!rs.isBeforeFirst()) { // Verifica se o ResultSet está vazio
                System.out.println("Nenhum administrador encontrado.");
            }

            while (rs.next()) {
                int id = rs.getInt("ID_Admin");
                String nome = rs.getString("Nome_Usuario");
                String senha = rs.getString("Senha"); // Idealmente, não listar senha, mas para propósitos de teste/exibição
                String permissoes = rs.getString("Permissoes");

                // Para criar um objeto Administrador, você precisaria das informações de
                // Usuario e Funcionario também, devido à hierarquia.
                // Aqui, estou criando um objeto Administrador de forma simplificada
                // assumindo que os atributos do Funcionario/Usuario seriam preenchidos
                // ou não seriam necessários para a simples listagem de 'admin'.
                // Em um sistema real, você faria um JOIN com as tabelas 'usuario' e 'funcionario'
                // e passaria todos os dados para o construtor do Administrador.
                // Por agora, estou passando valores padrão para 'tipoUsuario', 'login', 'senha', 'cargo', 'departamento'
                // porque a tabela 'admin' não os contém diretamente.
                Administrador admin = new Administrador(id, nome, "administrador", null, null, null, null, permissoes); //
                // Certifique-se de que o construtor da sua classe Administrador pode aceitar esses parâmetros.
                // Exemplo de construtor (se herdar de Funcionario que herda de Usuario):
                // public Administrador(int id, String nome, String tipoUsuario, String login, String senha, String cargo, String departamento, String permissoes) { ... }

                admin.exibirInformacoes(); // Exibe informações básicas do administrador (herdadas de Usuario/Funcionario)
                System.out.println("Permissões: " + permissoes);
                System.out.println("---------------------------------");
                administradores.add(admin);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar administradores: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexao.fechar(conn, stmt, rs); // Garante o fechamento dos recursos
        }
        return administradores;
    }

	private void exibirInformacoes() {
		// TODO Auto-generated method stub
		
	}
}