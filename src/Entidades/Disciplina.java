package Entidades;

import java.io.Serializable;
import java.util.ArrayList;

public class Disciplina implements Serializable {
    private String nome;
    private String codigo;
    private String preRequisito;
    private String area;
    private String tipo;
    private String horario;
    private String periodo;
    private float nota = 0;
    protected String[] requisitos;

    public Disciplina(String nome, String codigo, String preRequisito,
                      String area, String tipo, String horario, String periodo) {
        this.nome = nome;
        this.codigo = codigo;
        this.preRequisito = preRequisito;
        this.area = area;
        this.tipo = tipo;
        this.horario = horario;
        this.periodo = periodo;
    }

    public void setArrayRequisito(String[] requisitos) {
        this.requisitos = requisitos;
    }

    public String[] getRequisitos() {
        return requisitos;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getPreRequisito() {
        return preRequisito;
    }

    public String getPeriodo() {
        return periodo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getHorario() {
        return horario;
    }
}
