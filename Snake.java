package com.zetcode;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Snake extends JFrame {

    //Constructor
    public Snake() {
        
        initUI();
    }
    
    //Inicializa la interfaz de usuario
    private void initUI() {
        
        //Agrega un nuevo tablero al JFrame
        add(new Board());
        
        //Establece el tamaño del JFrame
        setResizable(false);
        pack();
        
        //Establece el título del JFrame
        setTitle("Snake");
        //Establece la posición del JFrame
        setLocationRelativeTo(null);
        //Establece la operación por defecto al cerrar el JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
