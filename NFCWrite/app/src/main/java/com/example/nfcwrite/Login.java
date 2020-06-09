package com.example.nfcwrite;

public class Login {

    private int idUser;
    private String username;
    private String password;


    public Login(String username, String password, int idUser) {

        this.username = username;
        this.password = password;
        this.idUser = idUser;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
