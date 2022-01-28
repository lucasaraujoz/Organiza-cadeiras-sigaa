package Entidades;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

public class Aluno extends Usuario{
    private Map<String, Disciplina> discFinalizadas;
    private Map<String, Disciplina> discTemporaria;
    private LinkedList<String> requisitos = new LinkedList<>();
    private String areaInteresse;

    public Aluno(String nome, String areaInteresse) {
        super(nome);
        discTemporaria = new LinkedHashMap<>();
        discFinalizadas = new LinkedHashMap<>();
        this.areaInteresse = areaInteresse;
    }

    public LinkedList<String> getRequisitos() {
        return requisitos;
    }

    public void addRequisitos(String codigo) {
        requisitos.add(codigo);
    }

    public Map<String, Disciplina> getDiscFinalizadas() {
        return discFinalizadas;
    }

    public void addDisciplinaFinalizada(Disciplina disciplina) {
        discFinalizadas.put(disciplina.getCodigo(), disciplina);
    }
    public void removerDisciplinaFinalizada(String codigo) {
        discFinalizadas.remove(codigo);
    }

    public Map<String, Disciplina> getDiscTemporaria() {
        LinkedList<Disciplina> lista = new LinkedList<>(discTemporaria.values());
        lista.sort((Disciplina d1, Disciplina d2) -> d1.getHorario().compareTo(d2.getHorario()));
        Map<String, Disciplina> aux = new LinkedHashMap<>();
        for (Disciplina d : lista) {
            aux.put(d.getCodigo(), d);
        }
        return aux;
    }

    public boolean adicionarDisciplinaTemporaria(Disciplina disciplina) {
        for (Disciplina d : discTemporaria.values()) {
            if (d.getHorario().equals(disciplina.getHorario())) {
                JOptionPane.showMessageDialog(null, "Horario ja ocupado");
                return false;
            }
        }
        discTemporaria.put(disciplina.getCodigo(), disciplina);
        return true;
    }

    public void removerDisciplinaTemporaria(String codigo) {
        discTemporaria.remove(codigo);
    }

    public void limparDisciplinasTemporarias() {
        discTemporaria.clear();
    }

    public boolean possuiRequisito(String[] array1) {
        if (array1 == null) return false;
        for (String s : array1) {
            if (!requisitos.contains(s)) {
                return false;
            }
        }
        return true;
    }
}
