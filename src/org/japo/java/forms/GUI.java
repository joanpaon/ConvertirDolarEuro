/*
 * Copyright 2017 José A. Pacheco Ondoño - joanpaon@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.japo.java.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.japo.java.components.BackgroundPanel;
import org.japo.java.events.AEM;
import org.japo.java.libraries.UtilesSwing;

/**
 *
 * @author José A. Pacheco Ondoño - joanpaon@gmail.com
 */
public class GUI extends JFrame {

    // Propiedades App
    public static final String PRP_LOOK_AND_FEEL = "look_and_feel";
    public static final String PRP_FAVICON = "favicon";
    public static final String PRP_BACKGROUND = "background";
    public static final String PRP_FACTOR = "factor";

    // Valores por Defecto
    public static final String DEF_LOOK_AND_FEEL = UtilesSwing.LNF_NIMBUS;
    public static final String DEF_FAVICON = "img/favicon.png";
    public static final String DEF_BACKGROUND = "img/dolar-euro.jpg";
    public static final String DEF_FACTOR = "1.20";

    // Referencias
    private Properties prp;
    private JLabel lblDol;
    private JLabel lblEur;
    private JTextField txfDol;
    private JTextField txfEur;

    // Constructor
    public GUI(Properties prp) {
        // Inicialización Anterior
        initBefore(prp);

        // Creación Interfaz
        initComponents();

        // Inicializacion Posterior
        initAfter();
    }

    // Construcción del IGU
    private void initComponents() {
        // Bordes
        EmptyBorder brdPNL = new EmptyBorder(10, 10, 10, 10);

        // Gestor de Eventos de Acción
        AEM aem = new AEM(this);

        // Etiqueta Euro
        lblEur = new JLabel("Euros");
        lblEur.setFont(new Font("Calibri", Font.BOLD, 32));
        lblEur.setPreferredSize(new Dimension(200, 50));
        lblEur.setOpaque(true);
        lblEur.setBackground(new Color(255, 255, 255, 200));

        // Etiqueta Dólar
        lblDol = new JLabel("Dólares");
        lblDol.setFont(new Font("Calibri", Font.BOLD, 32));
        lblDol.setPreferredSize(new Dimension(200, 50));
        lblDol.setOpaque(true);
        lblDol.setBackground(new Color(255, 255, 255, 200));
        
        // Campo de Euros
        txfEur = new JTextField("0.00");
        txfEur.setFont(new Font("Consolas", Font.PLAIN, 32));
        txfEur.setPreferredSize(new Dimension(200, 50));
        txfEur.setHorizontalAlignment(JTextField.RIGHT);
        txfEur.addActionListener(aem);

        // Campo de Dólares
        txfDol = new JTextField("0.00");
        txfDol.setFont(new Font("Consolas", Font.PLAIN, 32));
        txfDol.setPreferredSize(new Dimension(200, 50));
        txfDol.setHorizontalAlignment(JTextField.RIGHT);
        txfDol.addActionListener(aem);

        // Panel Principal
        String fondoPpal = prp.getProperty(PRP_BACKGROUND, DEF_BACKGROUND);
        URL urlPpal = ClassLoader.getSystemResource(fondoPpal);
        Image imgPpal = new ImageIcon(urlPpal).getImage();
        JPanel pnlPpal = new BackgroundPanel(imgPpal);
        pnlPpal.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 54));
        pnlPpal.add(lblEur);
        pnlPpal.add(txfEur);
        pnlPpal.add(lblDol);
        pnlPpal.add(txfDol);

        // Ventana principal
        setContentPane(pnlPpal);
        setTitle("Swing Manual #07");
        setResizable(false);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Inicialización Anterior    
    private void initBefore(Properties prp) {
        // Memorizar Referencia
        this.prp = prp;

        // Establecer LnF
        UtilesSwing.establecerLnF(prp.getProperty(PRP_LOOK_AND_FEEL, DEF_LOOK_AND_FEEL));
    }

    // Inicialización Posterior
    private void initAfter() {
        // Establecer Favicon
        UtilesSwing.establecerFavicon(this, prp.getProperty(PRP_FAVICON, DEF_FAVICON));
    }

    // Evento de Accion - Respuesta
    public void procesarAccion(ActionEvent ae) {
        try {
            // Factor Conversión E > D
            double factor = Double.parseDouble(prp.getProperty(PRP_FACTOR, DEF_FACTOR));

            // Tipo de Conversión
            if (ae.getSource().equals(txfEur)) {
                convertirE2D(factor);     // E >> D
            } else {
                convertirD2E(factor);     // D >> E
            }
        } catch (Exception e) {
            System.out.println("ERROR: Factor de conversión erróneo");
        }
    }

    // E >> D
    private void convertirE2D(double factor) {
        try {
            String textoEuros = txfEur.getText();
            textoEuros = textoEuros.replace(',', '.');
            double dineroEuros = Double.parseDouble(textoEuros);
            double dineroDolar = dineroEuros * factor;
            String textoDolar = String.format(Locale.ENGLISH, "%.2f", dineroDolar);
            txfDol.setText(textoDolar);
        } catch (NumberFormatException e) {
            txfDol.setText("???");
        }
    }

    // D >> E
    private void convertirD2E(double factor) {
        try {
            String textoDolar = txfDol.getText();
            textoDolar = textoDolar.replace(',', '.');
            double dineroDolar = Double.parseDouble(textoDolar);
            double dineroEuro = dineroDolar / factor;
            String textoEuro = String.format(Locale.ENGLISH, "%.2f", dineroEuro);
            txfEur.setText(textoEuro);
        } catch (NumberFormatException e) {
            txfEur.setText("???");
        }
    }
}
