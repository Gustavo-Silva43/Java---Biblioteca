package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
    private int id;
    private String nome;
    private String tipoUsuario;
    private String login;
    private String senha;

    // --- Detalhes da conexão com o banco de dados SQL Server ---
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Biblioteca;encrypt=true;trustServerCertificate=true;";
    private static final String USUARIO_DB = "sa";
    private static final String SENHA_DB = "gugu*123";


    // --- CONSTRUTORES ---
    public Usuario(int id, String nome, String tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
        this.login = null;
        this.senha = null;
    }

    public Usuario(int id, String nome, String tipoUsuario, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
        this.login = login;
        this.senha = senha;
    }


    // --- GETTERS & SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }


    // --- MÉTODO exibirInformacoes() ---
    public void exibirInformacoes() {
        System.out.println("--- Informações do Usuário ---");
        System.out.println("ID de Usuário: " + id);
        // Se você adicionar uma coluna 'Nome' na sua tabela SQL, mude "Usuário do Sistema" para o nome do banco
        System.out.println("Nome: " + (nome != null && !nome.isEmpty() ? nome : "Usuário do Sistema"));
        System.out.println("Tipo de Usuário: " + tipoUsuario); // Exibe o tipo de usuário lido do DB
        if (login != null && !login.isEmpty()) {
            System.out.println("Login: " + login);
        }
    }

    // --- Método de Autenticação (`autenticarUsuario`) ---
    public static Usuario autenticarUsuario(String loginDigitado, String senhaDigitada) {
        Usuario usuarioAutenticado = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 1. Estabelecer a conexão com o banco de dados
            conexao = DriverManager.getConnection(URL, USUARIO_DB, SENHA_DB);

            // 2. Preparar a consulta SQL, selecionando ID, Login, Senha e TipoUsuario
            String sql = "SELECT ID, Login, Senha, TipoUsuario FROM usuario WHERE Login = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, loginDigitado);

            // 3. Executar a consulta
            rs = pstmt.executeQuery();

            // 4. Processar o resultado da consulta
            if (rs.next()) { // Se encontrou um registro para o login fornecido
                int idBanco = rs.getInt("ID");
                String loginBanco = rs.getString("Login");
                String senhaBanco = rs.getString("Senha");
                // Lendo o TipoUsuario da coluna do banco de dados
                String tipoUsuarioBanco = rs.getString("TipoUsuario");

                // ATENÇÃO: Se você tiver uma coluna 'Nome' na sua tabela 'usuario',
                // você pode lê-la aqui usando rs.getString("Nome").
                // Caso contrário, um valor padrão será usado para o nome do objeto.
                String nomeBanco = "Usuário do Sistema"; // Valor padrão para 'nome' no objeto Usuario

                // 5. Comparar a senha digitada com a senha armazenada no banco
                if (senhaDigitada.equals(senhaBanco)) {
                    // Usuário autenticado com sucesso! Crie o objeto Usuario com todos os dados.
                    usuarioAutenticado = new Usuario(idBanco, nomeBanco, tipoUsuarioBanco, loginBanco, senhaBanco);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
            // Em desenvolvimento, você pode descomentar para ver o stack trace completo: e.printStackTrace();
        } finally {
            // 6. Fechar recursos do banco de dados para evitar vazamentos
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos do banco de dados: " + e.getMessage());
            }
        }
        return usuarioAutenticado; // Retorna o objeto Usuario ou null se a autenticação falhar
    }
}