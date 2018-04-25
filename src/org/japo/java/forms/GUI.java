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
import java.awt.event.FocusEvent;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.japo.java.components.BackgroundPanel;
import org.japo.java.events.AEM;
import org.japo.java.events.FEM;
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
    public static final String DEF_LOOK_AND_FEEL = UtilesSwing.LNF_WINDOWS;
    public static final String DEF_FAVICON = "img/favicon.png";
    public static final String DEF_BACKGROUND = "img/background.jpg";
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

    // Construcción - GUI
    private void initComponents() {
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
        txfEur.setBackground(Color.ORANGE);
        txfEur.setSelectionStart(0);
        txfEur.addActionListener(new AEM(this));
        txfEur.addFocusListener(new FEM(this));

        // Campo de Dólares
        txfDol = new JTextField("0.00");
        txfDol.setFont(new Font("Consolas", Font.PLAIN, 32));
        txfDol.setPreferredSize(new Dimension(200, 50));
        txfDol.setHorizontalAlignment(JTextField.RIGHT);
        txfDol.setBackground(Color.LIGHT_GRAY);
        txfDol.addActionListener(new AEM(this));
        txfDol.addFocusListener(new FEM(this));

        // Imagen de Fondo
        String pthImg = prp.getProperty(PRP_BACKGROUND, DEF_BACKGROUND);
        URL urlImg = ClassLoader.getSystemResource(pthImg);
        Image img = new ImageIcon(urlImg).getImage();

        // Panel Principal
        JPanel pnlPpal = new BackgroundPanel(img);
        pnlPpal.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 54));
        pnlPpal.add(lblEur);
        pnlPpal.add(txfEur);
        pnlPpal.add(lblDol);
        pnlPpal.add(txfDol);

        // Ventana Principal
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
                convertirE2D(txfEur, txfDol, factor);     // E >> D
            } else {
                convertirD2E(txfDol, txfEur, 1 / factor); // D >> E
            }

            // Selección
            ((JTextField) (ae.getSource())).setSelectionStart(0);
        } catch (NumberFormatException e) {
            // Gestión de Error
            if (ae.getSource().equals(txfEur)) {
                txfDol.setText("???");
            } else {
                txfEur.setText("???");
            }
        }
    }

    // E >> D
    private void convertirE2D(JTextField txfEur, JTextField txfDol, double factor) {
        try {
            // Campo de texto Euros > Texto
            String txtEur = txfEur.getText();

            // Formato Fraccionario SPANISH > ENGLISH 
            txtEur = txtEur.replace(',', '.');

            // Obtiene Euros y Dólares
            double dinEur = Double.parseDouble(txtEur);
            double dinDol = dinEur * factor;

            // Formatea Euros y Dólares
            txtEur = String.format(Locale.ENGLISH, "%.2f", dinEur);
            String txtDol = String.format(Locale.ENGLISH, "%.2f", dinDol);

            // Actualiza Campos de Texto
            txfEur.setText(txtEur);
            txfDol.setText(txtDol);
        } catch (NumberFormatException e) {
            txfDol.setText("???");
        }
    }

    // D >> E
    private void convertirD2E(JTextField txfDol, JTextField txfEur, double factor) {
        try {
            // Campo de Texto - Dólares > Texto
            String txtDol = txfDol.getText();

            // Formato Fraccionario SPANISH > ENGLISH 
            txtDol = txtDol.replace(',', '.');

            // Obtiene Dólares y Euros
            double dinDol = Double.parseDouble(txtDol);
            double dinEur = dinDol * factor;

            // Formatea Euros y Dólares
            txtDol = String.format(Locale.ENGLISH, "%.2f", dinDol);
            String txtEur = String.format(Locale.ENGLISH, "%.2f", dinEur);

            // Actualiza Campos de Texto
            txfDol.setText(txtDol);
            txfEur.setText(txtEur);
        } catch (NumberFormatException e) {
            txfEur.setText("???");
        }
    }

    // Gestión Foco Ganado
    public void procesarFocoGanado(FocusEvent e) {
        // Campo de Texto - Evento
        JTextField txfAct = (JTextField) e.getSource();

        // Color Fondo - GANADO
        txfAct.setBackground(Color.ORANGE);

        // Mover Cursor - PRINCIPIO
        txfAct.setSelectionStart(0);
    }

    // Gestión Foco Perdido
    public void procesarFocoPerdido(FocusEvent e) {
        // Campo de Texto - Evento
        JTextField txfAct = (JTextField) e.getSource();

        // Color Fondo - PERDIDO
        txfAct.setBackground(Color.LIGHT_GRAY);
    }
}
