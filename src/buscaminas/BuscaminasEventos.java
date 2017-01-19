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

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import static buscaminas.ImgUtil.nombreImg;
import static buscaminas.ImgUtil.cambiarDisfraz;

public class BuscaminasEventos extends MouseAdapter implements ActionListener {
    private Buscaminas vista;

    BuscaminasEventos(Buscaminas vista) {
        //Obtengo la referencia de la vista.
        this.vista = vista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String [] posc = e.getActionCommand().split(",");

        //Guardo la posición de la celda pulsada.
        int f = Integer.parseInt(posc [0]);
        int c = Integer.parseInt(posc [1]);

        //Compruebo si es una celda.
        if (nombreImg(vista.cuadros [f][c]).equals("celda")) {
            vista.mostrarValorDeCelda(f, c);

            //Compruebo si ha pulsado una mina.
            if (vista.tablero[f][c].equals(Buscaminas.MINA)) {
                //Comprobar si las posición de las banderas es valida.
                vista.banderaValida();
                //Mostrar las minas ocultas.
                vista.mostrarMinas();
                vista.mostrarMensaje("Perdio el juego", "Perdió este juego. Mejor suerte para la siguiente ocasión.",
                        CuadroDeDialogo.MENS_PERDIO);

            } else if (vista.gano()) {
                //Mostrar las minas ocultas.
                vista.mostrarMinas();
                vista.mostrarMensaje("Gano el juego", "¡Felicidades ha ganado el juego.!", CuadroDeDialogo.MENS_GANO);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
            String poscCelda = ((JButton)e.getSource()).getActionCommand();
            String [] posc = poscCelda.split(",");

            //Guardo la posición de la celda pulsada.
            int f = Integer.parseInt(posc [0]);
            int c = Integer.parseInt(posc [1]);

            //Compruebo si no ha pasado el rango de banderas y si es una celda.
            if (vista.poscBand.size() < 9 && nombreImg(vista.cuadros [f][c]).equals("celda")) {
                vista.cuadros[f][c].setIcon(cambiarDisfraz("bandera.jpg"));
                //Añado nueva posición a la lista enlazada.
                vista.poscBand.add(new Bandera (f, c));
                //Actualizo el campo de texto cantidad de bandera.
                vista.cambiarCantBandera();

            } else if (nombreImg(vista.cuadros [f][c]).equals("bandera")) {
                vista.cuadros[f][c].setIcon(cambiarDisfraz("celda.jpg"));
                //Remuevo una posición guarda de la lista enlazada.
                vista.poscBand.remove(new Bandera (f, c));
                //Actualizo el campo de texto cantidad de bandera.
                vista.cambiarCantBandera();
            }
        }
    }
}
