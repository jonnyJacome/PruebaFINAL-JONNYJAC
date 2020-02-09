package com.example.pruebafinal_jonnyjac;

public class Paises {
    String titulo, img;

    public Paises() {
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img ="http://www.geognos.com/api/en/countries/flag/"+img+".png";
    }
}
