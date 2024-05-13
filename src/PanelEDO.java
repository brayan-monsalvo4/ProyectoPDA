import javax.swing.*;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.rmi.*;

public class PanelEDO extends JPanel{
    @SuppressWarnings("rawtypes")
    private JList listaArchivosEOD;
    @SuppressWarnings("rawtypes")
    private DefaultListModel modelo;
    private JPanel panelBotones;
    private JButton botonDescargar;
    private JButton botonSubir;
    private JButton botonActualizar;
    private JFileChooser exploradorCarga;
    private JFileChooser exploradorDescarga;

    private DirectorioPeers directorio;


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PanelEDO(DirectorioPeers directorio){
        this.directorio = directorio;
        
        setLayout(new BorderLayout());

        panelBotones = new JPanel(new GridLayout(1, 3, 10, 10));

        modelo = new DefaultListModel<>();
        listaArchivosEOD = new JList(modelo);

        //escanerUploads = new EscanerUploads(listaArchivosEOD, modelo);

        JScrollPane scrollPane = new JScrollPane(listaArchivosEOD);

        botonDescargar = new JButton("DESCARGAR");
        botonActualizar = new JButton("ACTUALIZAR");

        botonDescargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                descargarArchivo();
            }
        });


        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarListaArchivos();
            }
            
        });

        panelBotones.add(botonDescargar);
        panelBotones.add(botonActualizar);

        exploradorCarga = new JFileChooser();
        exploradorDescarga = new JFileChooser();

        exploradorCarga.setFileSelectionMode( JFileChooser.FILES_ONLY );
        exploradorDescarga.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

    }
    private void descargarArchivo(){
        try{
            System.out.println("seleccionado: "+listaArchivosEOD.getSelectedValue());
            String seleccion = (String) listaArchivosEOD.getSelectedValue();
            
            String[] info = seleccion.split("@");

            RMIInterface fi = (RMIInterface) Naming.lookup( "/" + info[0] + "/" + "RMIImpl" );

            byte[] filedata = fi.descargarArchivo(info[1]);
            File file = new File("downloads/"+info[1]);
            BufferedOutputStream output = new
              BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()));
            output.write(filedata,0,filedata.length);
            output.flush();
            output.close();
            System.out.println("Descargado con exito!");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void actualizarListaArchivos(){
        HashMap<String, String> map = directorio.obtenerMapIpId();

        System.out.println("map: "+map);

        modelo.clear();

        for (String key : map.keySet()){
            String direccion = "/" + key + "/" + "RMIImpl";
            try{
                RMIInterface fi = (RMIInterface) Naming.lookup( direccion );
                ArrayList<String> archivos = fi.listaArchivos();

                System.out.println("Lista archivos : "+archivos);

                for (String file : archivos){
                    modelo.addElement( file );
                }
            }catch(Exception e){
                e.getStackTrace();
            }
        }
        
    }

}
