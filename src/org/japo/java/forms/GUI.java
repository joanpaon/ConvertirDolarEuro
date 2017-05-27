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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.japo.java.events.AEM;

/**
 *
 * @author José A. Pacheco Ondoño - joanpaon@gmail.com
 */
public class GUI extends JFrame {

    // Factor de conversión E > D
    public static final double FACTOR = 1.32;

    // Referencias a los componentes
    private JLabel lblDol;
    private JLabel lblEur;
    private JTextField txfDol;
    private JTextField txfEur;

    public GUI() {
        // Inicialización PREVIA
        beforeInit();

        // Creación del interfaz
        initComponents();

        // Inicialización POSTERIOR
        afterInit();
    }

    // Inicialización antes del IGU
    private void beforeInit() {

    }

    // Inicialización después del IGU
    private void afterInit() {

    }

    // Construcción del IGU
    private void initComponents() {
        // Fuente personalizada
        Font f = new Font("Calibri", Font.BOLD, 20);

        // Dimensiones
        Dimension dimFRM = new Dimension(300, 130);
        Dimension dimLBL = new Dimension(70, 30);
        Dimension dimTXF = new Dimension(150, 30);

        // Bordes
        EmptyBorder brdPNL = new EmptyBorder(10, 10, 10, 10);

        // Gestor de Eventos de Acción
        AEM aem = new AEM(this);

        // Etiqueta Euro
        lblEur = new JLabel("Euros");
        lblEur.setFont(f);
        lblEur.setPreferredSize(dimLBL);
        lblEur.setHorizontalAlignment(JLabel.RIGHT);

        // Etiqueta Dólar
        lblDol = new JLabel("Dólares");
        lblDol.setFont(f);
        lblDol.setPreferredSize(dimLBL);
        lblDol.setHorizontalAlignment(JLabel.RIGHT);

        // Campo de Euros
        txfEur = new JTextField("0.00");
        txfEur.setFont(f);
        txfEur.setPreferredSize(dimTXF);
        txfEur.addActionListener(aem);

        // Campo de Dólares
        txfDol = new JTextField("0.00");
        txfDol.setFont(f);
        txfDol.setPreferredSize(dimTXF);
        txfDol.addActionListener(aem);

        // Panel Principal
        JPanel pnlPpal = new JPanel();
        pnlPpal.setBorder(brdPNL);
        pnlPpal.add(lblEur);
        pnlPpal.add(txfEur);
        pnlPpal.add(lblDol);
        pnlPpal.add(txfDol);

        // Ventana principal
        setTitle("Conversor Dolar-Euro");
        setContentPane(pnlPpal);
        setResizable(false);
        setSize(dimFRM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Evento de Accion - Respuesta
    public void procesarAccion(ActionEvent e) {
        if (e.getSource().equals(txfEur)) {
            convertirE2D();     // E >> D
        } else {
            convertirD2E();     // D >> E
        }
    }

    // E >> D
    private void convertirE2D() {
        try {
            String textoEuros = txfEur.getText();
            textoEuros = textoEuros.replace(',', '.');
            double dineroEuros = Double.parseDouble(textoEuros);
            double dineroDolar = dineroEuros * FACTOR;
            String textoDolar = String.format("%.2f", dineroDolar);
            txfDol.setText(textoDolar);
        } catch (NumberFormatException e) {
            txfDol.setText("???");
        }
    }

    // D >> E
    private void convertirD2E() {
        try {
            String textoDolar = txfDol.getText();
            textoDolar = textoDolar.replace(',', '.');
            double dineroDolar = Double.parseDouble(textoDolar);
            double dineroEuro = dineroDolar / FACTOR;
            String textoEuro = String.format("%.2f", dineroEuro);
            txfEur.setText(textoEuro);
        } catch (NumberFormatException e) {
            txfEur.setText("???");
        }
    }
}
