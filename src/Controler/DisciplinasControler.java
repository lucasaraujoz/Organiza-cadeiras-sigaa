package Controler;

import Entidades.*;

import java.io.*;
import java.util.*;

public class DisciplinasControler implements Serializable {
    private ArrayList<Aluno> usuarios;
    private Map<String, Disciplina> mapDisciplinas;

    public DisciplinasControler() throws IOException {
        mapDisciplinas = importMapDisciplinas();
        usuarios = importAlunos();
    }

    public ArrayList<Aluno> importAlunos() throws IOException {
        System.out.println("Importando alunos...");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ArrayList<Aluno> alunos = new ArrayList<>();
        File file = new File("src/Alunos.dat");
        if (!file.exists()) {
            return alunos;
        }
        try {
            fis = new FileInputStream("src/Alunos.dat");
            ois = new ObjectInputStream(fis);
            alunos = (ArrayList<Aluno>) ois.readObject();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return alunos;
    }

    private Map<String, Disciplina> importMapDisciplinas() throws IOException {
        Map<String, Disciplina> disciplinas = new TreeMap<>();
        BufferedReader br = new BufferedReader(new FileReader("src/Disciplinas.csv"));
        String linha = "";
        br.readLine(); // pula a primeira linha
        br.readLine(); //pula o #1
        int periodo = 1;
        while ((linha = br.readLine()) != null) {
            if (!linha.contains("#")) {
                String[] dados = linha.split(";");
                String nome = dados[0].strip();
                String cod = dados[1].strip();
                String prereq = dados[2].strip();
                String area = dados[3].strip();
                String tipo = dados[4].strip();
                String horario = dados[5].strip();
                String periodo_str = String.valueOf(periodo).strip();
                String[] requisitos = null;
                try {
                    requisitos = prereq.replace(" ", "").split(",");

                } catch (Exception e) {
                    System.out.println("Erro ao ler requisitos da disciplina " + cod);
                    e.printStackTrace();
                }
                switch (tipo) {
                    case "BASICA" -> {
                        Basica basica = new Basica(nome, cod, prereq, area, tipo, horario, periodo_str);
                        basica.setArrayRequisito(requisitos);
                        disciplinas.put(cod, basica);
                    }
                    case "EL1" -> {
                        Eletiva_1 el1 = new Eletiva_1(nome, cod, prereq, area, tipo, horario, periodo_str);
                        el1.setArrayRequisito(requisitos);
                        disciplinas.put(cod, el1);
                    }
                    case "EL2" -> {
                        Eletiva_2 el2 = new Eletiva_2(nome, cod, prereq, area, tipo, horario, periodo_str);
                        el2.setArrayRequisito(requisitos);
                        disciplinas.put(cod, el2);
                    }
                }
            } else {
                periodo++;
            }
        }
        return disciplinas;
    }

    public Map<String, Disciplina> getMapDisciplinas() {
        return mapDisciplinas;
    }

    public void cadastrarDisciplina(Disciplina disciplina) {
        this.mapDisciplinas.put(disciplina.getCodigo(), disciplina);
    }

    public ArrayList<Aluno> getUsuarios() {
        return usuarios;
    }

    public void cadastrarUsuario(Aluno usuario) throws IOException {
        this.usuarios.add(usuario);
        atualizaUsuario();
    }

    public void atualizaUsuario() throws IOException {
        File file = new File("src/Alunos.dat");
        if (file.exists())
            file.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("src/Alunos.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.usuarios);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String nomePorCodigo(String codigo) {
        return mapDisciplinas.get(codigo).getNome();
    }

    public boolean verificar(String compara, String[] array1) {
        if (array1 == null) return false;
        for (String s : array1) {
                if (compara.equals(s)) {
                    return true;
                }
        }
        return false;
    }
}
