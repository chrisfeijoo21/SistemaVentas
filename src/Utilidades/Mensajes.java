package Utilidades;

import javax.swing.JOptionPane;

public class Mensajes {

    public static void exito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmar(String mensaje) {
        int r = JOptionPane.showConfirmDialog(null, mensaje,
                "Confirmar", JOptionPane.YES_NO_OPTION);
        return r == JOptionPane.YES_OPTION;
    }
}