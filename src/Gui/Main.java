package Gui;

import Entidades.*;
import Controler.DisciplinasControler;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class Main {
    private JPanel root;
    private JTabbedPane tabbedPane1;
    private JPanel panelnicio;
    private JTabbedPane tabbedPane2;
    private JPanel panelAAluno;
    private JTable tableSequencia;
    private JPanel panelAlun;
    private JComboBox comboBox1;
    private JTable table3;
    private JTable tableTempor;
    private JButton removerButton;
    private JButton adicionarButton;
    private JButton finalizarButton;
    private JPanel PanelAlunoAdd;
    private JPanel PanelDisciplinaAdd;
    private JTextField txfNomeA;
    private JTextField txfAreaA;
    private JTextField txfNomeDisc;
    private JTextField txfCodigoD;
    private JTextField txfPreReqA;
    private JTextField txfAreaDisc;
    private JComboBox cmbTipo;
    private JComboBox cmbHorario;
    private JButton adicionarDisciplinaButton;
    private JButton adicionarAlunoButton;
    private JSpinner spinner1;
    private JTable tableFinalizadas;
    private JLabel lblNome;
    private JButton button1;
    private JTextField textField1;
    private Aluno alunoSel;

    private boolean recarregarTabela(DisciplinasControler cs) {
        if (cs.getUsuarios().size() <= 0) {
            return false;
        }
        DefaultTableModel sequenciaModel = (DefaultTableModel) tableSequencia.getModel();
        DefaultTableModel temporModel = (DefaultTableModel) tableTempor.getModel();
        DefaultTableModel finalModel = (DefaultTableModel) tableFinalizadas.getModel();
        Aluno u = cs.getUsuarios().get(comboBox1.getSelectedIndex());
        sequenciaModel.setRowCount(0); // limpa a tabela
        Map<String, Disciplina> a = cs.getMapDisciplinas();
        LinkedList<Disciplina> lista = new LinkedList<>(a.values());
        lista.sort((Disciplina d1, Disciplina d2) -> d1.getPeriodo().compareTo(d2.getPeriodo()));
        Map<String, Disciplina> aux = new LinkedHashMap<>();
        for (Disciplina d : lista) {
            aux.put(d.getCodigo(), d);
        }
        int inicio = 1;
        for (Disciplina key : aux.values()) {
            if (Integer.parseInt(key.getPeriodo()) == inicio) {
                sequenciaModel.addRow(new Object[]{"#" + inicio + " período"});
                inicio++;
            }
            if (!u.getDiscFinalizadas().containsKey(key.getCodigo()) && (textField1.getText().equals("")
                    || cs.verificar(textField1.getText().toUpperCase(), key.getRequisitos()))) { // se ainda nao foi finalizada
                if (!u.getDiscTemporaria().containsKey(key.getCodigo())) { // nao mostrar o q ja tem em discTemp
                    sequenciaModel.addRow(new Object[]{key.getNome(), key.getCodigo(), key.getPreRequisito()
                            , key.getTipo(), key.getHorario()});
                }
            }
        }
        //recarregar tabela temporaria
        for (Disciplina d : u.getDiscTemporaria().values()) {
            temporModel.addRow(new Object[]{d.getHorario(), d.getNome(), d.getCodigo(), d.getNota()});
        }
        //recarregar tabela finalizadas
        finalModel.setRowCount(0);
        for (Disciplina d : u.getDiscFinalizadas().values()) {
            finalModel.addRow(new Object[]{d.getNome(), d.getCodigo()});
        }
        return true;
    }

    public Main() throws IOException {
        ///INICIAR O SISTEMA
        DisciplinasControler cs = new DisciplinasControler();
//        cs.cadastrarUsuario(new Aluno("Joao", ""));
//        cs.cadastrarUsuario(new Aluno("Maria", ""));
        for (Usuario uu : cs.getUsuarios()) {
            comboBox1.addItem(uu.getNome());
        }

        cmbTipo.addItem("BASICA");
        cmbTipo.addItem("EL1");
        cmbTipo.addItem("EL2");
        cmbHorario.addItem("T1");
        cmbHorario.addItem("T2");
        cmbHorario.addItem("T3");
        cmbHorario.addItem("T4");
        cmbHorario.addItem("T5");
        cmbHorario.addItem("T6");
        cmbHorario.addItem("T7");
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 8, 1);
        spinner1.setModel(model);
        //define o formato do spinner
        JFormattedTextField ftf = ((JSpinner.DefaultEditor) spinner1.getEditor()).getTextField();
        ftf.setEditable(false);
        ftf.setBackground(Color.WHITE);
        ftf.setHorizontalAlignment(JTextField.CENTER);
        ftf.setFont(new Font("Arial", Font.BOLD, 20));
        ftf.setForeground(Color.BLACK);
        ftf.setBorder(null);
        /////
        recarregarTabela(cs);
        tabbedPane1.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    recarregarTabela(cs);
                }
            }
        });
        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int viewrow = tableSequencia.getSelectedRow();
                int modelRow = tableSequencia.convertRowIndexToModel(viewrow);
                Object codigo = tableSequencia.getModel().getValueAt(modelRow, 1);
                Object prerequisito = tableSequencia.getModel().getValueAt(modelRow, 2);
                Object inicio = tableSequencia.getValueAt(modelRow, 0);
                String[] arrayReq = null;
                try {
                    arrayReq = prerequisito.toString().split(",");
                } catch (Exception e1) {
                }

                try {
                    Disciplina mask = cs.getMapDisciplinas().get(codigo.toString()); // pega a disciplina com o codigo
                    if (inicio != null && !inicio.toString().startsWith("#")) {
                        Aluno u = cs.getUsuarios().get(comboBox1.getSelectedIndex());
                        if (u.possuiRequisito(arrayReq) || prerequisito.toString().equals("0")) {
                            float nota = Float.parseFloat(JOptionPane.showInputDialog("Digite a nota da disciplina"));
                            mask.setNota(nota);
                            u.adicionarDisciplinaTemporaria(mask);
                            DefaultTableModel model = (DefaultTableModel) tableTempor.getModel();
                            model.setRowCount(0);
                            recarregarTabela(cs);
                        } else {
                            JOptionPane.showMessageDialog(null, "Nao possui pre requisito");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Selecione uma disciplina");
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Nenhuma linha selecionado");
                }
            }
        });
        finalizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Aluno u = cs.getUsuarios().get(comboBox1.getSelectedIndex());
                if (!u.getDiscTemporaria().isEmpty()) {
                    // percorrer disciplinas temporarias
                    for (var d : u.getDiscTemporaria().values()) {
                        u.addDisciplinaFinalizada(d);
                        u.addRequisitos(d.getCodigo()); // adicionar requisitos
                    }
                    DefaultTableModel model = (DefaultTableModel) tableTempor.getModel();
                    u.limparDisciplinasTemporarias();
                    model.setRowCount(0);
                    recarregarTabela(cs);
                    try {
                        cs.atualizaUsuario();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "Disciplinas adicionadas com sucesso");
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhuma disciplina adicionada");
                }
            }
        });
        removerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //verificar se linha esta selecionada e mandar mensagem de erro
                try {
                    int viewrow = tableTempor.getSelectedRow();
                    int modelRow = tableTempor.convertRowIndexToModel(viewrow);
                    Object codigo = tableTempor.getModel().getValueAt(modelRow, 2);
                    Aluno u = cs.getUsuarios().get(comboBox1.getSelectedIndex());
                    u.removerDisciplinaTemporaria(codigo.toString());
                    DefaultTableModel model = (DefaultTableModel) tableTempor.getModel();
                    model.setRowCount(0);
                    recarregarTabela(cs);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Nenhuma linha selecionado");
                }

            }
        });
        tableSequencia.setDefaultRenderer(Object.class,
                new DefaultTableCellRenderer() {
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                                   boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if (row % 2 == 0) { // && !isSelected
                            setBackground(new Color(183, 217, 241));
                            setForeground(Color.BLACK);
                        } else {
                            setBackground(new Color(255, 255, 255));
                            setForeground(Color.BLACK);
                        }
                        Aluno u = cs.getUsuarios().get(comboBox1.getSelectedIndex());
                        Object pre_req = table.getValueAt(row, 2);
                        String[] arrayReq = null;
                        try {
                            arrayReq = pre_req.toString().split(",");
                        } catch (Exception e1) {
//                            System.out.println("Nao conseguiu dividir");
//                            e1.printStackTrace();
                        }
                        if (pre_req != null && (pre_req.toString().equals("0")
                                || u.possuiRequisito(arrayReq))) {
                            setBackground(new Color(158, 245, 163));
                            //u.getRequisitos().contains(pre_req.toString()))
                        } else {
                            setBackground(new Color(245, 182, 182));
                        }
                        // colorir linha que começa com #
                        Object inicio = table.getValueAt(row, 0);
                        if (inicio != null && inicio.toString().startsWith("#")) {
                            setBackground(new Color(220, 220, 220));
                        }
                        if (isSelected) {
                            setBackground(new Color(206, 98, 252));
                            setForeground(Color.WHITE);
                            setFont(new Font("Arial", Font.BOLD, 12));
                        }
                        return this;
                    }
                }
        );

        adicionarAlunoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txfNomeA.getText().isEmpty() || !txfAreaA.getText().isEmpty()) {
                    String nomeAluno = txfNomeA.getText();
                    String area = txfAreaA.getText();
                    try {
                        cs.cadastrarUsuario(new Aluno(nomeAluno, area));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    comboBox1.removeAllItems();
                    int i = 0;
                    for (Usuario u : cs.getUsuarios()) {
                        comboBox1.addItem(i + " - " + u.getNome());
                        i++;
                    }
                    txfNomeA.setText("");
                    txfAreaA.setText("");
                    JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                }
            }
        });
        adicionarDisciplinaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txfNomeDisc.getText().isEmpty() || !txfCodigoD.getText().isEmpty()
                        || !txfPreReqA.getText().isEmpty() || !txfAreaDisc.getText().isEmpty()
                ) {
                    String nomeDisc = txfNomeDisc.getText();
                    String area = txfAreaDisc.getText();
                    String pre_req = txfPreReqA.getText();
                    String codigo = txfCodigoD.getText();
                    String tipo = Objects.requireNonNull(cmbTipo.getSelectedItem()).toString();
                    String horario = Objects.requireNonNull(cmbHorario.getSelectedItem()).toString();
                    String periodo = spinner1.getValue().toString();
                    switch (tipo) {
                        case "BASICA" -> {
                            cs.cadastrarDisciplina(new Basica(nomeDisc, codigo, pre_req, area, tipo, horario, periodo));
                        }
                        case "EL1" -> {
                            cs.cadastrarDisciplina(new Eletiva_1(nomeDisc, codigo, pre_req, area, tipo, horario, periodo));
                        }
                        case "EL2" -> {
                            cs.cadastrarDisciplina(new Eletiva_2(nomeDisc, codigo, pre_req, area, tipo, horario, periodo));
                        }
                    }
                    //limpar campos
                    txfNomeDisc.setText("");
                    txfCodigoD.setText("");
                    txfPreReqA.setText("");
                    txfAreaDisc.setText("");
                    cmbTipo.setSelectedIndex(0);
                    cmbHorario.setSelectedIndex(0);
                    spinner1.setValue(1);
                    JOptionPane.showMessageDialog(null, "Disciplina cadastrada com sucesso!");
                    recarregarTabela(cs);
                } else {
                    JOptionPane.showMessageDialog(null, "Preencha  os campos (*)!");
                }
            }
        });
        tableSequencia.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getClickCount() == 2) {
                    //show in dialog the selected row
                    int row = tableSequencia.getSelectedRow();
                    int column = 2; //tableSequencia.getSelectedColumn();
                    int columsCod = 1;
                    String codigoDisciplina = tableSequencia.getValueAt(row, columsCod).toString();
                    String value = tableSequencia.getValueAt(row, column).toString();
                    Disciplina d = cs.getMapDisciplinas().get(codigoDisciplina);
                    String[] requisito = d.getRequisitos();
                    if (!requisito[0].equals("0")) {
                        String msg = "Pre-requisitos: \n";
                        for (String s : requisito) {
                            msg += s + "    " + cs.nomePorCodigo(s) + "\n";
                        }
                        JOptionPane.showMessageDialog(null, msg);
                    } else {
                        JOptionPane.showMessageDialog(null, "Disciplina não possui pre-requisitos!");
                    }
//
//                    Disciplina pre_req = cs.getMapDisciplinas().get(value);
//                    if (pre_req != null) {
//                        JOptionPane.showMessageDialog(null, "Pré-requisito: " + pre_req.getNome());
//                    }
                }
            }
        });

        //mostrar o que está sendo digitado no textfield
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String text = textField1.getText();

                recarregarTabela(cs);
                System.out.println(text);
