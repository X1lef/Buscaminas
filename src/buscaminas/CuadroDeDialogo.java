/*
 * Copyright (C) 2017 Félix Pedrozo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package buscaminas;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public class CuadroDeDialogo extends JDialog implements ActionListener {
    private JButton jbSalir, jbJugarDeNuevo, jbReiniciar;
    private Buscaminas buscaminas;

    static final int MENS_GANO = 0, MENS_PERDIO = 1;

    CuadroDeDialogo(Buscaminas padre, String titulo, String mens, int tipoMens) {
        super (padre, true);
        buscaminas = padre;

        setSize (320, 150);
        setResizable(false);
        setLocationRelativeTo(padre);
        setLayout(new GridBagLayout());
        setTitle(titulo);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                buscaminas.iniciarNuevoJuego();
                dispose();
            }
        });

        GridBagConstraints conf = new GridBagConstraints();
        conf.gridx = conf.gridy = 0;
        conf.weighty = 1.0;
        conf.insets = new Insets (30, 10, 30, 10);

        JLabel mensaje = new JLabel (mens);
        add (mensaje, conf);

        conf.gridy = 1;
        conf.weighty = 0.0;
        conf.weightx = 1.0;
        conf.insets = new Insets(0, 10, 10, 10);
        conf.fill = GridBagConstraints.HORIZONTAL;

        add (panelBotones(tipoMens), conf);

        setVisible(true);
    }

    private JPanel panelBotones (int tipo) {
        JPanel panel = new JPanel ();

        jbJugarDeNuevo = new JButton ("Jugar de nuevo");
        jbJugarDeNuevo.addActionListener(this);

        jbSalir = new JButton ("Salir");
        jbSalir.addActionListener(this);

        panel.add(jbSalir);

        if (tipo == MENS_PERDIO) {
            jbReiniciar = new JButton ("Reiniciar juego");
            jbReiniciar.addActionListener(this);

            panel.add(jbReiniciar);
        }

        panel.add(jbJugarDeNuevo);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbSalir)
           System.exit(0);
        else if (e.getSource() == jbJugarDeNuevo)
            buscaminas.iniciarNuevoJuego();
        else
            buscaminas.reiniciarJuego();

        dispose();
    }
}
