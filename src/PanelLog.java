import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;

public class PanelLog extends JPanel{
    private JTextArea areaLog;
    private JScrollPane panelScroll;
    private JButton botonLimpiarLog;
    private JPanel panelBotones;

    public PanelLog(PeerProtocolos peer){
        setLayout(new BorderLayout());
        
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true); 
        areaLog.setWrapStyleWord(true);
        areaLog.setFont(new Font("Arial", Font.BOLD, 20));

        peer.setAreaLog(this.areaLog);

        panelScroll = new JScrollPane(areaLog);
        panelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelBotones = new JPanel(new BorderLayout());

        botonLimpiarLog = new JButton("LIMPIAR");

        botonLimpiarLog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarLog();
            }
        });

        panelBotones.add(botonLimpiarLog, BorderLayout.CENTER);

        add(panelScroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(0, 50));
    }

    private void limpiarLog(){
        this.areaLog.setText("");
        System.out.println("Limpiando");
    }
}
