/* 
 * Copyright 2019 José A. Pacheco Ondoño - joanpaon@gmail.com.
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
import java.util.Locale;
import java.util.Properties;
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
public final class GUI extends JFrame {

    // Propiedades App
    public static final String PRP_FAVICON_RESOURCE = "favicon_resource";
    public static final String PRP_FONT_RESOURCE = "font_resource";
    public static final String PRP_FORM_HEIGHT = "form_height";
    public static final String PRP_FORM_WIDTH = "form_width";
    public static final String PRP_FORM_TITLE = "form_title";
    public static final String PRP_IMAGE_RESOURCE = "image_resource";
    public static final String PRP_LOOK_AND_FEEL_PROFILE = "look_and_feel_profile";
    public static final String PRP_FACTOR_CONVERSION = "factor_conversion";

    // Valores por Defecto
    public static final String DEF_FAVICON_RESOURCE = "img/favicon.png";
    public static final String DEF_FONT_FALLBACK_NAME = Font.SERIF;
    public static final String DEF_FONT_SYSTEM_NAME = "Kaufmann BT";
    public static final int DEF_FORM_HEIGHT = 300;
    public static final int DEF_FORM_WIDTH = 500;
    public static final String DEF_FORM_TITLE = "Swing Manual App";
    public static final String DEF_LOOK_AND_FEEL_PROFILE = UtilesSwing.LNF_WINDOWS_PROFILE;
    public static final String DEF_FACTOR_CONVERSION = "1.2"; // E > D

    // Colores
    private static final Color COLOR_FOCO_GANADO = Color.ORANGE;
    private static final Color COLOR_FOCO_PERDIDO = Color.LIGHT_GRAY;

    // Referencias
    private Properties prp;

    // Componentes
    private JLabel lblDol;
    private JLabel lblEur;
    private JTextField txfDol;
    private JTextField txfEur;
    private JPanel pnlPpal;

    // Imágenes
    private Image imgSample;

    // Constructor
    public GUI(Properties prp) {
        // Conectar Referencia
        this.prp = prp;

        // Inicialización Anterior
        initBefore();

        // Creación Interfaz
        initComponents();

        // Inicializacion Posterior
        initAfter();
    }

    // Construcción - GUI
    private void initComponents() {
        // Imágenes
        imgSample = UtilesSwing.importarImagenRecurso(
                prp.getProperty(PRP_IMAGE_RESOURCE));

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
        txfEur.setBackground(COLOR_FOCO_GANADO);
        txfEur.setSelectionStart(0);

        // Campo de Dólares
        txfDol = new JTextField("0.00");
        txfDol.setFont(new Font("Consolas", Font.PLAIN, 32));
        txfDol.setPreferredSize(new Dimension(200, 50));
        txfDol.setHorizontalAlignment(JTextField.RIGHT);
        txfDol.setBackground(COLOR_FOCO_PERDIDO);

        // Panel Principal
        pnlPpal = new BackgroundPanel(imgSample);
        pnlPpal.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 54));
        pnlPpal.add(lblEur);
        pnlPpal.add(txfEur);
        pnlPpal.add(lblDol);
        pnlPpal.add(txfDol);

        // Ventana Principal
        setContentPane(pnlPpal);
        setTitle(prp.getProperty(PRP_FORM_TITLE, DEF_FORM_TITLE));
        try {
            int height = Integer.parseInt(prp.getProperty(PRP_FORM_HEIGHT));
            int width = Integer.parseInt(prp.getProperty(PRP_FORM_WIDTH));
            setSize(width, height);
        } catch (NumberFormatException e) {
            setSize(DEF_FORM_WIDTH, DEF_FORM_HEIGHT);
        }
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Inicialización Anterior    
    private void initBefore() {
        // Establecer LnF
        UtilesSwing.establecerLnFProfile(prp.getProperty(
                PRP_LOOK_AND_FEEL_PROFILE, DEF_LOOK_AND_FEEL_PROFILE));
    }

    // Inicialización Posterior
    private void initAfter() {
        // Establecer Favicon
        UtilesSwing.establecerFavicon(this, prp.getProperty(
                PRP_FAVICON_RESOURCE, DEF_FAVICON_RESOURCE));

        // Registrar Gestores de Eventos
        txfEur.addActionListener(new AEM(this));
        txfEur.addFocusListener(new FEM(this));
        txfDol.addActionListener(new AEM(this));
        txfDol.addFocusListener(new FEM(this));
    }

    // Evento de Accion - Respuesta
    public final void procesarAccion(ActionEvent ae) {
        try {
            // Factor Conversión E > D
            double factor = Double.parseDouble(
                    prp.getProperty(PRP_FACTOR_CONVERSION, DEF_FACTOR_CONVERSION));

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
    private void convertirE2D(JTextField txfEur, JTextField txfDol, double factor) throws NumberFormatException {
        // Campo de texto Euros > Texto
        String txtEur = txfEur.getText().trim();

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
    }

    // D >> E
    private void convertirD2E(JTextField txfDol, JTextField txfEur, double factor) throws NumberFormatException {
        // Campo de Texto - Dólares > Texto
        String txtDol = txfDol.getText().trim();

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
    }

    // Gestión Foco Ganado
    public final void procesarFocoGanado(FocusEvent e) {
        // Campo de Texto - Evento
        JTextField txfAct = (JTextField) e.getSource();

        // Color Fondo - GANADO
        txfAct.setBackground(COLOR_FOCO_GANADO);

        // Mover Cursor - PRINCIPIO
        txfAct.setSelectionStart(0);
    }

    // Gestión Foco Perdido
    public final void procesarFocoPerdido(FocusEvent e) {
        // Campo de Texto - Evento
        JTextField txfAct = (JTextField) e.getSource();

        // Color Fondo - PERDIDO
        txfAct.setBackground(COLOR_FOCO_PERDIDO);
    }
}
