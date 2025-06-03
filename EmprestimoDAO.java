package conexao;

import java.time.LocalDate; // For handling dates
import java.time.temporal.ChronoUnit; // For calculating days between dates

public class EmprestimoDAO {
    private int idEmprestimo;
    private int idLivro;
    private int idUsuario; // Assuming a user ID, which maps to your Cliente ID
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal; // Null if not yet returned
    private double valorMulta;

    // Constructor for creating a new loan (before return)
    public EmprestimoDAO(int idEmprestimo, int idLivro, int idUsuario, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        this.idEmprestimo = idEmprestimo;
        this.idLivro = idLivro;
        this.idUsuario = idUsuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = null; // Initially null
        this.valorMulta = 0.0; // Initially no fine
    }

    // Constructor for loading an existing loan from DB (including returned ones)
    public EmprestimoDAO(int idEmprestimo, int idLivro, int idUsuario, LocalDate dataEmprestimo,
                      LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoReal, double valorMulta) {
        this.idEmprestimo = idEmprestimo;
        this.idLivro = idLivro;
        this.idUsuario = idUsuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.valorMulta = valorMulta;
    }

    // Getters
    public int getIdEmprestimo() { return idEmprestimo; }
    public int getIdLivro() { return idLivro; }
    public int getIdUsuario() { return idUsuario; } // This is your Cliente ID
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public double getValorMulta() { return valorMulta; }

    // Setters (especially for dataDevolucaoReal and valorMulta when returning)
    public void setIdEmprestimo(int idEmprestimo) { this.idEmprestimo = idEmprestimo; }
    public void setIdLivro(int idLivro) { this.idLivro = idLivro; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }
    public void setValorMulta(double valorMulta) { this.valorMulta = valorMulta; }

    /**
     * Calculates the late fee based on the due date and the actual return date (or current date if not returned).
     * @param dataDevolucaoPrevista The date the book was supposed to be returned.
     * @param dataDevolucaoReal The actual date the book was returned. If null, uses today's date (LocalDate.now()).
     * @return The calculated fine amount (R$2.00 per day overdue). Returns 0 if not overdue or returned on time/early.
     */
    public static double calcularMulta(LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoReal) {
        LocalDate dataComparacao;
        if (dataDevolucaoReal != null) {
            dataComparacao = dataDevolucaoReal; // Book has been returned, use actual return date
        } else {
            dataComparacao = LocalDate.now(); // Book is still out, calculate potential fine based on today
        }

        // If the comparison date is AFTER the due date, then it's overdue
        if (dataComparacao.isAfter(dataDevolucaoPrevista)) {
            // Calculate days between (exclusive of the start date, inclusive of the end date for delay)
            long diasAtraso = ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataComparacao);
            return diasAtraso * 2.00; // R$2.00 per day
        }
        return 0.0; // Not overdue
    }

    public void exibirInformacoes() {
        System.out.println("--- Detalhes do Empréstimo ---");
        System.out.println("ID Empréstimo: " + idEmprestimo);
        System.out.println("ID Livro: " + idLivro);
        System.out.println("ID Usuário (Cliente): " + idUsuario);
        System.out.println("Data Empréstimo: " + dataEmprestimo);
        System.out.println("Data Devolução Prevista: " + dataDevolucaoPrevista);
        System.out.println("Data Devolução Real: " + (dataDevolucaoReal != null ? dataDevolucaoReal : "Não devolvido (Ativo)"));
        System.out.println("Valor da Multa: R$" + String.format("%.2f", valorMulta));
    }
}