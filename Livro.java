package conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // <--- ADD THIS IMPORT STATEMENT
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Livro {
    protected int id;
    protected String titulo;
    protected String autor;
    protected String topicos;
    protected String categoria;
    protected int anoPublicacao;

    // Constructor: Only includes fields up to anoPublicacao
    public Livro(int id, String titulo, String autor, String topicos, String categoria, int anoPublicacao) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.topicos = topicos;
        this.categoria = categoria;
        this.anoPublicacao = anoPublicacao;
    }

    // Constructor for new books (without ID, as it's often auto-generated)
    public Livro(String titulo, String autor, String topicos, String categoria, int anoPublicacao) {
        this(0, titulo, autor, topicos, categoria, anoPublicacao);
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getTopicos() { return topicos; }
    public String getCategoria() { return categoria; }
    public int getAnoPublicacao() { return anoPublicacao; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setTopicos(String topicos) { this.topicos = topicos; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    // NOTE: @Override is only valid if Livro extends a class that defines exibirInformacoes()
    // If Livro is the base class, remove @Override.
    public void exibirInformacoes() {
        System.out.println("--- Informações do Livro ---");
        System.out.println("ID (DB): " + id);
        System.out.println("Título: " + titulo);
        System.out.println("Autor: " + autor);
        System.out.println("Tópicos: " + topicos);
        System.out.println("Categoria: " + categoria);
        System.out.println("Ano de Publicação: " + anoPublicacao);
    }

    public static void listarLivros() {
        System.out.println("--- Lista de Todos os Livros ---");
        // SELECT statement only up to Ano_Publicacao
        String sql = "SELECT id, Titulo, Autor, Topicos, Categoria, Ano_Publicacao FROM livro";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("Nenhum livro encontrado no sistema.");
                return;
            }

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Título: " + rs.getString("Titulo") +
                                   ", Autor: " + rs.getString("Autor") +
                                   ", Ano: " + rs.getInt("Ano_Publicacao")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void pesquisar(String termoPesquisa) {
        System.out.println("--- Resultados da Pesquisa por: '" + termoPesquisa + "' ---");
        // SELECT statement only up to Ano_Publicacao
        String sql = "SELECT id, Titulo, Autor, Topicos, Categoria, Ano_Publicacao FROM livro WHERE Titulo LIKE ? OR Autor LIKE ? OR Topicos LIKE ? OR Categoria LIKE ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termoPesquisa + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);
            stmt.setString(4, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("Nenhum livro encontrado para o termo '" + termoPesquisa + "'.");
                    return;
                }
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                                       ", Título: " + rs.getString("Titulo") +
                                       ", Autor: " + rs.getString("Autor") +
                                       ", Ano: " + rs.getInt("Ano_Publicacao")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar livros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void editarLivroPorIdOuTitulo(Scanner scanner) {
        System.out.println("--- Editar Livro ---");
        System.out.print("Digite o ID do livro a ser editado (ou 0 para pesquisar por título): ");
        int idParaEditar = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        int livroId = -1;
        String tituloAtual = null;

        if (idParaEditar == 0) {
            System.out.print("Digite o título do livro a ser editado: ");
            String tituloPesquisa = scanner.nextLine();
            String sqlSelect = "SELECT id, Titulo FROM livro WHERE Titulo LIKE ?";
            try (Connection conn = Conexao.getConnection();
                 PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                stmtSelect.setString(1, "%" + tituloPesquisa + "%");
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    if (rs.next()) {
                        livroId = rs.getInt("id");
                        tituloAtual = rs.getString("Titulo");
                        System.out.println("Livro encontrado: ID " + livroId + ", Título: " + tituloAtual);
                    } else {
                        System.out.println("Nenhum livro encontrado com esse título.");
                        return;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao buscar livro por título: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        } else {
            livroId = idParaEditar;
            String sqlSelect = "SELECT Titulo FROM livro WHERE id = ?";
            try (Connection conn = Conexao.getConnection();
                 PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                stmtSelect.setInt(1, livroId);
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    if (rs.next()) {
                        tituloAtual = rs.getString("Titulo");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao buscar título por ID: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (livroId == -1) {
            System.out.println("Livro não encontrado para edição.");
            return;
        }

        System.out.println("Editando livro com ID: " + livroId + (tituloAtual != null ? " (Título atual: " + tituloAtual + ")" : ""));

        System.out.print("Novo título (deixe vazio para manter): ");
        String novoTitulo = scanner.nextLine();
        System.out.print("Novo autor (deixe vazio para manter): ");
        String novoAutor = scanner.nextLine();
        System.out.print("Novos tópicos (deixe vazio para manter): ");
        String novosTopicos = scanner.nextLine();
        System.out.print("Nova categoria (deixe vazio para manter): ");
        String novaCategoria = scanner.nextLine();
        System.out.print("Novo ano de publicação (0 para manter): ");
        int novoAnoPublicacao = scanner.nextInt();
        scanner.nextLine();

        String sqlUpdate = "UPDATE livro SET ";
        boolean first = true;
        if (!novoTitulo.isEmpty()) { sqlUpdate += "Titulo = ?"; first = false; }
        if (!novoAutor.isEmpty()) { sqlUpdate += (first ? "" : ", ") + "Autor = ?"; first = false; }
        if (!novosTopicos.isEmpty()) { sqlUpdate += (first ? "" : ", ") + "Topicos = ?"; first = false; }
        if (!novaCategoria.isEmpty()) { sqlUpdate += (first ? "" : ", ") + "Categoria = ?"; first = false; }
        if (novoAnoPublicacao != 0) { sqlUpdate += (first ? "" : ", ") + "Ano_Publicacao = ?"; first = false; }

        if (first) {
            System.out.println("Nenhuma alteração foi inserida.");
            return;
        }

        sqlUpdate += " WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

            int paramIndex = 1;
            if (!novoTitulo.isEmpty()) { stmt.setString(paramIndex++, novoTitulo); }
            if (!novoAutor.isEmpty()) { stmt.setString(paramIndex++, novoAutor); }
            if (!novosTopicos.isEmpty()) { stmt.setString(paramIndex++, novosTopicos); }
            if (!novaCategoria.isEmpty()) { stmt.setString(paramIndex++, novaCategoria); }
            if (novoAnoPublicacao != 0) { stmt.setInt(paramIndex++, novoAnoPublicacao); }

            stmt.setInt(paramIndex, livroId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livro atualizado com sucesso!");
            } else {
                System.out.println("Livro não encontrado ou nenhuma alteração foi aplicada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao editar livro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void removerLivro(int idLivro) {
        System.out.println("--- Remover Livro ---");
        String sql = "DELETE FROM livro WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLivro);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Livro com ID " + idLivro + " removido com sucesso!");
            } else {
                System.out.println("Livro com ID " + idLivro + " não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover livro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- LOAN MANAGEMENT METHODS (Adjusted for no Quantidade_Disponivel) ---

    /**
     * Records a new loan in the database.
     * IMPORTANT: This method cannot check or decrement book quantity.
     *
     * @param idLivro The ID of the book being borrowed.
     * @param idUsuario The ID of the user borrowing the book.
     * @param diasEmprestimo The number of days the book is loaned for (e.g., 7 days).
     */
    public static void realizarEmprestimo(int idLivro, int idUsuario, int diasEmprestimo) {
        Connection conn = null;
        PreparedStatement stmtEmprestimo = null;
        ResultSet rs = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível realizar o empréstimo.");
                return;
            }

            conn.setAutoCommit(false); // Start transaction

            // No check for book availability as Quantidade_Disponivel is removed.

            // Insert loan record
            LocalDate dataEmprestimo = LocalDate.now();
            LocalDate dataDevolucaoPrevista = dataEmprestimo.plusDays(diasEmprestimo);

            String insertEmprestimoSql = "INSERT INTO emprestimo (livro_id, usuario_id, data_emprestimo, return_date, nome_cliente, data_validade) VALUES (?, ?, ?, ?, ?, ?)";
            stmtEmprestimo = conn.prepareStatement(insertEmprestimoSql, Statement.RETURN_GENERATED_KEYS);

            
            stmtEmprestimo.setInt(1, idLivro);
            stmtEmprestimo.setInt(2, idUsuario);
            stmtEmprestimo.setDate(3, java.sql.Date.valueOf(dataEmprestimo));
            stmtEmprestimo.setDate(4, java.sql.Date.valueOf(dataDevolucaoPrevista));

            stmtEmprestimo.setString(5, "TESTE");
            stmtEmprestimo.setDate(6, java.sql.Date.valueOf("2025-06-02"));

            int rowsAffected = stmtEmprestimo.executeUpdate();

            if (rowsAffected > 0) {
                rs = stmtEmprestimo.getGeneratedKeys();
                if (rs.next()) {
                    int idEmprestimoGerado = rs.getInt(1);
                    System.out.println("Empréstimo realizado com sucesso! ID do empréstimo: " + idEmprestimoGerado);
                    System.out.println("Data de devolução prevista: " + dataDevolucaoPrevista);
                } else {
                    System.out.println("Empréstimo realizado com sucesso, mas ID não foi retornado.");
                }
                conn.commit(); // Commit transaction
            } else {
                conn.rollback(); // Rollback if loan insertion failed
                System.out.println("Falha ao registrar empréstimo.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao realizar empréstimo: " + e.getMessage());
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (SQLException rbk) {
                System.err.println("Erro ao reverter transação: " + rbk.getMessage());
            }
            e.printStackTrace();
        } finally {
            Conexao.fechar(conn, stmtEmprestimo, rs);
        }
    }

    /**
     * Records a book return and calculates late fees.
     * IMPORTANT: This method cannot update book quantity.
     *
     * @param idEmprestimo The ID of the loan record being returned.
     */
    public static void realizarDevolucao(int idEmprestimo) {
        Connection conn = null;
        PreparedStatement stmtEmprestimo = null;
        ResultSet rs = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                System.err.println("Falha na conexão com o banco de dados. Não é possível realizar a devolução.");
                return;
            }

            conn.setAutoCommit(false); // Start transaction

            // Get loan details to calculate fine
            String selectSql = "SELECT id, usuario_id, data_validade FROM emprestimo = ? AND Data_Devolucao_Real IS NULL";
            stmtEmprestimo = conn.prepareStatement(selectSql);
            stmtEmprestimo.setInt(1, idEmprestimo);
            rs = stmtEmprestimo.executeQuery();

            if (!rs.next()) {
                System.out.println("Empréstimo não encontrado ou já devolvido.");
                return;
            }

            int idLivro = rs.getInt("ID_Livro"); // Still useful for messages
            int idUsuario = rs.getInt("ID_Usuario");
            LocalDate dataDevolucaoPrevista = rs.getDate("Data_Devolucao_Prevista").toLocalDate();
            LocalDate dataDevolucaoReal = LocalDate.now();

            // Calculate the fine
            double valorMulta = EmprestimoDAO.calcularMulta(dataDevolucaoPrevista, dataDevolucaoReal);

            // Update loan record with actual return date and fine
            String updateEmprestimoSql = "UPDATE Emprestimos SET Data_Devolucao_Real = ?, Valor_Multa = ? WHERE ID_Emprestimo = ?";
            stmtEmprestimo = conn.prepareStatement(updateEmprestimoSql);
            stmtEmprestimo.setDate(1, java.sql.Date.valueOf(dataDevolucaoReal));
            stmtEmprestimo.setDouble(2, valorMulta);
            stmtEmprestimo.setInt(3, idEmprestimo);
            int rowsAffectedEmprestimo = stmtEmprestimo.executeUpdate();

            if (rowsAffectedEmprestimo == 0) {
                conn.rollback();
                System.out.println("Falha ao atualizar registro de empréstimo para devolução.");
                return;
            }

            conn.commit(); // Commit transaction

            System.out.println("Livro devolvido com sucesso!");
            if (valorMulta > 0) {
                System.out.println("Multa por atraso para o cliente ID " + idUsuario + ": R$" + String.format("%.2f", valorMulta));
            } else {
                 System.out.println("Devolução no prazo. Nenhuma multa aplicada.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao realizar devolução: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rbk) {
                System.err.println("Erro ao reverter transação: " + rbk.getMessage());
            }
            e.printStackTrace();
        } finally {
            Conexao.fechar(conn, stmtEmprestimo, rs);
        }
    }

    public static void listarEmprestimosAtivos() {
        System.out.println("--- Empréstimos Ativos ---");
        String sql = "SELECT e.ID, l.TITULO, u.Login, e.data_emprestimo, e.data_validade " +
                     "FROM emprestimo e " +
                     "JOIN livro l ON e.livro_id = l.ID " +
                     "JOIN usuario u ON e.usuario_id = u.ID " +
                     "WHERE e.return_date IS NULL";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("Nenhum empréstimo ativo encontrado.");
                return;
            }
            /**DESCOBRIR OQ TA VINDO DE rs PARA COLETAR INFORMACOES**/
            while (rs.next()) {
                LocalDate dataPrevista = rs.getDate("data_validade").toLocalDate();
                // Emprestimo.calcularMulta needs to be available and correct
                double multaPotencial = EmprestimoDAO.calcularMulta(dataPrevista, null);

                System.out.println("ID Empréstimo: " + rs.getInt("e.ID") +
                                   ", Livro: " + rs.getString("Titulo") +
                                   ", Usuário: " + rs.getString("Nome") +
                                   ", Data Empréstimo: " + rs.getDate("Data_Emprestimo").toLocalDate() +
                                   ", Data Prevista: " + dataPrevista +
                                   ", Multa Potencial Hoje: R$" + String.format("%.2f", multaPotencial));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos ativos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lists all past (returned) loans with their fines.
     */
    public static void listarEmprestimosHistorico() {
        System.out.println("--- Histórico de Empréstimos ---");
        String sql = "SELECT e.ID_Emprestimo, l.Titulo, u.Nome, e.Data_Emprestimo, e.Data_Devolucao_Prevista, e.Data_Devolucao_Real, e.Valor_Multa " +
                     "FROM Emprestimos e " +
                     "JOIN livro l ON e.ID_Livro = l.id " +
                     "JOIN Usuarios u ON e.ID_Usuario = u.ID_Usuario " +
                     "WHERE e.Data_Devolucao_Real IS NOT NULL";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("Nenhum histórico de empréstimos encontrado.");
                return;
            }

            while (rs.next()) {
                System.out.println("ID Empréstimo: " + rs.getInt("ID_Emprestimo") +
                                   ", Livro: " + rs.getString("Titulo") +
                                   ", Usuário: " + rs.getString("Nome") +
                                   ", Empréstimo: " + rs.getDate("Data_Emprestimo").toLocalDate() +
                                   ", Previsto: " + rs.getDate("Data_Devolucao_Prevista").toLocalDate() +
                                   ", Devolvido: " + rs.getDate("Data_Devolucao_Real").toLocalDate() +
                                   ", Multa: R$" + String.format("%.2f", rs.getDouble("Valor_Multa")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar histórico de empréstimos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}