import java.awt.BorderLayout;
import java.util.Scanner;
import java.awt.event.*;

import javax.swing.*;
import java.awt.*;

public class PanelChat extends JPanel{
    private JTextArea areaChat;
    private JTextField fieldMensaje;
    private JButton botonEnviarMensaje;
    private JScrollPane panelScroll;

    private JPanel panelMensaje;

    public PanelChat(PeerChat peer){
        setLayout(new BorderLayout());
    
        areaChat = new JTextArea();
        areaChat.setEditable(false);
        areaChat.setLineWrap(true); 
        areaChat.setWrapStyleWord(true);
        areaChat.setFont(new Font("Arial", Font.BOLD, 20));

        peer.setAreaChat(this.areaChat);
        
        panelMensaje = new JPanel(new BorderLayout());

        panelScroll = new JScrollPane(areaChat);
        panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        fieldMensaje = new JTextField();
        botonEnviarMensaje = new JButton("ENVIAR");

        botonEnviarMensaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje(peer);    
            }
        });

        fieldMensaje.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                botonEnviarMensaje.doClick();
                }
            }
        });

        panelMensaje.add(fieldMensaje, BorderLayout.CENTER);
        panelMensaje.add(botonEnviarMensaje, BorderLayout.EAST);

        add(panelScroll, BorderLayout.CENTER);
        add(panelMensaje, BorderLayout.SOUTH);
        
    }

    public void enviarMensaje(PeerChat peer){
        String mensaje = fieldMensaje.getText();
        if (!mensaje.isEmpty()) {
            //areaChat.append(System.getProperty("user.name")+": "+mensaje+"\n");
            fieldMensaje.setText("");
            peer.enviarMensaje(System.getProperty("user.name")+": "+mensaje+"\n");
        }

        System.out.println("Enviando");
    }
}