//                    String[] list = cs.listarDisciplinas(text);
//                    listaDisciplinas.setListData(list);
            }
        });

        tableFinalizadas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    System.out.println("Deletar");
                    int row = tableFinalizadas.getSelectedRow();
                    String codigo = tableFinalizadas.getValueAt(row, 1).toString();
                    Aluno u = cs.getUsuarios().get(comboBox1.getSelectedIndex());
                    u.removerDisciplinaFinalizada(codigo);
                    recarregarTabela(cs);
                    JOptionPane.showMessageDialog(null, "Disciplina removida com sucesso!");
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Organiza Cursos e Metas");
        try {
            frame.setContentPane(new Main().root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //change look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createUIComponents() {
        //tabela de disciplinas finalizadas
        String headerFinalizadas[] = {"Nome", "Código"};
        DefaultTableModel model1 = new DefaultTableModel(0, 2);
        model1.setColumnIdentifiers(headerFinalizadas);
        tableFinalizadas = new JTable(model1);
        tableFinalizadas.setDefaultEditor(Object.class, null);

        // tabela Sequencia aconselhada
        String headerSequencia[] = {"Disciplina", "Codigo", "Pre-Requisito", "Tipo", "Horario"};
        DefaultTableModel model2 = new DefaultTableModel(0, 5);
        model2.setColumnIdentifiers(headerSequencia);
        tableSequencia = new JTable(model2);
        TableColumnModel columnModel = tableSequencia.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(300);
        //bloquear edição da tabela
        tableSequencia.setDefaultEditor(Object.class, null);
        // tabela Temporária - escolha aluno
        String headerTemporaria[] = {"Horario", "Nome", "Codigo", "Nota"};
        DefaultTableModel model4 = new DefaultTableModel(0, 4);
        model4.setColumnIdentifiers(headerTemporaria);
        tableTempor = new JTable(model4);
        tableTempor.setDefaultEditor(Object.class, null);
        tableTempor.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0) { // && !isSelected
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(new Color(225, 225, 225));
                    setForeground(Color.BLACK);
                }
                if (isSelected) {
                    setBackground(new Color(206, 98, 252));
                    setForeground(Color.WHITE);
                    setFont(new Font("Arial", Font.BOLD, 12));
                }
                return this;
            }
        });


    }
}