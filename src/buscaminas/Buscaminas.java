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
import java.util.LinkedList;
import static buscaminas.ImgUtil.*;
import static java.lang.Math.random;

public class Buscaminas extends JFrame {
    //Valores que puede tener el tablero.
    static final String MINA = "M", VACIO = " ", RECORRIDO = "R";

    //Contiene el estado de los botones.
    JButton [][] cuadros = new JButton [9][9];

    //Contiene el resultado del juego buscaminas.
    String [][] tablero = new String [9][9];

    //Lista enlazada que guarda las posiciones de las banderas añadidas.
    LinkedList<Bandera> poscBand = new LinkedList<Bandera>();

    //Campo de texto que mostrara las banderas añadidas.
    private JLabel jtfCantBandera;

    //Guarda la referencia del controlador.
    private BuscaminasEventos controlador;

    Buscaminas() {
        controlador = new BuscaminasEventos(this);

        setLayout(new GridBagLayout());
        setTitle ("Buscaminas");
        setResizable(false);
        //Le asigno un tamaño a la ventana.
        setSize(450, 450);
        //Le asigno un icono para la barra de titulo.
        setIconImage(cambiarDisfraz("minaIcono.png").getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Asigno un minimo tamaño.
        setMinimumSize(new Dimension (360, 360));
        setLocationRelativeTo(null);

        GridBagConstraints conf = new GridBagConstraints();
        conf.gridx = conf.gridy = 0;
        conf.weightx = 1.0;
        conf.fill = GridBagConstraints.HORIZONTAL;
        conf.insets = new Insets(10, 10, 10, 10);

        //Añado el panelCantBandera al frame.
        add (panelCantBandera(), conf);

        conf.gridy = 1;
        conf.weighty = 1.0;
        conf.fill = GridBagConstraints.BOTH;
        conf.insets = new Insets(0, 10, 10, 10);

        //Añado el panelTablero al frame.
        add (panelTablero(), conf);

        //Introduzco las minas al tablero.
        ponerMinas();
        //Introduzco las cantidad de minas de cada celda.
        ponerNumeroDeMinas();

        setVisible(true);
    }

    void mostrarValorDeCelda(int f, int c) {
        if (!nombreImg(cuadros [f][c]).equals("bandera") && tablero[f][c].equals(VACIO)) {
            tablero [f][c] = RECORRIDO;
            cuadros [f][c].setIcon(cambiarDisfraz("vacio.jpg"));

            //Ir para arriba.
            if (poscValida(f - 1, c)) mostrarValorDeCelda (f - 1, c);
            //Ir para la derecha.
            if (poscValida(f, c + 1)) mostrarValorDeCelda (f, c + 1);
            //Ir para abajo.
            if (poscValida(f + 1, c)) mostrarValorDeCelda (f + 1, c);
            //Ir para izquierda.
            if (poscValida(f, c - 1)) mostrarValorDeCelda (f, c - 1);
            //Ir para la diagonal derecha arriba.
            if (poscValida(f - 1, c + 1)) mostrarValorDeCelda (f - 1, c + 1);
            //Ir para la diagonal izquierda arriba.
            if (poscValida(f - 1, c - 1)) mostrarValorDeCelda (f - 1, c - 1);
            //Ir para la diagonal derecha abajo.
            if (poscValida(f + 1, c + 1)) mostrarValorDeCelda (f + 1, c + 1);
            //Ir para la diagonal izquierda abajo.
            if (poscValida(f + 1, c - 1)) mostrarValorDeCelda (f + 1, c - 1);

        } else {
            if (!tablero[f][c].equals(RECORRIDO) && !nombreImg(cuadros [f][c]).equals("bandera")) {
                if (tablero[f][c].equals(MINA))
                    cuadros[f][c].setIcon(cambiarDisfraz("minaExp.jpg"));
                else
                    cuadros[f][c].setIcon(cambiarDisfraz(tablero[f][c] + ".jpg"));
            }
        }
    }

    boolean gano() {
        //Guarda las cantidad de celdas.
        int cantCuadros = 0;

        for (int f = 0; f < 9; f ++) {
            for (int c = 0; c < 9; c ++) {
                if (nombreImg(cuadros [f][c]).equals("celda") ||
                        nombreImg(cuadros [f][c]).equals("bandera"))
                    cantCuadros ++;

                if (cantCuadros > 9)
                    return false;
            }
        }
        return true;
    }

    void iniciarNuevoJuego() {
        for (int f = 0; f < 9; f ++) {
            for (int c = 0; c < 9; c ++) {
                //Elimino las minas que existan en el tablero.
                if (tablero[f][c].equals(MINA))
                    tablero[f][c] = VACIO;

                //Oculto la información de las celdas.
                if (!nombreImg(cuadros [f][c]).equals("celda"))
                    cuadros [f][c].setIcon(cambiarDisfraz("celda.jpg"));
            }
        }
        //Limpio la lista enlazada.
        poscBand.clear();
        //Inserto las minas al tablero.
        ponerMinas();
        //Inserto las cantidad de minas de cada celda.
        ponerNumeroDeMinas();
        //Actualizo el campo de texto cantidad bandera.
        cambiarCantBandera();
    }

    void reiniciarJuego() {
        for (int f = 0; f < 9; f ++) {
            for (int c = 0; c < 9; c ++) {
                if (tablero[f][c].equals(RECORRIDO)) tablero[f][c] = VACIO;

                //Oculto la información de las celdas.
                if (!nombreImg(cuadros [f][c]).equals("celda"))
                    cuadros [f][c].setIcon(cambiarDisfraz("celda.jpg"));
            }
        }
        //Limpio la lista enlazada.
        poscBand.clear();
        //Actualizo el campo de texto cantidad de banderas.
        cambiarCantBandera();
    }

    void mostrarMinas() {
        int cantMina = 0, totalMinas = 9;

        for (int f = 0; f < 9; f ++) {
            for (int c = 0; c < 9; c ++) {
                if (nombreImg(cuadros [f][c]).equals("minaExp"))
                    cantMina ++;

                else if (tablero [f][c].equals(MINA)) {
                    //Cambio la imagen del botón a mina.
                    cuadros[f][c].setIcon(cambiarDisfraz("mina.jpg"));
                    cantMina ++;
                }

                if (cantMina == totalMinas) return;
            }
        }
    }

    void cambiarCantBandera() {
        jtfCantBandera.setText("" + poscBand.size());
    }

    void banderaValida() {
        for (Bandera ban : poscBand) {
            //Compruebo si no hay una mina en esa posición.
            if (!tablero [ban.getX()][ban.getY()].equals(MINA))
                cuadros [ban.getX()][ban.getY()].setIcon(cambiarDisfraz("banderaError.jpg"));
        }
    }

    void mostrarMensaje(String titulo, String mensaje, int tipoMensaje) {
        new CuadroDeDialogo(this, titulo, mensaje, tipoMensaje);
    }

    private String obtenerCantMinas (int f, int c) {
        //Guarda la cantidad de minas que posee una celda.
        int cantMinas = 0;

        for (int fila = f - 1; fila <= f + 1; fila ++)
            for (int colum = c - 1; colum <= c + 1; colum ++)
                if (poscValida(fila, colum) && MINA.equals(tablero [fila][colum]))
                    cantMinas ++;

        return (cantMinas == 0) ? VACIO : Integer.toString(cantMinas);
    }

    private JPanel panelTablero () {
        JPanel panel = new JPanel(new GridLayout(9, 9, 4, 4));
        for (int f = 0; f < 9; f ++) {
            for (int c = 0; c < 9; c ++) {
                //Le asigno una imagen al botón.
                cuadros [f][c] = new JButton (cambiarDisfraz("celda.jpg"));
                //Le asigno un identificador al botón.
                cuadros [f][c].setActionCommand(String.format("%d,%d", f, c));
                cuadros [f][c].addActionListener(controlador);
                cuadros [f][c].addMouseListener(controlador);
                //Añado el panel al botón.
                panel.add(cuadros [f][c]);
            }
        }
        return panel;
    }

    private JPanel panelCantBandera () {
        JPanel panel = new JPanel ();

        jtfCantBandera = new JLabel ("0", JLabel.CENTER);
        jtfCantBandera.setPreferredSize(new Dimension (35,30));
        jtfCantBandera.setOpaque(true);
        //Le asigno un fondo negro.
        jtfCantBandera.setBackground(Color.black);
        //Le configuro un formato para la fuente del campo de texto.
        jtfCantBandera.setFont(new Font ("Arial", Font.BOLD, 19));
        //Le asigno a la letra del campo de texto el color blanco.
        jtfCantBandera.setForeground(Color.white);

        //Le añado al panel el campo de texto.
        panel.add (jtfCantBandera);
        //Le añado al panel una imagen.
        panel.add (new JLabel(cambiarDisfraz("miniBandera.png")));

        return panel;
    }

    private boolean poscValida(int f, int c) {
        return (f < 9 && f > - 1 && c < 9 && c > - 1);
    }

    private void ponerMinas() {
        //Guarda la cantidad de minas insertadas.
        int minasInsertadas = 0;

        while (minasInsertadas < 9) {
            //Genera una posición al azar.
            int f = (int)(random() * 9);
            int c = (int)(random() * 9);

            //Compruebo si la celda no posee una mina.
            if (!MINA.equals(tablero [f][c])) {
                tablero [f][c] = MINA;
                minasInsertadas ++;
            }
        }
    }

    private void ponerNumeroDeMinas() {
        for (int f = 0; f < 9; f ++)
            for (int c = 0; c < 9; c ++)
                if (!MINA.equals(tablero [f][c]))
                    tablero [f][c] = obtenerCantMinas(f, c);
    }
}
