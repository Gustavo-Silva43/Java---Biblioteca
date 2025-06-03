package conexao; // Or the package where your ClienteDAO class is

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Needed for listing operations
import java.sql.SQLException;
import java.util.ArrayList; // Needed for returning a list of Cliente objects
import java.util.List;     // Needed for returning a list of Cliente objects

// Assuming you have a Cliente class defined elsewhere, for example:
// package conexao;
// public class Cliente {
//     private int id;
//     private String nome;
//     private String sobrenome;
//     private String email;
//     private String telefone;
//     // Constructor, getters, setters, and possibly toString()
//     public Cliente(int id, String nome, String sobrenome, String email, String telefone) {
//         this.id = id;
//         this.nome = nome;
//         this.sobrenome = sobrenome;
//         this.email = email;
//         this.telefone = telefone;
//     }
//     // ... getters and setters ...
//     @Override
//     public String toString() {
//         return "ID: " + id + ", Nome: " + nome + " " + sobrenome + ", Email: " + email + ", Telefone: " + telefone;
//     }
// }


public class ClienteDAO {

    public ClienteDAO(int id, String nome, String sobrenome, String email, String telefone) {
		// TODO Auto-generated constructor stub
	}

	public static void adicionarCliente(String nome, String sobrenome, String email, String telefone) {
        System.out.println("Adicionando cliente...");
        String sql = "INSERT INTO cliente (nome, sobrenome, email, telefone) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection(); // Using try-with-resources for auto-closing
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // No need for 'if (conn == null)' here. If Conexao.getConnection() returns null,
            // the NPE will occur on the 'conn.prepareStatement(sql)' line anyway,
            // and the 'catch' block will handle the SQLException.
            // However, a previous NullPointerException on 'conn' suggests Conexao.getConnection() failed
            // and did NOT throw an SQLException, but returned null.
            // Ensure Conexao.getConnection() either returns a valid connection or throws SQLException.
            // If it returns null, you should check for null immediately after the call.
            if (conn == null) {
                System.err.println("Falha na conexão para adicionar cliente: Conexão nula.");
                return; // Exit method if connection is null
            }


            stmt.setString(1, nome);
            stmt.setString(2, sobrenome);
            stmt.setString(3, email);
            stmt.setString(4, telefone);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cliente '" + nome + " " + sobrenome + "' adicionado com sucesso!");
            } else {
                System.out.println("Falha ao adicionar cliente.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar cliente: " + e.getMessage());
            e.printStackTrace(); // Keep this for detailed debugging
        }
    }

    public static void editarCliente(int idCliente, String novoNome, String novoSobrenome, String novoEmail, String novoTelefone) {
        System.out.println("Editando cliente...");

        StringBuilder sqlBuilder = new StringBuilder("UPDATE cliente SET ");
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (novoNome != null && !novoNome.trim().isEmpty()) {
            updates.add("nome = ?");
            params.add(novoNome);
        }
        if (novoSobrenome != null && !novoSobrenome.trim().isEmpty()) {
            updates.add("sobrenome = ?");
            params.add(novoSobrenome);
        }
        if (novoEmail != null && !novoEmail.trim().isEmpty()) {
            updates.add("email = ?");
            params.add(novoEmail);
        }
        if (novoTelefone != null && !novoTelefone.trim().isEmpty()) {
            updates.add("telefone = ?");
            params.add(novoTelefone);
        }

        if (updates.isEmpty()) {
            System.out.println("Nenhuma alteração foi inserida para o cliente.");
            return;
        }

        sqlBuilder.append(String.join(", ", updates));
        sqlBuilder.append(" WHERE id = ?");
        String sql = sqlBuilder.toString();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("Falha na conexão para editar cliente: Conexão nula.");
                return;
            }

            int paramIndex = 1;
            for (Object param : params) {
                if (param instanceof String) {
                    stmt.setString(paramIndex++, (String) param);
                } else if (param instanceof Integer) {
                    stmt.setInt(paramIndex++, (Integer) param);
                }
                // Add more types if needed
            }
            stmt.setInt(paramIndex, idCliente);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cliente com ID " + idCliente + " atualizado com sucesso!");
            } else {
                System.out.println("Cliente com ID " + idCliente + " não encontrado ou nenhuma alteração foi aplicada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao editar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void removerCliente(int idCliente) {
        System.out.println("Removendo cliente...");
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("Falha na conexão para remover cliente: Conexão nula.");
                return;
            }

            stmt.setInt(1, idCliente);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cliente com ID " + idCliente + " removido com sucesso!");
            } else {
                System.out.println("Cliente com ID " + idCliente + " não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- NEW METHOD: listarClientes() ---
    public static List<ClienteDAO> listarClientes() {
        List<ClienteDAO> clientes = new ArrayList<>();
        System.out.println("Listando clientes...");
        String sql = "SELECT id, nome, sobrenome, email, telefone FROM cliente";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // Using try-with-resources for ResultSet too

            if (conn == null) {
                System.err.println("Falha na conexão para listar clientes: Conexão nula.");
                return clientes; // Return empty list if no connection
            }

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String sobrenome = rs.getString("sobrenome");
                String email = rs.getString("email");
                String telefone = rs.getString("telefone");

                // Assuming you have a Cliente class with a suitable constructor
                ClienteDAO cliente = new ClienteDAO(id, nome, sobrenome, email, telefone);
                clientes.add(cliente);
                System.out.println(cliente); // Or cliente.exibirInformacoes() if you have it
            }
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }
}