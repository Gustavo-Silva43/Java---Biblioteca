package conexao;

import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Usuario usuarioLogado = null;

        System.out.println("==== Bem-vindo ao Sistema de Biblioteca ====\n");

        // --- TELA DE LOGIN ---
        while (usuarioLogado == null) {
            System.out.println("--- Login ---");
            System.out.print("Login: ");
            String login = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            usuarioLogado = Usuario.autenticarUsuario(login, senha);

            if (usuarioLogado == null) {
                System.out.println("Login ou senha inválidos. Tente novamente.");
            } else {
                System.out.println("\nBem-vindo(a), " + usuarioLogado.getNome() + " (" + usuarioLogado.getTipoUsuario() + ")!");
            }
        }
        // --- FIM DA TELA DE LOGIN ---

        int opcao;
        // --- CORREÇÃO AQUI: Declare acaoReal antes do loop do-while ---
        int acaoReal = -1; // Inicialize com um valor que não seja a opção de saída (22)

        do {
            System.out.println("==== Sistema de Biblioteca ====");

            String tipoUsuario = usuarioLogado.getTipoUsuario();
            int contadorOpcao = 1;

            // --- Lógica de Exibição do Menu por Tipo de Usuário ---
            if ("Administrador".equals(tipoUsuario)) {
                System.out.println(contadorOpcao++ + ". Adicionar Livro");
                System.out.println(contadorOpcao++ + ". Listar Livros");
                System.out.println(contadorOpcao++ + ". Pesquisar Livros");
                System.out.println(contadorOpcao++ + ". Realizar Empréstimo");
                System.out.println(contadorOpcao++ + ". Realizar Devolução");
                System.out.println(contadorOpcao++ + ". Editar Livro");
                System.out.println(contadorOpcao++ + ". Remover Livro");
                System.out.println(contadorOpcao++ + ". Adicionar Cliente");
                System.out.println(contadorOpcao++ + ". Editar Cliente");
                System.out.println(contadorOpcao++ + ". Remover Cliente");
                System.out.println(contadorOpcao++ + ". Adicionar Administrador");
                System.out.println(contadorOpcao++ + ". Editar Administrador");
                System.out.println(contadorOpcao++ + ". Remover Administrador");
                System.out.println(contadorOpcao++ + ". Listar Administradores");
                System.out.println(contadorOpcao++ + ". Adicionar Funcionário");
                System.out.println(contadorOpcao++ + ". Editar Funcionário");
                System.out.println(contadorOpcao++ + ". Remover Funcionário");
                System.out.println(contadorOpcao++ + ". Listar Funcionários");
                System.out.println(contadorOpcao++ + ". Listar Clientes");
                System.out.println(contadorOpcao++ + ". Listar Empréstimos Ativos");
                System.out.println(contadorOpcao++ + ". Listar Histórico de Empréstimos");
                System.out.println(contadorOpcao++ + ". Sair");
                System.out.println("=== MENU ADMINISTRADOR ===");
            }
            else if ("Funcionario".equals(tipoUsuario)) {
                System.out.println(contadorOpcao++ + ". Adicionar Livro");
                System.out.println(contadorOpcao++ + ". Listar Livros");
                System.out.println(contadorOpcao++ + ". Pesquisar Livros");
                System.out.println(contadorOpcao++ + ". Realizar Empréstimo");
                System.out.println(contadorOpcao++ + ". Realizar Devolução");
                System.out.println(contadorOpcao++ + ". Editar Livro");
                System.out.println(contadorOpcao++ + ". Adicionar Cliente");
                System.out.println(contadorOpcao++ + ". Editar Cliente");
                System.out.println(contadorOpcao++ + ". Listar Clientes");
                System.out.println(contadorOpcao++ + ". Listar Empréstimos Ativos");
                System.out.println(contadorOpcao++ + ". Listar Histórico de Empréstimos");
                System.out.println(contadorOpcao++ + ". Sair");
                System.out.println("=== MENU FUNCIONÁRIO ===");
            }
            else if ("Cliente".equals(tipoUsuario)) {
                System.out.println(contadorOpcao++ + ". Listar Livros");
                System.out.println(contadorOpcao++ + ". Pesquisar Livros");
                System.out.println(contadorOpcao++ + ". Sair");
                System.out.println("=== MENU CLIENTE ===");
            }
            else {
                System.out.println("Acesso não autorizado para este tipo de usuário.");
                acaoReal = 22; // Força a saída para usuários sem permissão definida
                continue;
            }

            System.out.print("Escolha uma opção: ");

            opcao = lerInteiro(scanner, "");

            // --- Mapeamento da Opção Escolhida para a Ação Real (Crucial!) ---
            acaoReal = -1; // Resetar acaoReal a cada iteração para evitar valores antigos

            boolean permissaoNegada = false;

            if ("Administrador".equals(tipoUsuario)) {
                if (opcao >= 1 && opcao <= 22) {
                    acaoReal = opcao;
                } else {
                    permissaoNegada = true;
                }
            } else if ("Funcionario".equals(tipoUsuario)) {
                switch (opcao) {
                    case 1: acaoReal = 1; break; // Adicionar Livro
                    case 2: acaoReal = 2; break; // Listar Livros
                    case 3: acaoReal = 3; break; // Pesquisar Livros
                    case 4: acaoReal = 4; break; // Realizar Empréstimo
                    case 5: acaoReal = 5; break; // Realizar Devolução
                    case 6: acaoReal = 6; break; // Editar Livro
                    case 7: acaoReal = 8; break; // Adicionar Cliente (era 8 no menu completo)
                    case 8: acaoReal = 9; break; // Editar Cliente (era 9 no menu completo)
                    case 9: acaoReal = 19; break; // Listar Clientes (era 19 no menu completo)
                    case 10: acaoReal = 20; break; // Listar Empréstimos Ativos (era 20 no menu completo)
                    case 11: acaoReal = 21; break; // Listar Histórico de Empréstimos (era 21 no menu completo)
                    case 12: acaoReal = 22; break; // Sair (era 22 no menu completo)
                    default: permissaoNegada = true; break;
                }
            } else if ("Cliente".equals(tipoUsuario)) {
                switch (opcao) {
                    case 1: acaoReal = 2; break; // Listar Livros (era 2 no menu completo)
                    case 2: acaoReal = 3; break; // Pesquisar Livros (era 3 no menu completo)
                    case 3: acaoReal = 22; break; // Sair (era 22 no menu completo)
                    default: permissaoNegada = true; break;
                }
            } else {
                if (opcao != 22) {
                    permissaoNegada = true;
                } else {
                    acaoReal = 22; // Se o tipo é desconhecido, só permitir sair
                }
            }

            if (permissaoNegada) {
                System.out.println("Ação não permitida para o seu tipo de usuário ou opção inválida. Tente novamente.");
                continue;
            }

            // --- Execução da Ação Real (SWITCH) ---
            switch (acaoReal) {
                case 1: // Adicionar Livro
                    System.out.print("Digite o título do livro: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Digite o autor do livro: ");
                    String autor = scanner.nextLine();
                    System.out.print("Digite os tópicos: ");
                    String topicos = scanner.nextLine();
                    System.out.print("Digite a categoria: ");
                    String categoria = scanner.nextLine();
                    int anoPublicacao = lerInteiro(scanner, "Digite o ano de publicação: ");
                    LivroFisico.adicionarLivroFisico(titulo, autor, topicos, categoria, anoPublicacao);
                    break;

                case 2: // Listar Livros
                    Livro.listarLivros();
                    break;

                case 3: // Pesquisar Livros
                    System.out.print("Digite o termo de pesquisa: ");
                    String termoPesquisa = scanner.nextLine();
                    Livro.pesquisar(termoPesquisa);
                    break;

                case 4: // Realizar Empréstimo
                    System.out.print("Digite o ID do livro a ser emprestado: ");
                    int livroIdEmprestimo = lerInteiro(scanner, "");
                    System.out.print("Digite o ID do usuário (cliente) que está pegando o livro: ");
                    int usuarioIdEmprestimo = lerInteiro(scanner, "");
                    System.out.print("Digite o número de dias para empréstimo (ex: 7): ");
                    int diasEmprestimo = lerInteiro(scanner, "");
                    Livro.realizarEmprestimo(livroIdEmprestimo, usuarioIdEmprestimo, diasEmprestimo);
                    break;

                case 5: // Realizar Devolução
                    System.out.print("Digite o ID do empréstimo a ser devolvido: ");
                    int emprestimoIdDevolucao = lerInteiro(scanner, "");
                    Livro.realizarDevolucao(emprestimoIdDevolucao);
                    break;

                case 6: // Editar Livro
                    Livro.editarLivroPorIdOuTitulo(scanner);
                    break;

                case 7: // Remover Livro (Apenas Administrador)
                    int idLivroRemover = lerInteiro(scanner, "Digite o ID do livro a ser removido: ");
                    Livro.removerLivro(idLivroRemover);
                    break;

                case 8: // Adicionar Cliente (Antes 8, agora acaoReal 8 para Admin e 7 para Funcionario)
                    System.out.print("Digite o nome do cliente: ");
                    String nomeClienteAdd = scanner.nextLine();
                    System.out.print("Digite o sobrenome do cliente: ");
                    String sobrenomeClienteAdd = scanner.nextLine();
                    System.out.print("Digite o email do cliente: ");
                    String emailClienteAdd = scanner.nextLine();
                    System.out.print("Digite o telefone do cliente: ");
                    String telefoneClienteAdd = scanner.nextLine();
                    ClienteDAO.adicionarCliente(nomeClienteAdd, sobrenomeClienteAdd, emailClienteAdd, telefoneClienteAdd);
                    break;

                case 9: // Editar Cliente (Antes 9, agora acaoReal 9 para Admin e 8 para Funcionario)
                    int idClienteEdit = lerInteiro(scanner, "Digite o ID do cliente a ser editado: ");
                    System.out.print("Digite o novo nome (deixe vazio para manter o atual): ");
                    String novoNomeCliente = scanner.nextLine();
                    System.out.print("Digite o novo sobrenome (deixe vazio para manter o atual): ");
                    String novoSobrenomeCliente = scanner.nextLine();
                    System.out.print("Digite o novo email (deixe vazio para manter o atual): ");
                    String novoEmailCliente = scanner.nextLine();
                    System.out.print("Digite o novo telefone (deixe vazio para manter o atual): ");
                    String novoTelefoneCliente = scanner.nextLine();
                    ClienteDAO.editarCliente(idClienteEdit, novoNomeCliente, novoSobrenomeCliente, novoEmailCliente, novoTelefoneCliente);
                    break;

                case 10: // Remover Cliente (Apenas Administrador)
                    int idClienteRemover = lerInteiro(scanner, "Digite o ID do cliente a ser removido: ");
                    ClienteDAO.removerCliente(idClienteRemover);
                    break;

                case 11: // Adicionar Administrador (Apenas Administrador)
                    System.out.print("Digite o nome do administrador: ");
                    String nomeAdm = scanner.nextLine();
                    System.out.print("Digite a senha do administrador: ");
                    String senhaAdm = scanner.nextLine();
                    System.out.print("Digite as permissões do administrador (ex: Total, Parcial): ");
                    String permissoesAdm = scanner.nextLine();
                    AdministradorDAO.adicionarAdministrador(nomeAdm, senhaAdm, permissoesAdm);
                    break;

                case 12: // Editar Administrador (Apenas Administrador)
                    int idAdmEditar = lerInteiro(scanner, "Digite o ID do administrador a ser editado: ");
                    System.out.print("Digite o novo nome (deixe vazio para manter o atual): ");
                    String novoNomeAdm = scanner.nextLine();
                    System.out.print("Digite a nova senha (deixe vazio para manter a atual): ");
                    String novaSenhaAdm = scanner.nextLine();
                    System.out.print("Digite as novas permissões (deixe vazio para manter as atuais): ");
                    String novasPermissoesAdm = scanner.nextLine();
                    AdministradorDAO.editarAdministrador(idAdmEditar, novoNomeAdm, novaSenhaAdm, novasPermissoesAdm);
                    break;

                case 13: // Remover Administrador (Apenas Administrador)
                    int idAdmRemover = lerInteiro(scanner, "Digite o ID do administrador a ser removido: ");
                    AdministradorDAO.removerAdministrador(idAdmRemover);
                    break;

                case 14: // Listar Administradores (Apenas Administrador)
                    AdministradorDAO.listarAdministradores();
                    break;

                case 15: // Adicionar Funcionário (Apenas Administrador)
                    System.out.print("Digite o nome do funcionário: ");
                    String funcNome = scanner.nextLine();
                    System.out.print("Digite o cargo do funcionário: ");
                    String funcCargo = scanner.nextLine();
                    System.out.print("Digite o departamento do funcionário: ");
                    String funcDepartamento = scanner.nextLine();
                    Funcionario.adicionarFuncionario(funcNome, "Funcionario", funcCargo, funcDepartamento);
                    break;

                case 16: // Editar Funcionário (Apenas Administrador)
                    int funcIdEdit = lerInteiro(scanner, "Digite o ID do funcionário a ser editado: ");
                    System.out.print("Digite o novo nome (deixe vazio para manter o atual): ");
                    String novoFuncNome = scanner.nextLine();
                    System.out.print("Digite o novo cargo (deixe vazio para manter o atual): ");
                    String novoFuncCargo = scanner.nextLine();
                    System.out.print("Digite o novo departamento (deixe vazio para manter o atual): ");
                    String novoFuncDepartamento = scanner.nextLine();
                    Funcionario.atualizarFuncionario(funcIdEdit, novoFuncNome, "Funcionario", novoFuncCargo, novoFuncDepartamento);
                    break;

                case 17: // Remover Funcionário (Apenas Administrador)
                    int funcIdRemover = lerInteiro(scanner, "Digite o ID do funcionário a ser removido: ");
                    Funcionario.removerFuncionario(funcIdRemover);
                    break;

                case 18: // Listar Funcionários (Apenas Administrador)
                    Funcionario.listarFuncionarios();
                    break;

                case 19: // Listar Clientes (Funcionario e Administrador)
                    ClienteDAO.listarClientes();
                    break;

                case 20: // Listar Empréstimos Ativos (Funcionario e Administrador)
                    Livro.listarEmprestimosAtivos();
                    break;

                case 21: // Listar Histórico de Empréstimos (Funcionario e Administrador)
                    Livro.listarEmprestimosHistorico();
                    break;

                case 22: // Sair (Todos os usuários)
                    System.out.println("Saindo do sistema...");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

            System.out.println();

        } while (acaoReal != 22); // Agora acaoReal está no escopo correto

        scanner.close();
    }

    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } else {
                System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
                scanner.next();
            }
        }
    }

    private static double lerDouble(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            if (scanner.hasNextDouble()) {
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } else {
                System.out.println("Entrada inválida. Por favor, digite um número decimal.");
                scanner.next();
            }
        }
    }
}