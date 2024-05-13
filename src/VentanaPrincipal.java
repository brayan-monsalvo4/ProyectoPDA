import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.print.attribute.HashAttributeSet;
import javax.swing.*;

import com.google.gson.Gson;

public class VentanaPrincipal extends JFrame {

    private JPanel panelIzquierdo;
    private JPanel panelDerecho;
    private JPanel subPanelDerecho;

    private PanelChat chat;
    private PanelLog log;
    private PanelEDO edo;

    private Peer peer;

    public VentanaPrincipal(){
        super("Proyecto PDA");

        peer = new Peer("224.0.0.1");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                peer.detenerPeers();
            }
        });

        setLayout(new BorderLayout());

        initComponents();
        
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        peer.iniciarPeers();

    }

    public void initComponents(){
        chat = new PanelChat( peer.obtenerPeerChat() );
        log = new PanelLog( peer.obtenerPeerProtocolos() );

        edo = new PanelEDO(peer.obtenerDirectorio());

        panelDerecho = new JPanel(new BorderLayout());
        panelIzquierdo = new JPanel(new GridLayout(1, 1, 10, 10));
        subPanelDerecho = new JPanel(new GridLayout(2, 1, 10, 10));
        
        subPanelDerecho.add(chat);
        subPanelDerecho.add(log);

        panelDerecho.add(subPanelDerecho, BorderLayout.CENTER);

        panelIzquierdo.add(edo);

        add(panelDerecho, BorderLayout.CENTER);
        add(panelIzquierdo, BorderLayout.EAST);

    }   
}
