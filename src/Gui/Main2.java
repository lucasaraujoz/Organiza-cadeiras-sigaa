package Gui;

import Entidades.Aluno;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main2 {
    public boolean verificar(String[] array1, String[] array2) {
//        for (int i = 0; i < array2.length; i++) {
//            if(!array1.contains(array2[i])){
//                return false;
//            }
//        }
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array1));
        arrayList.contains(array2[0]);
        Arrays.stream(array1).toList();
        return true;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ArrayList<Aluno> alunos = new ArrayList<>();
//        alunos.add(new Aluno("Lucas", "engenharia"));
//        alunos.add(new Aluno("Maria", "engenharia"));
//        alunos.add(new Aluno("João", "engenharia"));
//        alunos.add(new Aluno("Pedro", "engenharia"));
//        //grava arratlis em um arquivo
//        FileOutputStream fileOutputStream = new FileOutputStream("xyz.dat");
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//        objectOutputStream.writeObject(alunos);
//        objectOutputStream.close();

        //ler objeto arquivo
//        FileInputStream fileInputStream = new FileInputStream("xyz.dat");
//        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//        alunos = (ArrayList<Aluno>) objectInputStream.readObject();
//        objectInputStream.close();
//        System.out.println(alunos.size());
        File file = new File("xyz.dat");
        if (file.exists()) {
            file.delete();
            System.out.println("Arquivo deletado");
        }

    }

}
